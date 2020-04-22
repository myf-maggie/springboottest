package com.web;


import com.QueryDataApplication;
import com.dao.TagDao;
import com.dao.TagDaoImplss;
import com.modle.DataInfo;
import com.service.TagHandle;
import com.util.CommonHandle;
import com.util.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * Created by zxc on 2020/4/7.
 */
@Component
public class Test {

    static {
        CommonHandle.initLog(Test.class.getName());
    }
    private static final Logger logger = LogManager.getLogger("Test");

    @Autowired
    private TagDaoImplss tagDaoimp;



    public static void main(String[] args) {

            try {
//                args= new String[]{"-n", "2", "-t", "3","-s","all","-p","data-md5.xlsx"};
                AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(QueryDataApplication.class);
                Test test1 =ctx.getBean(Test.class);
                test1.handle(null,args,test1.tagDaoimp);
            }catch (Exception e){
                logger.info(e);
            }finally {

                System.exit(0 );
            }




    }

    private void handle(TagDao tagDao,String[] args,TagDaoImplss tagDaoImplss) {

        // 初始化共享数据

        String path=new ApplicationHome(Test.class).getSource().getParent();
        logger.info("path----------"+path);

        ConcurrentLinkedQueue<DataInfo> readQueen=new ConcurrentLinkedQueue<>();
        ConcurrentLinkedQueue<DataInfo> writerQueen= new ConcurrentLinkedQueue<>();

        //      读取文件数据
        TagHandle tagHandle=new CommonHandle(path,null,tagDao,tagDaoImplss,readQueen,writerQueen);
        tagHandle.parseCmdArgs(args);
        tagHandle.excelUtil=new ExcelUtil();

        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                tagHandle.prossDate();
            }
        });thread.start();

        //查询数据
        tagHandle.start();
        //写入excel
        tagHandle.writerFile();

//        异步
//        CompletableFuture<String> future=CompletableFuture.supplyAsync(tagHandle,tagHandle.executorService);  有返回
//        CompletableFuture.runAsync(tagHandle,tagHandle.executorService); 无返回





    }








}
