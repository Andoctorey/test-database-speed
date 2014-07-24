package by.yegorov.testdb.db.ormlite;

import android.content.Context;
import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

import by.yegorov.testdb.db.model.TestModel;


public class OrmH2DatabaseHelper {

    private static final String TAG = OrmH2DatabaseHelper.class.toString();

    private static final String DATABASE_NAME = "orm_h2";


    private static final int DATABASE_VERSION = 1;

    private static OrmH2DatabaseHelper instance;

    private Context context;

    private Dao<TestModel, Long> testModelsDao;


    private OrmH2DatabaseHelper(Context context) throws SQLException {
        this.context = context;
        ConnectionSource connectionSource = null;
        try {
            connectionSource = new JdbcConnectionSource("jdbc:h2:" + context.getFilesDir() + "/" + DATABASE_NAME);
            testModelsDao = DaoManager.createDao(connectionSource, TestModel.class);
            TableUtils.createTableIfNotExists(connectionSource, TestModel.class);
            Log.d(TAG, "Database initialised");
        } finally {
            // destroy the data source which should close underlying connections
            if (connectionSource != null) {
                connectionSource.close();
            }
        }
    }

    public static OrmH2DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            try {
                Log.d(TAG, "Initialising database");
                instance = new OrmH2DatabaseHelper(context);
            } catch (SQLException e) {
                Log.d(TAG, "Error while initialising database");
                e.printStackTrace();
            }
        }
        return instance;
    }


    public Dao<TestModel, Long> getTestModelDao() throws SQLException {
        return testModelsDao;
    }

    public long getDatabaseSize() {
        return context.getFileStreamPath(DATABASE_NAME + ".mv.db").length();
    }
}

