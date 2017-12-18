package org.redrock.christmas.util;

import java.util.Calendar;

public class DateUtil {

    /**
     * 获得系统时间
     * @return 根据数据库date格式格式化的t_date
     */
    public static String getdate() {
        Calendar now = Calendar.getInstance();
        int month = now.get(Calendar.MONTH) + 1;
        int day = now.get(Calendar.DAY_OF_MONTH);
        int year = now.get(now.get(Calendar.ERA));
        String t_date = year+"-"+month+"-"+day;
        return t_date;
    }

    /**
     * 判断是否是同一个月
     * @param date
     * @param t_date
     * @return
     */
    public static boolean Same_month(String date , String t_date) {
        int month = Integer.parseInt(date.split("-")[1]);
        int t_month = Integer.parseInt(t_date.split("-")[1]);
        if (month == t_month) {
            return true;
        }
        return false;
    }


    /**
     * 判断是否是同一天
     * @param date
     * @param t_date
     * @return
     */
    public static boolean Same_day(String date , String t_date) {
        int day = Integer.parseInt(date.split("-")[2]);
        int t_day = Integer.parseInt(t_date.split("-")[2]);
        if (day == t_day) {
            return true;
        }
        return false;
    }
}
