package com.liubao.backbutton.common;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Build;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import com.liubao.backbutton.MLod;
import com.liubao.backbutton.MainApplication;
import com.liubao.backbutton.R;
import com.liubao.backbutton.ReflectUtils;
import com.liubao.backbutton.utils.DeviceUtils;
import com.liubao.backbutton.utils.OpenUDID;

import java.util.Date;
import java.util.HashMap;
import java.util.Set;
import java.util.TimeZone;

/**
 * * Created by liubao on 2019/1/13.
 */
public class LoginCommon {
    public static String AppPackageName; // app packageName
    public static String AppVerName;
    public static int AppVerCode;
    public static String Brand;
    public static String AppName;
    public static int ScreenWidth;
    public static int ScreenHeight;
    public static float Density;
    public static int DensityDPI;
    public static String SystemVersion;
    public static String HardwareModel;
    public static int timeOffset;
    public static String HAS_NOTCH;
    public static String OpenUuid;

    public static void initCommon() {
        Context context = MainApplication.context;
        AppPackageName = context.getPackageName();
        PackageInfo packageInfo = null;
        PackageManager pm = context.getPackageManager();
        try {
            packageInfo = pm.getPackageInfo(context.getPackageName(), 0);
            LoginCommon.AppVerName = packageInfo.versionName;
            LoginCommon.AppVerCode = packageInfo.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        LoginCommon.Brand = TextUtils.isEmpty(Build.BRAND) ? "unknown" : Build.BRAND;
        Resources resources = context.getResources();
        LoginCommon.AppName = resources.getString(R.string.app_name);
        DisplayMetrics dm = resources.getDisplayMetrics();
        LoginCommon.ScreenWidth = dm.widthPixels;
        LoginCommon.ScreenHeight = dm.heightPixels;
        LoginCommon.Density = dm.density;
        LoginCommon.DensityDPI = dm.densityDpi;
        LoginCommon.SystemVersion = Build.VERSION.RELEASE;
        LoginCommon.HardwareModel = Build.MODEL;
        LoginCommon.timeOffset = TimeZone.getDefault().getOffset(new Date().getTime()) / (60 * 1000);
        LoginCommon.HAS_NOTCH = DeviceUtils.hasNotch() ? DeviceUtils.HAS_NOTCH : DeviceUtils.NO_NOTCH;

        OpenUDID.syncContext(context, null);
        LoginCommon.OpenUuid = OpenUDID.getOpenUDIDInContext();
    }

    static HashMap<String, String> params;


    public static HashMap<String, String> getBaseParams() {
        if (params == null || params.isEmpty()) {
            initBaseParamsHM();
        }
        return params;
    }

    private static void initBaseParamsHM() {
        params = ReflectUtils.getFieldsKV(LoginCommon.class);
        Set<String> sets = params.keySet();
        for (String s : sets) {
            MLod.d("key " + s + "value " + params.get(s));
        }
    }
}
