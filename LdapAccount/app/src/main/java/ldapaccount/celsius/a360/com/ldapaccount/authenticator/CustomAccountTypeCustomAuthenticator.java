package ldapaccount.celsius.a360.com.ldapaccount.authenticator;

import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.accounts.NetworkErrorException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import ldapaccount.celsius.a360.com.ldapaccount.MainActivity;
import ldapaccount.celsius.a360.com.ldapaccount.constant.ConstKeysAndParams;

/**
 * Created by dennisshar on 22/12/2017.
 */

public class CustomAccountTypeCustomAuthenticator extends AbstractAccountAuthenticator {

    private final Context context;

    public CustomAccountTypeCustomAuthenticator(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public Bundle addAccount(AccountAuthenticatorResponse accountAuthenticatorResponse, String accountType, String authTokenType, String[] requiredFeatures, Bundle bundle) throws NetworkErrorException {
        Log.e("test","CustomAccountTypeCustomAuthenticator - addAccount");

        Bundle reply = new Bundle();

        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, accountAuthenticatorResponse);
        intent.putExtra(ConstKeysAndParams.CUSTOM_ACCOUNT_TYPE_KEY, accountType);
        intent.putExtra(ConstKeysAndParams.CUSTOM_ACCOUNT_AUTHANTICATION_TOKEN_TYPE_KEY, authTokenType);
        intent.putExtra(ConstKeysAndParams.CUSTOM_ACCOUNT_IS_NEW_ACCOUNT_KEY, true);
        intent.putExtra(ConstKeysAndParams.ARG_IS_ADDING_NEW_ACCOUNT_KEY, true);

        // return our AccountAuthenticatorActivity
        reply.putParcelable(AccountManager.KEY_INTENT, intent);

        return reply;
    }

    @Override
    public Bundle editProperties(AccountAuthenticatorResponse accountAuthenticatorResponse, String s) {
        Log.e("test","CustomAccountTypeCustomAuthenticator - editProperties");
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Bundle confirmCredentials(AccountAuthenticatorResponse accountAuthenticatorResponse, Account account, Bundle bundle) throws NetworkErrorException {
        Log.e("test","CustomAccountTypeCustomAuthenticator - confirmCredentials");
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Bundle getAuthToken(AccountAuthenticatorResponse response, Account account, String authTokenType, Bundle options)  throws NetworkErrorException {
        Log.e("test","CustomAccountTypeCustomAuthenticator - getAuthToken");

        // Extract the username and password from the Account Manager, and ask
        // the server for an appropriate AuthToken.
        final AccountManager accountManager = AccountManager.get(context);
        String authToken = accountManager.peekAuthToken(account, authTokenType);

        // Lets give another try to authenticate the user
        if (authToken !=null && authToken.isEmpty()) {
            final String password = accountManager.getPassword(account);
            if (password != null) {
                authToken = AccountUtilities.serverAuthenticatorInterface.signIn(account.name, password);
            }
        }

        // If we get an authToken - we return it
        if (authToken !=null && authToken.isEmpty())  {
            final Bundle result = new Bundle();
            result.putString(AccountManager.KEY_ACCOUNT_NAME, account.name);
            result.putString(AccountManager.KEY_ACCOUNT_TYPE, account.type);
            result.putString(AccountManager.KEY_AUTHTOKEN, authToken);
            return result;
        }

        // If we get here, then we couldn't access the user's password - so we need to re-prompt them for their credentials. We do that by creating an intent to display
        final Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);
        intent.putExtra(ConstKeysAndParams.CUSTOM_ACCOUNT_TYPE_KEY, account.type);
        intent.putExtra(ConstKeysAndParams.CUSTOM_ACCOUNT_AUTHANTICATION_TOKEN_TYPE_KEY, authTokenType);

        // This is for the case multiple accounts are stored on the device
        // and the AccountPicker dialog chooses an account without auth token.
        // We can pass out the account name chosen to the user of write it
        // again in the Login activity intent returned.
        if (account != null) {
            intent.putExtra(AccountManager.KEY_ACCOUNT_NAME, account.name);
        }

        final Bundle bundle = new Bundle();
        bundle.putParcelable(AccountManager.KEY_INTENT, intent);

        return bundle;
    }

    @Override
    public String getAuthTokenLabel(String s) {
        Log.e("test","CustomAccountTypeCustomAuthenticator - getAuthTokenLabel");
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Bundle updateCredentials(AccountAuthenticatorResponse accountAuthenticatorResponse, Account account, String s, Bundle bundle) throws NetworkErrorException {
        Log.e("test","CustomAccountTypeCustomAuthenticator - updateCredentials");
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Bundle hasFeatures(AccountAuthenticatorResponse accountAuthenticatorResponse, Account account, String[] strings) throws NetworkErrorException {
        Log.e("test","CustomAccountTypeCustomAuthenticator - hasFeatures");
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
