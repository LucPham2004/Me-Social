package com.me_social.MeSocial.utils;

import java.security.SecureRandom;

public class OtpUtil {
    public static String generateOtp(int length) {
        SecureRandom random = new SecureRandom();
        StringBuilder otp = new StringBuilder();
        for (int i = 0; i < length; i++) {
            otp.append(random.nextInt(10));  // generates a digit 0-9
        }
        return otp.toString();
    }
}
