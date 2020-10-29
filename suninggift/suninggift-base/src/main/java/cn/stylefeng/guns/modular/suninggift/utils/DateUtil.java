package cn.stylefeng.guns.modular.suninggift.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author:Neptune
 * @Description:DateUtil 提供一些常用的时间方法
 */
public final class DateUtil {

    //日期时间类型格式
    public static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    
  //日期时间类型格式
    public static final String SDF_YYYYMMDD = "yyyyMMdd";
    
    public static  SimpleDateFormat sdf= new SimpleDateFormat("yyyyMMddHHmmss");
    //日期类型格式
    public static final String DATE_FORMAT = "yyyy-MM-dd";

    //时间类型的格式
    public static final String TIME_FORMAT = "HH:mm:ss";

    /**
     * 返回短日期yyyyMMdd
     */
    public static String commonShortTime = (new SimpleDateFormat("yyyyMMdd")).format(new Date());
    
    /**
     * 日期格式: yyyyMMddHHmmss
     */
    public static String commonFullTime = (new SimpleDateFormat("yyyyMMddHHmmss")).format(new Date());

    
    //注意SimpleDateFormat不是线程安全的
    private static ThreadLocal<SimpleDateFormat> ThreadDateTime = new ThreadLocal<SimpleDateFormat>();
    private static ThreadLocal<SimpleDateFormat> ThreadDate = new ThreadLocal<SimpleDateFormat>();
    private static ThreadLocal<SimpleDateFormat> ThreadTime = new ThreadLocal<SimpleDateFormat>();

    private static SimpleDateFormat DateTimeInstance() {
        SimpleDateFormat df = ThreadDateTime.get();
        if (df == null) {
            df = new SimpleDateFormat(DATETIME_FORMAT);
            ThreadDateTime.set(df);
        }
        return df;
    }

    private static SimpleDateFormat DateInstance() {
        SimpleDateFormat df = ThreadDate.get();
        if (df == null) {
            df = new SimpleDateFormat(DATE_FORMAT);
            ThreadDate.set(df);
        }
        return df;
    }

    private static SimpleDateFormat TimeInstance() {
        SimpleDateFormat df = ThreadTime.get();
        if (df == null) {
            df = new SimpleDateFormat(TIME_FORMAT);
            ThreadTime.set(df);
        }
        return df;
    }


    /**
     * 获取当前日期时间
     *
     * @return 返回当前时间的字符串值
     */
    public static String currentDateTime() {
        return DateTimeInstance().format(new Date());
    }

    /**
     * 将指定的时间格式化成出返回
     *
     * @param date
     * @return
     */
    public static String dateTime(Date date) {
        return DateTimeInstance().format(date);
    }

    /**
     * 将指定的字符串解析为时间类型
     *
     * @param datestr
     * @return
     * @throws ParseException
     */
    public static Date dateTime(String datestr) throws ParseException {
        return DateTimeInstance().parse(datestr);
    }

    /**
     * 获取当前的日期
     *
     * @return
     */
    public static String currentDate() {
        return DateInstance().format(new Date());
    }

    /**
     * 将指定的时间格式化成出返回
     *
     * @param date
     * @return
     */
    public  static String date(Date date) {
        return DateInstance().format(date);
    }

    /**
     * 将指定的字符串解析为时间类型
     *
     * @param dateStr
     * @return
     * @throws ParseException
     */
    public  static Date date(String dateStr) throws ParseException {
        return DateInstance().parse(dateStr);
    }

    /**
     * 获取当前的时间
     *
     * @return
     */
    public  static String currentTime() {
        return TimeInstance().format(new Date());
    }

    /**
     * 讲指定的时间格式化成出返回
     *
     * @param date
     * @return
     */
    public  static String time(Date date) {
        return TimeInstance().format(date);
    }

    /**
     * 将指定的字符串解析为时间类型
     *
     * @param dateStr
     * @return
     * @throws ParseException
     */
    public  static Date time(String dateStr) throws ParseException {
        return TimeInstance().parse(dateStr);
    }


    /**
     * 在当前时间的基础上加或减去year年
     *
     * @param year
     * @return
     */
    public  static Date year(int year) {
        Calendar Cal = Calendar.getInstance();
        Cal.setTime(new Date());
        Cal.add(Calendar.YEAR, year);
        return Cal.getTime();
    }

    /**
     * 在指定的时间上加或减去几年
     *
     * @param date
     * @param year
     * @return
     */
    public  static Date year(Date date, int year) {
        Calendar Cal = Calendar.getInstance();
        Cal.setTime(date);
        Cal.add(Calendar.YEAR, year);
        return Cal.getTime();
    }



    /**
     * 在当前时间的基础上加或减去几月
     *
     * @param month
     * @return
     */
    public  static Date month(int month) {
        Calendar Cal = Calendar.getInstance();
        Cal.setTime(new Date());
        Cal.add(Calendar.MONTH, month);
        return Cal.getTime();
    }

    /**
     * 在指定的时间上加或减去几月
     *
     * @param date
     * @param month
     * @return
     */
    public  static Date month(Date date, int month) {
        Calendar Cal = Calendar.getInstance();
        Cal.setTime(date);
        Cal.add(Calendar.MONTH, month);
        return Cal.getTime();
    }

    /**
     * 在当前时间的基础上加或减去几天
     *
     * @param day
     * @return
     */
    public  static Date day(int day) {
        Calendar Cal = Calendar.getInstance();
        Cal.setTime(new Date());
        Cal.add(Calendar.DAY_OF_YEAR, day);
        return Cal.getTime();
    }

    /**
     * 在指定的时间上加或减去几天
     *
     * @param date
     * @param day
     * @return
     */
    public  static Date day(Date date, int day) {
        Calendar Cal = Calendar.getInstance();
        Cal.setTime(date);
        Cal.add(Calendar.DAY_OF_YEAR, day);
        return Cal.getTime();
    }

