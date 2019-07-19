package com.miaoshaproject.utils;

import sun.misc.BASE64Encoder;
import java.security.MessageDigest;


public class MD5Utils {

    public static String EncodeByMD5(String str) {
        String newStr = str;
        try{
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            BASE64Encoder base64Encoder = new BASE64Encoder();
            //加密字符串
            newStr = base64Encoder.encode(md5.digest(str.getBytes("utf-8")));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newStr;
    }

}
