package com.example.math;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Created by Kano on 09.11.2018.
 */
public class DataBase {
    //создать таблицу
    static void crateTable(Connection c, Statement stmt){
        try{
            String sql = "CREATE TABLE GOLD " +
                    "(ID INT PRIMARY KEY     NOT NULL," +
                    " COAST           REAL    NOT NULL, " +
                    " EMA            REAL     NOT NULL, " +
                    " VOLATILITY            REAL     NOT NULL)";
            stmt.executeUpdate(sql);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            //System.exit(0);
        }
    }
    //удалить таблицу
    static void dropTable(Connection c,Statement stmt) {
        try{
            String sql = "DROP TABLE GOLD ";
            stmt.executeUpdate(sql);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            //System.exit(0);
        }
    }
    //вывести на консоль базу
    static void printTable(Statement stmt) {
        try{
            ResultSet rs = stmt.executeQuery("SELECT * FROM GOLD;");
            while (rs.next()) {
                int id = rs.getInt("ID");
                float coast = rs.getFloat("COAST");
                float EMA = rs.getFloat("EMA");
                float volatitlity = rs.getFloat("VOLATILITY");
                System.out.println("ID = " + id + " COAST = " + coast + " EMA= " + EMA + "VOLATILITY= " + volatitlity);
            }
            System.out.println("Print successfully");
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
    }
    static void deleteAllRecords(Statement stmt,ArrayList<Double> arD){
        try{
            String sql;
            for (int i = 0; i < arD.size(); i++) {
                sql="DELETE from GOLD where ID ="+i+";";
                stmt.execute(sql);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
    }
    //заполнить базу
    static void fillTable(Statement stmt, ArrayList<Double> arD, ArrayList<Double> arDEma , ArrayList<Double> arDVol){
        try{
            String sql;
            int iVol;
            for (int i = 0; i < arD.size(); i++) {
                if(lastPeriodLess20(i/20,arD))
                    iVol=arDVol.size()-1;
                else
                    iVol=i/20;
                sql = "INSERT INTO GOLD (ID,COAST,EMA,VOLATILITY) "
                        + "VALUES ("+i+", "+arD.get(i)+", "+arDEma.get(i)+", "+arDVol.get(iVol)+" );";
                stmt.executeUpdate(sql);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
    }
    static double getVolFromID(Statement stmt, int id){
        double dVola=0;
        String sql="SELECT ID,VOLATILITY FROM GOLD WHERE ID=";
        try{
            ResultSet rs = stmt.executeQuery(sql+id+";");
            while (rs.next()) {
                float volatitlity = rs.getFloat("VOLATILITY");
                dVola=volatitlity;
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
        return dVola;
    }
    static boolean lastPeriodLess20(int iPeriod,ArrayList<Double> arD){
        boolean b=false;
        if((arD.size()-(iPeriod+1)*20)<20)
            b=true;
        return b;
    }
}