    /**
     * 在当前时间的基础上加或减去几小时-支持浮点数
     *
     * @param hour
     * @return
     */
    public  static Date hour(float hour) {
        Calendar Cal = Calendar.getInstance();
        Cal.setTime(new Date());
        Cal.add(Calendar.MINUTE, (int) (hour * 60));
        return Cal.getTime();
    }

    /**
     * 在制定的时间上加或减去几小时-支持浮点数
     *
     * @param date
     * @param hour
     * @return
     */
    public  static Date hour(Date date, float hour) {
        Calendar Cal = Calendar.getInstance();
        Cal.setTime(date);
        Cal.add(Calendar.MINUTE, (int) (hour * 60));
        return Cal.getTime();
    }

    /**
     * 在当前时间的基础上加或减去几分钟
     *
     * @param minute
     * @return
     */
    public  static Date minute(int minute) {
        Calendar Cal = Calendar.getInstance();
        Cal.setTime(new Date());
        Cal.add(Calendar.MINUTE, minute);
        return Cal.getTime();
    }

    /**
     * 在制定的时间上加或减去几分钟
     *
     * @param date
     * @param minute
     * @return
     */
    public  static Date minute(Date date, int minute) {
        Calendar Cal = Calendar.getInstance();
        Cal.setTime(date);
        Cal.add(Calendar.MINUTE, minute);
        return Cal.getTime();
    }


