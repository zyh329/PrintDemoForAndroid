package com.eyecool.utils;

import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created date: 2017/6/1
 * Author:  Leslie
 */

public class TimeUtil {
    private static SimpleDateFormat simpleDateFormat;

    public static String getDayOfYear() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        String monthStr = month + "";
        if (month < 10) {
            monthStr = "0" + month;
        }
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String dayStr = day + "";
        if (day < 10) {
            dayStr = "0" + day;
        }


        return year + "/" + monthStr + "/" + dayStr;
    }

    /**
     * 获取时间，是一天当中的某一时间
     *
     * @return
     */
    public static String getTimeOfDay() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        String minuteStr;
        String hourStr;
        String secondStr;
        if (minute < 10) {
            minuteStr = "0" + minute;
        } else {
            minuteStr = "" + minute;
        }
        if (hour < 10) {
            hourStr = "0" + hour;
        } else {
            hourStr = "" + hour;
        }
        if (second < 10) {
            secondStr = "0" + second;
        } else {
            secondStr = "" + second;
        }
        return hourStr + ":" + minuteStr + ":" + secondStr;
    }

    public static String getTimeOfDay2() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        String minuteStr;
        String hourStr;
        String secondStr;
        if (minute < 10) {
            minuteStr = "0" + minute;
        } else {
            minuteStr = "" + minute;
        }
        if (hour < 10) {
            hourStr = "0" + hour;
        } else {
            hourStr = "" + hour;
        }
        if (second < 10) {
            secondStr = "0" + second;
        } else {
            secondStr = "" + second;
        }
        return hourStr + "-" + minuteStr + "-" + secondStr;
    }

    /**
     * 刷新钟表时间
     *
     * @param textView
     */
    public static void refreshClockTimeOnUI(TextView textView) {
        String hourClockStr;
        String minuteClockStr;
        String secondClockStr;
        String millSecondClockStr;

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        if (hour < 10) {
            hourClockStr = "0" + hour;
        } else {
            hourClockStr = "" + hour;
        }
        if (minute < 10) {
            minuteClockStr = "0" + minute;
        } else {
            minuteClockStr = "" + minute;
        }
        if (second < 10) {
            secondClockStr = "0" + second;
        } else {
            secondClockStr = "" + second;
        }

        textView.setText(hourClockStr + ":" + minuteClockStr + ":" + secondClockStr);
    }

    public static String stampToDate(String timeStamp) {
        String res;
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (simpleDateFormat == null) {

            simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        }
        Long lt = new Long(timeStamp);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }

    public static long timeStr2Stamp(String timeStr) {
        //注意这个时间格式和存到本地文件的时间格式一样，日期和时间有 3 个空格！！,在 NewCheckFaceActivity 947 行，也是拼接了 3 个空格。
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd   HH:mm:ss");
        Date date = null;
        long timeStemp = 0;
        try {
            date = simpleDateFormat.parse(timeStr);
            timeStemp = date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timeStemp;
    }

}
