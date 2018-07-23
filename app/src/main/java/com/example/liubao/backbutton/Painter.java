package com.example.liubao.backbutton;

import android.graphics.Canvas;

/**
 * * Created by liubao on 2018/7/23.
 */
interface Painter {
    void onDraw(Canvas canvas);

    void setAlpha(int alpha);

    int getWidth();

    int getHeight();

    void onSizeChanged(int w, int h);

    IDataController<Integer> getSizeDS();

    IDataController<Integer> getAlphaDS();
}