    /**
     * 判断字符串是否为日期字符串
     *
     * @param date 日期字符串
     * @return true or false
     */
    public  static boolean isDate(String date) {
        try {
            DateTimeInstance().parse(date);
            return true;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * 时间date1和date2的时间差-单位秒
     *
     * @param date1
     * @param date2
     * @return 秒
     */
    public  static long subtract(Date date1, Date date2) {
        long cha = (date2.getTime() - date1.getTime()) / 1000;
        return cha;
    }

    /**
     * 时间date1和date2的时间差-单位秒
     *
     * @param date1
     * @param date2
     * @return 秒
     */
    public  static long subtract(String date1, String date2) {
        long rs = 0;
        try {
            Date start = DateTimeInstance().parse(date1);
            Date end = DateTimeInstance().parse(date2);
            long cha = (end.getTime() - start.getTime()) / 1000;
            rs = cha;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return rs;
    }


    /**
     * 时间date1和date2的时间差 -单位分钟
     *
     * @param date1
     * @param date2
     * @return 分钟
     */
    public  static int subtractMinute(String date1, String date2) {
        int rs = 0;
        try {
            Date start = DateTimeInstance().parse(date1);
            Date end = DateTimeInstance().parse(date2);
            long cha = (end.getTime() - start.getTime()) / 1000;
            rs = (int) cha / (60);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return rs;
    }

    /**
     * 时间date1和date2的时间差-单位分钟
     *
     * @param date1
     * @param date2
     * @return 分钟
     */
    public  static int subtractMinute(Date date1, Date date2) {
        long cha = date2.getTime() - date1.getTime();
        return (int) cha / (1000 * 60);
    }

    /**
     * 时间date1和date2的时间差-单位小时
     *
     * @param date1
     * @param date2
     * @return 小时
     */
    public  static int subtractHour(Date date1, Date date2) {
        long cha = (date2.getTime() - date1.getTime()) / 1000;
        return (int) cha / (60 * 60);
    }

    /**
     * 时间date1和date2的时间差-单位小时
     *
     * @param date1
     * @param date2
     * @return 小时
     */
    public  static int subtractHour(String date1, String date2) {
        int rs = 0;
        try {
            Date start = DateTimeInstance().parse(date1);
            Date end = DateTimeInstance().parse(date2);
            long cha = (end.getTime() - start.getTime()) / 1000;
            rs = (int) cha / (60 * 60);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return rs;
    }


    /**
     * 时间date1和date2的时间差-单位天
     *
     * @param date1
     * @param date2
     * @return 天
     */
    public  static int subtractDay(String date1, String date2) {
        int rs = 0;
        try {
            Date start = DateTimeInstance().parse(date1);
            Date end = DateTimeInstance().parse(date2);
            long sss = (end.getTime() - start.getTime()) / 1000;
            rs = (int) sss / (60 * 60 * 24);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return rs;
    }

    /**
     * 时间date1和date2的时间差-单位天
     *
     * @param date1
     * @param date2
     * @return 天
     */
    public  static int subtractDay(Date date1, Date date2) {
        long cha = date2.getTime() - date1.getTime();
        return (int) cha / (1000 * 60 * 60 * 24);
    }

    /**
     * 时间date1和date2的时间差-单位月
     *
     * @param date1
     * @param date2
     * @return 月
     */
    public  static int subtractMonth(String date1, String date2) {
        int result;
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        try {
            c1.setTime(DateInstance().parse(date1));
            c2.setTime(DateInstance().parse(date2));
            int year1 = c1.get(Calendar.YEAR);
            int month1 = c1.get(Calendar.MONTH);
            int year2 = c2.get(Calendar.YEAR);
            int month2 = c2.get(Calendar.MONTH);
            if (year1 == year2) {
                result = month2 - month1;
            } else {
                result = 12 * (year2 - year1) + month2 - month1;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            result = -1;
        }
        return result;
    }

    /**
     * 时间date1和date2的时间差-单位月
     *
     * @param date1
     * @param date2
     * @return 月
     */
    public  static int subtractMonth(Date date1, Date date2) {
        int result;
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c1.setTime(date1);
        c2.setTime(date2);
        int year1 = c1.get(Calendar.YEAR);
        int month1 = c1.get(Calendar.MONTH);
        int year2 = c2.get(Calendar.YEAR);
        int month2 = c2.get(Calendar.MONTH);
        if (year1 == year2) {
            result = month2 - month1;
        } else {
            result = 12 * (year2 - year1) + month2 - month1;
        }
        return result;
    }

    /**
     * 时间date1和date2的时间差-单位年
     *
     * @param date1
     * @param date2
     * @return 年
     */
    public  static int subtractYear(String date1, String date2) {
        int result;
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        try {
            c1.setTime(DateInstance().parse(date1));
            c2.setTime(DateInstance().parse(date2));
            int year1 = c1.get(Calendar.YEAR);
            int year2 = c2.get(Calendar.YEAR);
            result = year2 - year1;
        } catch (ParseException e) {
            e.printStackTrace();
            result = -1;
        }
        return result;
    }

    /**
     * 时间date1和date2的时间差-单位年
     *
     * @param date1
     * @param date2
     * @return 年
     */
    public  static int subtractYear(Date date1, Date date2) {
        int result;
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c1.setTime(date1);
        c2.setTime(date2);
        int year1 = c1.get(Calendar.YEAR);
        int year2 = c2.get(Calendar.YEAR);
        result = year2 - year1;
        return result;
    }

    /**
     * 获取俩个时间的查结果用时秒表示
     *
     * @param date1
     * @param date2
     * @return 几小时:几分钟:几秒钟
     * @Summary:此处可以讲计算结果包装成一个结构体返回便于格式化
     */
    public  static String subtractTime(String date1, String date2) {
        String result = "";
        try {
            Date start = DateTimeInstance().parse(date1);
            Date end = DateTimeInstance().parse(date2);
            long sss = (end.getTime() - start.getTime()) / 1000;
            int hh = (int) sss / (60 * 60);
            int mm = (int) (sss - hh * 60 * 60) / (60);
            int ss = (int) (sss - hh * 60 * 60 - mm * 60);
            result = hh + ":" + mm + ":" + ss;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 获取俩个时间的查结果用时秒表示
     *
     * @param date1
     * @param date2
     * @return 几天-几小时:几分钟:几秒钟
     * @Summary:此处可以讲计算结果包装成一个结构体返回便于格式化
     */
    public  static String subtractDate(String date1, String date2) {
        String result = "";
        try {
            Date start = DateTimeInstance().parse(date1);
            Date end = DateTimeInstance().parse(date2);
            long sss = (end.getTime() - start.getTime()) / 1000;
            int dd = (int) sss / (60 * 60 * 24);
            int hh = (int) (sss - dd * 60 * 60 * 24) / (60 * 60);
            int mm = (int) (sss - dd * 60 * 60 * 24 - hh * 60 * 60) / (60);
            int ss = (int) (sss - dd * 60 * 60 * 24 - hh * 60 * 60 - mm * 60);
            result = dd + "-" + hh + ":" + mm + ":" + ss;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 获取俩个时间之前的相隔的天数
     *
     * @param startTime
     * @param endTime
     * @return
     * @throws ParseException
     */
    public  static int subDay(Date startTime, Date endTime) {
        int days = 0;
        Calendar can1 = Calendar.getInstance();
        can1.setTime(startTime);
        Calendar can2 = Calendar.getInstance();
        can2.setTime(endTime);
        int year1 = can1.get(Calendar.YEAR);
        int year2 = can2.get(Calendar.YEAR);

        Calendar can = null;
        if (can1.before(can2)) {
            days -= can1.get(Calendar.DAY_OF_YEAR);
            days += can2.get(Calendar.DAY_OF_YEAR);
            can = can1;
        } else {
            days -= can2.get(Calendar.DAY_OF_YEAR);
            days += can1.get(Calendar.DAY_OF_YEAR);
            can = can2;
        }
        for (int i = 0; i < Math.abs(year2 - year1); i++) {
            days += can.getActualMaximum(Calendar.DAY_OF_YEAR);
            can.add(Calendar.YEAR, 1);
        }

        return days;
    }

    /**
     * 获取俩个时间之前的相隔的天数
     *
     * @param startTime
     * @param endTime
     * @return
     * @throws ParseException
     */
    public  static int subDay(String startTime, String endTime) {
        int days = 0;
        try {
            Date date1 = DateInstance().parse(DateInstance().format(DateTimeInstance().parse(startTime)));
            Date date2 = DateInstance().parse(DateInstance().format(DateTimeInstance().parse(endTime)));
            Calendar can1 = Calendar.getInstance();
            can1.setTime(date1);
            Calendar can2 = Calendar.getInstance();
            can2.setTime(date2);
            int year1 = can1.get(Calendar.YEAR);
            int year2 = can2.get(Calendar.YEAR);

            Calendar can = null;
            if (can1.before(can2)) {
                days -= can1.get(Calendar.DAY_OF_YEAR);
                days += can2.get(Calendar.DAY_OF_YEAR);
                can = can1;
            } else {
                days -= can2.get(Calendar.DAY_OF_YEAR);
                days += can1.get(Calendar.DAY_OF_YEAR);
                can = can2;
            }
            for (int i = 0; i < Math.abs(year2 - year1); i++) {
                days += can.getActualMaximum(Calendar.DAY_OF_YEAR);
                can.add(Calendar.YEAR, 1);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return days;
    }

    /**
     * 返回俩个时间在时间段(例如每天的08:00-18:00)的时长-单位秒
     *
     * @param startDate
     * @param endDate
     * @param timeBurst 只就按该时间段内的08:00-18:00时长
     * @return 计算后的秒数
     * @throws ParseException
     * @summary 格式错误返回0
     */
    public  static long subtimeBurst(String startDate, String endDate, String timeBurst)
            throws ParseException {
        Date start = DateTimeInstance().parse(startDate);
        Date end = DateTimeInstance().parse(endDate);
        return subtimeBurst(start, end, timeBurst);
    }

    /**
     * 返回俩个时间在时间段(例如每天的08:00-18:00)的时长-单位秒
     *
     * @param startDate
     * @param endDate
     * @param timeBurst 只就按该时间段内的08:00-18:00时长
     * @return 计算后的秒数
     * @throws ParseException
     */
    public  static long subtimeBurst(Date startDate, Date endDate, String timeBurst)
            throws ParseException {
        long second = 0;
        Pattern p = Pattern.compile("^\\d{2}:\\d{2}-\\d{2}:\\d{2}");
        Matcher m = p.matcher(timeBurst);
        boolean falg = false;
        if (startDate.after(endDate)) {
            Date temp = startDate;
            startDate = endDate;
            endDate = temp;
            falg = true;
        }
        if (m.matches()) {
            String[] a = timeBurst.split("-");
            int day = subDay(startDate, endDate);
            if (day > 0) {
                long firstMintues = 0;
                long lastMintues = 0;
                long daySecond = 0;
                String strDayStart = DateInstance().format(startDate) + " " + a[0] + ":00";
                String strDayEnd = DateInstance().format(startDate) + " " + a[1] + ":00";
                Date dayStart = DateTimeInstance().parse(strDayStart);
                Date dayEnd = DateTimeInstance().parse(strDayEnd);
                daySecond = subtract(dayStart, dayEnd);
                if ((startDate.after(dayStart) || startDate.equals(dayStart))
                        && startDate.before(dayEnd)) {
                    firstMintues = (dayEnd.getTime() - startDate.getTime()) / 1000;
                } else if (startDate.before(dayStart)) {
                    firstMintues = (dayEnd.getTime() - dayStart.getTime()) / 1000;
                }
                dayStart = DateTimeInstance().parse(DateInstance().format(endDate) + " " + a[0] + ":00");
                dayEnd = DateTimeInstance().parse(DateInstance().format(endDate) + " " + a[1] + ":00");
                if (endDate.after(dayStart) && (endDate.before(dayEnd) || endDate.equals(dayEnd))) {
                    lastMintues = (endDate.getTime() - dayStart.getTime()) / 1000;
                } else if (endDate.after(dayEnd)) {
                    lastMintues = (dayEnd.getTime() - dayStart.getTime()) / 1000;
                }
                //第一天的秒数 + 最好一天的秒数 + 天数*全天的秒数
                second = firstMintues + lastMintues;
                second += (day - 1) * daySecond;
            } else {
                String strDayStart = DateInstance().format(startDate) + " " + a[0] + ":00";
                String strDayEnd = DateInstance().format(startDate) + " " + a[1] + ":00";
                Date dayStart = DateTimeInstance().parse(strDayStart);
                Date dayEnd = DateTimeInstance().parse(strDayEnd);
                if ((startDate.after(dayStart) || startDate.equals(dayStart))
                        && startDate.before(dayEnd) && endDate.after(dayStart)
                        && (endDate.before(dayEnd) || endDate.equals(dayEnd))) {
                    second = (endDate.getTime() - startDate.getTime()) / 1000;
                } else {
                    if (startDate.before(dayStart)) {
                        if (endDate.before(dayEnd)) {
                            second = (endDate.getTime() - dayStart.getTime()) / 1000;
                        } else {
                            second = (dayEnd.getTime() - dayStart.getTime()) / 1000;
                        }
                    }
                    if (startDate.after(dayStart)) {
                        if (endDate.before(dayEnd)) {
                            second = (endDate.getTime() - startDate.getTime()) / 1000;
                        } else {
                            second = (dayEnd.getTime() - startDate.getTime()) / 1000;
                        }
                    }
                }
                if ((startDate.before(dayStart) && endDate.before(dayStart))
                        || startDate.after(dayEnd) && endDate.after(dayEnd)) {
                    second = 0;
                }
            }
        } else {
            second = (endDate.getTime() - startDate.getTime()) / 1000;
        }
        if (falg) {
            second = Long.parseLong("-" + second);
        }
        return second;
    }

    /**
     * 时间Date在时间段(例如每天的08:00-18:00)上增加或减去second秒
     *
     * @param date
     * @param second
     * @param timeBurst
     * @return 计算后的时间
     * @Suumary 指定的格式错误后返回原数据
     */
    public  static Date calculate(String date, int second, String timeBurst) {
        Date start = null;
        try {
            start = DateTimeInstance().parse(date);
            return calculate(start, second, timeBurst);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Date();
    }

    /**
     * 时间Date在时间段(例如每天的08:00-18:00)上增加或减去second秒
     *
     * @param date
     * @param second
     * @param timeBurst
     * @return 计算后的时间
     * @Suumary 指定的格式错误后返回原数据
     */
    public  static Date calculate(Date date, int second, String timeBurst) {
        Pattern p = Pattern.compile("^\\d{2}:\\d{2}-\\d{2}:\\d{2}");
        Matcher m = p.matcher(timeBurst);
        Calendar cal = Calendar.getInstance();
        if (m.matches()) {
            String[] a = timeBurst.split("-");
            try {
                Date dayStart = DateTimeInstance().parse(DateInstance().format(date) + " " + a[0] + ":00");
                Date dayEnd = DateTimeInstance().parse(DateInstance().format(date) + " " + a[1] + ":00");
                int DaySecond = (int) subtract(dayStart, dayEnd);
                int toDaySecond = (int) subtract(dayStart, dayEnd);
                if (second >= 0) {
                    if ((date.after(dayStart) || date.equals(dayStart))
                            && (date.before(dayEnd) || date.equals(dayEnd))) {
                        cal.setTime(date);
                        toDaySecond = (int) subtract(date, dayEnd);
                    }
                    if (date.before(dayStart)) {
                        cal.setTime(dayStart);
                        toDaySecond = (int) subtract(dayStart, dayEnd);
                    }
                    if (date.after(dayEnd)) {
                        cal.setTime(day(dayStart, 1));
                        toDaySecond = 0;
                    }

                    if (second > toDaySecond) {
                        int day = (second - toDaySecond) / DaySecond;
                        int remainder = (second - toDaySecond) % DaySecond;
                        cal.setTime(day(dayStart, 1));
                        cal.add(Calendar.DAY_OF_YEAR, day);
                        cal.add(Calendar.SECOND, remainder);
                    } else {
                        cal.add(Calendar.SECOND, second);
                    }

                } else {
                    if ((date.after(dayStart) || date.equals(dayStart))
                            && (date.before(dayEnd) || date.equals(dayEnd))) {
                        cal.setTime(date);
                        toDaySecond = (int) subtract(date, dayStart);
                    }
                    if (date.before(dayStart)) {
                        cal.setTime(day(dayEnd, -1));
                        toDaySecond = 0;
                    }
                    if (date.after(dayEnd)) {
                        cal.setTime(dayEnd);
                        toDaySecond = (int) subtract(dayStart, dayEnd);
                    }
                    if (Math.abs(second) > Math.abs(toDaySecond)) {
                        int day = (Math.abs(second) - Math.abs(toDaySecond)) / DaySecond;
                        int remainder = (Math.abs(second) - Math.abs(toDaySecond)) % DaySecond;
                        cal.setTime(day(dayEnd, -1));
                        cal.add(Calendar.DAY_OF_YEAR, Integer.valueOf("-" + day));
                        cal.add(Calendar.SECOND, Integer.valueOf("-" + remainder));
                    } else {
                        cal.add(Calendar.SECOND, second);
                    }
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            cal.setTime(date);
        }
        return cal.getTime();
    }

    /**
     * 判断是否在某个时间段内
     * @param startTime
     * @param endTime
     * @param date
     * @return
     * @throws ParseException
     */
    public static boolean between(String startTime,String endTime,Date date)
            throws ParseException {
        return between(dateTime(startTime),dateTime(endTime),date);
    }

    /**
     * 判断在某个时间内
     * @param startTime
     * @param endTime
     * @param date
     * @return
     */
    public static boolean between(Date startTime,Date endTime,Date date){
        return date.after(startTime) && date.before(endTime);
    }
    
  public static int compare_date(String date1, String date2) {
        try {
            Date dt1 = DateTimeInstance().parse(date1);
            Date dt2 = DateTimeInstance().parse(date2);
            if (dt1.getTime() > dt2.getTime()) {
                return 1;
            } else if (dt1.getTime() < dt2.getTime()) {
                return -1;
            } else {
                return 0;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;
    }
  
  /**
 * @Author:YuanYan
 * @CreateAt:2017年11月3日下午2:58:29
 * @Params:日期格式"YYYY-MM-DD"
 * @Return:
 * @Description:
 */
public static int compareDate(String date1, String date2) {
      try {
    	  Integer d1 = Integer.valueOf(date1.replace("-",""));
    	  Integer d2 = Integer.valueOf(date2.replace("-",""));
          if (d1 > d2) {
              return 1;
          } else if (d1 < d2) {
              return -1;
          } else {
              return 0;
          }
      } catch (Exception exception) {
          exception.printStackTrace();
      }
      return 0;
  }


    /**
     * 时间格式化
     * @param date 需要格式化的时间
     * @param pattern 日期转换格试，为null时按"yyyy-MM-dd HH:mm:ss"处理
     * @return date为null或空,返回空字符
     */
    public static String formatDate(Date date,String pattern) {
        if(pattern == null || "".equals(pattern)){
            pattern = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        if(date != null && !"".equals(date)){
            return format.format(date);
        }else{
            return "";
        }
    }

    
    //================ 2019-01-09 新增
	public static  String YYYY_MM_DD = "yyyy-MM-dd";
	public static  SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyyMMdd");
	public static  SimpleDateFormat SDF_YYYY_MM_DD = new SimpleDateFormat(YYYY_MM_DD);
	public static  SimpleDateFormat SDF_YYYY_MM = new SimpleDateFormat("yyyy-MM");
	
	/**
     * 返回指定的时间字符串
     */
    public static String commontime = (new SimpleDateFormat("yyyyMMddHHmmss")).format(new Date());

	/**
	 * 返回某天的开始时间   00:00:00。  当天的开始时间  getDayStart(0)
	* @param num 增加的天数
	 */
	public static Date getDayStart(int num) {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DAY_OF_YEAR, num);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		return c.getTime();
	}
	
	/**
	 * 返回某天的结束时间   day+1 00:00:00。  当天的开始时间  getDayEnd(0)
	* @param num 增加的天数
	 */
	public static Date getDayEnd(int num) {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DAY_OF_YEAR, num + 1);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		return c.getTime();
	}
	
	/**
	 * 返回当天 0点
	 */
	public static Date getTodayDate() {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		return c.getTime();
	}
	
	/**
	 * 返回昨天 0点
	 */
	public static Date getYesterdayDate() {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DAY_OF_YEAR, -1);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		return c.getTime();
	}
	
	/**
	 * 返回明天 0点
	 */
	public static Date getTomorrowDate() {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DAY_OF_YEAR, 1);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		return c.getTime();
	}
	
	/**
	 * 返回本月第一天
	 * @param num 增加的月数
	 */
	public static Date getMonthFirst(int num) {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MONTH, num);
		
		c.set(Calendar.DAY_OF_MONTH, 1);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		return c.getTime();
	}
	
	
	/**
	 * 返回本月最后一天
	 * @param num 增加的月数
	 */
	public static Date getMonthEnd(int num) {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MONTH, num+1);
		c.set(Calendar.DAY_OF_MONTH, 1);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		return c.getTime();
	}

    /**
     * 返回本月最后一天
     * @param num 增加的月数
     */
    public static Date getMonthEndDay(int num) {
        Calendar cal = Calendar.getInstance();
        cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.add(Calendar.MONTH, 1);
        cal.add(Calendar.DATE, num);
        return cal.getTime();
    }
	
	/**
	 * 返回本周第一天
	* @param num 增加的周数
	 */
	public static Date getWeekFirst(int num) {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.WEEK_OF_YEAR, num);
		
		//美国人星期一是第二天，星期天是第1天
		c.set(Calendar.DAY_OF_WEEK, 2);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		return c.getTime();
	}
	
	/**
	 * 返回本周最后一天
	* @param num 增加的周数
	 */
	public static Date getWeekEnd(int num) {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.WEEK_OF_YEAR, num + 1);
		
		//美国人星期一是第二天，星期天是第1天
		c.set(Calendar.DAY_OF_WEEK, 2);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		return c.getTime();
	}
	
	/**
	 * 返回某年第一天，
	 * @param num 0 表示今年， 1 明年，-1 去年
	 */
	public static Date getYearFirst(int num) {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.YEAR, num);
		c.set(Calendar.DAY_OF_YEAR, 1);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		return c.getTime();
	}
	
	
	/**
	 * 返回本月最后一天
	 */
	public static Date getYearEnd(int num) {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.YEAR, num + 1);
		c.set(Calendar.DAY_OF_YEAR, 1);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		return c.getTime();
	}
	
	
	/**
	 * 返回当天 0点
	 */
	public static Date getToday() {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		return c.getTime();
	}
	
	//获取当前给定时间处于几号
	public static int getDay(Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.DAY_OF_MONTH);
	}
	
	public static String getYesterday() {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DAY_OF_YEAR, -1);
		return yyyyMMdd.format(c.getTime());
	}
	
	//判断两个日期是否是相同年
	public static boolean isSameYear(Date date1, Date date2){
		Calendar calendar1 = Calendar.getInstance();
		calendar1.setTime(date1);
		
		Calendar calendar2 = Calendar.getInstance();
		calendar2.setTime(date2);
		
		return calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR);
	}
	
	//判断两个日期是否是相同的月份
	public static boolean isSameMonth(Date date1, Date date2){
		Calendar calendar1 = Calendar.getInstance();
		calendar1.setTime(date1);
		
		Calendar calendar2 = Calendar.getInstance();
		calendar2.setTime(date2);
		
		return calendar1.get(Calendar.MONTH) == calendar2.get(Calendar.MONTH);
	}
	
	//判断两个日期是否是相同的一天
	public static boolean isSameDay(Date date1, Date date2){
		Calendar calendar1 = Calendar.getInstance();
		calendar1.setTime(date1);
		
		Calendar calendar2 = Calendar.getInstance();
		calendar2.setTime(date2);
		
		return calendar1.get(Calendar.DAY_OF_MONTH) == calendar2.get(Calendar.DAY_OF_MONTH);
	}
	
	//获取给守日期当前是第几天
	public static int getDayByGivenTime(Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.DAY_OF_MONTH);
	}
	
	//获取上一个月中与给定时间相同的时间
	public static Date getLastMonthSameStartTimeByGivenDate(Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, -1);
		return getStartTimeByDate(calendar.getTime());
	}
	
	public static Date getLastMonthSameEndTimeByGivenDate(Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, -1);
		return getEndTimeByDate(calendar.getTime());
	}
	
	//获取给定日期当月的第一天时间
	public static Date getBeforeFirstMonthDate(Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, -1);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		return getStartTimeByDate(calendar.getTime());
	}
	//获取某个月有多少天
	public static int getMaxDaysByDate(Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
	}
	
	//根据日期获取上一个月的第一天
	public static Date getLastMonthFirstDateByGivenDate(Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, -1);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		return getStartTimeByDate(calendar.getTime());
	}
	
	//根据给定的日期获取最后一天
	public static Date getLastMonthLastDateByGivenDate(Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, -1);
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		return getEndTimeByDate(calendar.getTime());
	}
	
