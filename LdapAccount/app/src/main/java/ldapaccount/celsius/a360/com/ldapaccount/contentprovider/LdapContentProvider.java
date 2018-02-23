package ldapaccount.celsius.a360.com.ldapaccount.contentprovider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by dennisshar on 29/01/2018.
 */

public class LdapContentProvider extends ContentProvider {


    @Override
    public boolean onCreate() {
        Log.e("content_provider_test","LdapContentProvider - onCreate");
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        Log.e("content_provider_test","LdapContentProvider - query");
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        Log.e("content_provider_test","LdapContentProvider - getType");
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        Log.e("content_provider_test","LdapContentProvider - insert");
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        Log.e("content_provider_test","LdapContentProvider - delete");
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        Log.e("content_provider_test","LdapContentProvider - update");
        return 0;
    }
}
