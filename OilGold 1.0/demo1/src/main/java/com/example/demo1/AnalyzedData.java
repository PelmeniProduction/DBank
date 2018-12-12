package com.example.demo1;


import java.util.Date;

public class AnalyzedData {


    Date date;
    double dCoast;


    double dSMA;
    double dEMA;
    double dKalman;

    public AnalyzedData() {
    }

    public double getdCoast() {
        return dCoast;
    }

    public void setdCoast(double dCoast) {
        this.dCoast = dCoast;
    }

    @Override
    public String toString() {
        return "AnalyzedData{" +
                "date=" + date +
                ", dCoast=" + dCoast +
                ", dSMA=" + dSMA +
                ", dEMA=" + dEMA +
                ", dKalman=" + dKalman +
                '}';
    }

    public AnalyzedData(Date date, double dCoast, double dSMA, double dEMA, double dKalman) {
        this.date = date;
        this.dCoast = dCoast;
        this.dEMA = dEMA;
        this.dSMA = dSMA;
        this.dKalman = dKalman;
    }

    public Date getDate() {
        return date;
    }
    public void setDate(Date date) {
        this.date = date;
    }
    public double getdEMA() {
        return dEMA;
    }

    public void setdEMA(double dEMA) {
        this.dEMA = dEMA;
    }

    public double getdSMA() {
        return dSMA;
    }

    public void setdSMA(double dSMA) {
        this.dSMA = dSMA;
    }

    public double getdKalman() {
        return dKalman;
    }

    public void setdKalman(double dKalman) {
        this.dKalman = dKalman;
    }
}
