package com.github.landyking.mavenPlugin.junkman.utils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

/**
 * <br/>
 *
 * @author: landy
 * @date: 2016/08/25 16:35
 * note:
 */
public class Texts {
    public static boolean hasLength(String text) {
        return text != null && text.length() > 0;
    }

    public static boolean hasText(String text) {
        return text != null && text.trim().length() > 0;
    }

    /**
     * 修复url,增加http://前缀
     *
     * @param url
     * @return
     */
    public static URL fixUrl(String url) throws MalformedURLException {
        URL rst = null;
        try {
            rst = new URL(url);
        } catch (MalformedURLException e) {
            String lowerCase = url.toLowerCase();
            if (!lowerCase.startsWith("http:") && !lowerCase.startsWith("https:")) {
                rst = new URL("http://" + url);
            }
        }
        return rst;
    }

    public static String uuidLong() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static String fixContextPath(String localContext) {
        if (!hasText(localContext)) {
            return "";
        }
        if (!localContext.startsWith("/") && !localContext.startsWith("\\")) {
            localContext = "/" + localContext;
        }
        return localContext;
    }

    public static String extractHost(String host) {
        if (host.toLowerCase().startsWith("http://")) {
            return host.substring("http://".length());
        }
        return host;
    }

    public static String dataSizeToHuman(long size) {
        StringBuilder sb = new StringBuilder();
        long kbUnit = 1024;
        long mbUnit = kbUnit * 1024;
        long gbUnit = mbUnit * 1024;
        long tbUnit = gbUnit * 1024;

        long remain = size;

        long tb = size / tbUnit;
        if (tb > 0) {
            sb.append(tb);
            sb.append("T ");
            remain -= tb * tbUnit;
        }
        long gb = remain / gbUnit;
        if (gb > 0) {
            sb.append(gb);
            sb.append("G ");
            remain -= gb * gbUnit;
        }
        long mb = remain / mbUnit;
        if (mb > 0) {
            sb.append(mb);
            sb.append("M ");
            remain -= mb * mbUnit;
        }
        long kb = remain / kbUnit;
        if (kb > 0) {
            sb.append(kb);
            sb.append("K ");
        }

        String result = sb.toString();
        if (!Texts.hasText(result)) {
            return "0";
        }
        return result.trim();
    }

    public static String getMessage(Throwable cause) {
        return cause == null ? "" : cause.getMessage();
    }

}
