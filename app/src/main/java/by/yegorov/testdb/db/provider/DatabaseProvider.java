package by.yegorov.testdb.db.provider;

import by.yegorov.testdb.db.provider.helpers.TestModelConsts;
import by.yegorov.testdb.db.provider.helpers.TestModelHelper;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

public class DatabaseProvider extends ContentProvider implements TestModelConsts {

    private static final String TAG = DatabaseProvider.class.getName();

    private NativeDatabaseHelper mDbHelper;

    private static final int TEST_MODEL = 1;

    private static final int TEST_MODEL_ID = 2;

    public static final String PROVIDER_NAME = "by.yegorov.testdb.db.DatabaseProvider";

    private static UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(PROVIDER_NAME, TABLE_TEST, TEST_MODEL);
        uriMatcher.addURI(PROVIDER_NAME, TABLE_TEST + "/#", TEST_MODEL_ID);
    }


    @Override
    public boolean onCreate() {
        mDbHelper = NativeDatabaseHelper.getInstance(getContext());
        return mDbHelper != null;
    }


    @Override
    public String getType(Uri uri) {
        Log.i(TAG, "getType: " + uri.toString());
        switch (uriMatcher.match(uri)) {
            case TEST_MODEL:
                return TestModelHelper.CONTENT_TYPE_DIR;
            case TEST_MODEL_ID:
                return TestModelHelper.CONTENT_TYPE_ITEM;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Log.i(TAG, "insert: " + uri.toString());
        ContentValues cv = null;
        String table;
        Uri contentUri = null;
        switch (uriMatcher.match(uri)) {
            case TEST_MODEL:
                table = TABLE_TEST;
                cv = values;
                contentUri = TestModelHelper.TEST_MODEL_URI;
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        Uri notifyUri = null;
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            long id = db.replace(table, null, cv);
            if (id > 0) {
                notifyUri = ContentUris.withAppendedId(contentUri, id);
            } else {
                SQLException exc = new SQLException("Failed to insert row into " + table);
                Log.e(TAG, "Failed to insert row into " + table, exc);
                throw exc;
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        return notifyUri;
    }


    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        Log.i(TAG, "query: " + uri.toString());
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        switch (uriMatcher.match(uri)) {
            case TEST_MODEL:
                queryBuilder.setTables(TABLE_TEST);
                break;
            case TEST_MODEL_ID:
                queryBuilder.setTables(TABLE_TEST);
                queryBuilder.appendWhere(TABLE_TEST_ID + "=" + uri.getPathSegments().get(1));
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        Cursor c = null;
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        db.beginTransaction();
        try {
            c = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        Log.i(TAG, "update: " + uri.toString());
        ContentValues cv = null;
        String table;
        long id;
        int count = 0;
        switch (uriMatcher.match(uri)) {
            case TEST_MODEL:
                table = TABLE_TEST;
                cv = values;
                break;
            case TEST_MODEL_ID:
                table = TABLE_TEST;
                cv = values;
                id = Long.parseLong(uri.getPathSegments().get(1));
                if (!TextUtils.isEmpty(selection)) {
                    selection = TEST_MODEL_ID + "=" + id + " AND (" + selection + ")";
                } else {
                    selection = TEST_MODEL_ID + "=" + id;
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            count = db.update(table, cv, selection, selectionArgs);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        return count;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        Log.i(TAG, "delete: " + uri.toString());
        int count = 0;
        String table = "";
        String where = "";
        long id;
        switch (uriMatcher.match(uri)) {
            case TEST_MODEL:
                table = TABLE_TEST;
                where = selection;
                break;
            case TEST_MODEL_ID:
                table = TABLE_TEST;
                where = selection;
                id = Long.parseLong(uri.getPathSegments().get(1));
                if (!TextUtils.isEmpty(selection)) {
                    where = " AND (" + selection + ")";
                }
                where = TABLE_TEST_ID + "=" + id + " " + where;
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            count = db.delete(table, where, selectionArgs);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        return count;
    }

}
