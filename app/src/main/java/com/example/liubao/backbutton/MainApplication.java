package com.example.liubao.backbutton;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import com.tencent.bugly.crashreport.CrashReport;

/**
 * * Created by liubao on 2018/5/21.
 */
public class MainApplication extends Application {
    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        initBugly();
        initVersion();
        initScreenInfo();
    }


    private void initVersion() {
        PackageInfo packageInfo = Utils.getPackageInfo(context);
        if (packageInfo != null) {
            BBCommon.versionName = packageInfo.versionName;
            BBCommon.versioncode = packageInfo.versionCode;
        }
    }

    private void initBugly() {
        CrashReport.initCrashReport(getApplicationContext(), "16f874abc6", true);
    }

    private void initScreenInfo() {
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        if (wm == null) {
            return;
        }
        Display display = wm.getDefaultDisplay();
        if (display == null) {
            return;
        }
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getMetrics(displayMetrics);
        BBCommon.screenWidth = displayMetrics.widthPixels;
        BBCommon.screenHeight = displayMetrics.heightPixels;
    }
}
