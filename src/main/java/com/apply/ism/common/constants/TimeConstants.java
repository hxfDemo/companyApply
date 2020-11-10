package com.apply.ism.common.constants;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/**
 * @author Leon
 * @date 2019-09-29
 */
public interface TimeConstants {

    ZoneOffset BEIJING = ZoneOffset.ofHours(8);

    ZoneId BJ_ZONE = ZoneId.of("Asia/Shanghai");

    String PATTERN_YYYY_MM_DD = "yyyy-MM-dd";

    DateTimeFormatter FORMATTER_YYYY_MM_DD = DateTimeFormatter.ofPattern(PATTERN_YYYY_MM_DD);

    String PATTERN_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    DateTimeFormatter FORMATTER_YYYY_MM_DD_HH_MM_SS = DateTimeFormatter.ofPattern(PATTERN_YYYY_MM_DD_HH_MM_SS);

    String PATTERN_YYYY_MM_DD_HH_MM_SS_SSS = "yyyy-MM-dd HH:mm:ss.SSS";

    DateTimeFormatter FORMATTER_YYYY_MM_DD_HH_MM_SS_SSS = DateTimeFormatter.ofPattern(PATTERN_YYYY_MM_DD_HH_MM_SS_SSS);

    String PATTERN_YYYYMMDDHHMMSS_SSS = "yyyyMMddHHmmssSSS";

    DateTimeFormatter FORMATTER_YYYYMMDDHHMMSS_SSS = DateTimeFormatter.ofPattern(PATTERN_YYYYMMDDHHMMSS_SSS);

    String PATTERN_YYYYMMDDHHMMSS = "yyyyMMddHHmmss";

    DateTimeFormatter FORMATTER_YYYYMMDDHHMMSS = DateTimeFormatter.ofPattern(PATTERN_YYYYMMDDHHMMSS);

    String PATTERN_YYMMDDHHMMSS_SSS = "yyMMddHHmmssSSS";

    DateTimeFormatter FORMATTER_YYMMDDHHMMSS_SSS = DateTimeFormatter.ofPattern(PATTERN_YYMMDDHHMMSS_SSS);
}
