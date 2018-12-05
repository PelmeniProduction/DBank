package com.example.math;


import java.sql.*;
import java.util.*;

public class Main {
    public static ArrayList<Double> arD;
    public static ArrayList<Double> arDSma;
    public static ArrayList<Double> arDEma;
    public static ArrayList<Double> arDEma5;
    public static ArrayList<Double> arDVol;
    public static ArrayList<Double> arDKalm;


    //данные взяты с http://www.investfunds.kz/world/indicators/224/
    public static void main(String[] args) {
	//write your code here
            GetXLS g= new GetXLS();//работа с экселем
            g.getDataExcel();
            arD=g.arD;
            arDEma=arGetEMA(arD);
            arDSma=arGetSMA(arD);
            arDVol=arGetVol();

        for (int i = 0; i < arDVol.size(); i++) {
            System.out.println(arDVol.get(i));;
        }
            //показать гарфик

            //базируем данные слегка по-другому
            Connection c = null;
            Statement stmt = null;
            try {
                //подрубиться к постгре
                Class.forName("org.postgresql.Driver");
                c = DriverManager
                        .getConnection("jdbc:postgresql://localhost:5432/gold",
                                "postgres", "postgres");
                c.setAutoCommit(false);
                System.out.println("Opened database successfully");
                //добавление в дата базу
                stmt = c.createStatement();

                arDKalm=arGetKalman(stmt,arD);

                System.out.println("Success create");
                String sql;
                DataBase.deleteAllRecords(stmt,arD);
                DataBase.fillTable(stmt,arD,arDEma,arDVol);
                DataBase.printTable(stmt);
                c.commit();

                stmt.close();
                c.close();
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println(e.getClass().getName()+": "+e.getMessage());
                System.exit(0);
            }
    }
    /**
     * Это тест докумендации
     * @param k пельмени
     *
     * */
    static void Pelmeni(int k){
        System.out.println("Pelmeni");
    }
    //получить проустую скользящую среднюю   http://allfi.biz/Forex/TechnicalAnalysis/Trend-Indicators/prostoe-skolzjashhee-srednee.php
    private static ArrayList<Double> arGetSMA(ArrayList<Double> arCoast) {
        ArrayList<Double> arSMA = new ArrayList<Double>();
        int n=arCoast.size();
        int iSmoothIn=4;//интервал сглаживания
        double dMid;
        for (int i = iSmoothIn; i < n; i++) {
            dMid=0;
            for (int j = i-iSmoothIn; j < i; j++) {
                dMid+=arCoast.get(j);
            }
            arSMA.add(dMid/iSmoothIn);
        }
        return arSMA;
    }
    //получить экспоненциально взевешенную скользящую среднюю - упрощенный фильтр Калмана
    private static ArrayList<Double> arGetEMA(ArrayList<Double> arCoast){
        ArrayList<Double> arEMA = new ArrayList<Double>();
        int n=arCoast.size();
        Double alpha=2/(1+(double)n);
        for (int i = 0; i < n; i++) {
            if(i==0)
                arEMA.add(i,(arCoast.get(i)*alpha)+(arCoast.get(i)*(1-alpha)));
            else
                arEMA.add(i,(arCoast.get(i)*alpha)+(arEMA.get(i-1)*(1-alpha)));
        }
        return arEMA;
    }
    //упрощенный фильтр калмана тоже самое что и скользящее среднее   https://habr.com/post/166693/
    //получить полный фильтр Калмана
    static ArrayList<Double> arGetKalman(Statement stmt,ArrayList<Double> arCoast){
        ArrayList<Double> arKalm = new ArrayList<Double>();
        double dLastError=0;
        for (int i = 0; i < arCoast.size(); i++) {
            if(i==0)
                dLastError=0;
            else
                dLastError=arCoast.get(i)-arCoast.get(i-1);
            if(i==0)
                arKalm.add(arCoast.get(0));
            else
                arKalm.add(arKalm.get(i-1)+coeffKalman(stmt, dLastError,i)*(arCoast.get(i)-arKalm.get(i-1)));

        }
        return arKalm;
    }
    //рассчитать коэффициент Калмана
    static double coeffKalman(Statement stmt, double dLastError,int id){
        double iCoeff=0;
        double dVola=DataBase.getVolFromID(stmt,id);
        iCoeff=dVola/(dVola+Math.abs(dLastError));//модуль чтобы коэффицинет в космос не улетел
        System.out.println(iCoeff);
        return iCoeff;
    }
    //получить волотильность
    static ArrayList<Double> arGetVol(){
        ArrayList<Double> arVol = new ArrayList<>();
        int iDiv=arD.size()/20;
        for (int i = 0; i < iDiv; i++) {
            arVol.add(getSqrtDisp20(i));
        }
        return arVol;
    }
    //получить дисперсию
    static double getSqrtDisp20(int iPeriod){
        double dSqrtDisp=0;
        int n=20;
        //если меньше 20 то добавить оставшийся отрезок
        if (lastPeriodLess20(iPeriod))
            n+=arD.size()-(iPeriod+1)*20;
        int k=iPeriod*20;
        double dAvd=0;
        for (int i = 0+k; i < n+k; i++) {
            dAvd+=arD.get(i);
        }
        dAvd/=n;
        for (int i = 0+k; i < n+k; i++) {
            dSqrtDisp+=Math.pow((arD.get(i)-dAvd),2.0);
        }
        dSqrtDisp=Math.sqrt(dSqrtDisp/n);
        return dSqrtDisp;
    }
    //меньше ли 20 следующий отрезок
    static boolean lastPeriodLess20(int iPeriod){
        boolean b=false;
        if((arD.size()-(iPeriod+1)*20)<20)
            b=true;
        return b;
    }
}
