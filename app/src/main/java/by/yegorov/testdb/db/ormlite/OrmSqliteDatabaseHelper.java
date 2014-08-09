package by.yegorov.testdb.db.ormlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

import by.yegorov.testdb.db.model.DummyModel;


public class OrmSqliteDatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String TAG = OrmSqliteDatabaseHelper.class.toString();

    private static final String DATABASE_NAME = "orm_sqlite.db";

    private static final int DATABASE_VERSION = 1;

    private static OrmSqliteDatabaseHelper instance;

    private Context context;

    private Dao<DummyModel, Long> testModelsDao;


    private OrmSqliteDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    public static OrmSqliteDatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new OrmSqliteDatabaseHelper(context);
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        Log.d(TAG, "Initialising database");
        try {
            TableUtils.createTable(connectionSource, DummyModel.class);
            Log.d(TAG, "Database initialised");
        } catch (Exception e) {
            Log.d(TAG, "Error while initialising database");
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource,
                          int oldVersion, int newVersion) {
        if (newVersion > oldVersion) {
            Log.d(TAG, "Upgrading database");
            clearAll(database);
        }
    }

    public void clearAll(SQLiteDatabase database) {
        try {
            if (database == null)
                database = getWritableDatabase();
            TableUtils.dropTable(connectionSource, DummyModel.class, true);
            onCreate(database, connectionSource);
            Log.d(TAG, "Database upgraded successfully");
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "Error while upgrading database");
        }
    }

    public Dao<DummyModel, Long> getTestModelDao() throws SQLException {
        if (testModelsDao == null) {
            testModelsDao = getDao(DummyModel.class);
        }
        return testModelsDao;
    }

    public long getDatabaseSize() {
        return context.getDatabasePath(DATABASE_NAME).length();
    }
}

