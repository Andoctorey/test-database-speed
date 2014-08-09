package by.yegorov.testdb.db;

import android.content.Context;

import java.util.ArrayList;

import by.yegorov.testdb.db.model.DummyModel;
import by.yegorov.testdb.db.ormlite.OrmH2DatabaseCore;
import by.yegorov.testdb.db.ormlite.OrmH2DatabaseHelper;
import by.yegorov.testdb.db.ormlite.OrmSqliteDatabaseCore;
import by.yegorov.testdb.db.ormlite.OrmSqliteDatabaseHelper;
import by.yegorov.testdb.db.provider.SqliteDatabaseHelper;
import by.yegorov.testdb.db.provider.helpers.DummyModelHelper;

public class CommonDbHelper {
    public enum DbType {
        NATIVE("native"), ORMLITE_SQLITE("ormlite+sql"), ORMLITE_H2("ormlite+h2");

        private final String name;

        private DbType(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    public static ArrayList<DummyModel> getDummyModels(Context context, DbType dbType) {
        ArrayList<DummyModel> dummyModels = null;
        if (dbType.equals(DbType.NATIVE)) {
            dummyModels = DummyModelHelper.getDummyModels(context);
        } else if (dbType.equals(DbType.ORMLITE_SQLITE)) {
            dummyModels = OrmSqliteDatabaseCore.getInstance(context).getDummyModels();
        } else if (dbType.equals(DbType.ORMLITE_H2)) {
            dummyModels = OrmH2DatabaseCore.getInstance(context).getDummyModels();
        }
        return dummyModels;
    }

    public static boolean insertDummyModels(Context context, DbType dbType, ArrayList<DummyModel> dummyModels) {
        boolean result = false;
        if (dbType.equals(DbType.NATIVE)) {
            try {
                result = DummyModelHelper.insertDummyModels(context, dummyModels) == dummyModels.size();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (dbType.equals(DbType.ORMLITE_SQLITE)) {
            result = OrmSqliteDatabaseCore.getInstance(context).insertDummyModels(dummyModels);
        } else if (dbType.equals(DbType.ORMLITE_H2)) {
            result = OrmH2DatabaseCore.getInstance(context).insertDummyModels(dummyModels);
        }
        return result;
    }

    public static boolean clearDummyModels(Context context, DbType dbType) {
        boolean result = false;
        if (dbType.equals(DbType.NATIVE)) {
            DummyModelHelper.clearDummyModels(context);
            result = true;
        } else if (dbType.equals(DbType.ORMLITE_SQLITE)) {
            result = OrmSqliteDatabaseCore.getInstance(context).clearAllDummyModels();
        } else if (dbType.equals(DbType.ORMLITE_H2)) {
            result = OrmH2DatabaseCore.getInstance(context).clearAllDummyModels();
        }
        return result;
    }

    public static long getDatabaseSize(Context context, DbType dbType) {
        if (dbType.equals(DbType.NATIVE)) {
            return SqliteDatabaseHelper.getInstance(context).getDatabaseSize();
        } else if (dbType.equals(DbType.ORMLITE_SQLITE)) {
            return OrmSqliteDatabaseHelper.getInstance(context).getDatabaseSize();
        } else if (dbType.equals(DbType.ORMLITE_H2)) {
            return OrmH2DatabaseHelper.getInstance(context).getDatabaseSize();
        }
        return 0;
    }
}
