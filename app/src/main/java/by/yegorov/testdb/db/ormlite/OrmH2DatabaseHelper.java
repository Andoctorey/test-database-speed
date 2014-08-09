package by.yegorov.testdb.db.ormlite;

import android.content.Context;
import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

import by.yegorov.testdb.db.model.DummyModel;


public class OrmH2DatabaseHelper {

    private static final String TAG = OrmH2DatabaseHelper.class.toString();

    private static final String DATABASE_NAME = "orm_h2";

    private static OrmH2DatabaseHelper instance;

    private Context context;

    private Dao<DummyModel, Long> dummyModelsDao;


    private OrmH2DatabaseHelper(Context context) throws SQLException {
        this.context = context;
        ConnectionSource connectionSource = null;
        try {
            connectionSource = new JdbcConnectionSource("jdbc:h2:" + context.getFilesDir() + "/" + DATABASE_NAME);
            dummyModelsDao = DaoManager.createDao(connectionSource, DummyModel.class);
            TableUtils.createTableIfNotExists(connectionSource, DummyModel.class);
            Log.d(TAG, "Database initialised");
        } finally {
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


    public Dao<DummyModel, Long> getDummyModelDao() throws SQLException {
        return dummyModelsDao;
    }

    public long getDatabaseSize() {
        return context.getFileStreamPath(DATABASE_NAME + ".mv.db").length();
    }
}

