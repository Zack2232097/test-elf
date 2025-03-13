package com.sky.logtool;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {
    // 提取时间戳并转换为毫秒
    public static Long extractAndConvertTimeToMilliseconds(String line) {
        String pattern = "\\[(\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}\\.\\d{3})\\]";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(line);
        if (m.find()) {
            String timeStr = m.group(1);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            try {
                Date dt = sdf.parse(timeStr);
                return dt.getTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    // 提取时间戳
    public static String extractTimestamp(String line) {
        String pattern = "\\[(\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}\\.\\d{3})\\]";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(line);
        if (m.find()) {
            String timeStr = m.group(1);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            try {
                Date dt = sdf.parse(timeStr);
                SimpleDateFormat outputFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
                return outputFormat.format(dt);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}