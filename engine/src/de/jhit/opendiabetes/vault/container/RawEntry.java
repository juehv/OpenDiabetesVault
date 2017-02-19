/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jhit.opendiabetes.vault.container;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 *
 * @author mswin
 */
@DatabaseTable(tableName = "RawValues")
public class RawEntry {

    // for QueryBuilder to be able to find the fields
    public static final String TIMESTAMP_FIELD_NAME = "timestamp";
    public static final String VALUE_FIELD_NAME = "value";
    public static final String IS_INTERPRETED_FIELD_NAME = "isInterpreted";

    @DatabaseField(generatedId = true)
    private long id;

    @DatabaseField(columnName = VALUE_FIELD_NAME, canBeNull = false, dataType = DataType.LONG_STRING)
    private String value;

    @DatabaseField(columnName = IS_INTERPRETED_FIELD_NAME, canBeNull = false)
    private boolean interpreted;

    public RawEntry() {
        // all persisted classes must define a no-arg constructor with at least package visibility
    }

    public RawEntry(String value, boolean interpreted) {
        this.value = value.trim();
        this.interpreted = interpreted;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public boolean isInterpreted() {
        return interpreted;
    }

}
