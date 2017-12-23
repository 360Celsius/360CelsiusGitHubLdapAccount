package ldapaccount.celsius.a360.com.ldapaccount.receivers;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import ldapaccount.celsius.a360.com.ldapaccount.constant.ConstKeysAndParams;
import ldapaccount.celsius.a360.com.ldapaccount.iterface.CreateCustomLdapAccountInterface;
import ldapaccount.celsius.a360.com.ldapaccount.services.AttempLoginToAccountService;

/**
 * Created by dennisshar on 22/12/2017.
 */

public class AttempLoginToAccountServiceResponseReciver extends BroadcastReceiver {

    private CreateCustomLdapAccountInterface listener = null;

    public static final String ACTION_RESP = "com.ldap.BroadcastReceiverAttempLogin";

    @Override
    public void onReceive(Context context, Intent intent) {

        final String accountName = intent.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
        final String accountPassword = intent.getStringExtra(ConstKeysAndParams.PARAM_USER_PASSWORD);
        final Account account = new Account(accountName, intent.getStringExtra(AccountManager.KEY_ACCOUNT_TYPE));
        String authToken = intent.getStringExtra(AccountManager.KEY_AUTHTOKEN);

        if (listener != null) {
            listener.createCustomLdapAccount(account,accountName ,accountPassword,intent,authToken);
        }
    }


    public void createCustomLdapAccountListener(Context context) {
        this.listener = (CreateCustomLdapAccountInterface) context;
    }

}
