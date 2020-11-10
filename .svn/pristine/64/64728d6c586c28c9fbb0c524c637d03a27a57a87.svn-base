package com.apply.ism.common;

import org.springframework.core.env.PropertySource;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author xiehao
 * @date 2019-08-14
 */
public final class ReplacePatternUtils {

    private static final Pattern PATTERN = Pattern.compile("\\$\\{([\\w-\\.]+)}");

    private ReplacePatternUtils() {

    }

    public static String replace(String key, PropertySource propertySource) {
        String value = propertySource.getProperty(key).toString();
        Matcher matcher = PATTERN.matcher(value);
        boolean matchResult = matcher.find();
        if (!matchResult) {
            return value;
        }
        while (matchResult) {
            String matchValue = matcher.group();
            String replacePropertyKey = matchValue.substring(2, matchValue.length() - 1);
            value = value.replace(matchValue, replace(replacePropertyKey, propertySource));
            matchResult = matcher.find();
        }
        return value;
    }
}
