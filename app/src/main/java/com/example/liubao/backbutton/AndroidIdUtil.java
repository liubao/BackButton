package com.example.liubao.backbutton;

import android.content.Context;
import android.provider.Settings;
import android.text.TextUtils;

import java.util.UUID;

/**
 * * Created by liubao on 2019/1/13.
 */
public class AndroidIdUtil {
    public static String getAndroidId(Context ctx) {
        if (ctx != null) {
            String id = Settings.Secure.getString(ctx.getContentResolver(), Settings.Secure.ANDROID_ID);
//            use-cases, use AdvertisingIdClient$Info#getId and for analytics, use InstanceId#getId.
            UUID.randomUUID().toString();
            return TextUtils.isEmpty(id) ? "" : id.toLowerCase();
        }
        return null;
    }
}
