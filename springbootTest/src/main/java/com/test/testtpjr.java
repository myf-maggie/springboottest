package com.test;

import java.awt.*;
import java.util.*;
import java.util.List;


/**
 * Created by zxc on 2020/3/26.
 */
public class testtpjr extends LogProducer {

    public testtpjr() throws Exception {
    }

    public static void main(String[] args) {
//        Frame f = new Frame();
//        f.setLayout(new FlowLayout());
//        Label l = new Label("用户名");
//        TextField tf = new TextField("你好啊",10);//10列
//        f.add(l);
//        f.add(tf);
//        f.setSize(300, 200);//窗口大小
//        f.show();//显示窗口
//        List<String> list=new ArrayList<>();
//        list.add("sss");




        Map<String,Object> map= Collections.synchronizedMap(new HashMap<String,Object>());
        for (int i=0;i<20;i++){
            map.put("a"+i,"aa"+i);

        }

    }
}
