package com.example.liubao.backbutton;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.accessibility.AccessibilityManager;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 * * Created by liubao on 2018/5/21.
 */
public class Utils {


    public static Bitmap drawableToBitmap(Drawable drawable, int w, int h) {
        System.out.println("Drawable转Bitmap");
        Bitmap.Config config =
                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                        : Bitmap.Config.RGB_565;
        Bitmap bitmap = Bitmap.createBitmap(w, h, config);
        //注意，下面三行代码要用到，否则在View或者SurfaceView里的canvas.drawBitmap会看不到图
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        drawable.draw(canvas);
        return bitmap;
    }

    public static void toLogo() {
        Drawable d = new LogoDrawable();
        Bitmap bmp = drawableToBitmap(d, 180, 180);
        //先把Drawable转成Bitmap，如果是Bitmap，就不用这一步了
        FileOutputStream fop;
        try {
            fop = new FileOutputStream("/sdcard/test.png");
            //实例化FileOutputStream，参数是生成路径
            bmp.compress(Bitmap.CompressFormat.PNG, 100, fop);
            //压缩bitmap写进outputStream 参数：输出格式  输出质量  目标OutputStream
            //格式可以为jpg,png,jpg不能存储透明
            fop.close();
            //关闭流
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 返回当前程序版本名
     */
    public static PackageInfo getPackageInfo(Context context) {
        try {
            PackageManager pm = context.getPackageManager();
            return pm.getPackageInfo(context.getPackageName(), 0);
        } catch (Exception e) {
            Log.e("VersionInfo", "Exception", e);
        }
        return null;
    }

    public static boolean isAccessibilitySettingsOn(Context context, String accessibilityServiceName) {
        AccessibilityManager manager = (AccessibilityManager) context.getSystemService(Context.ACCESSIBILITY_SERVICE);
        if (manager == null) {
            return false;
        }
        List<AccessibilityServiceInfo> list = manager.getEnabledAccessibilityServiceList(
                AccessibilityServiceInfo.FEEDBACK_ALL_MASK);
        for (int i = 0; i < list.size(); i++) {
            AccessibilityServiceInfo accessibilityServiceInfo = list.get(i);
            if (accessibilityServiceInfo == null) {
                continue;
            }
            if (accessibilityServiceName.equals(accessibilityServiceInfo.getId())) {
                return true;
            }
        }
        return false;
    }

    public static Object findKey(HashMap map, Object value) {
        if (map == null) {
            return null;
        }
        for (Object key : map.keySet()) {
            if (map.get(key) == value) {
                return key;
            }
        }
        return null;
    }
}
