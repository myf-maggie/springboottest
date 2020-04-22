package com.util;

import com.bhcredit.common.config.ConfigMgr;
import com.dao.TagDao;
import com.dao.TagDaoImplss;
import com.modle.DataInfo;
import com.service.TagHandle;
import com.web.Test;
import com.zzc.cassandra.NezhaAccessor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.springframework.boot.system.ApplicationHome;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by zxc on 2020/4/9.
 */
public class CommonHandle extends TagHandle{

    public CommonHandle(String path, NezhaAccessor nezhaAccessor, TagDao tagDao, TagDaoImplss tagDaoImplss, ConcurrentLinkedQueue readQueen, ConcurrentLinkedQueue writerQueen) {
        super(path, nezhaAccessor, tagDao,tagDaoImplss, readQueen, writerQueen);
    }




    public static void initLog(String mainClass) {
        try {
            // 指定log4j2.xml文件路径，使用jar外的文件方便配置
            System.out.println("====================");
            String path=new ApplicationHome(Test.class).getSource().getParent();
            System.out.println("path--"+path);
            LoggerContext context = (LoggerContext) LogManager.getContext(false);

            System.out.println("Loading log4j2.xml from path "+path);
            File file = new File(path,"log4j2.xml");
            context.setConfigLocation(file.toURI());
//            ConfigMgr.init(path+File.separator+"config.properties");
            ConfigMgr.init("config.properties");
//            ConfigMgr.init("E:\\project\\queryDate\\src\\main\\resources\\config.properties");
        }catch (Exception e){
            System.out.println(e);
        }

    }

    public static void main(String[] args) {

        DataInfo dataInfo=new DataInfo(null,null);
        dataInfo.setPid256(null);
        String a=dataInfo.getMobile256();
        Map<String,String> map=new HashMap<>();
        map.put("mobile",dataInfo.getMobile256());
        map.put("pid",dataInfo.getPid256());
    }


}
