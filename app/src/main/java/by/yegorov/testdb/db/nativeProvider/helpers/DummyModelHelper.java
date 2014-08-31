package by.yegorov.testdb.db.nativeProvider.helpers;


import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.net.Uri;
import android.os.RemoteException;

import java.util.ArrayList;
import java.util.List;

import by.yegorov.testdb.db.model.DummyModel;
import by.yegorov.testdb.db.nativeProvider.ColumnStorage;
import by.yegorov.testdb.db.nativeProvider.DatabaseProvider;
import by.yegorov.testdb.db.nativeProvider.SqliteDatabaseHelper;

public class DummyModelHelper implements DummyModelConstants {

    public static final String CONTENT_TYPE_ITEM =
            "vnd.android.cursor.item/by.yegorov.testdb.db.provider.DatabaseProvider.dummy_models";

    public static final String CONTENT_TYPE_DIR =
            "vnd.android.cursor.dir/by.yegorov.testdb.db.provider.DatabaseProvider.dummy_models";

    public static final Uri DUMMY_MODEL_URI =
            Uri.parse("content://" + DatabaseProvider.PROVIDER_NAME + "/" + TABLE_TEST);

    public static final List<String> TEST_MODEL_LIST_COLUMNS =
            new ColumnStorage(new String[]{TABLE_TEST_ID, TABLE_TEST_LONG, TABLE_TEST_STRING});

    public static int insertDummyModels(Context ctx, List<DummyModel> dummyModels) throws RemoteException, OperationApplicationException {
        ArrayList<ContentProviderOperation> operations = new ArrayList<>();
        for (DummyModel entity : dummyModels) {
            ContentValues cv = new ContentValues();
            cv.put(TABLE_TEST_STRING, entity.getDummyString());
            cv.put(TABLE_TEST_LONG, entity.getDummyLong());
            operations.add(ContentProviderOperation.newInsert(DUMMY_MODEL_URI)
                    .withValues(cv).withYieldAllowed(true).build());
        }
        return ctx.getContentResolver().applyBatch(DUMMY_MODEL_URI.getAuthority(), operations).length;
    }

    private static DummyModel parseDummyModelCursor(Cursor c) {
        DummyModel entity = new DummyModel();
        entity.setId(c.getLong(TEST_MODEL_LIST_COLUMNS.indexOf(TABLE_TEST_ID)));
        entity.setDummyString(c.getString(TEST_MODEL_LIST_COLUMNS.indexOf(TABLE_TEST_STRING)));
        entity.setDummyLong(c.getLong(TEST_MODEL_LIST_COLUMNS.indexOf(TABLE_TEST_LONG)));
        return entity;
    }

    public static ArrayList<DummyModel> getDummyModels(Context ctx) {
        ArrayList<DummyModel> dummyModels = new ArrayList<>();
        Cursor c = ctx.getContentResolver().query(DUMMY_MODEL_URI, null, null, null, null);
        if (c.getCount() != 0) {
            while (c.moveToNext()) {
                DummyModel entity = parseDummyModelCursor(c);
                dummyModels.add(entity);
            }
        }
        c.close();
        return dummyModels;
    }

    public static void clearDummyModels(Context ctx) {
        SqliteDatabaseHelper.getInstance(ctx).getWritableDatabase().delete(TABLE_TEST, null, null);
    }


}
