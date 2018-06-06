package com.example.liubao.backbutton;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * * Created by liubao on 2018/5/19.
 */
public class FloatService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
        FloatingView.getInstance(this).show();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    

}
