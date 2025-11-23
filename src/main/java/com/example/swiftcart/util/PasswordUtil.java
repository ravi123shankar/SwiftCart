package com.example.swiftcart.util;

import java.security.MessageDigest;

public class PasswordUtil {

    public static String hash(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashed = md.digest(input.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder();
            for (byte b : hashed) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException("Hashing failed");
        }
    }

    public static boolean matches(String raw, String hashed) {
        return hash(raw).equals(hashed);
    }
}
