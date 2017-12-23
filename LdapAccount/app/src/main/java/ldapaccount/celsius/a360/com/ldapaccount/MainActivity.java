package ldapaccount.celsius.a360.com.ldapaccount;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;

import ldapaccount.celsius.a360.com.ldapaccount.constant.ConstKeysAndParams;
import ldapaccount.celsius.a360.com.ldapaccount.iterface.CreateCustomLdapAccountInterface;
import ldapaccount.celsius.a360.com.ldapaccount.receivers.AttempLoginToAccountServiceResponseReciver;
import ldapaccount.celsius.a360.com.ldapaccount.receivers.LdapServerInstance;
import ldapaccount.celsius.a360.com.ldapaccount.services.AttempLoginToAccountService;

public class MainActivity extends AccountAuthenticatorActivity implements CreateCustomLdapAccountInterface {

    private AttempLoginToAccountServiceResponseReciver receiver;
    private AccountManager mAccountManager;
    private IntentFilter filter;
    private Thread mAuthThread;
    private LdapServerInstance ldapServer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Custom Account type
        mAccountManager = AccountManager.get(this);


        filter = new IntentFilter(AttempLoginToAccountServiceResponseReciver.ACTION_RESP);
        filter.addCategory(Intent.CATEGORY_DEFAULT);

        receiver = new AttempLoginToAccountServiceResponseReciver();
        receiver.createCustomLdapAccountListener(this);

        registerReceiver(receiver, filter);

        //Ldap server
        ldapServer = new LdapServerInstance("ldap.forumsys.com", 389, 0, "cn=read-only-admin,dc=example,dc=com", "password","ou=mathematicians,dc=example,dc=com");

    }



    @Override
    protected void onResume() {
        super.onResume();
        if(getIntent().getExtras()!=null && getIntent().getExtras().getString(ConstKeysAndParams.CUSTOM_ACCOUNT_TYPE_KEY)!=null) {
            Log.e("test", "MainActivity - intent from Account Authenticator-> " + getIntent().getExtras().getString(ConstKeysAndParams.CUSTOM_ACCOUNT_TYPE_KEY));
            Log.e("test", "MainActivity - intent from Account Authenticator-> " + getIntent().getExtras().getString(ConstKeysAndParams.CUSTOM_ACCOUNT_AUTHANTICATION_TOKEN_TYPE_KEY));
            Log.e("test", "MainActivity - intent from Account Authenticator-> " + String.valueOf(getIntent().getExtras().getBoolean(ConstKeysAndParams.CUSTOM_ACCOUNT_IS_NEW_ACCOUNT_KEY)));

            Intent msgIntent = new Intent(this, AttempLoginToAccountService.class);
            msgIntent.putExtra(ConstKeysAndParams.CUSTOM_ACCOUNT_USER_EMAIL, "cn=read-only-admin,dc=example,dc=com");
            msgIntent.putExtra(ConstKeysAndParams.CUSTOM_ACCOUNT_USER_PASSWORD, "password");
            startService(msgIntent);
        }


    }



    @Override
    public void createCustomLdapAccount(Account account, String accountMail , String accountPassword, Intent intent, String authToken) {

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

        unregisterReceiver(receiver);



        finish();
    }
}
