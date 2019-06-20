package cn.bfreeman.common.util;


import cn.bfreeman.common.exception.FatalException;
import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.PeriodType;

import java.util.Date;
import java.util.Objects;

/**
 * PackageName: com.cgg.util ClassName: 日期处理方法类 Description: TODO add
 * Description. <br/>
 * CreateDate: 2014-11-17 下午11:27:27 by _13[526028311@qq.com]<br/>
 * UpdateDate: 2014-11-17 下午11:27:27 by <br/>
 *
 * @author _13[526028311@qq.com]
 * @since JDK 1.6
 */
public class DateUtil {

    /**
     * 比较两个时间相差的分钟数
     *
     * @param time1
     * @param time2
     * @return
     */
    public static long minutesBetween(long time1, long time2) {
        long min = 0;
        long diff;
        if (time1 < time2) {
            diff = time2 - time1;
        } else {
            diff = time1 - time2;
        }
        min = diff / (60 * 1000);
        return min;
    }
    /**
     * 根据日期获取年龄
     *
     * @param date
     * @return long
     */
    public static long getBirthday(Date date) {
        return new Period(new DateTime(date), DateTime.now(), PeriodType.years()).getYears();
    }


    /**
     * 获取过期日期
     *
     * @param life 有效时长（秒）
     * @return
     */
    public static Date getExpireDate(long life) {
        long expire = System.currentTimeMillis();
        expire += life * 1000;
        return new Date(expire);
    }

    public static Date getDateByTime(long time) {
        return new Date(time);
    }

    public static Date getDateByTime(int time) {
        return new Date(time * 1000L);
    }

    private static final String[] DATE_PATTERN_ARRAY = new String[]{
            "yyyy-MM-dd HH:mm:ss.SSS",
            "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd", "yyyy-MM-dd HH:mm", "yyyy-MM-dd HH", "yyyyMMdd",
            "yyyy-MM-dd'T'HH:mm:ss.SSS"

    };

    public static Date parseDateTimeString(String dateStr) {
        try {
            return DateUtils.parseDate(dateStr, DATE_PATTERN_ARRAY);
        } catch (Exception e) {
            throw new FatalException(e.getMessage(), e);
        }
    }

    public static String datetimeString(Date date) {
        if (Objects.isNull(date)) {
            return StringUtils.EMPTY;
        }
        return DateFormatUtils.format(date, "yyyy-MM-dd HH:mm:ss");
    }


    /**
     * 每个月星座的分隔时间. <= 分隔时间, 对应每个月的前一个星座, 大于分隔时间, 对应后一个星座
     */
    private final static int[] DAY_ARR = new int[]{20, 18, 20, 19, 21, 21, 22,
            22, 22, 23, 21, 20};
    /**
     * 星座枚举: 1,白羊座、2,金牛座、3,双子座、4,巨蟹座、5,狮子座、6,处女座、7,天秤座、8,天蝎座、9,射手座、10,摩羯座、11,水瓶座、12,双鱼座
     * 数据参考: http://www.starsignhk.com/horoscope/
     * 星座计算时每个月的前一个星座. 依次表示从1月到12月
     */
    private final static int[] CONSTELLATION = new int[]{10,
            11, 12, 1, 2, 3, 4, 5, 6, 7, 8, 9};

    /**
     * Java通过生日计算星座
     *
     * @param month
     * @param day
     * @return
     */
    public static int getConstellation(int month, int day) {
        Preconditions.checkArgument(month > 0 && month < 13);
        Preconditions.checkArgument(day > 0 && month <= 31);
        int monthDayIndex = month - 1;
        int monthIndex = monthDayIndex;
        if (day > DAY_ARR[monthDayIndex]) {
            monthIndex = monthIndex + 1;
        }
        // 如果小于 1, 把月份降低
        if (monthIndex == 12) {
            monthIndex = 1;
        }

        return CONSTELLATION[monthIndex];
    }

    /**
     * Java通过生日计算星座
     *
     * @return
     */
    public static int getConstellation(Date date) {
        DateTime dateTime = new DateTime(date);
        return getConstellation(dateTime.getMonthOfYear(), dateTime.getDayOfMonth());
    }

    /**
     * 获取DateTime今日零点
     *
     * @return
     */
    public static Date getStartOfToday(DateTime dateTime) {
        return dateTime.withMillisOfDay(0).toDate();
    }

    /**
     * 获取DateTime昨日零点
     *
     * @return
     */
    public static Date getStartOfYesterday(DateTime dateTime) {
        return dateTime.plusDays(-1).withMillisOfDay(0).toDate();
    }

    /**
     * 获取 dateTime 对应月份的最后一天
     *
     * @param dateTime
     * @return
     */
    public static DateTime lastDayOfMonth(DateTime dateTime) {
        return dateTime.plusMonths(1).withDayOfMonth(1).minusDays(1).withTimeAtStartOfDay();
    }

    /**
     * 获取 dateTime 对应星期的最后一天
     *
     * @param dateTime
     * @return
     */
    public static DateTime lastDayOfWeek(DateTime dateTime) {
        return dateTime.plusWeeks(1).withDayOfWeek(1).minusDays(1).withTimeAtStartOfDay();
    }

    /**
     * 获取当日的字符串类型yyyy-MM-dd
     * @return
     */
    public static String getDayStr() {
        return DateTime.now().toString("yyyy-MM-dd");
    }

    /**
     * 获取本周第一天的字符串类型yyyy-MM-dd
     * @return
     */
    public static String getWeekFirstDayStr() {
        return DateTime.now().withDayOfWeek(1).toString("yyyy-MM-dd");
    }

    /**
     * 获取本月第一天的字符串类型yyyy-MM-dd
     * @return
     */
    public static String getMonthFirstDayStr() {
        return DateTime.now().withDayOfMonth(1).toString("yyyy-MM-dd");
    }

    /**
     * 获取上月最后一天的字符串类型yyyy-MM-dd
     * @return
     */
    public static String getUpperMonthLastDayStr() {
        return DateTime.now().withDayOfMonth(1).minusDays(1).toString("yyyy-MM-dd");
    }
}
