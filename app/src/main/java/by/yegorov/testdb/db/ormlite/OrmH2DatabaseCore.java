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

    private OrmH2DatabaseHelper databaseHelper;

    private Dao<DummyModel, Long> testModelDao;


    public static OrmH2DatabaseCore getInstance(Context context) {
        if (instance == null) {
            instance = new OrmH2DatabaseCore(context);
        }
        return instance;
    }

    public OrmH2DatabaseCore(Context context) {
        databaseHelper = OrmH2DatabaseHelper.getInstance(context);
        try {
            testModelDao = databaseHelper.getTestModelDao();
        } catch (Exception e) {
            Log.d(TAG, "Error getting daos");
            e.printStackTrace();
        }
    }

    public boolean insertTestModels(final List<DummyModel> dummyModels) {
        try {
            return testModelDao.callBatchTasks(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    for (DummyModel dummyModel : dummyModels) {
                        testModelDao.createOrUpdate(dummyModel);
                    }
                    return true;
                }
            });
        } catch (Exception e) {
            Log.d(TAG, "Error inserting testModels");
            e.printStackTrace();
            return false;
        }
    }


    public boolean clearAllTestModels() {
        try {
            testModelDao.deleteBuilder().delete();
            return true;
        } catch (Exception e) {
            Log.d(TAG, "Error clearing database");
            return false;
        }
    }


    public ArrayList<DummyModel> getTestModels() {
        try {
            return (ArrayList<DummyModel>) testModelDao.queryForAll();
        } catch (Exception e) {
            Log.d(TAG, "Error getting testModels");
            e.printStackTrace();
        }
        return null;
    }


    public DummyModel getTestModel(long id) {
        try {
            DummyModel dummyModel = testModelDao.queryForId(id);
            // testModel.setImages(getTestModelImages(testModel.getId()));
            return dummyModel;
        } catch (Exception e) {
            Log.d(TAG, "Error getting single testModel");
            e.printStackTrace();
        }
        return null;
    }


}
