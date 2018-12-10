package com.example.demo1;


import java.util.Date;

public class AnalyzedData {


    Date date;
    double dEMA;
    double dSMA;
    double dKalman;

    public AnalyzedData() {
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
