package by.yegorov.testdb.db.ormlite;

import android.content.Context;
import android.util.Log;

import com.j256.ormlite.dao.Dao;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import by.yegorov.testdb.db.model.TestModel;

public class OrmSqliteDatabaseCore {

    private static final String TAG = OrmSqliteDatabaseCore.class.toString();

    private static OrmSqliteDatabaseCore instance;

    private OrmSqliteDatabaseHelper databaseHelper;

    private Dao<TestModel, Long> testModelDao;


    public static OrmSqliteDatabaseCore getInstance(Context context) {
        if (instance == null) {
            instance = new OrmSqliteDatabaseCore(context);
        }
        return instance;
    }

    public OrmSqliteDatabaseCore(Context context) {
        databaseHelper = OrmSqliteDatabaseHelper.getInstance(context);
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
