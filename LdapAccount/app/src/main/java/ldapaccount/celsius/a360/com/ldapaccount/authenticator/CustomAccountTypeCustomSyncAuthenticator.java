package ldapaccount.celsius.a360.com.ldapaccount.authenticator;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;
import android.util.Log;

import java.util.Date;

/**
 * Created by dshar on 24/12/2017.
 */

public class CustomAccountTypeCustomSyncAuthenticator extends AbstractThreadedSyncAdapter {

    private final AccountManager mAccountManager;
    private final Context mContext;

    private Date mLastUpdated;

    public CustomAccountTypeCustomSyncAuthenticator(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        Log.e("test","SyncAdapter - SyncAdapter");
        mContext = context;
        mAccountManager = AccountManager.get(context);
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {

        Log.e("test","SyncAdapter - onPerformSync");
    }
}
