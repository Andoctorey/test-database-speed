package by.yegorov.testdb.db;

import android.content.Context;

import java.util.ArrayList;

import by.yegorov.testdb.db.model.TestModel;
import by.yegorov.testdb.db.ormlite.DatabaseCore;
import by.yegorov.testdb.db.ormlite.OrmDatabaseHelper;
import by.yegorov.testdb.db.provider.NativeDatabaseHelper;
import by.yegorov.testdb.db.provider.helpers.TestModelHelper;

public class CommonDbHelper {
    public static final String DATA = "data";


    public enum DbType {
        NATIVE("native"), ORMLITE_SQL("ormlite+sql"), ORMLITE_H2("ormlite+h2");

        private String name;

        private DbType(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    public static ArrayList<TestModel> getTestModels(Context context, DbType dbType) {
        ArrayList<TestModel> testModels = null;
        if (dbType.equals(DbType.NATIVE)) {
            testModels = TestModelHelper.getTestModels(context);
        } else if (dbType.equals(DbType.ORMLITE_SQL)) {
            testModels = DatabaseCore.getInstance(context).getTestModels();
        }
        return testModels;
    }

    public static boolean insertTestModels(Context context, DbType dbType,
                                           ArrayList<TestModel> testModels) {
        boolean result = false;
        if (dbType.equals(DbType.NATIVE)) {
            result = TestModelHelper.insertTestModels(context, testModels) == testModels.size();
        } else if (dbType.equals(DbType.ORMLITE_SQL)) {
            result = DatabaseCore.getInstance(context).insertTestModels(testModels);
        }
        return result;
    }

    public static boolean clearTestModels(Context context, DbType dbType) {
        boolean result = false;
        if (dbType.equals(DbType.NATIVE)) {
            TestModelHelper.clearTestModels(context);
            result = true;
        } else if (dbType.equals(DbType.ORMLITE_SQL)) {
            result = DatabaseCore.getInstance(context).clearAllTestModels();
        }
        return result;
    }

    public static long getDatabaseSize(Context context, DbType dbType) {
        if (dbType.equals(DbType.NATIVE)) {
            return NativeDatabaseHelper.getInstance(context).getDatabaseSize();
        } else if (dbType.equals(DbType.ORMLITE_SQL)) {
            return OrmDatabaseHelper.getInstance(context).getDatabaseSize();
        }
        return 0;
    }
}
