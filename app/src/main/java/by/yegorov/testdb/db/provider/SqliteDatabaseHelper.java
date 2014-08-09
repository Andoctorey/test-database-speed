package by.yegorov.testdb.db.provider;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.HashMap;

import by.yegorov.testdb.db.provider.helpers.DummyModelConstants;

public class SqliteDatabaseHelper extends SQLiteOpenHelper implements DummyModelConstants {

    public final static String DATABASE_NAME = "native.db";

    private final static int DATABASE_VERSION = 1;

    private static SqliteDatabaseHelper instance;

    private static final HashMap<String, String> TABLES_CREATE = new HashMap<>();

    static {
        TABLES_CREATE.put(TABLE_TEST, "CREATE TABLE " + TABLE_TEST + " (" + TABLE_TEST_ID +
                " INTEGER PRIMARY KEY AUTOINCREMENT, " + TABLE_TEST_LONG + " REAL, " +
                TABLE_TEST_STRING + " TEXT);");
    }

    private final Context context;


    public static SqliteDatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new SqliteDatabaseHelper(context);
        }
        return instance;
    }

    private SqliteDatabaseHelper(Context ctx) {
        this(ctx, DATABASE_NAME, null, DATABASE_VERSION);
    }

    private SqliteDatabaseHelper(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context = context;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        for (String sql : TABLES_CREATE.values()) {
            db.execSQL(sql);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE " + TABLE_TEST + ";");
        db.execSQL(TABLES_CREATE.get(TABLE_TEST));
    }


    public long getDatabaseSize() {
        return context.getDatabasePath(DATABASE_NAME).length();
    }
}
