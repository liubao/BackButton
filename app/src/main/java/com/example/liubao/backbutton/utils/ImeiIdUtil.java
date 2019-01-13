package com.example.liubao.backbutton.utils;

import android.content.Context;
import android.telephony.TelephonyManager;

/**
 * * Created by liubao on 2019/1/13.
 */
public class ImeiIdUtil {
    static String generateImeiId(Context context) {
        String openUdid = null;
        try {
            TelephonyManager TelephonyMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            String szImei = TelephonyMgr.getDeviceId(); // Requires READ_PHONE_STATE

            if (null != szImei && !szImei.substring(0, 3).equals("000")) {
                openUdid = szImei;
            }
        } catch (Exception ex) {
            openUdid = null;
        }

        return openUdid;
    }
}
