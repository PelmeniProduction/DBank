package com.example.demo1;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;


@Component
public class ScheduledTasks {

    @Scheduled(fixedDelay = 5000)
    public void reportCurrentTime() {
        System.out.println("Метод sceduled срабатывает каждые 5 сек.");
    }

    //Метод для скачивания excel-файла по типу данных (золото или нефть), дате старта и дате конца
    public void downloadByPeriodAndType(String type, String startDay, String startMonth, String startYear, String finishDay, String finishMonth, String finishYear)
    {
        String url = "";
        switch(type)
        {
            case "gold":
                url = "http://gold.investfunds.ru/indicators/export_to_xls.php?id=224&start_day="+startDay+"&start_month="+startMonth+"&start_year="+startYear+"&finish_day="+finishDay+"&finish_month="+finishMonth+"&finish_year="+finishYear;
                break;
            case "oil":
                url = "https://investfunds.ru/indexes/624?date_start="+startDay+"."+startMonth+"."+startYear+"&date_end="+finishDay+"."+finishMonth+"."+finishYear+"&index_id=624&file_name=Нефть+Brent";
                break;
        }
        try {
            // качаем файл с помощью Stream
            downloadUsingStream(url, "C://downloadFolder/db/22.xls");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    // качаем файл с помощью Stream
    public void downloadUsingStream(String urlStr, String file) throws IOException{
        URL url = new URL(urlStr);
        BufferedInputStream bis = new BufferedInputStream(url.openStream());
        FileOutputStream fis = new FileOutputStream(file);
        byte[] buffer = new byte[1024];
        int count=0;
        while((count = bis.read(buffer,0,1024)) != -1)
        {
            fis.write(buffer, 0, count);
        }
        fis.close();
        bis.close();
    }

}
