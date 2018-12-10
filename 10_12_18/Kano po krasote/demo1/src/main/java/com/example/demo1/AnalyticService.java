package com.example.demo1;


import com.example.math.DataBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Component
public class AnalyticService {

    @Autowired
    ConnectionManager connectionManager;
    //вычислим все что вычисляется и кэшируем
    private volatile List<TimeSeriesData> cachedData = Collections.emptyList();
    public static ArrayList<Double> arD;
    public static ArrayList<Double> arDSma;
    public static ArrayList<Double> arDEma;
    public static ArrayList<Double> arDVol;
    public static ArrayList<Double> arDKalm;
    //@Scheduled
    public void reloadData(Date start,Date end) throws Exception {//начало и конец отрезка который выбрал юзер
        Connection connection = connectionManager.getConnection1();
        Statement stmt=connection.createStatement();
        cachedData= DataBase.getListFromDate(connection,stmt,start,end);//получить из бд список объектов TimeSeriesData
        //вычислять...

    }
    public List<TimeSeriesData> getCachedData() {
        return cachedData;
    }
}
