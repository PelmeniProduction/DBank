package com.example.math;

import com.example.demo1.AnalyticService;
import com.example.demo1.ConnectionManager;
import com.example.demo1.DataCrawler;
import com.example.demo1.TimeSeriesData;

import java.sql.*;
import java.util.*;
import java.util.Date;


public class Main{
    public static ArrayList<Double> arD;
    public static ArrayList<Double> arDSma;
    public static ArrayList<Double> arDEma;
    public static ArrayList<Double> arDVol;
    public static ArrayList<Double> arDKalm;
    public static ArrayList<TimeSeriesData> arTsd;
    //переопределим метод javafx

    //данные взяты с http://www.investfunds.kz/world/indicators/224/
    public static void main(String[] args) {
	//write your code here

        String urlStr = "http://gold.investfunds.ru/indicators/export_to_xls.php?id=224&start_day=1&start_month=04&start_year=2018&finish_day=24&finish_month=11&finish_year=2018";


            //показать гарфик

            //базируем данные слегка по-другому
            ConnectionManager connectionManager = new ConnectionManager();
            Connection c = null;
            Statement stmt = null;
            try {
                //отчистим
                c=connectionManager.getConnection1();
                stmt=c.createStatement();
                /*DataBase.deleteAllRecords("oil",stmt);
                DataBase.deleteAllRecords("gold",stmt);*/
                //stmt.execute("DELETE FROM GOLD WHERE ID=11639;");
                //DataBase.printTable(stmt);
                //rTsd=DataBase.getListFromDate(c,stmt,new Date(118,11,1),new Date(118,11,5));
                /*c.commit();
                stmt.close();
                c.close();*/
                //на мужчину
                DataCrawler dataCrawler= new DataCrawler();
                dataCrawler.reloadData();

                //получаем данные из инета в массив
                /*Date start=new Date(118,8,1);
                Date end=new Date(118,10,10);
               *//*arTsd=DataBase.getListFromDate(c,stmt,start, end);
                //подрубиться к постгре
                arDEma=arGetEMA(arTsd);
                arDSma=arGetSMA(arTsd);
                arDVol=arGetVol();
                arDKalm=arGetKalman(stmt,arTsd);*/
                /*AnalyticService analyticService= new AnalyticService();
                analyticService.reloadData();*/
                c.commit();
                stmt.close();
                c.close();
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println(e.getClass().getName()+": "+e.getMessage());
            }
    }

}
