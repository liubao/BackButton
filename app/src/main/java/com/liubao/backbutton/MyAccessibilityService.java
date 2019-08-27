package com.liubao.backbutton;

import android.accessibilityservice.AccessibilityService;
import android.content.Intent;
import android.content.IntentFilter;
import android.provider.Settings;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Toast;

import com.liubao.backbutton.common.BBCommon;
import com.liubao.backbutton.view.BBView;

import java.util.Observable;
import java.util.Observer;

/**
 * * Created by liubao on 2018/5/12.
 */
public class MyAccessibilityService extends AccessibilityService {
    private final static String TAG = MyAccessibilityService.class.getSimpleName();

    private ActionObserver actionObserver = new ActionObserver();

    @Override
    public void onCreate() {
        super.onCreate();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BBCommon.ACTION_BACK);
        intentFilter.addAction(BBCommon.ACTION_HOME);
        intentFilter.addAction(BBCommon.ACTION_RECENT);
        ActionObservable.getInstance().addObserver(actionObserver);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onServiceConnected() {
        if (Utils.isAccessibilitySettingsOn(this, BBCommon.serviceName) &&
                Settings.canDrawOverlays(this)) {
            BBView.getInstance().show();
            Toast.makeText(this, "BackButton正常运行中", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

    }

    @Override
    public void onInterrupt() {
    }

    public class ActionObserver implements Observer {
        @Override
        public void update(Observable o, Object arg) {
            ActionIntent intent = (ActionIntent) arg;
            if (null != intent) {
                String action = intent.getAction();
                if (action == null) {
                    return;
                }
                boolean success = false;
                switch (action) {
                    case BBCommon.ACTION_BACK:
                        success = performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
                        break;
                    case BBCommon.ACTION_HOME:
                        success = performGlobalAction(AccessibilityService.GLOBAL_ACTION_HOME);
                        break;
                    case BBCommon.ACTION_RECENT:
                        success = performGlobalAction(AccessibilityService.GLOBAL_ACTION_RECENTS);
                        break;
                }
                if (BBCommon.DEBUG) {
                    Log.d(TAG, success + " is success");
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ActionObservable.getInstance().deleteObserver(actionObserver);
    }
}
