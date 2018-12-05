package com.example.math;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Kano on 05.11.2018.
 */
@Component
public class GetXLS {

    public  ArrayList<String> arDDate = new ArrayList<String>();
    public  ArrayList<Double> arD = new ArrayList<Double>();
    public  void readExcel() throws IOException {
        Workbook wb = new HSSFWorkbook();
        Sheet newSheet = wb.createSheet("sheet1");

        try {
            FileOutputStream fileOut = new FileOutputStream("C:\\Users\\Kano\\Downloads\\музончик\\новый музончик\\test.xls");
            wb.write(fileOut);
            fileOut.close();
            System.out.println();
        } catch (Exception e) {
            System.out.println("Smth wrong");
        }
        //читать
        // Read XSL file
        FileInputStream inputStream = new FileInputStream(new File("C:\\Users\\Kano\\Downloads\\учеба\\проганье\\java\\дойчбанк\\TestExcel\\src\\gold.xls"));

        // Get the workbook instance for XLS file
        HSSFWorkbook workbook = new HSSFWorkbook(inputStream);

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
                        arD.add(cell.getNumericCellValue());
                        break;
                    case STRING:
                        System.out.print(cell.getStringCellValue());
                        System.out.print("\t");
                        arDDate.add(cell.getStringCellValue());
                        break;
                    case ERROR:
                        System.out.print("!");
                        System.out.print("\t");
                        break;
                }


            }
            System.out.println("");

            //Main readerExcel = new Main();
            //readerExcel.getDataExcel("sheet1");
        }
        for (int i = 0; i < arD.size(); i++) {
            System.out.println(arD.get(i)+" "+arDDate.get(i));
        }
    }

    public  void getDataExcel() {
        System.out.println(String.format("\nДанные из таблицы:"));
        try {
            readExcel();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
