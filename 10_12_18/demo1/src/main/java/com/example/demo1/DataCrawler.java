package com.example.demo1;

import com.example.math.DataBase;
import com.example.math.DownloadClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

@Component
public class DataCrawler {

    @Autowired
    ConnectionManager connectionManager;


    public static ArrayList<TimeSeriesData> arTimeSeriesData;

    @Scheduled(fixedDelay = 1000*60*60*24)
    public void reloadData() throws Exception {
        connectionManager= new ConnectionManager();
        Connection connection = connectionManager.getConnection1();
        Statement stmt =connection.createStatement();
        Date dInit= new Date(0001,01,01);//начало времен, для сравнения
        Date start = new Date(0001,01,01); // DB.getLastUpdated()
        start= DataBase.getLastUpdated(stmt);
        if (isSameDate(dInit,start)) {//если в таблице ничего нет
            start = subtractFromDate(120,new Date());// 120 дней назад
        }
        Date end = new Date();
        //Date end = new Date(118,10,29);
        if(start==end) {//если сегодня уже обновлялось
            connection.close();
        }else {
            start=subtractFromDate(-1,start);
            String url = makeUrl(start, end);
            arTimeSeriesData = DownloadClass.downloadUsingStream(url);
            DataBase.fillTable(connection, arTimeSeriesData);
            connection.commit();
            connection.close();
        }
    }
    public boolean isSameDate(Date d0, Date d1){
        //d0=subtractFromDate(-1,new Date());//добавим 1
        Calendar cal = Calendar.getInstance();
        cal.setTime(d0);
        int yearStart = cal.get(Calendar.YEAR);
        int monthStart = cal.get(Calendar.MONTH);//sql считает месяцы с 0, а сайт с 1
        int dayStart = cal.get(Calendar.DAY_OF_MONTH);
        cal.setTime(d1);
        int yearEnd = cal.get(Calendar.YEAR);
        int monthEnd = cal.get(Calendar.MONTH);//sql считает месяцы с 0, а сайт с 1
        int dayEnd = cal.get(Calendar.DAY_OF_MONTH);
        boolean b =(yearStart==yearEnd)&&(monthStart==monthEnd)&&(dayStart==dayEnd);
        return b;
    }
    //сделаем урл
    public String makeUrl(Date dStart,Date dEnd){
        Calendar cal = Calendar.getInstance();
        cal.setTime(dStart);
        int yearStart = cal.get(Calendar.YEAR);
        int monthStart = cal.get(Calendar.MONTH)+1;//sql считает месяцы с 0, а сайт с 1
        int dayStart = cal.get(Calendar.DAY_OF_MONTH);
        cal.setTime(dEnd);
        int yearEnd = cal.get(Calendar.YEAR);
        int monthEnd = cal.get(Calendar.MONTH)+1;//sql считает месяцы с 0, а сайт с 1
        int dayEnd = cal.get(Calendar.DAY_OF_MONTH);
        String url = "http://gold.investfunds.ru/indicators/export_to_xls.php?id=224&start_day="+makeSecondSign(dayStart)+"&start_month="+makeSecondSign(monthStart)+"&start_year="+yearStart+"&finish_day="+makeSecondSign(dayEnd)+"&finish_month="+makeSecondSign(monthEnd)+"&finish_year="+yearEnd;
        return url;
    }
    public String makeSecondSign(int i){
        String str="";
        if(i/10==0){
            str+="0";
        }
        str+=i;
        return str;
    }
    //вычесть из текущей даты несколько дней
    public Date subtractFromDate(int iDays,Date date){
        Calendar instance = Calendar.getInstance();
        instance.setTime(date); //устанавливаем дату, с которой будет производить операции
        instance.add(Calendar.DAY_OF_MONTH, -iDays);
        Date newDate = instance.getTime(); // получаем измененную дату
        return newDate;
    }
}
