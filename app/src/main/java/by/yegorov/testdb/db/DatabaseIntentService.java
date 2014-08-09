package by.yegorov.testdb.db;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;

import by.yegorov.testdb.db.model.DummyModel;

public class DatabaseIntentService extends IntentService {

    public static final String RECEIVER = "receiver";

    public static final String DATA = "data";

    public static final String TIME = "time";

    private static final String DB_TYPE = "db_type";

    private static final String TAG = DatabaseIntentService.class.getName();

    public static final String ACTION_GET_DUMMY_MODELS = "by.yegorov.testdb.ACTION_GET_DUMMY_MODELS";

    public static final String ACTION_INSERT_DUMMY_MODELS = "by.yegorov.testdb.ACTION_INSERT_DUMMY_MODELS";

    public static final String ACTION_CLEAR_DUMMY_MODELS = "by.yegorov.testdb.ACTION_CLEAR_DUMMY_MODELS";

    public DatabaseIntentService() {
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
        if (ACTION_GET_DUMMY_MODELS.equalsIgnoreCase(action)) {
            result = CommonDbHelper.getDummyModels(this, dbType);
        } else if (ACTION_INSERT_DUMMY_MODELS.equalsIgnoreCase(action)) {
            result = CommonDbHelper.insertDummyModels(this, dbType, (ArrayList<DummyModel>) data);
        } else if (ACTION_CLEAR_DUMMY_MODELS.equalsIgnoreCase(action)) {
            result = CommonDbHelper.clearDummyModels(this, dbType);
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

    public static void getDummyModels(Context context, CommonDbHelper.DbType dbType,
                                     DatabaseResultReceiver receiver) {
        Intent intent = new Intent(context, DatabaseIntentService.class);
        intent.setAction(ACTION_GET_DUMMY_MODELS);
        intent.putExtra(RECEIVER, receiver);
        intent.putExtra(DB_TYPE, dbType);
        context.startService(intent);
    }

    public static void insertDummyModels(Context context, CommonDbHelper.DbType dbType,
                                        ArrayList<DummyModel> dummyModels,
                                        DatabaseResultReceiver receiver) {
        Intent intent = new Intent(context, DatabaseIntentService.class);
        intent.setAction(ACTION_INSERT_DUMMY_MODELS);
        intent.putExtra(RECEIVER, receiver);
        intent.putExtra(DATA, dummyModels);
        intent.putExtra(DB_TYPE, dbType);
        context.startService(intent);
    }

    public static void clearDummyModels(Context context, CommonDbHelper.DbType dbType,
                                       DatabaseResultReceiver receiver) {
        Intent intent = new Intent(context, DatabaseIntentService.class);
        intent.setAction(ACTION_CLEAR_DUMMY_MODELS);
        intent.putExtra(RECEIVER, receiver);
        intent.putExtra(DB_TYPE, dbType);
        context.startService(intent);
    }

}
