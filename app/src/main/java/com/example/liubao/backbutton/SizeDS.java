package com.example.liubao.backbutton;

import android.graphics.Path;

/**
 * * Created by liubao on 2018/7/16.
 */
public class SizeDS implements IDataController<Integer> {
    public static final int DEFAULT_MAX_WIDTH = (int) (BBCommon.screenWidth / 2);
    public int width;
    public int height;
    private float radius;
    public float stokeWidth = 20;
    private float bianXinJu;
    private float triangleSideLength;
    public Path path;

    public SizeDS() {
        int w = getFromDisk();
        if (w <= 0) {
            w = DEFAULT_MAX_WIDTH;
        }
        width = w;
        height = w;
        compute();
        path = new Path();
    }

    @Override
    public Integer getFromMemory() {
        return width;
    }

    @Override
    public Integer getFromDisk() {
        Integer key = SharedPreferencesUtils.getInt(BBCommon.SHARED_PREFERENCES_SIZE, DEFAULT_MAX_WIDTH / 2);
        return key;
    }

    @Override
    public void set(Integer w) {
        width = w == null ? DEFAULT_MAX_WIDTH : w;
        height = width;
        compute();
    }

    @Override
    public void putToDisk() {
        SharedPreferencesUtils.putInt(BBCommon.SHARED_PREFERENCES_SIZE, width);
    }

    public void compute() {
        radius = width / 2f - stokeWidth;
        bianXinJu = (float) (radius * Math.cos(Math.PI / 3f));
        triangleSideLength = (float) ((radius * Math.cos(Math.PI / 6f)) * 2);
    }

    public void onLayout(int w, int h) {
        int halfH = h / 2;
        float start = halfH + bianXinJu;
        float halfSide = triangleSideLength / 2f;
        path.reset();
        path.moveTo(start, halfH - halfSide);
        path.lineTo(start, halfH + halfSide);
        path.lineTo(halfH - radius, halfH);
        path.close(); // 使这些点构成封闭的多边形

        path.moveTo(halfH + radius, halfH);
        path.lineTo(halfH - bianXinJu, halfH - halfSide);
        path.lineTo(halfH - bianXinJu, halfH + halfSide);
        path.close(); // 使这些点构成封闭的多边形
    }

}
