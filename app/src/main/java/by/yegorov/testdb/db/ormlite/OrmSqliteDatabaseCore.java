package by.yegorov.testdb.db.ormlite;

import android.content.Context;
import android.util.Log;

import com.j256.ormlite.dao.Dao;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import by.yegorov.testdb.db.model.DummyModel;

public class OrmSqliteDatabaseCore {

    private static final String TAG = OrmSqliteDatabaseCore.class.toString();

    private static OrmSqliteDatabaseCore instance;

    private OrmSqliteDatabaseHelper databaseHelper;

    private Dao<DummyModel, Long> testModelDao;


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
