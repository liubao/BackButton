package com.example.liubao.backbutton.common;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import com.example.liubao.backbutton.MainApplication;
import com.example.liubao.backbutton.Utils;

/**
 * * Created by liubao on 2018/6/8.
 */
public class BBCommon {
    static {
        PackageInfo packageInfo = Utils.getPackageInfo(MainApplication.context);
        if (packageInfo != null) {
            BBCommon.versionName = packageInfo.versionName;
            BBCommon.versionCode = packageInfo.versionCode;
        }
        BBCommon.serviceName = MainApplication.context.getPackageName() + "/." + "MyAccessibilityService";
        WindowManager wm = (WindowManager) MainApplication.context.getSystemService(Context.WINDOW_SERVICE);
        if (wm != null && wm.getDefaultDisplay() != null) {
            Display display = wm.getDefaultDisplay();
            DisplayMetrics displayMetrics = new DisplayMetrics();
            display.getMetrics(displayMetrics);
            BBCommon.screenWidth = displayMetrics.widthPixels;
            BBCommon.screenHeight = displayMetrics.heightPixels;
        }
    }

    public static final boolean DEBUG = false;
    public static String serviceName;
    public static float screenWidth;
    public static float screenHeight;
    public static String versionName;
    public static int versionCode;
    public static final int DEFAULT_X = 0;
    public static final int DEFAULT_Y = (int) (screenHeight / 2);

    public static final String ACTION_BACK = "b";
    public static final String ACTION_HOME = "h";
    public static final String ACTION_RECENT = "r";
    public static final String STYLE_STAR = "style_star";
    public static final String STYLE_LINE = "style_line";

    public static final String SHARED_PREFERENCES_PREFIX = "s_p_";

    public static final String SHARED_PREFERENCES_STYLE = SHARED_PREFERENCES_PREFIX + "style";
    public static final String SHARED_PREFERENCES_DOUBLE = SHARED_PREFERENCES_PREFIX + "double";
    public static final String SHARED_PREFERENCES_LONG = SHARED_PREFERENCES_PREFIX + "long";
    public static final String SHARED_PREFERENCES_ALPHA = SHARED_PREFERENCES_PREFIX + "alpha";
    public static final String SHARED_PREFERENCES_SIZE = SHARED_PREFERENCES_PREFIX + "size";
    public static final String SHARED_PREFERENCES_X_PORTRAIT = SHARED_PREFERENCES_PREFIX + "x_portrait";
    public static final String SHARED_PREFERENCES_Y_PORTRAIT = SHARED_PREFERENCES_PREFIX + "y_portrait";
    public static final String SHARED_PREFERENCES_X_LANDSCAPE = SHARED_PREFERENCES_PREFIX + "x_landscape";
    public static final String SHARED_PREFERENCES_Y_LANDSCAPE = SHARED_PREFERENCES_PREFIX + "y_landscape";


}
