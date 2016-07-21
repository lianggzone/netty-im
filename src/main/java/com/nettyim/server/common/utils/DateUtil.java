package com.nettyim.server.common.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * <p>Title: 时间工具类         </p>
 * <p>Description: DateUtil </p>
 * <p>Create Time: 2016年03月30日           </p>
 * @author lianggz
 */
public class DateUtil {

    private DateUtil(){}
    
    /**
     * 获取当日的截止时间戳
     * @author lianggz
     */
    public static Long getTodayEndTs(){
        Calendar cal = Calendar.getInstance(); 
        cal.set(Calendar.HOUR_OF_DAY, 24); 
        cal.set(Calendar.SECOND, 0); 
        cal.set(Calendar.MINUTE, 0); 
        cal.set(Calendar.MILLISECOND, 0); 
        return cal.getTimeInMillis(); 
    }
    
    /**
     * 字符串转换成日期
     * @param strDate       待转换的字符串数据[例如 "2013-10-20"]
     * @param strDateFormat 待转换的字符串数据的格式[例如 "yyyy-MM-dd"]
     * @return 
     * @author lianggz
     */
    public static Date str2Date(String strDate, String strDateFormat) {
        Date date = null;   
        if (!(strDate == null || strDate.length() == 0)) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);
                date = sdf.parse(strDate);
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
        return date;
    }
    
    /**
     * 将字符串转换成日期
     * 注意：默认格式 yyyy-MM-dd
     * @param strDate 待转换的字符串数据[例如 "2013-10-20"]
     * @return
     * @author lianggz
     */
    public static Date str2Date(String strDate) {
        return str2Date(strDate, "yyyy-MM-dd");
    }
    
    /**
     * 将字符串转换成日期[当天最早时间]
     * 注意：默认格式 yyyy-MM-dd
     * @param strDate 待转换的字符串数据[例如 "2013-10-20"]
     * @return
     * @author lianggz
     */
    public static Date str2DateBeginTime(String strDate) {
        return str2Date(strDate+ " 00:00:00", "yyyy-MM-dd HH:mm:ss");
    }
    
    /**
     * 将字符串转换成日期[当天最晚时间]
     * 注意：默认格式 yyyy-MM-dd
     * @param strDate 待转换的字符串数据[例如 "2013-10-20"]
     * @return
     * @author lianggz
     */
    public static Date str2DateEndTime(String strDate) {
        return str2Date(strDate+ " 23:59:59", "yyyy-MM-dd HH:mm:ss");
    }
}
