/*
 * Copyright (c) 2017.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.itfsw.query.builder.support.filter;

import com.itfsw.query.builder.exception.FilterException;
import com.itfsw.query.builder.support.model.IRule;
import com.itfsw.query.builder.support.model.JsonRule;
import com.itfsw.query.builder.support.model.enums.EnumRuleType;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * ---------------------------------------------------------------------------
 *
 * ---------------------------------------------------------------------------
 * @author: hewei
 * @time:2017/11/2 13:22
 * ---------------------------------------------------------------------------
 */
public class DatetimeConvertFilter implements IRuleFilter {
    private final static String DEFAULT_DATE_FMT = "yyy-MM-dd";
    private final static String DEFAULT_TIME_FMT = "HH:mm:ss";
    private final static String DEFAULT_DATETIME_FMT = "yyyy-MM-dd HH:mm:ss";

    private DateFormat datetimeFormat;
    private DateFormat dateFormat;
    private DateFormat timeFormat;

    /**
     * 构造函数
     */
    public DatetimeConvertFilter() {
        this.datetimeFormat = new SimpleDateFormat(DEFAULT_DATETIME_FMT);
        this.dateFormat = new SimpleDateFormat(DEFAULT_DATE_FMT);
        this.timeFormat = new SimpleDateFormat(DEFAULT_TIME_FMT);
    }

    /**
     * 构造函数
     * @param datetimeFormat
     */
    public DatetimeConvertFilter(DateFormat datetimeFormat) {
        this.datetimeFormat = datetimeFormat;
    }

    /**
     * 构造函数
     * @param datetimeFormat
     * @param dateFormat
     */
    public DatetimeConvertFilter(DateFormat datetimeFormat, DateFormat dateFormat) {
        this.datetimeFormat = datetimeFormat;
        this.dateFormat = dateFormat;
    }

    /**
     * 构造函数
     * @param datetimeFormat
     * @param dateFormat
     * @param timeFormat
     */
    public DatetimeConvertFilter(DateFormat datetimeFormat, DateFormat dateFormat, DateFormat timeFormat) {
        this.datetimeFormat = datetimeFormat;
        this.dateFormat = dateFormat;
        this.timeFormat = timeFormat;
    }

    @Override
    public void doFilter(JsonRule jsonRule) throws FilterException {
        if (!jsonRule.isGroup()) {
            IRule rule = jsonRule.toRule();

            if (rule.getValue() != null && (
                    EnumRuleType.DATE.equals(rule.getType())
                            || EnumRuleType.TIME.equals(rule.getType())
                            || EnumRuleType.DATETIME.equals(rule.getType())
            )) {
                try {
                    if (rule.getValue() instanceof List) {
                        List<Object> list = new ArrayList<>();
                        for (Object value : (List) rule.getValue()) {
                            list.add(convert(value, rule.getType()));
                        }
                        rule.setValue(list);
                    } else {
                        rule.setValue(convert(rule.getValue(), rule.getType()));
                    }
                } catch (Exception e) {
                    throw new FilterException(e.getMessage() + " for:" + rule + "!");
                }
            }
        }
    }

    private Object convert(Object value, String type) throws ParseException {
        if (!(value instanceof Date)) {
            if (EnumRuleType.DATETIME.equals(type)) {
                return datetimeFormat.parse(value.toString());
            } else if (EnumRuleType.DATE.equals(type)) {
                return dateFormat.parse(value.toString());
            } else if (EnumRuleType.TIME.equals(type)) {
                return timeFormat.parse(value.toString());
            }
        }
        return value;
    }
}
