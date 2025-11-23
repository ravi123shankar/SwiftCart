package com.example.swiftcart.util;

import jakarta.servlet.http.HttpServletRequest;

public class RequestParamUtil {

    public static String getString(HttpServletRequest req, String name, String defaultValue) {
        String v = req.getParameter(name);
        return v == null ? defaultValue : v.trim();
    }

    public static String getString(HttpServletRequest req, String name) {
        return getString(req, name, null);
    }

    public static int getInt(HttpServletRequest req, String name, int defaultValue) {
        String v = req.getParameter(name);
        if (v == null) return defaultValue;
        try {
            return Integer.parseInt(v.trim());
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public static long getLong(HttpServletRequest req, String name, long defaultValue) {
        String v = req.getParameter(name);
        if (v == null) return defaultValue;
        try {
            return Long.parseLong(v.trim());
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public static boolean getBoolean(HttpServletRequest req, String name, boolean defaultValue) {
        String v = req.getParameter(name);
        if (v == null) return defaultValue;
        return "true".equalsIgnoreCase(v.trim()) || "1".equals(v.trim());
    }

    public static int getPage(HttpServletRequest req) {
        int p = getInt(req, "page", 1);
        return p <= 0 ? 1 : p;
    }

    public static int getSize(HttpServletRequest req) {
        int s = getInt(req, "size", 10);
        return s <= 0 ? 10 : s;
    }
}
