package by.yegorov.testdb.db.ormlite;

import android.content.Context;
import android.util.Log;

import com.j256.ormlite.dao.Dao;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import by.yegorov.testdb.db.model.TestModel;

public class OrmH2DatabaseCore {

    private static final String TAG = OrmH2DatabaseCore.class.toString();

    private static OrmH2DatabaseCore instance;

    private OrmH2DatabaseHelper databaseHelper;

    private Dao<TestModel, Long> testModelDao;


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

    public boolean insertTestModels(final List<TestModel> testModels) {
        try {
            return testModelDao.callBatchTasks(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    for (TestModel testModel : testModels) {
                        testModelDao.createOrUpdate(testModel);
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


    public ArrayList<TestModel> getTestModels() {
        try {
            return (ArrayList<TestModel>) testModelDao.queryForAll();
        } catch (Exception e) {
            Log.d(TAG, "Error getting testModels");
            e.printStackTrace();
        }
        return null;
    }


    public TestModel getTestModel(long id) {
        try {
            TestModel testModel = testModelDao.queryForId(id);
            // testModel.setImages(getTestModelImages(testModel.getId()));
            return testModel;
        } catch (Exception e) {
            Log.d(TAG, "Error getting single testModel");
            e.printStackTrace();
        }
        return null;
    }


}
