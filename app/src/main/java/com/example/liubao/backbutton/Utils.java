package com.example.liubao.backbutton;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * * Created by liubao on 2018/5/21.
 */
public class Utils {
    public static float screenWidth;
    public static int screenHeight;

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
}
