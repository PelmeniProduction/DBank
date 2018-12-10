package com.example.demo1;

import com.example.math.DataBase;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

@org.springframework.web.bind.annotation.RestController
public class RestController {
    @RequestMapping(value ="/update", method = RequestMethod.POST)//для кнопки обновить данные
    public ArrayList<TimeSeriesData> update_data(@RequestBody SessionRequest params)
    {
        //инициализируем переменную
        ArrayList<TimeSeriesData> arrTSD = new ArrayList<TimeSeriesData>();
        try{
            //Создаём отдельный connection и statement
            ConnectionManager connectionManager = new ConnectionManager();
            Connection conn = connectionManager.getConnection1();
            Statement stmt = conn.createStatement();
            //парсим на две переменные params.getPeriod()
            Date startD = new Date(118,0,1);
            Date endD = new Date(118,0,1);
            String[] tempStr;
            String delimeter = "-"; // Разделитель
            tempStr = params.getPeriod().split(delimeter); // Разделение строки params.getPeriod() по тире с помощью метода split()
            //Меняем местами год и день в строке и парсим в тип date
            startD = changeFormatForString("\\.", tempStr[0].trim());
            endD = changeFormatForString("\\.", tempStr[1].trim());
            //Вызываем функцию отдающую list<timeSeriesData>
            arrTSD = DataBase.getListFromDate(conn, stmt, startD, endD);
            //Закрываем connection и statmnet
            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return arrTSD;
    }

    public static Date changeFormatForString (String del, String strDate)
    {
        String[] strArr;
        strArr = strDate.split(del);
        int iDay=Integer.parseInt(strArr[0]);
        int iMonth = Integer.parseInt(strArr[1])-1;
        int iYear = Integer.parseInt(strArr[2])-1900;
        Date d= new Date(iYear, iMonth, iDay);
        return d;
    }
}
