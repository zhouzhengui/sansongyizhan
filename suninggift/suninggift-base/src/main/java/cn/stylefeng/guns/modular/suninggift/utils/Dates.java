package cn.stylefeng.guns.modular.suninggift.utils;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;


/**
 * 日期相关操作。
 *
 * @author mall
 */
public class Dates {
    /**
     * yyyy-MM-dd HH:mm:ss.
     */
    public static final SimpleDateFormat STD_DATE_FORMAT = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss");
    /**
     * yyyy-MM-dd HH:mm:ss.SSS.
     */
    public static final SimpleDateFormat STD_DATE_FORMAT2 = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss.SSS");

    /**
     * yyyy-MM-dd.
     */
    public static final SimpleDateFormat STD_DATE_FORMAT3 = new SimpleDateFormat(
            "yyyy-MM-dd");

    /**
     * yyyyMMdd_HHmm
     */
    public static final SimpleDateFormat STD_DATE_FORMAT4 = new SimpleDateFormat(
            "yyyyMMdd_HHmm");

    /**
     * 获取当前时间字符串(yyyy-MM-dd HH:mm:ss)。
     * @return 字符串
     */
    public static String now() {
        return STD_DATE_FORMAT.format(Calendar.getInstance().getTime());
    }

    /**
     * 格式化日期。
     * @param date 日期
     * @param pattern 格式
     * @return 字符串
     */
    public static String format(Date date, String pattern) {
        return new SimpleDateFormat(pattern).format(date);
    }

    /**
     * 格式化日期。
     * @param date 日期
     * @param pattern 格式
     * @param locale 支持显示语言
     * @return 字符串
     */
    public static String format(Date date, String pattern, Locale locale) {
        return new SimpleDateFormat(pattern, locale).format(date);
    }

    /**
     * 使用标准格式yyyy-MM-dd HH:mm:ss格式化日期。
     * @param date Date日期
     * @return String
     */
    public static String format(Date date) {
        return STD_DATE_FORMAT.format(date);
    }

    /**
     * 使用标准格式yyyy-MM-dd HH:mm:ss.SSS格式化日期。
     * @param date Date日期
     * @return String
     */
    public static String format2(Date date) {
        return STD_DATE_FORMAT2.format(date);
    }

    /**
     * 使用标准格式yyyy-MM-dd格式化日期。
     * @param date Date日期
     * @return String
     */
    public static String format3(Date date) {
        return STD_DATE_FORMAT3.format(date);
    }

    /**
     * 使用标准格式yyyyMMdd_HHmm格式化日期。
     * @param date Date日期
     * @return String
     */
    public static String format4(Date date) {
        return STD_DATE_FORMAT4.format(date);
    }

    /**
     * 使用标准格式yyyy-MM-dd HH:mm:ss格式化日期。
     * @param milliSeconds 微秒
     * @return String 格式化日期字符串。
     */
    public static String format(long milliSeconds) {
        return STD_DATE_FORMAT.format(new Date(milliSeconds));
    }

    /**
     * 使用标准格式yyyy-MM-dd HH:mm:ss.SSS格式化日期。
     * @param milliSeconds 微秒
     * @return String 格式化日期字符串。
     */
    public static String format2(long milliSeconds) {
        return STD_DATE_FORMAT2.format(new Date(milliSeconds));
    }

    /**
     * 使用标准格式yyyy-MM-dd格式化日期。
     * @param milliSeconds 微秒
     * @return String 格式化日期字符串。
     */
    public static String format3(long milliSeconds) {
        return STD_DATE_FORMAT3.format(new Date(milliSeconds));
    }

    /**
     * 解析日期。
     * @param value 字符串
     * @return 日期 
     * @throws Exception 
     */
    public static Date parse(String value) throws Exception {
        try {
            return STD_DATE_FORMAT.parse(value);
        } catch (ParseException e) {
            throw new Exception(e);
        }
    }

