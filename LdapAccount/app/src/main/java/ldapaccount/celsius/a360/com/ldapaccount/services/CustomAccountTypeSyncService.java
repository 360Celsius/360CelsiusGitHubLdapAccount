package ldapaccount.celsius.a360.com.ldapaccount.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import ldapaccount.celsius.a360.com.ldapaccount.authenticator.CustomAccountTypeCustomSyncAuthenticator;


/**
 * Created by dshar on 24/12/2017.
 */

public class CustomAccountTypeSyncService extends Service {

    private static final Object sSyncAdapterLock = new Object();
    private static CustomAccountTypeCustomSyncAuthenticator sSyncAdapter = null;

    @Override
    public void onCreate() {
        Log.e("test","SyncService - onCreate");
        synchronized (sSyncAdapterLock) {
            if (sSyncAdapter == null) {
                sSyncAdapter = new CustomAccountTypeCustomSyncAuthenticator(getApplicationContext(), true);
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.e("test","SyncService - onBind");
        return sSyncAdapter.getSyncAdapterBinder();
    }
}
