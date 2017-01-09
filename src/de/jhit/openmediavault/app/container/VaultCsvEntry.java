/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jhit.openmediavault.app.container;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author mswin
 */
public class VaultCsvEntry {

    private Date timestamp;
    private double bgValue;
    private double cgmValue;
    private double cgmAlertValue;
    private double bolusValue;
    private double mealValue;
    private double exerciseTimeValue;
    private String exerciseTypeValue;

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

    public double getExerciseTimeValue() {
        return exerciseTimeValue;
    }

    public void setExerciseTimeValue(double exerciseTimeValue) {
        this.exerciseTimeValue = exerciseTimeValue;
    }

    public String getExerciseTypeValue() {
        return exerciseTypeValue;
    }

    public void setExerciseTypeValue(String exerciseTypeValue) {
        this.exerciseTypeValue = exerciseTypeValue;
    }

    public boolean isEmpty() {
        return bgValue == 0.0
                && cgmValue == 0.0
                && cgmAlertValue == 0.0
                && bolusValue == 0.0
                && mealValue == 0.0
                && exerciseTimeValue == 0.0
                && exerciseTypeValue == null;
    }

    public String toCsvString() {
        StringBuilder sb = new StringBuilder();
        sb.append(new SimpleDateFormat("dd.MM.yy").format(timestamp)).append(", ");
        sb.append(new SimpleDateFormat("HH:mm").format(timestamp)).append(", ");
        if (bgValue > 0.0) {
            sb.append(bgValue);
        }
        sb.append(", ");
        if (cgmValue > 0.0) {
            sb.append(cgmValue);
        }
        sb.append(", ");
        if (cgmAlertValue > 0.0) {
            sb.append(cgmAlertValue);
        }
        sb.append(", ");
        if (bolusValue > 0.0) {
            sb.append(bolusValue);
        }
        sb.append(", ");
        if (mealValue > 0.0) {
            sb.append(mealValue);
        }
        sb.append(", ");
        if (exerciseTimeValue > 0.0) {
            sb.append(exerciseTimeValue);
        }
        sb.append(", ");
        if (exerciseTypeValue != null && !exerciseTypeValue.isEmpty()) {
            sb.append(exerciseTypeValue);
        }
        return sb.toString();
    }

    public static String getCsvHeaderString() {
        return "date, time, bgValue, cgmValue, cgmAlertValue, bolusValue, mealValue, exerciseTimeValue, exerciseTypeValue";
    }
}
