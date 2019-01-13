package com.example.liubao.backbutton.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

/**
 * * Created by liubao on 2019/1/13.
 */
class RandomIdUtil {
    static String generateRandomId() {
        return Md5(UUID.randomUUID().toString());
    }

    private static String Md5(String input) {
        String mOutput = "";
        try {
            MessageDigest m = MessageDigest.getInstance("MD5");
            m.update(input.getBytes("UTF-8"), 0, input.length());
            byte p_md5Data[] = m.digest();
            for (int i = 0; i < p_md5Data.length; i++) {
                int b = (0xFF & p_md5Data[i]);
                // if it is a single digit, make sure it have 0 in front (proper padding)
                if (b <= 0xF) mOutput += "0";
                // add number to string
                mOutput += Integer.toHexString(b);
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {

        }
        // hex string to uppercase
        return mOutput.toUpperCase();
    }
}
