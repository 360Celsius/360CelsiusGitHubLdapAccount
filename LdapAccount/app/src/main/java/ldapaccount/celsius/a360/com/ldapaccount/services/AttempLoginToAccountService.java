package ldapaccount.celsius.a360.com.ldapaccount.services;

import android.accounts.AccountManager;
import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import ldapaccount.celsius.a360.com.ldapaccount.authenticator.AccountUtilities;
import ldapaccount.celsius.a360.com.ldapaccount.constant.ConstKeysAndParams;
import ldapaccount.celsius.a360.com.ldapaccount.receivers.AttempLoginToAccountServiceResponseReciver;

/**
 * Created by dennisshar on 22/12/2017.
 */

public class AttempLoginToAccountService extends IntentService {


    public AttempLoginToAccountService() {
        super("AttempLoginToAccountService");
    }

    public AttempLoginToAccountService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        String authToken = AccountUtilities.serverAuthenticatorInterface.signIn(intent.getExtras().getString(ConstKeysAndParams.CUSTOM_ACCOUNT_USER_EMAIL) , intent.getExtras().getString(ConstKeysAndParams.CUSTOM_ACCOUNT_USER_PASSWORD));


        final Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(AttempLoginToAccountServiceResponseReciver.ACTION_RESP);
        broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
        broadcastIntent.putExtra(AccountManager.KEY_ACCOUNT_NAME, intent.getExtras().getString(ConstKeysAndParams.CUSTOM_ACCOUNT_USER_EMAIL));
        broadcastIntent.putExtra(AccountManager.KEY_ACCOUNT_TYPE, ConstKeysAndParams.ACCOUNT_TYPE);
        broadcastIntent.putExtra(AccountManager.KEY_AUTHTOKEN, authToken);
        broadcastIntent.putExtra(ConstKeysAndParams.PARAM_USER_PASSWORD, intent.getExtras().getString(ConstKeysAndParams.CUSTOM_ACCOUNT_USER_PASSWORD));

        sendBroadcast(broadcastIntent);
    }
}
