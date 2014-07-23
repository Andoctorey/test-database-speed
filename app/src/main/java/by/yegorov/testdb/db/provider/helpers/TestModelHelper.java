package by.yegorov.testdb.db.provider.helpers;


import by.yegorov.testdb.db.model.TestModel;
import by.yegorov.testdb.db.provider.ColumnStorage;
import by.yegorov.testdb.db.provider.DatabaseProvider;
import by.yegorov.testdb.db.provider.NativeDatabaseHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

public class TestModelHelper implements TestModelConsts {

    public static final String CONTENT_TYPE_ITEM =
            "vnd.android.cursor.item/by.yegorov.testdb.db.provider.DatabaseProvider.test_models";

    public static final String CONTENT_TYPE_DIR =
            "vnd.android.cursor.dir/by.yegorov.testdb.db.provider.DatabaseProvider.test_models";

    public static final Uri TEST_MODEL_URI =
            Uri.parse("content://" + DatabaseProvider.PROVIDER_NAME + "/" + TABLE_TEST);

    public static final List<String> TEST_MODEL_LIST_COLUMNS =
            new ColumnStorage(new String[]{TABLE_TEST_ID, TABLE_TEST_LONG, TABLE_TEST_STRING});

    public static int insertTestModels(Context ctx, List<TestModel> testModels) {
        ContentValues[] contentValuesArray = new ContentValues[testModels.size()];
        for (int i = 0; i < testModels.size(); i++) {
            TestModel entity = testModels.get(i);
            ContentValues cv = new ContentValues();
            cv.put(TABLE_TEST_STRING, entity.getTestString());
            cv.put(TABLE_TEST_LONG, entity.getTestLong());
            contentValuesArray[i] = cv;
        }
        return ctx.getContentResolver().bulkInsert(TEST_MODEL_URI, contentValuesArray);
    }

    private static TestModel parseTestModelCursor(Cursor c) {
        TestModel entity = new TestModel();
        entity.setId(c.getInt(TEST_MODEL_LIST_COLUMNS.indexOf(TABLE_TEST_ID)));
        entity.setTestString(c.getString(TEST_MODEL_LIST_COLUMNS.indexOf(TABLE_TEST_STRING)));
        entity.setTestLong(c.getLong(TEST_MODEL_LIST_COLUMNS.indexOf(TABLE_TEST_LONG)));
        return entity;
    }

    public static ArrayList<TestModel> getTestModels(Context ctx) {
        ArrayList<TestModel> testModels = new ArrayList<TestModel>();
        Cursor c = ctx.getContentResolver().query(TEST_MODEL_URI, null, null, null, null);
        if (c.getCount() != 0) {
            while (c.moveToNext()) {
                TestModel entity = parseTestModelCursor(c);
                testModels.add(entity);
            }
        }
        c.close();
        return testModels;
    }

    public static void clearTestModels(Context ctx) {
        NativeDatabaseHelper.getInstance(ctx).getWritableDatabase().delete(TABLE_TEST, null, null);
    }


}
