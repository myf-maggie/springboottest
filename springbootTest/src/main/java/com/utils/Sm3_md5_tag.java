package com.utils;

import org.springframework.boot.system.ApplicationHome;

import java.io.*;

/**
 * Created by zxc on 2020/1/14.
 */
public class Sm3_md5_tag {
//    private static String path="D:\\桌面\\加密解密工具\\sm3_md5";
    private static String path="D:/桌面/加密解密工具/sm3_md5";
    public static void main(String[] args) {
//      args=new String[3];
//      args[0]="md5.txt";
//      args[1]="md5";
//      args[2]="pid";
        String tblNo="";
        try {

            ApplicationHome h = new ApplicationHome(Sm3_md5_tag.class);
            File file= new File(h.getSource().getParent());
            path = file.toString();
            System.out.print("path--"+path);


            if(new File(path+"/out_"+args[0]).exists()){
                new File(path+"/out_"+args[0]).delete();
            }
            BufferedReader br = new BufferedReader( new InputStreamReader( new FileInputStream(path+"/"+args[0])) );
            BufferedWriter bw= new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path+"/out_"+args[0])));

            String str = "";
            while ((str = br.readLine()) != null) {
                String targetDate="";
                String[] strs=str.split(",");
                if("sm3".equals(args[1])){
                    targetDate=strs[0];
                    tblNo=  args[1]+"_"+args[2]+"_"+Integer.valueOf(strs[0].substring(6, 8), 16)%64;
                }if("md5".equals(args[1])){
                    targetDate=strs[0].substring((strs[0].length()/2)-8, (strs[0].length()/2)+8);
                    tblNo =args[1]+"_"+args[2]+"_"+Integer.valueOf(targetDate.substring(6,8), 16)%64;
                }if("sha256".equals(args[1])){
                    targetDate=strs[0];
                    tblNo ="tag"+"_"+args[2]+"_"+Integer.valueOf(strs[0].substring(6,8), 16)%256;
                }

                bw.write(targetDate+"   "+strs[1]+"   "+tblNo+"\n");
                bw.newLine();
            }
            bw.flush();
            bw.close();
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.print(e);
        }


        System.out.print("finish");

    }

}
