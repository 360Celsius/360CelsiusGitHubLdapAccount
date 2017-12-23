package ldapaccount.celsius.a360.com.ldapaccount.receivers;

import android.accounts.AccountManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import ldapaccount.celsius.a360.com.ldapaccount.constant.ConstKeysAndParams;
import ldapaccount.celsius.a360.com.ldapaccount.iterface.CreateCustomLdapAccountInterface;
import ldapaccount.celsius.a360.com.ldapaccount.iterface.CreateLdapServerConnectionInterface;

/**
 * Created by dennisshar on 23/12/2017.
 */

public class LdapServerConnectionServiceResponseReciver extends BroadcastReceiver {

    private CreateLdapServerConnectionInterface listner = null;

    public static final String ACTION_RESP = "com.ldap.BroadcastReceiverLdapServerConnection";

    @Override
    public void onReceive(Context context, Intent intent) {
        boolean isSuccessulConnection = intent.getBooleanExtra(ConstKeysAndParams.LDAP_SERVER_CONNECTION_IS_CONNECTION_SUCCESSFUL,false);
        String[] baseDN = (String[]) intent.getExtras().get(ConstKeysAndParams.LDAP_SERVER_CONNECTION_BASE_DN_KEY);
        String errorMessage = intent.getStringExtra(ConstKeysAndParams.LDAP_SERVER_CONNECTION_MESSAGE_KEY);

        if (listner != null) {
            listner.createLdapServerConnection(baseDN,errorMessage,isSuccessulConnection);
        }

    }

    public void createCustomLdapServerConnectionListner(Context context) {
        this.listner = (CreateLdapServerConnectionInterface) context;
    }
}
