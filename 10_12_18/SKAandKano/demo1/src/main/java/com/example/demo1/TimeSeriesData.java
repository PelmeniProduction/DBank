package com.example.demo1;

import java.util.Date;

public class TimeSeriesData {
    String category;
    Date date;
    Double price;

    @Override
    public String toString() {
        return "TimeSeriesData{" +
                "category='" + category + '\'' +
                ", date=" + date +
                ", price=" + price +
                '}';
    }

    public TimeSeriesData() {
    }
    public TimeSeriesData(String category, Date date, Double price) {

        this.category = category;
        this.date = date;
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }


}
