package com.liubao.backbutton.utils;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;

import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

/**
 * * Created by liubao on 2019/1/13.
 */
class WifiIdUtil {

    static String generateWifiId(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return getWifiMacAddressM();
        } else {
            return getWifiMacAddress(context);
        }
    }

    private static String getWifiMacAddressM() {
        try {
            String interfaceName = "wlan0";
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                if (!intf.getName().equalsIgnoreCase(interfaceName)) {
                    continue;
                }

                byte[] mac = intf.getHardwareAddress();
                if (mac == null) {
                    return "";
                }

                StringBuilder buf = new StringBuilder();
                for (byte aMac : mac) {
                    buf.append(String.format("%02X:", aMac));
                }
                if (buf.length() > 0) {
                    buf.deleteCharAt(buf.length() - 1);
                }
                return buf.toString();
            }
        } catch (Exception ex) {
            // for now eat exceptions
        }
        return "";
    }

    private static String getWifiMacAddress(Context context) {
        try {
            WifiManager wifiMan = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            if (wifiMan != null) {
                WifiInfo wifiInf = wifiMan.getConnectionInfo();
                if (wifiInf != null) {
                    String macAddr = wifiInf.getMacAddress();
                    if (macAddr != null) {
                        return macAddr;
                    }
                }
            }
        } catch (Exception ex) {
            // for now eat exceptions
        }
        return null;
    }
}
