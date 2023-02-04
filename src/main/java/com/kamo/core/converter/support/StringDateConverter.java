package com.kamo.core.converter.support;

import com.kamo.core.converter.Converter;
import javafx.util.converter.DateStringConverter;

import java.text.DateFormat;
import java.util.Date;

public  class StringDateConverter implements Converter<String, Date> {
        @Override
        public Date convert(String value) {
            return new DateStringConverter(DateFormat.DEFAULT).fromString(value);
        }
    }