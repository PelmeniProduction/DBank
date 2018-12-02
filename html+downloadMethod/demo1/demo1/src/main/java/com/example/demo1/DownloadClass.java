package com.example.demo1;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

public class DownloadClass {
    public static void main(String[] args) {
        //скачивает excel-файл по ссылке при нажатии на зелёный треугольник слева
        String url = "http://gold.investfunds.ru/indicators/export_to_xls.php?id=224&start_day=1&start_month=04&start_year=2018&finish_day=24&finish_month=11&finish_year=2018";

        try {
            // качаем файл с помощью NIO
            downloadUsingNIO(url, "C://downloadFolder/11.xls");

            // качаем файл с помощью Stream
            downloadUsingStream(url, "C://downloadFolder/22.xls");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    // качаем файл с помощью Stream
    private static void downloadUsingStream(String urlStr, String file) throws IOException{
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

    // качаем файл с помощью NIO
    private static void downloadUsingNIO(String urlStr, String file) throws IOException {
        URL url = new URL(urlStr);
        ReadableByteChannel rbc = Channels.newChannel(url.openStream());
        FileOutputStream fos = new FileOutputStream(file);
        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        fos.close();
        rbc.close();
    }
}
