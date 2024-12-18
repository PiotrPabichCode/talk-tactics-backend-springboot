package com.piotrpabich.talktactics.common.util;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ThrowableUtil {
    public static String getStackTrace(Throwable throwable){
        StringWriter sw = new StringWriter();
        try (PrintWriter pw = new PrintWriter(sw)) {
            throwable.printStackTrace(pw);
            return sw.toString();
        }
    }
}
