package com.util;



import com.modle.DataInfo;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.boot.system.ApplicationHome;

import java.io.*;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;

/**
 * Created by zxc on 2020/4/10.
 */
public class ExcelUtil {

    private static final String XLS = "xls";
    private static final String XLSX = "xlsx";
    public boolean isfinies=false;

    /**
     * 信号量，用以同步fileParser类
     */
    public Semaphore semaphore=new Semaphore(1000);


    /**
     * 读取excel
     * @param fileName
     * @return
     */
    public  Sheet getexelSheet(String fileName){
        Workbook workbook = null;
        FileInputStream fis=null;
        File file=new File(fileName);
        try {
            fis=new FileInputStream(file);
            String fileType = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
            // 获取Excel工作簿
            workbook=getWorkbook(fis,fileType);
            //获取工作sheet
            Sheet sheet=getSheet(workbook);
            if (sheet!=null){
                return sheet;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                fis.close();
                workbook.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public Workbook writeSheet(String fileName){
            Workbook workbook=null;
            String fileType = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
            // 获取Excel工作簿
           if(fileType.equals(XLS)){
               workbook = new HSSFWorkbook();
           }else {
               workbook = new SXSSFWorkbook();
           }
        return workbook;
    }

    /**
     * 根据文件后缀名类型获取对应的工作簿对象
     * @param inputStream 读取文件的输入流
     * @param fileType 文件后缀名类型（xls或xlsx）
     * @return 包含文件数据的工作簿对象
     * @throws IOException
     */
    public static Workbook getWorkbook(InputStream inputStream, String fileType) throws IOException {
        Workbook workbook = null;
        if (fileType.equalsIgnoreCase(XLS)) {
            workbook = new HSSFWorkbook(inputStream);
        } else if (fileType.equalsIgnoreCase(XLSX)) {
            workbook = new XSSFWorkbook(inputStream);
        }
        return workbook;
    }

    public static Sheet getSheet(Workbook workbook){
        Sheet sheet = workbook.getSheetAt(0);
        // 校验sheet是否合法
        if (sheet == null) {
            return null;
        }
        return sheet;
    }

    public static void main(String[] args) {
        ExcelUtil excelUtil=new ExcelUtil();
        ConcurrentLinkedQueue<DataInfo> readDatas=new ConcurrentLinkedQueue<DataInfo>();
        ApplicationHome h = new ApplicationHome(ExcelUtil.class);
        File file= new File(h.getSource().getParent());
        Sheet sheet=excelUtil.getexelSheet("D:\\data.xlsx");
        int num=sheet.getLastRowNum();
        for (int i=0;i<num;i++){
            if(sheet.getRow(i)!=null){
                DataInfo dataInfo=new DataInfo(String.valueOf(sheet.getRow(i).getCell(0)),String.valueOf(sheet.getRow(i).getCell(1)));
                dataInfo.setType(3);
                readDatas.add(dataInfo);
                try {
                    excelUtil.semaphore.acquire(i);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if(i==num-1){
                excelUtil.isfinies=true;
            }
        }

        DataInfo dataInfo=null;
        int i=0;
        Workbook workbookWriter=excelUtil.writeSheet("xlsx");
        Sheet writerSheet=workbookWriter.createSheet();
        while (true){
            do{
            dataInfo=readDatas.poll();
            }while (dataInfo==null && !excelUtil.isfinies);
            if(dataInfo !=null){
                Row row = writerSheet.createRow(i);
                row.createCell(0).setCellValue(dataInfo.getPid());
                row.createCell(1).setCellValue(dataInfo.getMoble());
                row.createCell(2).setCellValue(2);
                excelUtil.semaphore.release(1);
            }
            if(dataInfo==null && excelUtil.isfinies){
                break;
            }
        }
        try {
            FileOutputStream fos=new FileOutputStream(new File("D:\\data-out.xlsx"));
            workbookWriter.write(fos);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }


    }


}
