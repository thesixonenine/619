package io.github.thesixonenine.common.utils;

import java.time.format.DateTimeFormatter;

/**
 * @author Simple
 * @version 1.0
 * @date 2020/06/26 0:58
 * @since 1.0
 */
public class DateUtils extends org.apache.commons.lang3.time.DateUtils {
    public static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final DateTimeFormatter TIMESTAMP_MILLISECOND_FORMATER = DateTimeFormatter.ofPattern(DateUtils.DATETIME_FORMAT);
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final DateTimeFormatter DATE_FORMAT_FORMATER = DateTimeFormatter.ofPattern(DateUtils.DATE_FORMAT);
    public static final String DATE_SHORT_FORMAT = "yyyyMMdd";
    public static final DateTimeFormatter DATE_SHORT_FORMAT_FORMATER = DateTimeFormatter.ofPattern(DateUtils.DATE_SHORT_FORMAT);
    public static final String TIMESTAMP_FORMAT = "yyyyMMddHHmmss";
    public static final DateTimeFormatter TIMESTAMP_FORMAT_FORMATER = DateTimeFormatter.ofPattern(DateUtils.TIMESTAMP_FORMAT);
    public static final String TIMESTAMP_MILLISECOND_FORMAT = "yyyyMMddHHmmssSSS";
    public static final String TIME_FORMAT = "HH:mm:ss";
}