	//根据日期获取当前月的第一天
	public static Date getCurrentMonthFirstDateByGivenDate(Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		return getStartTimeByDate(calendar.getTime());
	}
	
	//根据给定的日期获取最后一天
	public static Date getCurrentMonthLastDateByGivenDate(Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		return getEndTimeByDate(calendar.getTime());
	}
	
	/**
	 * 获取当前月的第一天
	 * @return
	 */
	public static Date getCurrentMonthFirstDate(){
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		return getStartTimeByDate(calendar.getTime());
	}
	
	/**
	 * 获取当前月的最后一天
	 * @return
	 */
	public static Date getCurrentMonthLastDate(){
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		return getEndTimeByDate(calendar.getTime());
	}
	
	/**
	 * 获取上一个月的第一天
	 * @return
	 */
	public static Date getBeforeFirstMonthDate(){
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, -1);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		return getStartTimeByDate(calendar.getTime());
	}
	
	//获取上一个月的最后一天
	public static Date getBeforeLastMonthDate(){
		Calendar calendar = Calendar.getInstance();
		int month = calendar.get(Calendar.MONTH);
		calendar.set(Calendar.MONTH, month-1);
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		return getEndTimeByDate(calendar.getTime());
	}
	
	//获取长整形时间字符串
	public static String getNowLongStr(){
		String nowStr = String.valueOf(System.currentTimeMillis());
		return nowStr.substring(0,nowStr.length()-3);
	}
    public static String getNowLongStr(Long time){
    	String nowStr = String.valueOf(time);
    	return nowStr.substring(0,nowStr.length()-3);
    }
	//将数据库长整形转换为时间字符串
	public static String coverLongStr2NoNowStr(String timeLongStr){
		String str;
		if(!"".equals(timeLongStr) || null != timeLongStr){
		  timeLongStr = timeLongStr+"000";
		  str = sdf.format(new Date(Long.valueOf(timeLongStr)));
		}else{
		  str = "";
		}
		return str;
	}
	// 获取当前登录时间
	public static String getDate() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String date = df.format(new Date());
		return date;
	}
    //序曲当前月份
	public static String getMonthDate() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM");
		String date = df.format(new Date());
		return date;
	}
	// 获取当月的第一天的日期
	public static String getFirstDayDate() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar cal_1 = Calendar.getInstance();// 获取当前日期
		cal_1.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天
		return format.format(cal_1.getTime());
	}

	// 获取当前的日期
	public static String getNowDate() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar cal_1 = Calendar.getInstance();// 获取当前日期
		return format.format(cal_1.getTime());
	}
	
	
	/**
	 * 取得一个日期当天的开始时间
	 * @param Date对象
	 * @return Date对象
	 */
	public static String getStartTimeStringByDate(Date date){
		Calendar c = Calendar.getInstance();
		c.setTime(date);  
        c.set(Calendar.HOUR_OF_DAY, 0);  
        c.set(Calendar.MINUTE, 0);  
        c.set(Calendar.SECOND, 0);  
        c.set(Calendar.MILLISECOND, 0); 
        Date d = c.getTime();
        return formatDate(d, "yyyy-MM-dd HH:mm:ss");
		
	}
	
	/**
	 * 取得一个日期当天的结束时间
	 * @param Date对象
	 * @return Date对象
	 */
	public static String getEndTimeStringByDate(Date date){
		Calendar c = Calendar.getInstance();
		c.setTime(date);  
        c.set(Calendar.HOUR_OF_DAY, 23);  
        c.set(Calendar.MINUTE, 59);  
        c.set(Calendar.SECOND, 59);  
        c.set(Calendar.MILLISECOND, 997); 
        Date d = c.getTime();
        return formatDate(d, "yyyy-MM-dd HH:mm:ss");
	}
	
	
	/**
	 * 取得一个日期当天的开始时间
	 * @param Date对象
	 * @return Date对象
	 */
	public static Date getStartTimeByDate(Date date){
		Calendar c = Calendar.getInstance();
		c.setTime(date);  
        c.set(Calendar.HOUR_OF_DAY, 0);  
        c.set(Calendar.MINUTE, 0);  
        c.set(Calendar.SECOND, 0);  
        c.set(Calendar.MILLISECOND, 0); 
        Date d = c.getTime();
        return d;
		
	}
	
	/**
	 * 取得一个日期当天的结束时间
	 * @param Date对象
	 * @return Date对象
	 */
	public static Date getEndTimeByDate(Date date){
		Calendar c = Calendar.getInstance();
		c.setTime(date);  
        c.set(Calendar.HOUR_OF_DAY, 23);  
        c.set(Calendar.MINUTE, 59);  
        c.set(Calendar.SECOND, 59);  
        c.set(Calendar.MILLISECOND, 997); 
        Date d = c.getTime();
        return d;
	}
	
	
	public static Date getStartTimeByDate(String day) throws ParseException{
		Date date = parseTime(day, "yyyy-MM-dd");
		return getStartTimeByDate(date);
	}
	
	/**
	 * 取得一个日期当天的结束时间
	 * @param Date对象
	 * @return Date对象
	 */
	public static Date getEndTimeByDate(String day) throws ParseException{
		Date date = parseTime(day, "yyyy-MM-dd");
		return getEndTimeByDate(date);
	}
	
	/**
	 * 获取当前时间指定分前/后的时间
	 * @param data 当前时间Date对象
	 * @param num 正数据向后，负数为向前
	 * @return Date对象
	 */
	public static Date getAfterDataByMinute(Date data,int num){
		Calendar c = Calendar.getInstance();
		c.setTime(data);
		c.add(Calendar.MINUTE, num);
		Date date = c.getTime();
		return date;
	}
	
	/**
	 * 取得当前时间指定日前/后的时间
	 * @param num 正数据向后，负数为向前
	 * @param Date对象
	 * @return Date对象
	 */
	public static String getDateByOffset(Date date, int num){
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DAY_OF_MONTH, num);
		return formatDate(c.getTime(), "yyyy-MM-dd HH:mm:ss");
	}
	
	public static Date getDateByOffset(int num,Date date){
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DAY_OF_MONTH, num);
		return c.getTime();
	}
	
	/**
	 * 取得当前时间指定月前/后的时间
	 * @param date 日期对象
	 * @param num 月个数 正数据为月后，负数为月前
	 * @return
	 */
	public static String getAfterByMonth(Date date,int num){
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.MONTH, num);
		return formatDate(c.getTime(), "yyyy-MM-dd HH:mm:ss");
	}
	
	/**
	 * 
	 * @param date
	 * @param afterMonth
	 * @param day
	 * @return
	 */
	public static Date getAfterDayByMonth(Date date, int afterMonth, int day){
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.MONTH, afterMonth);
		c.set(Calendar.DAY_OF_MONTH, day);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.MILLISECOND, 0);
		
		return c.getTime();
	}
	
	/**
	 * 获取当前日期后延 afterMonth 个月的 day 天。 例如：thisDay = 2018-12-11 getAfterDayByMonth(2,3) = 2019-02-03
	 * @param afterMonth
	 * @param day
	 * @return
	 */
	public static Date getAfterDayByMonth(int afterMonth, int day){
		return getAfterDayByMonth(new Date(), afterMonth, day);
	}
	
	/**
	 * 将字符窜转换成date
	 * @param time 字符窜日期
	 * @param pattern 处理字符窜的格式，为null时，默认按照0000-00-00或0000-00-00 00:00:00处理
	 * @return time为null或空，pattern格式错误，将返回null
	 * @throws ParseException
	 */
	public static Date parseTime(String time,String pattern) throws ParseException {
		SimpleDateFormat format=null;
		//如果日期为空，将不做处理
		if(time == null || "".equals(time)){
			return null;
		}
		//如果格式为null,则默认格式为0000-00-00或0000-00-00 00:00:00
		if(pattern == null || "".equals(pattern)){
			if (time!=null&&time.length()>10) {
				format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				if (time.equals("0000-00-00 00:00:00"))
					return null;
				else
					return format.parse(time);
			}else {
				format = new SimpleDateFormat("yyyy-MM-dd");
				if (time.equals("0000-00-00"))
					return null;
				else
					return format.parse(time);
			}
		}else{//按指定格式转换
			format = new SimpleDateFormat(pattern);
			return format.parse(time);
		}
	}
	
	/**
	 * 获取当前时间 精确到毫秒
	 * @return
	 */
	public static String printTimeStamp() {
		Calendar ca = Calendar.getInstance();
		ca.setTimeInMillis(System.currentTimeMillis());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS ",
				Locale.CHINA);
		// 显示当前时间 精确到毫秒
		return sdf.format(ca.getTime());
	}
	/**
	 * 判断是否过期
	 */
	public static Boolean IfInSession(Date time,Date now,long sessionTime){
		long between=(now.getTime()-time.getTime())/1000;//除以1000是为了转换成秒
		if(between <= sessionTime){
			return true;
		}else{
			return false;
		}
			
	}
	
	/** 
	   * 得到几天前的时间 
	   * @param d 
	   * @param day 
	   * @return 
	   */  
	  public static Date getDateBefore(Date d,int day){  
	   Calendar now =Calendar.getInstance();  
	   now.setTime(d);  
	   now.set(Calendar.DATE,now.get(Calendar.DATE)-day);  
	   return now.getTime();  
	  }  
	    
	  /** 
	   * 得到几天后的时间 
	   * @param d 
	   * @param day 
	   * @return 
	   */  
	  public static Date getDateAfter(Date d,int day){  
	   Calendar now =Calendar.getInstance();  
	   now.setTime(d);  
	   now.set(Calendar.DATE,now.get(Calendar.DATE)+day);  
	   return now.getTime();  
	  } 
	
	/**
	 * 获取当前毫秒数
	 * @return
	 */
	public static Long getMilliSeconds(){
		return new Date().getTime();
	}
