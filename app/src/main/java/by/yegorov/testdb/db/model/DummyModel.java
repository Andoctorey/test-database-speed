package by.yegorov.testdb.db.model;


import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

@DatabaseTable()
public class DummyModel implements Serializable {

    @DatabaseField(generatedId = true)
    private long id;

    @DatabaseField
    private String dummyString;

    @DatabaseField
    private long dummyLong;


    public DummyModel() {
        // required by ormlite
    }

    public DummyModel(long dummyLong, String dummyString) {
        this.dummyLong = dummyLong;
        this.dummyString = dummyString;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDummyString() {
        return dummyString;
    }

    public void setDummyString(String dummyString) {
        this.dummyString = dummyString;
    }

    public long getDummyLong() {
        return dummyLong;
    }

    public void setDummyLong(long dummyLong) {
        this.dummyLong = dummyLong;
    }
}
