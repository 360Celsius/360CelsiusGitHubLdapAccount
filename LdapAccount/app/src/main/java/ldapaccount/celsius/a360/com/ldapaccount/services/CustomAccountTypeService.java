package ldapaccount.celsius.a360.com.ldapaccount.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import ldapaccount.celsius.a360.com.ldapaccount.authenticator.CustomAccountTypeCustomAuthenticator;

/**
 * Created by dennisshar on 22/12/2017.
 */

public class CustomAccountTypeService extends Service {

    private static CustomAccountTypeCustomAuthenticator authenticator = null;
    private static IBinder binder = null;

    @Override
    public IBinder onBind(Intent intent) {
        binder = null;
        if (intent.getAction().equals(android.accounts.AccountManager.ACTION_AUTHENTICATOR_INTENT)) {
            binder = getAuthenticator().getIBinder();
        }
        return binder;
    }

    private CustomAccountTypeCustomAuthenticator getAuthenticator() {
        if (CustomAccountTypeService.authenticator == null) {
            CustomAccountTypeService.authenticator = new CustomAccountTypeCustomAuthenticator(this);
        }
        return CustomAccountTypeService.authenticator;
    }

}