//
//	public static void main(String[] args) throws ParseException {
//		System.out.println("getTodayDate:"+getTodayDate());
//		System.out.println("getYesterdayDate:"+getYesterdayDate());
//		System.out.println("getTomorrowDate:"+getTomorrowDate());
//		System.out.println("thisMonth:"+getMonthFirst(0) + "today:"+ getMonthEnd(0));
//		System.out.println("nextMonth:"+getMonthFirst(1) + "today:"+ getMonthEnd(1));
//
//		System.out.println("thisWeek:"+getWeekFirst(0) + "today:"+ getWeekEnd(0));
//		System.out.println("lastWeek:"+getWeekFirst(-1) + "today:"+ getWeekEnd(-1));
//		System.out.println("nextWeek:"+getWeekFirst(1) + "today:"+ getWeekEnd(1));
//
//		System.out.println("thisYear:"+getYearFirst(0) + "today:"+ getYearEnd(0));
//		System.out.println("last2Year:"+getYearFirst(-2) + "today:"+ getYearEnd(-2));
//
//	}
	//获取给定日期的那一个月的最早一天,最早的时间00:00:00
	public static Date getMonthFirstDateByGivenDate(Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		return getStartTimeByDate(calendar.getTime());
	}
	//根据给定的日期，获取那一个月份的最后一天,最后的时间23:59:59
	public static Date getMonthLastDateByGivenDate(Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		return getEndTimeByDate(calendar.getTime());
	}
	
	/**
	 * 获取两个日期之间的日期集合字符串
	 * @param startDate
	 * @param endDate
	 * @param partern
	 * @return
	 * @throws ParseException
	 */
	public static ArrayList<String> getDateStr(Date startDate, Date endDate,String partern) throws ParseException{
		  
		 Calendar dd = Calendar.getInstance();//定义日期实例
		 dd.setTime(startDate);//设置日期起始时间
		 ArrayList<String> dates = new ArrayList<String>();
		 while(dd.getTime().compareTo(endDate)<=0){//判断是否到结束日期
			 SimpleDateFormat sdf = new SimpleDateFormat(partern);
			 String str = sdf.format(dd.getTime());
			 dates.add(str);
			 dd.add(Calendar.DAY_OF_YEAR, 1);//进行当前日期月份加1
		 }
		 	return dates;
		}
	
	/**
	 * 获取两个日期之间的日期
	 * @param start 开始日期
	 * @param end 结束日期
	 * @return 日期集合
	 * @throws ParseException 
	 */
	public static  List<Date> getDates(Date startDate, Date endDate) throws ParseException {
	    List<Date> result = new ArrayList<Date>();
	    Calendar tempStart = Calendar.getInstance();
	    tempStart.setTime(startDate);
	    
	    Calendar tempEnd = Calendar.getInstance();
	    tempEnd.setTime(endDate);
	    while (tempStart.compareTo(tempEnd)<1) {
	        result.add(tempStart.getTime());
	        tempStart.add(Calendar.DAY_OF_YEAR, 1);
	    }
	    return result;
	}
	
	
	/**
	 * 判断一个起始时间与过期间隔是否大于当前时间，如果大于当前时间，则过期
	 * @param startDate
	 * @param currentTime
	 * @param expireTime
	 * @return
	 */
	public static boolean isExpire(Date startDate,Date currentTime,long expireTime){
		Calendar c = Calendar.getInstance();
		c.setTime(startDate);
		c.add(Calendar.MILLISECOND, (int)expireTime);
		
		return c.getTimeInMillis() - currentTime.getTime() > 0 ? false : true;
	}

}
