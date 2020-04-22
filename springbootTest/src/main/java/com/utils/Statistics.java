package com.utils;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zxc on 2020/2/6.
 */
public class Statistics {
    private static String path="D:/桌面/加密解密工具/sm3_md5";
    private static final String UUID_USER_REGEXP = "\\[([0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12})\\] \\[(.*?)\\]";
    private static final String REQUEST_REGEXP = "\\[api input\\]:(.*)";
    private static final String RESPONSE_REGEXP = "Process rules done, result: (.*)";
    private static final String V1_ENTRANCE_REGEXP = "/api/antifraud/rule-result";
    public static void main(String[] args) {
        try {
            BufferedReader br = new BufferedReader( new InputStreamReader( new FileInputStream(path+"/"+args[0])) );
            BufferedWriter bw= new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path+"/out_"+args[0])));

            Pattern uuidPattern = Pattern.compile(UUID_USER_REGEXP);
            Pattern v1Pattern = Pattern.compile(V1_ENTRANCE_REGEXP);
            Pattern responsePattern = Pattern.compile(RESPONSE_REGEXP);
            Pattern requestPattern = Pattern.compile(REQUEST_REGEXP);

            BufferedReader reader = null;
            String line = null;
            Matcher uuidMatcher = null;
            Matcher resMatcher = null;
            Matcher reqMatcher = null;
            Map map=new HashMap();
            try {
                while ((line=br.readLine())!=null){
                    uuidMatcher = uuidPattern.matcher(line);
                    if(uuidMatcher.find()){


                    }

                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }
}
