/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jhit.opendiabetes.vault.container;

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
    public static final long ID_UNUSED = -5L;

    // for QueryBuilder to be able to find the fields
    public static final String TYPE_FIELD_NAME = "type";
    public static final String TIMESTAMP_FIELD_NAME = "timestamp";
    public static final String VALUE_FIELD_NAME = "value";
    public static final String VALUE2_FIELD_NAME = "value2";
    public static final String RAW_ID_FIELD_NAME = "rawId";

    @DatabaseField(generatedId = true)
    private long id;

    @DatabaseField(columnName = TYPE_FIELD_NAME, canBeNull = false)
    private VaultEntryType type;

    @DatabaseField(columnName = TIMESTAMP_FIELD_NAME, canBeNull = false)
    private Date timestamp;

    @DatabaseField(columnName = VALUE_FIELD_NAME, canBeNull = false)
    private double value;

    @DatabaseField(columnName = VALUE2_FIELD_NAME, canBeNull = true)
    private double value2 = VALUE_UNUSED;

    @DatabaseField(columnName = RAW_ID_FIELD_NAME, canBeNull = false)
    private long rawId = ID_UNUSED;

    public VaultEntry() {
        // all persisted classes must define a no-arg constructor with at least package visibility
    }

    public VaultEntry(VaultEntryType type, Date timestamp, double value) {
        this.type = type;
        this.timestamp = timestamp;
        this.value = value;
    }

    public VaultEntry(VaultEntry copy) {
        this.type = copy.type;
        this.timestamp = copy.timestamp;
        this.value = copy.value;
        this.value2 = copy.value2;
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

    public void setValue(double value) {
        this.value = value;
    }

    public long getRawId() {
        return rawId;
    }

    public void setRawId(long rawId) {
        this.rawId = rawId;
    }

    public double getValue2() {
        return value2;
    }

    public void setValue2(double value2) {
        this.value2 = value2;
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
        return "VaultEntry{" + "id=" + id + ", type=" + type + ", timestamp=" + timestamp + ", value=" + value + ", value2=" + value2 + ", rawId=" + rawId + '}';
    }

}
