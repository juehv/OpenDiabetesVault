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
    private List<String> basalAnnotation = new ArrayList<>();
    private double bolusValue = UNINITIALIZED_DOUBLE;
    private List<String> bolusAnnotation = new ArrayList<>();
    private double mealValue = UNINITIALIZED_DOUBLE;
    private List<String> pumpAnnotation = new ArrayList<>();
    private double exerciseTimeValue = UNINITIALIZED_DOUBLE;
    private List<String> exerciseAnnotation = new ArrayList<>();
    private double hrValue = UNINITIALIZED_DOUBLE;
    private double hrvValue = UNINITIALIZED_DOUBLE;
    private List<String> sleepAnnotation = new ArrayList<>();

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

    public List<String> getBasalAnnotation() {
        return basalAnnotation;
    }

    public void setBasalAnnotation(List<String> basalAnnotation) {
        this.basalAnnotation = basalAnnotation;
    }

    public void addBasalAnnotation(String basalAnnotation) {
        this.basalAnnotation.add(basalAnnotation);
    }

    public List<String> getBolusAnnotation() {
        return bolusAnnotation;
    }

    public void setBolusAnnotation(List<String> bolusAnnotation) {
        this.bolusAnnotation = bolusAnnotation;
    }

    public void addBolusAnnotation(String bolusAnnotation) {
        this.bolusAnnotation.add(bolusAnnotation);
    }

    public double getPulseValue() {
        return hrValue;
    }

    public void setPulseValue(double pulseValue) {
        this.hrValue = pulseValue;
    }

    public double getStressValue() {
        return hrvValue;
    }

    public void setStressValue(double stressValue) {
        this.hrvValue = stressValue;
    }

    public List<String> getSleepAnnotation() {
        return sleepAnnotation;
    }

    public void setSleepAnnotation(List<String> sleepAnnotation) {
        this.sleepAnnotation = sleepAnnotation;
    }

    public void addSleepAnnotation(String sleepAnnotation) {
        this.sleepAnnotation.add(sleepAnnotation);
    }

    public boolean isEmpty() {
        return bgValue == UNINITIALIZED_DOUBLE
                && cgmValue == UNINITIALIZED_DOUBLE
                && cgmAlertValue == UNINITIALIZED_DOUBLE
                && basalValue == UNINITIALIZED_DOUBLE
                && basalAnnotation.isEmpty()
                && bolusValue == UNINITIALIZED_DOUBLE
                && bolusAnnotation.isEmpty()
                && mealValue == UNINITIALIZED_DOUBLE
                && pumpAnnotation.isEmpty()
                && exerciseTimeValue == UNINITIALIZED_DOUBLE
                && exerciseAnnotation.isEmpty()
                && hrValue == UNINITIALIZED_DOUBLE
                && hrvValue == UNINITIALIZED_DOUBLE
                && sleepAnnotation.isEmpty();
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
        ArrayList<String> csvRecord = new ArrayList<>();
        csvRecord.add(new SimpleDateFormat("dd.MM.yy").format(timestamp));
        csvRecord.add(new SimpleDateFormat("HH:mm").format(timestamp));
        if (bgValue > 0.0) {
            csvRecord.add(String.valueOf(bgValue));
        } else {
            csvRecord.add("");
        }
        if (cgmValue > UNINITIALIZED_DOUBLE) {
            csvRecord.add(String.valueOf(cgmValue));
        } else {
            csvRecord.add("");
        }
        if (cgmAlertValue > UNINITIALIZED_DOUBLE) {
            csvRecord.add(String.valueOf(cgmAlertValue));
        } else {
            csvRecord.add("");
        }
        if (basalValue > UNINITIALIZED_DOUBLE) {
            csvRecord.add(String.valueOf(basalValue));
        } else {
            csvRecord.add("");
        }
        if (!basalAnnotation.isEmpty()) {
            StringBuilder annotations = new StringBuilder();
            for (String item : basalAnnotation) {
                annotations.insert(0, CSV_LIST_DELIMITER).insert(0, item);
            }
            annotations.deleteCharAt(annotations.length() - 1);
            csvRecord.add(annotations.toString());
        } else {
            csvRecord.add("");
        }
        if (bolusValue > UNINITIALIZED_DOUBLE) {
            csvRecord.add(String.valueOf(bolusValue));
        } else {
            csvRecord.add("");
        }
        if (!bolusAnnotation.isEmpty()) {
            StringBuilder annotations = new StringBuilder();
            for (String item : bolusAnnotation) {
                annotations.insert(0, CSV_LIST_DELIMITER).insert(0, item);
            }
            annotations.deleteCharAt(annotations.length() - 1);
            csvRecord.add(annotations.toString());
        } else {
            csvRecord.add("");
        }
        if (mealValue > UNINITIALIZED_DOUBLE) {
            csvRecord.add(String.valueOf(mealValue));
        } else {
            csvRecord.add("");
        }
        if (!pumpAnnotation.isEmpty()) {
            StringBuilder annotations = new StringBuilder();
            for (String item : pumpAnnotation) {
                annotations.insert(0, CSV_LIST_DELIMITER).insert(0, item);
            }
            annotations.deleteCharAt(annotations.length() - 1);
            csvRecord.add(annotations.toString());
        } else {
            csvRecord.add("");
        }
        if (exerciseTimeValue > UNINITIALIZED_DOUBLE) {
            csvRecord.add(String.valueOf(exerciseTimeValue));
        } else {
            csvRecord.add("");
        }
        if (!exerciseAnnotation.isEmpty()) {
            StringBuilder annotations = new StringBuilder();
            for (String item : exerciseAnnotation) {
                annotations.append(item).append(CSV_LIST_DELIMITER);
            }
            annotations.deleteCharAt(annotations.length() - 1);
            csvRecord.add(annotations.toString());
        } else {
            csvRecord.add("");
        }
        if (hrValue > UNINITIALIZED_DOUBLE) {
            csvRecord.add(String.valueOf(hrValue));
        } else {
            csvRecord.add("");
        }
        if (hrvValue > UNINITIALIZED_DOUBLE) {
            csvRecord.add(String.valueOf(hrvValue));
        } else {
            csvRecord.add("");
        }
        if (!sleepAnnotation.isEmpty()) {
            StringBuilder annotations = new StringBuilder();
            for (String item : sleepAnnotation) {
                annotations.insert(0, CSV_LIST_DELIMITER).insert(0, item);
            }
            annotations.deleteCharAt(annotations.length() - 1);
            csvRecord.add(annotations.toString());
        } else {
            csvRecord.add("");
        }

        return csvRecord.toArray(new String[]{});
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
            "basalAnnotation",
            "bolusValue",
            "bolusAnnotation",
            "mealValue",
            "pumpAnnotation",
            "exerciseTimeValue",
            "exerciseAnnotation",
            "heartRateValue",
            "heartRateVariabilityValue",
            "sleepAnnotation"
        };
    }

    @Override
    public String toString() {
        return "VaultCsvEntry{" + "timestamp=" + timestamp + ", bgValue=" + bgValue + ", cgmValue=" + cgmValue + ", cgmAlertValue=" + cgmAlertValue + ", basalValue=" + basalValue + ", basalAnnotation=" + basalAnnotation + ", bolusValue=" + bolusValue + ", bolusAnnotation=" + bolusAnnotation + ", mealValue=" + mealValue + ", pumpAnnotation=" + pumpAnnotation + ", exerciseTimeValue=" + exerciseTimeValue + ", exerciseAnnotation=" + exerciseAnnotation + ", hrValue=" + hrValue + ", hrvValue=" + hrvValue + ", sleepAnnotation=" + sleepAnnotation + '}';
    }

}