    /**
     * 从字符串中解析日期。
     * @param value 字符串
     * @param pattern 格式
     * @return 日期
     * @throws Exception 
     */
    public static Date parse(String value, String pattern) throws Exception {
        try {
            Date date = new SimpleDateFormat(pattern).parse(value);
            if (value.equals(format(date, pattern))) {
                return date;
            }
            throw new Exception("Unable to parse date from " + value + " by format " + pattern);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    /**
     * 当前月有多少天
     * @return 当天月的天数
     */
    public static int getCurrentMonthLastDay() {
        Calendar a = Calendar.getInstance();
        //把日期设置为当月第一天
        a.set(Calendar.DATE, 1);
        //日期回滚一天，也就是最后一天
        a.roll(Calendar.DATE, -1);
        int maxDate = a.get(Calendar.DATE);
        return maxDate;
    }

    /**
     * 格式化某天的日期
     * @param offset 距当天日期的后多少天
     * @param pattern 格式
     * @return 字符串
     */
    public static String formatOneDayDate(int offset, String pattern) {
        return formatOneDayDate(offset, pattern, Locale.getDefault());
    }

    /**
     * 格式化某天的日期
     * @param offset 距当天日期的后多少天
     * @param pattern 格式
     * @param locale 支持显示语言
     * @return 字符串
     */
    public static String formatOneDayDate(int offset, String pattern, Locale locale) {
        Calendar c = Calendar.getInstance();
        //设置当前日期
        c.setTime(new Date());
        //日期加offset天
        c.add(Calendar.DATE, offset);
        //结果
        Date date = c.getTime();
        return format(date, pattern, locale);
    }

    /**
     * 格式化某些天日期的集合
     * @param offset 距当天日期的后多少天
     * @param maxSize  
     * @param pattern 格式
     * @return 字符串
     */
//    public static ArrayList<String> formatSomeDayDates(int offset, int maxSize, String pattern, Locale locale) {
//        ArrayList<String> dates = Lists.newArrayList();
//        for (int i = offset; i < offset + maxSize; i++) {
//            dates.add(formatOneDayDate(i, pattern, locale));
//        }
//        return dates;
//    }

    /** 
     * 得到几天前的时间 
     * @param day
     * @return 
     */
    public static Date getDateBefore(int day) {
        Calendar now = Calendar.getInstance();
        //把当前时间小时变成０
        now.set(Calendar.HOUR, 0);
        //把当前时间分钟变成０
        now.set(Calendar.MINUTE, 0);
        //把当前时间秒数变成０
        now.set(Calendar.SECOND, 0);
        //把当前时间毫秒变成０
        now.set(Calendar.MILLISECOND, 0);
        now.set(Calendar.DATE, now.get(Calendar.DATE) - day);
        return now.getTime();
    }

    /** 
     * 得到几天后的时间 
     * @param day
     * @return 
     */
    public static Date getDateAfter(int day) {
        Calendar now = Calendar.getInstance();
        //把当前时间小时变成０
        now.set(Calendar.HOUR, 0);
        //把当前时间分钟变成０
        now.set(Calendar.MINUTE, 0);
        //把当前时间秒数变成０
        now.set(Calendar.SECOND, 0);
        //把当前时间毫秒变成０
        now.set(Calendar.MILLISECOND, 0);
        now.set(Calendar.DATE, now.get(Calendar.DATE) + day);
        return now.getTime();
    }

    /**
     * 得到当前时间的前几个月时间
     * @param monthNum 
     * @return
     */
    public static Date getDateBeforeMonth(int monthNum) {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(new Date());
        gc.add(Calendar.MONTH, -monthNum);
        return gc.getTime();
    }

    /**
     * 得到当前时间的后几个月时间
     * @param monthNum
     * @return
     */
    public static Date getDateAfterMonth(int monthNum) {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(new Date());
        gc.add(Calendar.MONTH, monthNum);
        return gc.getTime();
    }

    public static void main(String[] args) throws Exception {
        //2013-03-28(星期四)
        System.out.println(Dates.parse("2013-03-28(星期四)", "yyyy-MM-dd(EEEE)"));
        String maxTime = Dates.format3(new Date());
        String minTime = Dates.format3(Dates.getDateBeforeMonth(1));
        System.out.println("maxTime:" + maxTime);
        System.out.println("minTime:" + minTime);
    }

}
