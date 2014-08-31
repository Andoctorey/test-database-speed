package by.yegorov.testdb.db.greendao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import by.yegorov.testdb.db.model.DummyModel;

public class GreenDaoDatabaseCore {

    private static final String TAG = GreenDaoDatabaseCore.class.toString();
    private static final String DATABASE_NAME = "green_dao.db";
    private static GreenDaoDatabaseCore instance;
    private final Context context;

    private DummyModelDao dummyModelDao;


    public static GreenDaoDatabaseCore getInstance(Context context) {
        if (instance == null) {
            instance = new GreenDaoDatabaseCore(context);
        }
        return instance;
    }

    public GreenDaoDatabaseCore(Context context) {
        this.context = context;
        try {
            DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, DATABASE_NAME, null);
            SQLiteDatabase db = helper.getWritableDatabase();
            DaoMaster daoMaster = new DaoMaster(db);
            DaoSession daoSession = daoMaster.newSession();
            dummyModelDao = daoSession.getDummyModelDao();
        } catch (Exception e) {
            Log.d(TAG, "Error getting daos");
            e.printStackTrace();
        }
    }

    public boolean insertDummyModels(final List<DummyModel> dummyModels) {
        try {
            if (dummyModels.size() == 1)
                dummyModelDao.insert(dummyModels.get(0));
            else {
                dummyModelDao.insertInTx(dummyModels);
            }
            return true;
        } catch (Exception e) {
            Log.d(TAG, "Error inserting dummyModels");
            e.printStackTrace();
            return false;
        }
    }


    public boolean clearAllDummyModels() {
        try {
            dummyModelDao.deleteAll();
            return true;
        } catch (Exception e) {
            Log.d(TAG, "Error clearing database");
            return false;
        }
    }


    public ArrayList<DummyModel> getDummyModels() {
        try {
            return (ArrayList<DummyModel>) dummyModelDao.loadAll();
        } catch (Exception e) {
            Log.d(TAG, "Error getting dummyModels");
            e.printStackTrace();
        }
        return null;
    }


    public DummyModel getDummyModel(long id) {
        try {
            return dummyModelDao.queryBuilder().where(DummyModelDao.Properties.Id.eq(id)).build().unique();
        } catch (Exception e) {
            Log.d(TAG, "Error getting single dummyModel");
            e.printStackTrace();
        }
        return null;
    }

    public long getDatabaseSize() {
        return context.getDatabasePath(DATABASE_NAME).length();
    }

}
