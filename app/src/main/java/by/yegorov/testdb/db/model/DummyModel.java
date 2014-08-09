package by.yegorov.testdb.db.model;


import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

@DatabaseTable()
public class DummyModel implements Serializable {

    @DatabaseField(generatedId = true)
    private long id;

    @DatabaseField
    private String testString;

    @DatabaseField
    private long testLong;


    public DummyModel() {
        // required by ormlite
    }

    public DummyModel(long testLong, String testString) {
        this.testLong = testLong;
        this.testString = testString;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTestString() {
        return testString;
    }

    public void setTestString(String testString) {
        this.testString = testString;
    }

    public long getTestLong() {
        return testLong;
    }

    public void setTestLong(long testLong) {
        this.testLong = testLong;
    }
}
