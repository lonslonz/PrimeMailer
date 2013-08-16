package com.skplanet.mailer.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Random;

public class CommonUtils {
    public static String genUniqueStr() {
        Random rand = new Random();
        return System.currentTimeMillis() + "." + rand.nextInt(10000);
    }
    public static String exception2Str(Exception e) {
        StringWriter out = new StringWriter();
        e.printStackTrace(new PrintWriter(out));
        return out.toString();
     }
    public static void validate(String targetStr, String indexStr) throws Exception {
        if(targetStr == null) {
            throw new Exception(indexStr + " is null. ");
        }
    }
}
