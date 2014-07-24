package by.yegorov.testdb.db;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;

import by.yegorov.testdb.db.model.TestModel;

public class DatabaseService extends IntentService {

    public static final String RECEIVER = "receiver";

    public static final String DATA = "data";

    public static final String TIME = "time";

    private static final String DB_TYPE = "db_type";

    private static final String TAG = DatabaseService.class.getName();

    public static final String ACTION_GET_TEST_MODELS = "by.yegorov.testdb.ACTION_GET_TEST_MODELS";

    public static final String ACTION_INSERT_APPS = "by.yegorov.testdb.ACTION_INSERT_APPS";

    public static final String ACTION_CLEAR_TEST_MODELS = "by.yegorov.testdb.ACTION_CLEAR_TEST_MODELS";

    public DatabaseService() {
        super(TAG);
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        String action = intent.getAction();
        final ResultReceiver receiver = intent.getParcelableExtra(RECEIVER);
        Serializable data = intent.getSerializableExtra(DATA);
        CommonDbHelper.DbType dbType = (CommonDbHelper.DbType) intent.getSerializableExtra(DB_TYPE);
        Serializable result;
        long start = System.currentTimeMillis();
        if (ACTION_GET_TEST_MODELS.equalsIgnoreCase(action)) {
            result = CommonDbHelper.getTestModels(this, dbType);
        } else if (ACTION_INSERT_APPS.equalsIgnoreCase(action)) {
            result = CommonDbHelper.insertTestModels(this, dbType, (ArrayList<TestModel>) data);
        } else if (ACTION_CLEAR_TEST_MODELS.equalsIgnoreCase(action)) {
            result = CommonDbHelper.clearTestModels(this, dbType);
        } else {
            Log.d(TAG, "Error: unsupported action (" + action + ")");
            return;
        }
        if (receiver != null) {
            Bundle bundleExtra = new Bundle();
            bundleExtra.putSerializable(DATA, result);
            bundleExtra.putLong(TIME, System.currentTimeMillis() - start);
            receiver.send(200, bundleExtra);
        }

    }

    public static void getTestModels(Context context, CommonDbHelper.DbType dbType,
                                     DatabaseResultReceiver receiver) {
        Intent intent = new Intent(context, DatabaseService.class);
        intent.setAction(ACTION_GET_TEST_MODELS);
        intent.putExtra(RECEIVER, receiver);
        intent.putExtra(DB_TYPE, dbType);
        context.startService(intent);
    }

    public static void insertTestModels(Context context, CommonDbHelper.DbType dbType,
                                        ArrayList<TestModel> testModels,
                                        DatabaseResultReceiver receiver) {
        Intent intent = new Intent(context, DatabaseService.class);
        intent.setAction(ACTION_INSERT_APPS);
        intent.putExtra(RECEIVER, receiver);
        intent.putExtra(DATA, testModels);
        intent.putExtra(DB_TYPE, dbType);
        context.startService(intent);
    }

    public static void clearTestModels(Context context, CommonDbHelper.DbType dbType,
                                       DatabaseResultReceiver receiver) {
        Intent intent = new Intent(context, DatabaseService.class);
        intent.setAction(ACTION_CLEAR_TEST_MODELS);
        intent.putExtra(RECEIVER, receiver);
        intent.putExtra(DB_TYPE, dbType);
        context.startService(intent);
    }

}
