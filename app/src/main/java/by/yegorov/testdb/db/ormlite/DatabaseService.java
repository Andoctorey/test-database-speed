package by.yegorov.testdb.db.ormlite;

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

    private static final String EXTRA_ID = "id";

    private static final String TAG = DatabaseService.class.getName();

    public static final String ACTION_GET_TEST_MODELS = "by.wimix.promo.ACTION_GET_TEST_MODELS";

    public static final String ACTION_INSERT_APPS = "by.wimix.promo.ACTION_INSERT_APPS";

    public static final String ACTION_CLEAR_TEST_MODELS = "by.wimix.promo.ACTION_CLEAR_TEST_MODELS";

    private DatabaseCore database;

    public DatabaseService() {
        super(TAG);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        database = DatabaseCore.getInstance(getApplicationContext());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String action = intent.getAction();
        final ResultReceiver receiver = intent.getParcelableExtra(RECEIVER);
        Serializable extra = intent.getSerializableExtra(EXTRA_ID);
        Serializable data;
        if (ACTION_GET_TEST_MODELS.equalsIgnoreCase(action)) {
            data = database.getTestModels();
        } else if (ACTION_INSERT_APPS.equalsIgnoreCase(action)) {
            data = database.insertTestModels((ArrayList<TestModel>) extra);
        } else if (ACTION_CLEAR_TEST_MODELS.equalsIgnoreCase(action)) {
            data = database.clearAllTestModels();
        } else {
            Log.d(TAG, "Error: unsupported action (" + action + ")");
            return;
        }
        if (receiver != null) {
            Bundle bundleExtra = new Bundle();
            bundleExtra.putSerializable(DATA, data);
            receiver.send(200, bundleExtra);
        }

    }

    public static void getTestModels(Context context,  DatabaseResultReceiver receiver) {
        Intent intent = new Intent(context, DatabaseService.class);
        intent.setAction(ACTION_GET_TEST_MODELS);
        intent.putExtra(RECEIVER, receiver);
        context.startService(intent);
    }

    public static void insertTestModels(Context context, ArrayList<TestModel> testModels,
                                        DatabaseResultReceiver receiver) {
        Intent intent = new Intent(context, DatabaseService.class);
        intent.setAction(ACTION_INSERT_APPS);
        intent.putExtra(RECEIVER, receiver);
        intent.putExtra(EXTRA_ID, testModels);
        context.startService(intent);
    }

    public static void clearTestModels(Context context,                                       DatabaseResultReceiver receiver) {
        Intent intent = new Intent(context, DatabaseService.class);
        intent.setAction(ACTION_CLEAR_TEST_MODELS);
        intent.putExtra(RECEIVER, receiver);
        context.startService(intent);
    }

}
