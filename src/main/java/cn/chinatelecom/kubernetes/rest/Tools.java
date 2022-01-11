package cn.chinatelecom.kubernetes.rest;

import java.text.SimpleDateFormat;
import java.util.Date;


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

}
