package com.utils;

/**
 * Created by zxc on 2020/1/14.
 */
public class query_weizhi {
    public static void main(String[] args) {
//        args=new String[3];
//        args[0]="2491a19ce53e31beb0d5d2fb3f517cfc0caab4fd7c8f3b7966c01ae1ce0842ab";
//        args[1]="sm3";
//        args[2]="pid";
        String tblNo="";
        if("sm3".equals(args[1])){
            String ss=args[0].substring(6, 8);
            int a=Integer.valueOf(args[0].substring(6, 8), 16);
            tblNo=  args[1]+"_"+args[2]+"_"+Integer.valueOf(args[0].substring(6, 8), 16)%64;
        }if("md5".equals(args[1])){
            String targetDate_md5=args[0].substring((args[0].length()/2)-8, (args[0].length()/2)+8);
            String s=targetDate_md5.substring(6,8);
            tblNo =args[1]+"_"+args[2]+"_"+Integer.valueOf(targetDate_md5.substring(6,8), 16)%64;
        }
        if("tag".equals(args[1])){
            tblNo ="tag"+"_"+args[2]+"_"+Integer.valueOf(args[0].substring(6,8), 16)%256;
        }
        System.out.print(tblNo);

    }

}
