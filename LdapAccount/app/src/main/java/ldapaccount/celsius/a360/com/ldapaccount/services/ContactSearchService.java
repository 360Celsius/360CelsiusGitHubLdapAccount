package ldapaccount.celsius.a360.com.ldapaccount.services;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.unboundid.ldap.sdk.Filter;
import com.unboundid.ldap.sdk.LDAPConnection;
import com.unboundid.ldap.sdk.LDAPException;
import com.unboundid.ldap.sdk.LDAPSearchException;
import com.unboundid.ldap.sdk.SearchRequest;
import com.unboundid.ldap.sdk.SearchResult;
import com.unboundid.ldap.sdk.SearchScope;

import ldapaccount.celsius.a360.com.ldapaccount.constant.ConstKeysAndParams;
import ldapaccount.celsius.a360.com.ldapaccount.ldapserver.LdapServerInstance;

/**
 * Created by dennisshar on 23/12/2017.
 */

public class ContactSearchService extends IntentService {

    private SearchResult result = null;
    private LDAPConnection connection = null;
    private SearchRequest request = null;
    private Filter filter = null;

    public ContactSearchService() {
        super("ContactSearchService");
    }

    public ContactSearchService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String BASE_DN = "dc=example,dc=com";
        int SIZE_LIMIT = 2000;
        int TIME_LIMIT_SECONDS = 120;

        try {
            connection = ((LdapServerInstance) intent.getSerializableExtra(ConstKeysAndParams.LDAP_SERVER_INSTANCE)).getConnection();
            filter = ((Filter) intent.getSerializableExtra(ConstKeysAndParams.LDAP_SERVER_SEARCH_FILTER));;
            request = new SearchRequest(BASE_DN, SearchScope.SUB, filter);
            request.setSizeLimit(SIZE_LIMIT);
            request.setTimeLimitSeconds(TIME_LIMIT_SECONDS);

            result = connection.search(request);
        } catch (LDAPException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }
}
