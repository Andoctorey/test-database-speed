
package by.yegorov.testdb.db;

import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.util.Log;

import java.io.Serializable;

public abstract class DatabaseResultReceiver<T> extends ResultReceiver {

    private static final String TAG = ResultReceiver.class.getSimpleName();

    public DatabaseResultReceiver() {
        super(new Handler());
    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {
        Serializable data = resultData.getSerializable(DatabaseService.DATA);
        long time = resultData.getLong(DatabaseService.TIME);
        try {
            resultReceived((T) data, time);
        } catch (Exception e) {
            Log.d(TAG, "Error: wrong data type");
            e.printStackTrace();
        }
    }

    public abstract void resultReceived(T result, long time);

}
