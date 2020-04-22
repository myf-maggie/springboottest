package com.test;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by zxc on 2019/12/6.
 */
@RestController
@RequestMapping("springboot/")
public class ControllerTetst {

    private final static int startPos = 6;
    private final static int endPos = 8;
    private final static int radix = 256;
    public static final String TAG_TABLE_PREFIX = "tag_";

    @RequestMapping("helloworld")
    public String springbootHellowrld(){

        return "helloWorld";
    }


    public static void main(String[] args) {

        StringBuilder result = new StringBuilder();
        String keyValue="0cfaf028af3cfc54be97baebbee60998e416ac152325d7f7cd81c651c6146ff9";

        int mod = Integer.parseInt(keyValue.substring(startPos, endPos), 16) % radix;
        System.out.print("mod-----"+mod);


//        result.append(TAG_TABLE_PREFIX).append(middle).append("_" + mod);
//        return result.toString();

    }


}
