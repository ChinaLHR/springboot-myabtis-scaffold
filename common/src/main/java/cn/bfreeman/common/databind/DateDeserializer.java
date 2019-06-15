package cn.bfreeman.common.databind;

import cn.bfreeman.common.exception.FatalException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

/**
 * jackson dateParser相关
 *
 * @author xiang.rao created on 11/15/17 2:00 PM
 * @version $Id$
 */
public class DateDeserializer extends JsonDeserializer<Date> {
    private static final String[] DATE_PATTERN_ARRAY = {
            "yyyy-MM-dd HH:mm:ss",
            "yyyy-MM-dd",
            "yyyy-MM-dd HH:mm",
            "yyyy-MM-dd HH",
            "yyyyMMdd"
    };

    @Override
    public Date deserialize(JsonParser jsonParser, DeserializationContext ctxt) throws IOException {
        String date = jsonParser.getText();
        if (StringUtils.isNotEmpty(date)) {
            try {
                if (NumberUtils.isNumber(date)) {
                    return new Date(Long.parseLong(date));
                }
                return DateUtils.parseDate(date, DATE_PATTERN_ARRAY);
            } catch (ParseException e) {
                throw new FatalException("cannot parse date string: " + date, jsonParser.getCurrentLocation(), e);
            }
        }
        return null;
    }
}
