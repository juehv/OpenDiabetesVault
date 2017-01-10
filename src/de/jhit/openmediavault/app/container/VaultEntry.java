/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jhit.openmediavault.app.container;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import java.util.Date;
import java.util.Objects;

/**
 *
 * @author mswin
 */
@DatabaseTable(tableName = "VaultEntries")
public class VaultEntry {

    public static final double VALUE_UNUSED = -5.0;

    // for QueryBuilder to be able to find the fields
    public static final String TYPE_FIELD_NAME = "type";
    public static final String TIMESTAMP_FIELD_NAME = "timestamp";
    public static final String VALUE_FIELD_NAME = "value";

    @DatabaseField(generatedId = true)
    private long id;

    @DatabaseField(columnName = TYPE_FIELD_NAME, canBeNull = false)
    private VaultEntryType type;

    @DatabaseField(columnName = TIMESTAMP_FIELD_NAME, canBeNull = false)
    private Date timestamp;

    @DatabaseField(columnName = VALUE_FIELD_NAME, canBeNull = false)
    private double value;

    public VaultEntry() {
        // all persisted classes must define a no-arg constructor with at least package visibility
    }

    public VaultEntry(VaultEntryType type, Date timestamp, double value) {
        this.type = type;
        this.timestamp = timestamp;
        this.value = value;
    }

    public long getId() {
        return id;
    }

    public VaultEntryType getType() {
        return type;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public double getValue() {
        return value;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 73 * hash + Objects.hashCode(this.type);
        hash = 73 * hash + Objects.hashCode(this.timestamp);
        hash = 73 * hash + Objects.hashCode(this.value);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final VaultEntry other = (VaultEntry) obj;
        if (!Objects.equals(this.type, other.type)) {
            return false;
        }
        if (!Objects.equals(this.timestamp, other.timestamp)) {
            return false;
        }
        if (!Objects.equals(this.value, other.value)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "VaultEntry{" + "type=" + type + ", timestamp=" + timestamp + ", value=" + value + '}';
    }

}
