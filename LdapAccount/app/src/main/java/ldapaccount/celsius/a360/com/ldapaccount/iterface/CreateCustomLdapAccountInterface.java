package ldapaccount.celsius.a360.com.ldapaccount.iterface;

import android.accounts.Account;
import android.content.Intent;

/**
 * Created by dennisshar on 22/12/2017.
 */

public interface CreateCustomLdapAccountInterface {
    public void createCustomLdapAccount(Account avvount, String accountMail , String password, Intent intent, String authToken);
}
