package com.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.concurrent.*;

/**
 * Created by zxc on 2020/3/26.
 */
public class Ntest implements Callable{

    @Autowired
    private RestTemplate restTemplate;
    ExecutorService executorService= Executors.newFixedThreadPool(20);
    public static void main(String[] args) throws ExecutionException, InterruptedException {
//        //虚拟机的线程管理接口
//        ThreadMXBean threadMXBean= ManagementFactory.getThreadMXBean();
//        //获取线程信息的方法
//        ThreadInfo[] threadInfos=threadMXBean.dumpAllThreads(false,false);
//        for (ThreadInfo threadInfo:threadInfos){
//            System.out.println(threadInfo.getThreadId()+"--"+threadInfo.getThreadName()+"---"+threadInfo.getThreadState());
//        }

        FutureTask<String> futureTask=null;
        for (int i=0;i<1000;i++){
            Ntest ntest=new Ntest();
            futureTask= (FutureTask<String>) ntest.executorService.submit(ntest);
            Object s1=futureTask.get();
            System.out.println("SSSS111---"+s1+"isdone----"+futureTask.isDone());
        }

        while (!futureTask.isDone()){
            try {
                String s=futureTask.get();
                System.out.println("SSSS---"+s);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }







    }

    @Override
    public Object call() throws Exception {
        System.out.println("run---");
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "ok run done";
    }


//    @Override
//    public void run() {
//        System.out.println("run---");
//        try {
//
//            Thread.sleep(10000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//    }



    public void restTemplates(){
        restTemplate.postForEntity("","",Integer.class);
        try {
            Reader r=new InputStreamReader(new BufferedInputStream(new FileInputStream(new File(""))));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}
