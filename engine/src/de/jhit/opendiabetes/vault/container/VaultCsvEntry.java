/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jhit.opendiabetes.vault.container;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author mswin
 */
public class VaultCsvEntry {

    public final static double UNINITIALIZED_DOUBLE = -1.0;
    public final static char CSV_DELIMITER = ',';
    public final static char CSV_LIST_DELIMITER = ':';

    private Date timestamp;
    private double bgValue = UNINITIALIZED_DOUBLE;
    private double cgmValue = UNINITIALIZED_DOUBLE;
    private double cgmAlertValue = UNINITIALIZED_DOUBLE;
    private double basalValue = UNINITIALIZED_DOUBLE;
    private double bolusValue = UNINITIALIZED_DOUBLE;
    private double mealValue = UNINITIALIZED_DOUBLE;
    private List<String> pumpAnnotation = new ArrayList<>();
    private double exerciseTimeValue = UNINITIALIZED_DOUBLE;
    private List<String> exerciseAnnotation = new ArrayList<>();

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public double getBgValue() {
        return bgValue;
    }

    public void setBgValue(double bgValue) {
        this.bgValue = bgValue;
    }

    public double getCgmValue() {
        return cgmValue;
    }

    public void setCgmValue(double cgmValue) {
        this.cgmValue = cgmValue;
    }

    public double getCgmAlertValue() {
        return cgmAlertValue;
    }

    public void setCgmAlertValue(double cgmAlertValue) {
        this.cgmAlertValue = cgmAlertValue;
    }

    public double getBasalValue() {
        return basalValue;
    }

    public void setBasalValue(double basalValue) {
        this.basalValue = basalValue;
    }

    public double getBolusValue() {
        return bolusValue;
    }

    public void setBolusValue(double bolusValue) {
        this.bolusValue = bolusValue;
    }

    public double getMealValue() {
        return mealValue;
    }

    public void setMealValue(double mealValue) {
        this.mealValue = mealValue;
    }

    public List<String> getPumpAnnotation() {
        return pumpAnnotation;
    }

    public void setPumpAnnotation(List<String> pumpAnnotation) {
        this.pumpAnnotation = pumpAnnotation;
    }

    public void addPumpAnnotation(String pumpAnnotation) {
        this.pumpAnnotation.add(pumpAnnotation);
    }

    public double getExerciseTimeValue() {
        return exerciseTimeValue;
    }

    public void setExerciseTimeValue(double exerciseTimeValue) {
        this.exerciseTimeValue = exerciseTimeValue;
    }

    public void addExerciseAnnotation(String exerciseAnnotation) {
        this.exerciseAnnotation.add(exerciseAnnotation);
    }

    public List<String> getExerciseAnnotation() {
        return exerciseAnnotation;
    }

    public void setExerciseAnnotation(List<String> exerciseAnnotation) {
        this.exerciseAnnotation = exerciseAnnotation;
    }

    public boolean isEmpty() {
        return bgValue == UNINITIALIZED_DOUBLE
                && cgmValue == UNINITIALIZED_DOUBLE
                && cgmAlertValue == UNINITIALIZED_DOUBLE
                && bolusValue == UNINITIALIZED_DOUBLE
                && basalValue == UNINITIALIZED_DOUBLE
                && mealValue == UNINITIALIZED_DOUBLE
                && pumpAnnotation.isEmpty()
                && exerciseTimeValue == UNINITIALIZED_DOUBLE
                && exerciseAnnotation.isEmpty();
    }

    public String toCsvString() {
        StringBuilder sb = new StringBuilder();

        String[] record = this.toCsvRecord();
        for (String item : record) {
            sb.append(item).append(CSV_DELIMITER);
        }
        sb.deleteCharAt(sb.length() - 1);

        return sb.toString();
    }

    public String[] toCsvRecord() {
        ArrayList<String> sb = new ArrayList<>();
        sb.add(new SimpleDateFormat("dd.MM.yy").format(timestamp));
        sb.add(new SimpleDateFormat("HH:mm").format(timestamp));
        if (bgValue > 0.0) {
            sb.add(String.valueOf(bgValue));
        } else {
            sb.add("");
        }
        if (cgmValue > UNINITIALIZED_DOUBLE) {
            sb.add(String.valueOf(cgmValue));
        } else {
            sb.add("");
        }
        if (cgmAlertValue > UNINITIALIZED_DOUBLE) {
            sb.add(String.valueOf(cgmAlertValue));
        } else {
            sb.add("");
        }
        if (basalValue > UNINITIALIZED_DOUBLE) {
            sb.add(String.valueOf(basalValue));
        } else {
            sb.add("");
        }
        if (bolusValue > UNINITIALIZED_DOUBLE) {
            sb.add(String.valueOf(bolusValue));
        } else {
            sb.add("");
        }
        if (mealValue > UNINITIALIZED_DOUBLE) {
            sb.add(String.valueOf(mealValue));
        } else {
            sb.add("");
        }
        if (!pumpAnnotation.isEmpty()) {
            StringBuilder annotations = new StringBuilder();
            for (String item : pumpAnnotation) {
                annotations.insert(0,CSV_LIST_DELIMITER).insert(0,item);
            }
            annotations.deleteCharAt(annotations.length() - 1);
            sb.add(annotations.toString());
        } else {
            sb.add("");
        }
        if (exerciseTimeValue > UNINITIALIZED_DOUBLE) {
            sb.add(String.valueOf(exerciseTimeValue));
        } else {
            sb.add("");
        }
        if (!exerciseAnnotation.isEmpty()) {
            StringBuilder annotations = new StringBuilder();
            for (String item : exerciseAnnotation) {
                annotations.append(item).append(CSV_LIST_DELIMITER);
            }
            annotations.deleteCharAt(annotations.length() - 1);
            sb.add(annotations.toString());
        } else {
            sb.add("");
        }

        return sb.toArray(new String[]{});
    }

    public static String getCsvHeaderString() {
        StringBuilder sb = new StringBuilder();

        String[] header = getCsvHeaderRecord();
        for (String item : header) {
            sb.append(item).append(CSV_DELIMITER);
        }
        sb.deleteCharAt(sb.length() - 1);

        return sb.toString();
    }

    public static String[] getCsvHeaderRecord() {
        return new String[]{
            "date",
            "time",
            "bgValue",
            "cgmValue",
            "cgmAlertValue",
            "basalValue",
            "bolusValue",
            "mealValue",
            "pumpAnnotation",
            "exerciseTimeValue",
            "exerciseTypeValue"
        };
    }

    @Override
    public String toString() {
        return "VaultCsvEntry{" + "timestamp=" + timestamp + ", bgValue=" + bgValue + ", cgmValue=" + cgmValue + ", cgmAlertValue=" + cgmAlertValue + ", basalValue=" + basalValue + ", bolusValue=" + bolusValue + ", mealValue=" + mealValue + ", pumpAnnotation=" + pumpAnnotation + ", exerciseTimeValue=" + exerciseTimeValue + ", exerciseAnnotation=" + exerciseAnnotation + '}';
    }
}
