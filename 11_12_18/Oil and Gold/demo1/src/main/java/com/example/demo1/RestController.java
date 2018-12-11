package com.example.demo1;

import com.example.math.DataBase;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@org.springframework.web.bind.annotation.RestController
public class RestController {
    @RequestMapping(value ="/update", method = RequestMethod.POST)//для кнопки обновить данные
    public List<AnalyzedData> update_data(@RequestBody SessionRequest params)
    {
        //инициализируем переменную
        List<AnalyzedData> arrTSD = new ArrayList<AnalyzedData>();
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
            //проверки есть ли тире и равноколичественны ли части слева и права
            String str = params.getPeriod();
            boolean bTest = false, bTest1 = false;
            for (int i = 0; i < str.length(); i++) {
                if (str.charAt(i) == '-')
                    bTest = true;
            }
            if (bTest) {
                tempStr = str.split(delimeter); // Разделение строки params.getPeriod() по тире с помощью метода split()
                if (tempStr[0].trim().length() == tempStr[1].trim().length()) {//Меняем местами год и день в строке и парсим в тип date
                    startD = changeFormatForString("\\.", tempStr[0].trim());
                    endD = changeFormatForString("\\.", tempStr[1].trim());
                } else {
                    endD = new Date(118, 5, 1);
                }
            } else {
                endD = new Date(118, 5, 1);
            }
            //Вызываем функцию отдающую list<timeSeriesData>
            AnalyticService.reloadDataFromDate("gold",conn, stmt, startD, endD);
            arrTSD = AnalyticService.getCachedAnalData();
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
