package com.example.demo1;


import com.example.math.DataBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.xml.crypto.Data;
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
    private static volatile List<TimeSeriesData> cachedData = Collections.emptyList();
    private static volatile ArrayList<AnalyzedData> cachedAnalyzedData ;
    public static ArrayList<Double> arDSma;
    public static ArrayList<Double> arDEma;
    public static ArrayList<Double> arDVol;//только для внутренних расчетов
    public static ArrayList<Double> arDKalm;
    @Scheduled(fixedDelay = 1000*60*60*24)
    public void reloadData() throws Exception {//кэширует данные за последние полгода
        ConnectionManager connectionManager= new ConnectionManager();
        Connection con= connectionManager.getConnection1();
        Statement stmt=con.createStatement();
        reloadDataFromDate("gold",con,stmt,DataCrawler.subtractFromDate(121,new Date()),new Date());
        reloadDataFromDate("oil",con,stmt,DataCrawler.subtractFromDate(121,new Date()),new Date());

    }
    public static void makeAnalyzedData(){
        for (int i = 0; i < arDSma.size(); i++) {
            AnalyzedData analyzedData = new AnalyzedData(cachedData.get(i).getDate(),cachedData.get(i).getPrice(),
            arDSma.get(i),arDEma.get(i),arDKalm.get(i));
            cachedAnalyzedData.add(i,analyzedData);
        }
    }
    public static void reloadDataFromDate(String category,Connection connection, Statement stmt, Date start,Date end)throws Exception{//начало и конец отрезка который выбрал юзер
        String cat="";
        if(category=="Золото")
            cat="gold";
        else if(category=="Нефть")
            cat="oil";
        else
            cat=category;
        cachedAnalyzedData = new ArrayList<>();//отчистим список
        cachedData= DataBase.getListFromDate(cat,connection,stmt,start,end);//получить из бд список объектов TimeSeriesData
        //вычислять...
        arDSma=arGetSMA((ArrayList)cachedData);
        arDEma=arGetEMA((ArrayList)cachedData);
        arDVol=arGetVol();
        arDKalm=arGetKalman((ArrayList)cachedData);
        makeAnalyzedData();
        for (int i = 0; i < cachedAnalyzedData.size(); i++) {
            System.out.println(cachedAnalyzedData.get(i));
        }
        System.out.println("Yeah baby");
        stmt.close();
        connection.close();

    }
    public static List<AnalyzedData> getCachedAnalData() {
        return cachedAnalyzedData;
    }
    public static List<TimeSeriesData> getCachedData() {
        return cachedData;
    }
    //получить проустую скользящую среднюю   http://allfi.biz/Forex/TechnicalAnalysis/Trend-Indicators/prostoe-skolzjashhee-srednee.php
    private static ArrayList<Double> arGetSMA(ArrayList<TimeSeriesData> cachedData) {
        ArrayList<Double> arSMA = new ArrayList<Double>();
        int n=cachedData.size();
        int iSmoothIn=4;//интервал сглаживания
        double dMid;
        for (int i = iSmoothIn; i < n; i++) {
            dMid=0;
            for (int j = i-iSmoothIn; j < i; j++) {
                dMid+=cachedData.get(j).getPrice();
            }
            arSMA.add(dMid/iSmoothIn);
        }
        ArrayList<Double> arSMAready= new ArrayList<>();
        for (int i = 0; i < 4; i++) {//заполним начальный пробел равный интервалу сглаживания
            arSMAready.add(arSMA.get(i));
        }
        arSMAready.addAll(arSMA);
        return arSMAready;
    }
    //получить экспоненциально взевешенную скользящую среднюю - упрощенный фильтр Калмана
    private static ArrayList<Double> arGetEMA(ArrayList<TimeSeriesData> cachedData){
        ArrayList<Double> arEMA = new ArrayList<Double>();
        int n=cachedData.size();
        //Double alpha=2/(1+(double)n);
        Double alpha=0.3;
        for (int i = 0; i < n; i++) {
            if(i==0)
                arEMA.add(i,(cachedData.get(i).getPrice()*alpha)+(cachedData.get(i).getPrice()*(1-alpha)));
            else
                arEMA.add(i,(cachedData.get(i).getPrice()*alpha)+(arEMA.get(i-1)*(1-alpha)));
        }
        return arEMA;
    }
    //упрощенный фильтр калмана тоже самое что и скользящее среднее   https://habr.com/post/166693/
    //получить полный фильтр Калмана
    static ArrayList<Double> arGetKalman(ArrayList<TimeSeriesData> cachedData){
        ArrayList<Double> arKalm = new ArrayList<Double>();
        double dLastError=0;
        for (int i = 0; i < cachedData.size(); i++) {
            if(i==0)
                dLastError=0;
            else
                dLastError=cachedData.get(i).getPrice()-cachedData.get(i-1).getPrice();
            if(i==0)
                arKalm.add(cachedData.get(0).getPrice());
            else
                arKalm.add(arKalm.get(i-1)+coeffKalman(dLastError,i)*(cachedData.get(i).getPrice()-arKalm.get(i-1)));

        }
        return arKalm;
    }
    //рассчитать коэффициент Калмана
    static double coeffKalman(double dLastError,int id){
        double iCoeff=0;
        double dVola=arDVol.get(id/20);
        iCoeff=dVola/(dVola+Math.abs(dLastError));//модуль чтобы коэффицинет в космос не улетел
        System.out.println(iCoeff);
        return iCoeff;
    }
    //получить волотильность
    static ArrayList<Double> arGetVol(){
        ArrayList<Double> arVol = new ArrayList<>();
        int iDiv=cachedData.size()/20;
        for (int i = 0; i < iDiv; i++) {
            arVol.add(getSqrtDisp20(i));
        }
        if(cachedData.size()%20!=0)//если последний период 0-19 дней,
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
            n+=cachedData.size()-(iPeriod+1)*20;
        int k=iPeriod*20;
        double dAvd=0;
        for (int i = 0+k; i < n+k; i++) {
            dAvd+=cachedData.get(i).getPrice();
        }
        dAvd/=n;
        for (int i = 0+k; i < n+k; i++) {
            dSqrtDisp+=Math.pow((cachedData.get(i).getPrice()-dAvd),2.0);
        }
        dSqrtDisp=Math.sqrt(dSqrtDisp/n);
        return dSqrtDisp;
    }
    //меньше ли 20 следующий отрезок
    static boolean lastPeriodLess20(int iPeriod){
        boolean b=false;
        if((cachedData.size()-(iPeriod+1)*20)<20)
            b=true;
        return b;
    }

}
