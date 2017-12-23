package ldapaccount.celsius.a360.com.ldapaccount.services;

import android.app.IntentService;
import android.content.Intent;

import com.unboundid.ldap.sdk.LDAPConnection;
import com.unboundid.ldap.sdk.LDAPException;
import com.unboundid.ldap.sdk.RootDSE;

import ldapaccount.celsius.a360.com.ldapaccount.constant.ConstKeysAndParams;
import ldapaccount.celsius.a360.com.ldapaccount.ldapserver.LdapServerInstance;
import ldapaccount.celsius.a360.com.ldapaccount.receivers.AttempLoginToAccountServiceResponseReciver;
import ldapaccount.celsius.a360.com.ldapaccount.receivers.LdapServerConnectionServiceResponseReciver;

/**
 * Created by dennisshar on 23/12/2017.
 */

public class LdapServerConnectionService extends IntentService {

    LDAPConnection connection = null;
    String[] baseDNs = null;

    public LdapServerConnectionService() {
        super("LdapServerConnectionService");
    }

    public LdapServerConnectionService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        final Intent broadcastIntent = new Intent(LdapServerConnectionServiceResponseReciver.ACTION_RESP);
        broadcastIntent.setAction(LdapServerConnectionServiceResponseReciver.ACTION_RESP);
        broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
        try {
            connection = ((LdapServerInstance) intent.getSerializableExtra(ConstKeysAndParams.LDAP_SERVER_INSTANCE)).getConnection();

            if (connection != null) {
                RootDSE s = connection.getRootDSE();

                if (s != null) {
                    baseDNs = s.getNamingContextDNs();
                }
            }
            broadcastIntent.putExtra(ConstKeysAndParams.LDAP_SERVER_CONNECTION_IS_CONNECTION_SUCCESSFUL, true);
            broadcastIntent.putExtra(ConstKeysAndParams.LDAP_SERVER_CONNECTION_MESSAGE_KEY, "N/A");
            broadcastIntent.putExtra(ConstKeysAndParams.LDAP_SERVER_CONNECTION_BASE_DN_KEY, baseDNs);

        } catch (LDAPException e) {
            e.printStackTrace();
            broadcastIntent.putExtra(ConstKeysAndParams.LDAP_SERVER_CONNECTION_IS_CONNECTION_SUCCESSFUL, false);
            broadcastIntent.putExtra(ConstKeysAndParams.LDAP_SERVER_CONNECTION_MESSAGE_KEY, e.getMessage());
            broadcastIntent.putExtra(ConstKeysAndParams.LDAP_SERVER_CONNECTION_BASE_DN_KEY, "N/A");

        }finally {
            if (connection != null) {
                connection.close();
            }
        }
        sendBroadcast(broadcastIntent);
    }
}
