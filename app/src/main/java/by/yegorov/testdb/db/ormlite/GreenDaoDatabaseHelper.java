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


public class GreenDaoDatabaseHelper {

    private static final String TAG = GreenDaoDatabaseHelper.class.toString();

    private static final String DATABASE_NAME = "green.db";

    private static GreenDaoDatabaseHelper instance;

    private Context context;

    private Dao<DummyModel, Long> dummyModelsDao;


    private GreenDaoDatabaseHelper(Context context) throws SQLException {
        this.context = context;
        DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, DATABASE_NAME, null);
        db = helper.getWritableDatabase();
        daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
        noteDao = daoSession.getNoteDao();
        ConnectionSource connectionSource = null;
        try {
            connectionSource = new JdbcConnectionSource("jdbc:h2:" + context.getFilesDir() + "/" + DATABASE_NAME +
                    ";FILE_LOCK=FS" + ";PAGE_SIZE=1024" + ";CACHE_SIZE=8192");
            dummyModelsDao = DaoManager.createDao(connectionSource, DummyModel.class);
            TableUtils.createTableIfNotExists(connectionSource, DummyModel.class);
            Log.d(TAG, "Database initialised");
        } finally {
            if (connectionSource != null) {
                connectionSource.close();
            }
        }
    }

    public static GreenDaoDatabaseHelper getInstance(Context context) {
        if (instance == null) {
            try {
                Log.d(TAG, "Initialising database");
                instance = new GreenDaoDatabaseHelper(context);
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

