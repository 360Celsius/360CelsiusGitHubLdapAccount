package ldapaccount.celsius.a360.com.ldapaccount.services;

import android.app.IntentService;
import android.content.Intent;

import com.unboundid.ldap.sdk.LDAPConnection;
import com.unboundid.ldap.sdk.LDAPException;
import com.unboundid.ldap.sdk.RootDSE;

import ldapaccount.celsius.a360.com.ldapaccount.constant.ConstKeysAndParams;
import ldapaccount.celsius.a360.com.ldapaccount.ldapserver.LdapServerInstance;

/**
 * Created by dennisshar on 23/12/2017.
 */

public class LdapServerConnectionService extends IntentService {

    LDAPConnection connection = null;

    public LdapServerConnectionService() {
        super("LdapServerConnectionService");
    }

    public LdapServerConnectionService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            connection = ((LdapServerInstance) intent.getSerializableExtra(ConstKeysAndParams.LDAP_SERVER_INSTANCE)).getConnection();

            if (connection != null) {
                RootDSE s = connection.getRootDSE();
                String[] baseDNs = null;
                if (s != null) {
                    baseDNs = s.getNamingContextDNs();
                }
            }



        } catch (LDAPException e) {
            e.printStackTrace();
        }finally {
            if (connection != null) {
                connection.close();
            }
        }
    }
}
