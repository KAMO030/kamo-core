package com.kamo.core.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public  final class IOUtils {
    private IOUtils(){}
    public static  String readString(InputStream inputStream) {
        StringBuffer buf = new StringBuffer();

        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))){
            bufferedReader.lines().forEach(line -> buf.append(line));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return buf.toString();
    }
}
