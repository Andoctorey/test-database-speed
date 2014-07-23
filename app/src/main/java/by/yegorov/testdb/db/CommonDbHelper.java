package by.yegorov.testdb.db;

import by.yegorov.testdb.db.model.TestModel;
import by.yegorov.testdb.db.ormlite.DatabaseResultReceiver;
import by.yegorov.testdb.db.ormlite.DatabaseService;
import by.yegorov.testdb.db.ormlite.OrmDatabaseHelper;
import by.yegorov.testdb.db.provider.NativeDatabaseHelper;
import by.yegorov.testdb.db.provider.helpers.TestModelHelper;

import android.content.Context;
import android.os.Bundle;

import java.util.ArrayList;

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

    public static void getTestModels(Context context, DbType dbType,
                                     DatabaseResultReceiver receiver) {
        if (dbType.equals(DbType.NATIVE)) {
            Bundle bundleExtra = new Bundle();
            bundleExtra.putSerializable(DATA, TestModelHelper.getTestModels(context));
            receiver.send(200, bundleExtra);
        } else if (dbType.equals(DbType.ORMLITE_SQL)) {
            DatabaseService.getTestModels(context, receiver);
        }
    }

    public static void insertTestModels(Context context, DbType dbType,
                                        ArrayList<TestModel> testModels,
                                        DatabaseResultReceiver receiver) {
        if (dbType.equals(DbType.NATIVE)) {
            int rows = TestModelHelper.insertTestModels(context, testModels);
            Bundle bundleExtra = new Bundle();
            bundleExtra.putSerializable(DATA, rows == testModels.size());
            receiver.send(200, bundleExtra);
        } else if (dbType.equals(DbType.ORMLITE_SQL)) {
            DatabaseService.insertTestModels(context, testModels, receiver);
        }
    }

    public static void clearTestModels(Context context, DbType dbType,
                                       DatabaseResultReceiver receiver) {
        if (dbType.equals(DbType.NATIVE)) {
            TestModelHelper.clearTestModels(context);
            Bundle bundleExtra = new Bundle();
            bundleExtra.putSerializable(DATA, true);
            receiver.send(200, bundleExtra);
        } else if (dbType.equals(DbType.ORMLITE_SQL)) {
            DatabaseService.clearTestModels(context, receiver);
        }
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
