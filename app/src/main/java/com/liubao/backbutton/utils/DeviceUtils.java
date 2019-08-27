package com.liubao.backbutton.utils;

import android.os.Build;
import android.provider.Settings;

import com.liubao.backbutton.MainApplication;

import java.lang.reflect.Method;

/**
 * * Created by liubao on 2019/1/13.
 */
public class DeviceUtils {
    private static final String OPPO_BRAND = "oppo";
    private static final String VIVO_BRAND = "vivo";
    private static final String HUAWEI_BRAND = "huawei";
    private static final String HONOR_BRAND = "honor";
    private static final String XIAOMI_BRAND = "xiaomi";
    public static final String HAS_NOTCH = "1";
    public static final String NO_NOTCH = "0";

    public static boolean isOPPO() {
        return isBrandEquals(OPPO_BRAND);
    }

    public static boolean isVIVO() {
        return isBrandEquals(VIVO_BRAND);
    }

    public static boolean isHuawei() {
        return isBrandEquals(HUAWEI_BRAND) || isBrandEquals(HONOR_BRAND);
    }

    public static boolean isXiaomi() {
        return isBrandEquals(XIAOMI_BRAND);
    }

    private static boolean isBrandEquals(String str) {
        String brand = Build.BRAND;
        return brand != null && str.equals(brand.trim().toLowerCase());
    }

    /**
     * 判断手机是否是刘海屏, 还需要适配P
     *
     * @return
     */
    public static boolean hasNotch() {
        if (isOPPO()) {
            return hasNotchInOppo();
        } else if (isVIVO()) {
            return hasNotchInVivo();
        } else if (isHuawei()) {
            return hasNotchInHuawei();
        } else if (isXiaomi()) {
            return hasNotchInMIUI();
        }

        return false;
    }

    public static boolean hasNotchInOppo() {
        return MainApplication.context.getPackageManager().
                hasSystemFeature("com.oppo.feature.screen.heteromorphism");
    }

    public static boolean hasNotchInVivo() {
        boolean hasNotch = false;
        try {
            ClassLoader cl = MainApplication.class.getClassLoader();
            Class ftFeature = cl.loadClass("android.util.FtFeature");
            Method[] methods = ftFeature.getDeclaredMethods();
            if (methods != null) {
                for (int i = 0; i < methods.length; i++) {
                    Method method = methods[i];
                    if (method.getName().equalsIgnoreCase("isFeatureSupport")) {
                        hasNotch = (boolean) method.invoke(ftFeature, 0x00000020);
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            hasNotch = false;
        }
        return hasNotch;
    }

    public static boolean hasNotchInHuawei() {
        boolean hasNotch = false;
        try {
            ClassLoader cl = MainApplication.class.getClassLoader();
            Class HwNotchSizeUtil = cl.loadClass("com.huawei.android.util.HwNotchSizeUtil");
            Method get = HwNotchSizeUtil.getMethod("hasNotchInScreen");
            hasNotch = (boolean) get.invoke(HwNotchSizeUtil);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hasNotch;
    }

    public static boolean hasNotchInMIUI() {
        try {
            Class cls = Class.forName("android.os.SystemProperties");
            Method mMethod = cls.getDeclaredMethod("getInt", String.class, int.class);
            mMethod.setAccessible(true);
            Object object = cls.newInstance();
            int values = (Integer) mMethod.invoke(object, "ro.miui.notch", 0);
            if (values == 1) {
                return Settings.Global.getInt(MainApplication.context.getContentResolver(),
                        "force_black", 0) != 1;
            } else {
                return false;
            }
        } catch (Exception var4) {
            var4.printStackTrace();
            return false;
        }
    }
}
