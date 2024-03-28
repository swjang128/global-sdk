package io.snplab.gsdk.common.util;

import java.security.SecureRandom;

public class IDTool {

    static final String ALPHANUMERIC = "123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz";

    static SecureRandom secureRandom = new SecureRandom();

    public static String randomString(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++)
            sb.append(ALPHANUMERIC.charAt(secureRandom.nextInt(ALPHANUMERIC.length())));
        return sb.toString();
    }
}
