package ldapaccount.celsius.a360.com.ldapaccount.authenticator;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.util.Log;

import ldapaccount.celsius.a360.com.ldapaccount.constant.ConstKeysAndParams;
import ldapaccount.celsius.a360.com.ldapaccount.iterface.ServerAuthenticatorInterface;

/**
 * Created by dennisshar on 22/12/2017.
 */

public class AccountUtilities {

    public static ServerAuthenticatorInterface serverAuthenticatorInterface = new UserAuthenticator();


    public static Account getAccount(Context context, String accountName) {
        AccountManager accountManager = AccountManager.get(context);
        Account[] accounts = accountManager.getAccountsByType(ConstKeysAndParams.ACCOUNT_TYPE);
        for (Account account : accounts) {
            if (account.name.equalsIgnoreCase(accountName)) {
                return account;
            }
        }
        return null;
    }
}
