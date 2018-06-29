package com.example.liubao.backbutton;

import android.accessibilityservice.AccessibilityService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Toast;

/**
 * * Created by liubao on 2018/5/12.
 */
public class MyAccessibilityService extends AccessibilityService {
    private final static String TAG = MyAccessibilityService.class.getSimpleName();
    public final static String ACTION_BACK = MainActivity.APP_PACKAGE_NAME + "back";
    public final static String ACTION_HOME = MainActivity.APP_PACKAGE_NAME + "home";
    public final static String ACTION_RECENTS = MainActivity.APP_PACKAGE_NAME + "recents";

    private MyBroadcastReceiver myBroadcastReceiver;
    private LocalBroadcastManager localBroadcastManager;

    @Override
    public void onCreate() {
        super.onCreate();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_BACK);
        intentFilter.addAction(ACTION_HOME);
        intentFilter.addAction(ACTION_RECENTS);
        myBroadcastReceiver = new MyBroadcastReceiver();
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        localBroadcastManager.registerReceiver(myBroadcastReceiver, intentFilter);
        Toast.makeText(this, "服务已开启", Toast.LENGTH_SHORT).show();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onServiceConnected() {
        FloatingView.getInstance().show();
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

    }

    @Override
    public void onInterrupt() {
    }

    class MyBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (null != intent) {
                String action = intent.getAction();
                if (action == null) {
                    return;
                }
                boolean success = false;
                switch (action) {
                    case ACTION_BACK:
                        success = performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
                        break;
                    case ACTION_HOME:
                        success = performGlobalAction(AccessibilityService.GLOBAL_ACTION_HOME);
                        break;
                    case ACTION_RECENTS:
                        success = performGlobalAction(AccessibilityService.GLOBAL_ACTION_RECENTS);
                        break;
                }
                if (MainActivity.DEBUG) {
                    Log.d(TAG, success + " is success");
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        localBroadcastManager.unregisterReceiver(myBroadcastReceiver);
    }
}
