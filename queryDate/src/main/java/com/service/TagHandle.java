package com.service;


import com.dao.TagDao;
import com.dao.TagDaoImplss;
import com.modle.DataInfo;
import com.util.ExcelUtil;
import com.zzc.cassandra.NezhaAccessor;
import org.apache.commons.cli.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.boot.system.ApplicationHome;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**n
 * Created by zxc on 2020/4/9.
 */
public class TagHandle<T> extends Thread {
//    public static ExecutorService executorService= Executors.newFixedThreadPool(10);
private static final Logger logger = LogManager.getLogger("TagHandle");
    public String path;
    private NezhaAccessor nezhaAccessor;
    private TagDao tagDao;
    private TagDaoImplss tagDaoImplss;
    public boolean isfinies=false;
    public boolean selectIsfinies=false;

    protected int    dbThreads = 1;
    protected int    encryptType =2;
    protected String datafile = null;
    protected String filename = null;
    public ExcelUtil excelUtil;
    int i=0;

    public ConcurrentLinkedQueue<DataInfo> readQueen;
    public ConcurrentLinkedQueue<DataInfo> writerQueen;

    public TagHandle(String path, NezhaAccessor nezhaAccessor, TagDao tagDao,TagDaoImplss tagDaoImplss, ConcurrentLinkedQueue<DataInfo> readQueen, ConcurrentLinkedQueue<DataInfo> writerQueen) {
        this.path = path;
        this.nezhaAccessor = nezhaAccessor;
        this.tagDao = tagDao;
        this.tagDaoImplss=tagDaoImplss;
        this.readQueen = readQueen;
        this.writerQueen = writerQueen;
    }



    public  void prossDate() {
        //读文件操作

        Sheet sheet=excelUtil.getexelSheet(path+File.separator+filename);
//          Sheet sheet=excelUtil.getexelSheet("D:\\桌面\\query\\data-md5.xlsx");
        int num=sheet.getLastRowNum();
                for (int i=0;i<num+1;i++){
                    if(sheet.getRow(i)!=null){
                        DataInfo dataInfo=new DataInfo(String.valueOf(sheet.getRow(i).getCell(0)),String.valueOf(sheet.getRow(i).getCell(1)));
                        if(encryptType==2){
                            dataInfo.setPid256(String.valueOf(sheet.getRow(i).getCell(0)));
                            dataInfo.setMobile256(String.valueOf(sheet.getRow(i).getCell(1)));
                        }else {
                            tagDaoImplss.getSha256Info(dataInfo, encryptType);
                        }
                        dataInfo.setTime(sheet.getRow(i).getCell(2)==null?null:String.valueOf(sheet.getRow(i).getCell(2)));
                        logger.info("mobile:("+dataInfo.getMobile256()+" "+dataInfo.getMoble()+"-----pid:("+dataInfo.getPid256()+" "+dataInfo.getPid());
                        System.out.println("读取第----"+i+"行");
                        readQueen.add(dataInfo);
                    }
                    if(0==num-i){
                        isfinies=true;
                    }
                }
    }


    public void writerFile(){
        //写文件操作
        DataInfo dataInfo=null;
        Workbook workbookWriter=excelUtil.writeSheet(filename);
        Sheet writerSheet=workbookWriter.createSheet();
        try {
        while (true){
            do{
                dataInfo=writerQueen.poll();
            }while (dataInfo==null && !selectIsfinies);

            if(dataInfo !=null){
                Row row = writerSheet.createRow(i);
                row.createCell(0).setCellValue(dataInfo.getPid());
                row.createCell(1).setCellValue(dataInfo.getMoble());
                row.createCell(2).setCellValue(dataInfo.getTime());
                row.createCell(3).setCellValue(dataInfo.getType());
                i++;
            }
           System.out.println("写入第----"+i+"行");

            if(dataInfo==null && selectIsfinies){
                break;
            }
        }

            FileOutputStream fos=new FileOutputStream(new File(path,"out_"+filename));
            System.out.println("filename--------out_"+filename);
            workbookWriter.write(fos);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void run() {
        DataInfo dataInfo=null;
        while (true) {
            do {
                dataInfo = readQueen.poll();
            } while (dataInfo == null && !isfinies);

            while (dataInfo==null && isfinies){
                selectIsfinies=true;
               return;
            }

            if((dataInfo.getPid256()==null|| "".equals(dataInfo.getPid256())) && (dataInfo.getMobile256()==null ||"".equals(dataInfo.getMobile256()))){
                dataInfo.setType(0);
                writerQueen.add(dataInfo);
                continue;
            }

            if(getinfosCount(dataInfo,datafile)>0){
                dataInfo.setType(1);
            }else {
                dataInfo.setType(0);
            }
            writerQueen.add(dataInfo);
        }

    }


    public int getinfosCount(DataInfo dataInfo,String dataType){
        if (dataType==null ||dataType.equals("all")){
            if(tagDaoImplss.getsq(dataInfo,dataType)>0||tagDaoImplss.getoverdue(dataInfo,dataType)>0||tagDaoImplss.getgaofa(dataInfo,dataType)>0 ||tagDaoImplss.getp2p(dataInfo,dataType)>0){
                return 1;
            }}
        if(dataType.equals("gaofa") && tagDaoImplss.getgaofa(dataInfo,dataType)>0){
            return 1;
        }
        if(dataType.equals("p2p") && tagDaoImplss.getp2p(dataInfo,dataType)>0){
            return 1;
        }
        if(dataType.equals("sq") &&tagDaoImplss.getsq(dataInfo,dataType)>0){
            return 1;
        }
        if(dataType.equals("overdue") &&tagDaoImplss.getoverdue(dataInfo,dataType)>0){
            return 1;
        }

        return 0;
    }

    /**
     * 参数处理
     * @param args
     */
    public void parseCmdArgs(String[] args) {
        Options options = new Options();

        Option nThread = new Option("n", "threads", true, "number of data-handle threads");
        nThread.setRequired(false);
        options.addOption(nThread);

        Option type = new Option("t", "type", true, "loading type(1 明文，2 sha256 ,3 md5 ,4 sm3 )");
        type.setRequired(true);
        options.addOption(type);

        Option datafileOp = new Option("s", "datafile", true, "(p2p/gaofa/sqsj/overdue/)all");
        datafileOp.setRequired(true);
        options.addOption(datafileOp);

        Option pathfileOp = new Option("p", "pathfile", true, "the file path");
        pathfileOp.setRequired(true);
        options.addOption(pathfileOp);

        CommandLineParser parser = new GnuParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd = null;

        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            formatter.printHelp(Thread.currentThread().getStackTrace()[1].getClassName(), options, true);
            System.exit(-1);
        }
        dbThreads = Math.max(1,Integer.valueOf(cmd.getOptionValue("n")));
        encryptType = Integer.valueOf(cmd.getOptionValue("t"));
        datafile= cmd.getOptionValue("s")==null?"all":cmd.getOptionValue("s");
        filename= cmd.getOptionValue("p");
    }


}
