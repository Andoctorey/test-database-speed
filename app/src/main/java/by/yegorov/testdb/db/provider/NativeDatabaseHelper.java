package by.yegorov.testdb.db.provider;


import by.yegorov.testdb.db.provider.helpers.TestModelConsts;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.HashMap;

public class NativeDatabaseHelper extends SQLiteOpenHelper implements TestModelConsts {

    public final static String DATABASE_NAME = "native_test.db";

    private final static int DATABASE_VERSION = 1;

    private static NativeDatabaseHelper instance;

    private static HashMap<String, String> TABLES_CREATE = new HashMap<String, String>();

    static {
        TABLES_CREATE.put(TABLE_TEST, "CREATE TABLE " + TABLE_TEST + " (" + TABLE_TEST_ID +
                " INTEGER PRIMARY KEY AUTOINCREMENT, " + TABLE_TEST_LONG + " REAL, " +
                TABLE_TEST_STRING + " TEXT);");
    }

    private final Context context;

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE " + TABLE_TEST + ";");
        db.execSQL(TABLES_CREATE.get(TABLE_TEST));
    }

    public static NativeDatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new NativeDatabaseHelper(context);
        }
        return instance;
    }

    private NativeDatabaseHelper(Context ctx) {
        this(ctx, DATABASE_NAME, null, DATABASE_VERSION);
    }

    private NativeDatabaseHelper(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        for (String sql : TABLES_CREATE.values()) {
            db.execSQL(sql);
        }
    }

    public long getDatabaseSize() {
        return context.getDatabasePath(DATABASE_NAME).length();
    }
}
