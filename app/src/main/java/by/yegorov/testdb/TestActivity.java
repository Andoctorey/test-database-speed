package by.yegorov.testdb;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

import by.yegorov.testdb.db.model.TestModel;
import by.yegorov.testdb.db.ormlite.DatabaseResultReceiver;
import by.yegorov.testdb.db.ormlite.DatabaseService;


public class TestActivity extends Activity implements View.OnClickListener {

    private EditText etCount;
    private int recordsCount;
    private LogAdapter logAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        initViews();
    }

    private void initViews() {
        etCount = (EditText) findViewById(R.id.et_count);
        Button btGet = (Button) findViewById(R.id.bt_get);
        btGet.setOnClickListener(this);
        Button btInsert = (Button) findViewById(R.id.bt_insert);
        btInsert.setOnClickListener(this);
        ListView lvLogs = (ListView) findViewById(R.id.lv_logs);
        logAdapter = new LogAdapter(this);
        lvLogs.setAdapter(logAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_get:
                testInsert();
                break;
            case R.id.bt_insert:
                try {
                    recordsCount = Integer.valueOf(etCount.getText().toString());
                    testInsert();
                } catch (NumberFormatException e) {
                    logAdapter.addItem(e.toString());
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }

    private void testInsert() {
        ArrayList<TestModel> testModels = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < recordsCount; i++) {
            testModels.add(new TestModel((long) (random.nextDouble() * (1000)), UUID.randomUUID().toString()));
        }
        final long start = System.currentTimeMillis();
        DatabaseService.insertApps(this, testModels, new DatabaseResultReceiver() {
            @Override
            public void resultReceived(Object result) {
                if (((Boolean) result) == true) {
                    long time = System.currentTimeMillis() - start;
                    logAdapter.addItem("ormlite inserted " + recordsCount + " records in time - " + time + " ms");
                    logAdapter.addItem("average speed of inserting  - " + doubleToString(1000d / (time / (double) recordsCount), 2) + " in second");
                }
            }
        });
    }

    private String doubleToString(double d, double pow) {
        double round = Math.pow(10.0, pow);
        d = Math.round(d * round) / round;
        return (long) d == d ? "" + (long) d : "" + d;
    }
}
