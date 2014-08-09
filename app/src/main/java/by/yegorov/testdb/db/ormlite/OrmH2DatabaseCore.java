package by.yegorov.testdb.db.ormlite;

import android.content.Context;
import android.util.Log;

import com.j256.ormlite.dao.Dao;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import by.yegorov.testdb.db.model.DummyModel;

public class OrmH2DatabaseCore {

    private static final String TAG = OrmH2DatabaseCore.class.toString();

    private static OrmH2DatabaseCore instance;

    private Dao<DummyModel, Long> dummyModelDao;


    public static OrmH2DatabaseCore getInstance(Context context) {
        if (instance == null) {
            instance = new OrmH2DatabaseCore(context);
        }
        return instance;
    }

    public OrmH2DatabaseCore(Context context) {
        OrmH2DatabaseHelper databaseHelper = OrmH2DatabaseHelper.getInstance(context);
        try {
            dummyModelDao = databaseHelper.getDummyModelDao();
        } catch (Exception e) {
            Log.d(TAG, "Error getting daos");
            e.printStackTrace();
        }
    }

    public boolean insertDummyModels(final List<DummyModel> dummyModels) {
        try {
            boolean result;
            if (dummyModels.size() == 1)
                result = dummyModelDao.createOrUpdate(dummyModels.get(0)).isCreated();
            else {
                result = dummyModelDao.callBatchTasks(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        for (DummyModel dummyModel : dummyModels) {
                            dummyModelDao.createOrUpdate(dummyModel);
                        }
                        return true;
                    }
                });
            }
            return result;
        } catch (Exception e) {
            Log.d(TAG, "Error inserting dummyModels");
            e.printStackTrace();
            return false;
        }
    }


    public boolean clearAllDummyModels() {
        try {
            dummyModelDao.deleteBuilder().delete();
            return true;
        } catch (Exception e) {
            Log.d(TAG, "Error clearing database");
            return false;
        }
    }


    public ArrayList<DummyModel> getDummyModels() {
        try {
            return (ArrayList<DummyModel>) dummyModelDao.queryForAll();
        } catch (Exception e) {
            Log.d(TAG, "Error getting dummyModels");
            e.printStackTrace();
        }
        return null;
    }


    public DummyModel getDummyModel(long id) {
        try {
            return dummyModelDao.queryForId(id);
        } catch (Exception e) {
            Log.d(TAG, "Error getting single dummyModel");
            e.printStackTrace();
        }
        return null;
    }


}
