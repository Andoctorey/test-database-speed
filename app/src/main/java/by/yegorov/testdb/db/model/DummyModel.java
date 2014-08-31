package by.yegorov.testdb.db.model;


import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

@DatabaseTable()
public class DummyModel implements Serializable {

    @DatabaseField(generatedId = true)
    //not long for greendao generator
    private Long id;

    @DatabaseField
    private String dummyString;

    @DatabaseField
    private Long dummyLong;


    public DummyModel() {
        // required by ormlite
    }

    public DummyModel(long dummyLong, String dummyString) {
        this.dummyLong = dummyLong;
        this.dummyString = dummyString;
    }

    public DummyModel(Long id, long dummyLong, String dummyString) {
        this.id = id;
        this.dummyLong = dummyLong;
        this.dummyString = dummyString;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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
