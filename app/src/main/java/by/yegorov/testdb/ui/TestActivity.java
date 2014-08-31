package by.yegorov.testdb.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import by.yegorov.testdb.R;
import by.yegorov.testdb.db.CommonDbHelper;
import by.yegorov.testdb.db.DatabaseIntentService;
import by.yegorov.testdb.db.DatabaseResultReceiver;
import by.yegorov.testdb.db.model.DummyModel;


public class TestActivity extends Activity implements View.OnClickListener {

    private int recordsCount;
    private EditText etCount;
    private LogAdapter logAdapter;
    private CheckBox chNative;
    private CheckBox chH2;
    private CheckBox chOrmSql;
    private CheckBox chGreenDao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        initContentView();
    }

    private void initContentView() {
        chNative = (CheckBox) findViewById(R.id.ch_native);
        chNative.setText(CommonDbHelper.DbType.NATIVE.toString());
        chOrmSql = (CheckBox) findViewById(R.id.ch_orm_sql);
        chOrmSql.setText(CommonDbHelper.DbType.ORMLITE_SQLITE.toString());
        chH2 = (CheckBox) findViewById(R.id.ch_orm_h2);
        chH2.setText(CommonDbHelper.DbType.ORMLITE_H2.toString());
        chGreenDao = (CheckBox) findViewById(R.id.ch_green_dao);
        chGreenDao.setText(CommonDbHelper.DbType.GREENDAO.toString());
        etCount = (EditText) findViewById(R.id.et_count);
        findViewById(R.id.bt_get_all).setOnClickListener(this);
        findViewById(R.id.bt_clear_all).setOnClickListener(this);
        findViewById(R.id.bt_insert).setOnClickListener(this);
        ListView lvLogs = (ListView) findViewById(R.id.lv_logs);
        logAdapter = new LogAdapter(this);
        lvLogs.setAdapter(logAdapter);
    }

    @Override
    public void onClick(View v) {
        List<CommonDbHelper.DbType> dbTypes = new ArrayList<>();
        if (chNative.isChecked())
            dbTypes.add(CommonDbHelper.DbType.NATIVE);
        if (chOrmSql.isChecked())
            dbTypes.add(CommonDbHelper.DbType.ORMLITE_SQLITE);
        if (chH2.isChecked())
            dbTypes.add(CommonDbHelper.DbType.ORMLITE_H2);
        if (chGreenDao.isChecked())
            dbTypes.add(CommonDbHelper.DbType.GREENDAO);
        switch (v.getId()) {
            case R.id.bt_get_all:
                for (CommonDbHelper.DbType dbType : dbTypes) {
                    getAll(dbType);
                }
                break;
            case R.id.bt_clear_all:
                for (CommonDbHelper.DbType dbType : dbTypes) {
                    clearAll(dbType);
                }
                break;
            case R.id.bt_insert:
                try {
                    recordsCount = Integer.valueOf(etCount.getText().toString());
                    ArrayList<DummyModel> dummyModels = new ArrayList<>();
                    for (int i = 0; i < recordsCount; i++) {
                        dummyModels.add(new DummyModel(System.currentTimeMillis(), UUID.randomUUID().toString()));
                    }
                    for (CommonDbHelper.DbType dbType : dbTypes) {
                        insert(dbType, dummyModels);
                    }
                } catch (NumberFormatException e) {
                    logAdapter.addItem(e.toString());
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }

    private void insert(final CommonDbHelper.DbType dbType, ArrayList<DummyModel> dummyModels) {
        DatabaseIntentService.insertDummyModels(this, dbType, dummyModels,
                new DatabaseResultReceiver() {
                    @Override
                    public void resultReceived(Object result, long time) {
                        if (((Boolean) result)) {
                            long dbSize =
                                    CommonDbHelper.getDatabaseSize(TestActivity.this,
                                            dbType);
                            logAdapter.addItem(
                                    "---------" + dbType.toString() + "---------");
                            logAdapter.addItem(
                                    "inserted " + recordsCount + " records in time - " +
                                            time + " ms");
                            logAdapter.addItem("average speed of inserting  - " +
                                    doubleToString(
                                            1000d / (time / (double) recordsCount), 2) +
                                    " in second");
                            logAdapter.addItem("database size  - " + dbSize + " bytes");
                        }
                    }
                });
    }

    private void clearAll(final CommonDbHelper.DbType dbType) {
        DatabaseIntentService.clearDummyModels(this, dbType, new DatabaseResultReceiver() {
            @Override
            public void resultReceived(Object result, long time) {
                long dbSize = CommonDbHelper.getDatabaseSize(TestActivity.this, dbType);
                logAdapter.addItem("---------" + dbType.toString() + "---------");
                logAdapter.addItem("clear db in time - " + time + " ms");
                logAdapter.addItem("database size  - " + dbSize + " bytes");
            }
        });
    }

    private void getAll(final CommonDbHelper.DbType dbType) {
        DatabaseIntentService.getDummyModels(this, dbType, new DatabaseResultReceiver() {
            @Override
            public void resultReceived(Object result, long time) {
                long dbSize = CommonDbHelper.getDatabaseSize(TestActivity.this, dbType);
                ArrayList<DummyModel> dummyModels = (ArrayList<DummyModel>) result;
                recordsCount = dummyModels.size();
                logAdapter.addItem("---------" + dbType.toString() + "---------");
                logAdapter.addItem("get " + recordsCount + " records in time - " + time +
                        " ms");
                logAdapter.addItem("average speed of getting  - " +
                        doubleToString(1000d / (time / (double) recordsCount), 2) +
                        " in second");
                logAdapter.addItem("database size  - " + dbSize + " bytes");
            }
        });
    }

    private String doubleToString(double d, double pow) {
        double round = Math.pow(10.0, pow);
        d = Math.round(d * round) / round;
        return (long) d == d ? "" + (long) d : "" + d;
    }
}
