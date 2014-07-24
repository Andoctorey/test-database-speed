package by.yegorov.testdb;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.UUID;

import by.yegorov.testdb.db.CommonDbHelper;
import by.yegorov.testdb.db.DatabaseResultReceiver;
import by.yegorov.testdb.db.DatabaseService;
import by.yegorov.testdb.db.model.TestModel;


public class TestActivity extends Activity implements View.OnClickListener {

    private EditText etCount;

    private int recordsCount;

    private LogAdapter logAdapter;

    private Spinner spDbType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        initViews();
    }

    private void initViews() {
        spDbType = (Spinner) findViewById(R.id.sp_db_type);
        spDbType.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,
                CommonDbHelper.DbType.values()));
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
        final long start = System.currentTimeMillis();
        final CommonDbHelper.DbType dbType = (CommonDbHelper.DbType) spDbType.getSelectedItem();
        switch (v.getId()) {
            case R.id.bt_get_all:
                DatabaseService.getTestModels(this, dbType, new DatabaseResultReceiver() {
                    @Override
                    public void resultReceived(Object result) {
                        long time = System.currentTimeMillis() - start;
                        long dbSize = CommonDbHelper.getDatabaseSize(TestActivity.this, dbType);
                        ArrayList<TestModel> testModels = (ArrayList<TestModel>) result;
                        recordsCount = testModels.size();
                        logAdapter.addItem("---------" + dbType.toString() + "---------");
                        logAdapter.addItem("get " + recordsCount + " records in time - " + time +
                                " ms");
                        logAdapter.addItem("average speed of getting  - " +
                                doubleToString(1000d / (time / (double) recordsCount), 2) +
                                " in second");
                        logAdapter.addItem("database size  - " + dbSize + " bytes");
                    }
                });
                break;
            case R.id.bt_clear_all:
                DatabaseService.clearTestModels(this, dbType, new DatabaseResultReceiver() {
                    @Override
                    public void resultReceived(Object result) {
                        long time = System.currentTimeMillis() - start;
                        long dbSize = CommonDbHelper.getDatabaseSize(TestActivity.this, dbType);
                        logAdapter.addItem("---------" + dbType.toString() + "---------");
                        logAdapter.addItem("clear db in time - " + time + " ms");
                        logAdapter.addItem("database size  - " + dbSize + " bytes");
                    }
                });
                break;
            case R.id.bt_insert:
                try {
                    recordsCount = Integer.valueOf(etCount.getText().toString());
                    ArrayList<TestModel> testModels = new ArrayList<>();
                    for (int i = 0; i < recordsCount; i++) {
                        testModels.add(new TestModel(System.currentTimeMillis(),
                                UUID.randomUUID().toString()));
                    }
                    DatabaseService.insertTestModels(this, dbType, testModels,
                            new DatabaseResultReceiver() {
                                @Override
                                public void resultReceived(Object result) {
                                    if (((Boolean) result) == true) {
                                        long time = System.currentTimeMillis() - start;
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
                } catch (NumberFormatException e) {
                    logAdapter.addItem(e.toString());
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }

    private String doubleToString(double d, double pow) {
        double round = Math.pow(10.0, pow);
        d = Math.round(d * round) / round;
        return (long) d == d ? "" + (long) d : "" + d;
    }
}
