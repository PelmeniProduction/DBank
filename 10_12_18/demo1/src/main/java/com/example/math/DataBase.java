package com.example.math;


import com.example.demo1.TimeSeriesData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Kano on 09.11.2018.
 */
public class DataBase {
    //создать таблицу
    static void crateTable(Statement stmt){
        try{
            String sql = "CREATE TABLE GOLD " +
                    "(ID INT PRIMARY KEY     NOT NULL," +
                    " COAST           REAL    NOT NULL, " +
                    " DAYS            DATE     NOT NULL)";
            stmt.executeUpdate(sql);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            //System.exit(0); убить процесс
        }
    }
    //удалить таблицу
    static void dropTable(Statement stmt) {
        try{
            String sql = "DROP TABLE GOLD ";
            stmt.executeUpdate(sql);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
        }
    }
    //вывести на консоль базу
    static void printTable(Statement stmt) {
        try{
            ResultSet rs = stmt.executeQuery("SELECT * FROM GOLD;");
            while (rs.next()) {
                int id = rs.getInt("ID");
                float coast = rs.getFloat("COAST");
                Date days = rs.getDate("DAYS");
                System.out.println("ID = " + id + " COAST = " + coast + " DATE= " + days);
            }
            System.out.println("Print successfully");
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
        }
    }
    static void deleteAllRecords(Statement stmt){
        try{
            String sql;
            sql="DELETE from GOLD ;";
            stmt.execute(sql);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
        }
    }
    //заполнить базу
    public static void fillTable(Connection c, ArrayList<TimeSeriesData> arTsd ){//saveAll
        try{
            ArrayList<Double> ar = new ArrayList<>();
            for (int i = 0; i < arTsd.size(); i++) {
                ar.add(arTsd.get(i).getPrice());
            }
            String sqlPrepSt;
            for (int i = 0; i < arTsd.size(); i++) {
                java.sql.Date sDate = new java.sql.Date(arTsd.get(i).getDate().getTime());
                sqlPrepSt = "INSERT INTO GOLD (COAST,DAYS) "
                        + "VALUES (?, ? );";
                PreparedStatement preparedStatement = c.prepareStatement(sqlPrepSt);
                //preparedStatement.setInt(1,i);
                preparedStatement.setDouble(1,arTsd.get(i).getPrice());
                preparedStatement.setDate(2,sDate);
                preparedStatement.executeUpdate();
                //stmt.executeUpdate(sqlPrepSt);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
        }
    }
    public static Date getLastUpdated(Statement stmt){
        Date dLastDate= new Date(0001,01,01);
        Date dCurrent;
        try{
            ResultSet rs = stmt.executeQuery("SELECT * FROM GOLD ;");//так не очень, если будет время поменять
            while (rs.next()) {
                int k=0;
                dCurrent= rs.getDate("DAYS");
                if(dCurrent==new Date(2018,10,23))
                    k=3;
                if(dCurrent.getTime()>dLastDate.getTime())
                    dLastDate=dCurrent;
            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
        }
        return dLastDate;
    }
    static boolean lastPeriodLess20(int iPeriod,ArrayList<Double> arD){
        boolean b=false;
        if((arD.size()-(iPeriod+1)*20)<20)
            b=true;
        return b;
    }
}
