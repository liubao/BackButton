package com.example.liubao.backbutton.view;

import android.graphics.Canvas;

import com.example.liubao.backbutton.IDataController;

/**
 * * Created by liubao on 2018/7/23.
 */
public interface Painter {
    void onDraw(Canvas canvas);

    void setAlpha(int alpha);

    int getWidth();

    int getHeight();

    void onSizeChanged(int w, int h);

    IDataController<Integer> getSizeDS();

    IDataController<Integer> getAlphaDS();
}
