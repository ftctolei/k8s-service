package cn.kubernetes.service.rest;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.stream.Collectors;


public class Tools {

    /**
     * 获取当前时间
     * @return 格式化后的时间
     */
    public static String getNowTime(){
        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.applyPattern("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        return sdf.format(date);
    }


    /**
     * 判断字符串是否为空
     * @param str 待判断的字符串
     * @return 是否为空
     */
    public static Boolean strEmpty(String str){
        return (str == null || str.isEmpty());
    }


    /**
     * 将Java InputStream转换为Java String
     * @param inStream InputStream
     * @return String
     */
    public static String inputStream2Str(InputStream inStream){
        return new BufferedReader(new InputStreamReader(inStream))
                .lines().collect(Collectors.joining(System.lineSeparator()));

    }


}
