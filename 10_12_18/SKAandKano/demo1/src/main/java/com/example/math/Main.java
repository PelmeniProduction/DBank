package com.example.math;

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
                //DataBase.deleteAllRecords(stmt);
                //stmt.execute("DELETE FROM GOLD WHERE ID=11639;");
                //DataBase.printTable(stmt);
                //rTsd=DataBase.getListFromDate(c,stmt,new Date(118,11,1),new Date(118,11,5));
                /*c.commit();
                stmt.close();
                c.close();*/
                //на мужчину
                DataCrawler dataCrawler= new DataCrawler();
                dataCrawler.reloadData();

                /*//получаем данные из инета в массив
                Date start=new Date(118,8,1);
                Date end=new Date(118,10,10);
                arTsd=DataBase.getListFromDate(c,stmt,start, end);
                //подрубиться к постгре
                arDEma=arGetEMA(arTsd);
                arDSma=arGetSMA(arTsd);
                arDVol=arGetVol();
                for (int i = 0; i < arDVol.size(); i++) {
                    System.out.println(arDVol.get(i));
                }

                System.out.println("Success create");
                String sql;

                arDKalm=arGetKalman(stmt,arTsd);
                //DataBase.deleteAllRecords(stmt);
                //DataBase.fillTable(c,arTsd);



                Date d=DataBase.getLastUpdated(stmt);
                DataBase.printTable(stmt);

                c.commit();
                stmt.close();
                c.close();*/
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println(e.getClass().getName()+": "+e.getMessage());
                System.exit(0);
            }
    }
    //получить проустую скользящую среднюю   http://allfi.biz/Forex/TechnicalAnalysis/Trend-Indicators/prostoe-skolzjashhee-srednee.php
    private static ArrayList<Double> arGetSMA(ArrayList<TimeSeriesData> arTsd) {
        ArrayList<Double> arSMA = new ArrayList<Double>();
        int n=arTsd.size();
        int iSmoothIn=4;//интервал сглаживания
        double dMid;
        for (int i = iSmoothIn; i < n; i++) {
            dMid=0;
            for (int j = i-iSmoothIn; j < i; j++) {
                dMid+=arTsd.get(j).getPrice();
            }
            arSMA.add(dMid/iSmoothIn);
        }
        return arSMA;
    }
    //получить экспоненциально взевешенную скользящую среднюю - упрощенный фильтр Калмана
    private static ArrayList<Double> arGetEMA(ArrayList<TimeSeriesData> arTsd){
        ArrayList<Double> arEMA = new ArrayList<Double>();
        int n=arTsd.size();
        Double alpha=2/(1+(double)n);
        for (int i = 0; i < n; i++) {
            if(i==0)
                arEMA.add(i,(arTsd.get(i).getPrice()*alpha)+(arTsd.get(i).getPrice()*(1-alpha)));
            else
                arEMA.add(i,(arTsd.get(i).getPrice()*alpha)+(arEMA.get(i-1)*(1-alpha)));
        }
        return arEMA;
    }
    //упрощенный фильтр калмана тоже самое что и скользящее среднее   https://habr.com/post/166693/
    //получить полный фильтр Калмана
    static ArrayList<Double> arGetKalman(Statement stmt,ArrayList<TimeSeriesData> arTsd){
        ArrayList<Double> arKalm = new ArrayList<Double>();
        double dLastError=0;
        for (int i = 0; i < arTsd.size(); i++) {
            if(i==0)
                dLastError=0;
            else
                dLastError=arTsd.get(i).getPrice()-arTsd.get(i-1).getPrice();
            if(i==0)
                arKalm.add(arTsd.get(0).getPrice());
            else
                arKalm.add(arKalm.get(i-1)+coeffKalman(dLastError,i)*(arTsd.get(i).getPrice()-arKalm.get(i-1)));

        }
        return arKalm;
    }
    //рассчитать коэффициент Калмана
    static double coeffKalman(double dLastError,int id){
        double iCoeff=0;
        //double dVola=DataBase.getVolFromID(stmt,id);
        double dVola=10;
        iCoeff=dVola/(dVola+Math.abs(dLastError));//модуль чтобы коэффицинет в космос не улетел
        System.out.println(iCoeff);
        return iCoeff;
    }
    //получить волотильность
    static ArrayList<Double> arGetVol(){
        ArrayList<Double> arVol = new ArrayList<>();
        int iDiv=arTsd.size()/20;
        for (int i = 0; i < iDiv; i++) {
            arVol.add(getSqrtDisp20(i));
        }
        if(arTsd.size()%20!=0)//если последний период 0-19 дней,
            //то выставить его волатильность равной волатильности прошлого
            arVol.add(arVol.get(arVol.size()-1));
        return arVol;
    }
    //получить дисперсию
    static double getSqrtDisp20(int iPeriod){
        double dSqrtDisp=0;
        int n=20;
        //если меньше 20 то добавить оставшийся отрезок
        if (lastPeriodLess20(iPeriod))
            n+=arTsd.size()-(iPeriod+1)*20;
        int k=iPeriod*20;
        double dAvd=0;
        for (int i = 0+k; i < n+k; i++) {
            dAvd+=arTsd.get(i).getPrice();
        }
        dAvd/=n;
        for (int i = 0+k; i < n+k; i++) {
            dSqrtDisp+=Math.pow((arTsd.get(i).getPrice()-dAvd),2.0);
        }
        dSqrtDisp=Math.sqrt(dSqrtDisp/n);
        return dSqrtDisp;
    }
    //меньше ли 20 следующий отрезок
    static boolean lastPeriodLess20(int iPeriod){
        boolean b=false;
        if((arTsd.size()-(iPeriod+1)*20)<20)
            b=true;
        return b;
    }
}
