package com.example.demo1;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by SK
 **/
public class DownloadClass {
    public static void main(String[] args) {
        //GetXLS.getDataExcel();//проверка метода считвания .xls файла в массив

        //скачивает excel-файл по ссылке при нажатии на зелёный треугольник слева
        String url = "http://gold.investfunds.ru/indicators/export_to_xls.php?id=224&start_day=1&start_month=04&start_year=2018&finish_day=24&finish_month=11&finish_year=2018";

        try {
            // качаем файл с помощью Stream
            downloadUsingStream(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // качаем файл с помощью Stream
    private static List<TimeSeriesData> downloadUsingStream(String urlStr) throws IOException, ParseException {
        String category = "gold";
        ArrayList<TimeSeriesData> result = new ArrayList<>();
        URL url = new URL(urlStr);
        BufferedInputStream bis = new BufferedInputStream(url.openStream());
        //FileOutputStream fis = new FileOutputStream(file);

        // Get the workbook instance for XLS file
        HSSFWorkbook workbook = new HSSFWorkbook(bis);
        // Get first sheet from the workbook
        HSSFSheet sheet = workbook.getSheetAt(0);
        // Get iterator to all the rows in current sheet
        Iterator<Row> rowIterator = sheet.iterator();
        //пропустить вступление
        for (int i = 0; i < 2; i++) {
            rowIterator.next();
        }
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            // Get iterator to all cells of current row
            Iterator<Cell> cellIterator = row.cellIterator();

            TimeSeriesData timeSeriesData = new TimeSeriesData();
            timeSeriesData.setCategory(category);
            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                // Change to getCellType() if using POI 4.x
                CellType cellType = cell.getCellTypeEnum();
                switch (cellType) {
                    case _NONE:
                        System.out.print("");
                        System.out.print("\t");
                        break;
                    case BOOLEAN:
                        System.out.print(cell.getBooleanCellValue());
                        System.out.print("\t");
                        break;
                    case BLANK:
                        System.out.print("");
                        System.out.print("\t");
                        break;
                    case FORMULA:
                        // Formula
                        System.out.print(cell.getCellFormula());
                        System.out.print("\t");
                        FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
                        // Print out value evaluated by formula
                        System.out.print(evaluator.evaluate(cell).getNumberValue());
                        break;
                    case NUMERIC:
                        System.out.print(cell.getNumericCellValue());
                        System.out.print("\t");
                        timeSeriesData.setPrice(cell.getNumericCellValue());
                        break;
                    case STRING:
                        System.out.print(cell.getStringCellValue());
                        System.out.print("\t");
                        String stringDate = cell.getStringCellValue();
                        Date date = new SimpleDateFormat("dd.mm.yyyy").parse(stringDate);
                        timeSeriesData.setDate(date);
                        break;
                    case ERROR:
                        System.out.print("!");
                        System.out.print("\t");
                        break;
                }
            }
            result.add(timeSeriesData);
            System.out.println("");
        }
        result.stream().forEach(System.out::println);
        byte[] buffer = new byte[1024];
        int count = 0;
        bis.close();
    }
}
