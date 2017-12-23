package ldapaccount.celsius.a360.com.ldapaccount;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;

import com.unboundid.ldap.sdk.Filter;
import com.unboundid.ldap.sdk.LDAPException;

import ldapaccount.celsius.a360.com.ldapaccount.constant.ConstKeysAndParams;
import ldapaccount.celsius.a360.com.ldapaccount.iterface.CreateCustomLdapAccountInterface;
import ldapaccount.celsius.a360.com.ldapaccount.iterface.CreateLdapServerConnectionInterface;
import ldapaccount.celsius.a360.com.ldapaccount.receivers.AttempLoginToAccountServiceResponseReciver;
import ldapaccount.celsius.a360.com.ldapaccount.ldapserver.LdapServerInstance;
import ldapaccount.celsius.a360.com.ldapaccount.receivers.LdapServerConnectionServiceResponseReciver;
import ldapaccount.celsius.a360.com.ldapaccount.services.AttempLoginToAccountService;
import ldapaccount.celsius.a360.com.ldapaccount.services.ContactSearchService;
import ldapaccount.celsius.a360.com.ldapaccount.services.LdapServerConnectionService;

public class MainActivity extends AccountAuthenticatorActivity implements CreateCustomLdapAccountInterface, CreateLdapServerConnectionInterface {

    private AttempLoginToAccountServiceResponseReciver receiverCustonAccountLogIN;
    private LdapServerConnectionServiceResponseReciver receiverConnectToLdapServer;
    private AccountManager mAccountManager;
    private IntentFilter filterCustonAccountLogIN;
    private IntentFilter filterLdapServeerConnection;
    private LdapServerInstance ldapServer;
    public Filter filter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Custom Account type
        mAccountManager = AccountManager.get(this);


        filterCustonAccountLogIN = new IntentFilter(AttempLoginToAccountServiceResponseReciver.ACTION_RESP);
        filterCustonAccountLogIN.addCategory(Intent.CATEGORY_DEFAULT);

        filterLdapServeerConnection = new IntentFilter(LdapServerConnectionServiceResponseReciver.ACTION_RESP);
        filterLdapServeerConnection.addCategory(Intent.CATEGORY_DEFAULT);

        receiverCustonAccountLogIN = new AttempLoginToAccountServiceResponseReciver();
        receiverCustonAccountLogIN.createCustomLdapAccountListener(this);

        receiverConnectToLdapServer = new LdapServerConnectionServiceResponseReciver();
        receiverConnectToLdapServer.createCustomLdapServerConnectionListner(this);

        //Ldap server
        ldapServer = new LdapServerInstance("ldap.forumsys.com", 389, 0, "cn=read-only-admin,dc=example,dc=com", "password","ou=mathematicians,dc=example,dc=com");

    }



    @Override
    protected void onResume() {

        registerReceiver(receiverCustonAccountLogIN, filterCustonAccountLogIN);
        registerReceiver(receiverConnectToLdapServer, filterLdapServeerConnection);

        super.onResume();
        if(getIntent().getExtras()!=null && getIntent().getExtras().getString(ConstKeysAndParams.CUSTOM_ACCOUNT_TYPE_KEY)!=null) {
            //start login service
            Intent msgIntent = new Intent(this, AttempLoginToAccountService.class);
            msgIntent.putExtra(ConstKeysAndParams.CUSTOM_ACCOUNT_USER_EMAIL, "cn=read-only-admin,dc=example,dc=com");
            msgIntent.putExtra(ConstKeysAndParams.CUSTOM_ACCOUNT_USER_PASSWORD, "password");
            startService(msgIntent);
        }


    }


    @Override
    protected void onPause() {
        try {
            unregisterReceiver(receiverCustonAccountLogIN);
            unregisterReceiver(receiverConnectToLdapServer);
        }catch (IllegalArgumentException e){
            e.printStackTrace();
        }

        super.onPause();
    }

    @Override
    public void createCustomLdapAccount(Account account, String accountMail , String accountPassword, Intent intent, String authToken) {

        //create custom type account
        mAccountManager.setPassword(account, accountPassword);

        account = new Account(accountMail, ConstKeysAndParams.ACCOUNT_TYPE);

        if (getIntent().getBooleanExtra(ConstKeysAndParams.ARG_IS_ADDING_NEW_ACCOUNT_KEY, false)) {
            // Creating the account on the device and setting the auth token we got
            // (Not setting the auth token will cause another call to the server to authenticate the user)
            mAccountManager.addAccountExplicitly(account, accountPassword, null);
            mAccountManager.setAuthToken(account, "full_access", authToken);
        } else {
            mAccountManager.setPassword(account, accountPassword);
        }

        setAccountAuthenticatorResult(intent.getExtras());
        setResult(AccountAuthenticatorActivity.RESULT_OK, intent);


        //start connect to ldap server service
        Intent msgIntent = new Intent(this, LdapServerConnectionService.class);
        msgIntent.putExtra(ConstKeysAndParams.LDAP_SERVER_INSTANCE, ldapServer);
        startService(msgIntent);

        finish();
    }

    @Override
    public void createLdapServerConnection(String[] baseDn, String errorMEssage, boolean isConnected) {
        //Log.e("test","baseDn-> "+baseDn[0]);
        Log.e("test","errorMEssage-> "+errorMEssage);
        Log.e("test","isConnected-> "+String.valueOf(isConnected));


        //if connection is ok then make contacts search
        final String filterString = "(objectClass=*)";
        try {
            filter = Filter.create(filterString);
        } catch (LDAPException e) {
            e.printStackTrace();
        }
        //start search
        Intent msgIntent = new Intent(this, ContactSearchService.class);
        msgIntent.putExtra(ConstKeysAndParams.LDAP_SERVER_INSTANCE, ldapServer);
        msgIntent.putExtra(ConstKeysAndParams.LDAP_SERVER_SEARCH_FILTER, filter);
        startService(msgIntent);


    }
}
