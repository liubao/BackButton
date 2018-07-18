package com.example.liubao.backbutton;

import android.app.Application;
import android.content.Context;

import com.tencent.bugly.crashreport.CrashReport;

/**
 * * Created by liubao on 2018/5/21.
 */
public class MainApplication extends Application {
    private static final String TAG = MainApplication.class.getSimpleName();
    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        initBugly();
    }

    private void initBugly() {
        CrashReport.initCrashReport(getApplicationContext(), "16f874abc6", true);
    }

}
