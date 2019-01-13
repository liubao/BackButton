package com.example.liubao.backbutton.view;

import android.graphics.Canvas;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;

import com.example.liubao.backbutton.IDataController;

/**
 * * Created by liubao on 2018/7/23.
 */
public class LinePainter implements Painter {

    public AlphaDS alphaDS;
    public SizeDS sizeDS;
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    int rectW = 12;
    BBView view;

    public LinePainter(BBView v) {
        view = v;
        sizeDS = new SizeDS();
        alphaDS = new AlphaDS();
        paint.setColor(0xFFB0C4DE);
        paint.setAlpha(alphaDS.alpha);
        paint.setPathEffect(new CornerPathEffect(4));
    }


    @Override
    public void onDraw(Canvas canvas) {
        int width = canvas.getWidth();
        int height = canvas.getHeight();
        if (view.mParams != null) {
            if (view.mParams.x <= view.getWidth()) {
                canvas.drawRect(0, 0, rectW, height, paint);
            } else {
                canvas.drawRect(width - rectW, 0, width, height, paint);
            }
        }
    }

    @Override
    public void setAlpha(int alpha) {
        alphaDS.set(alpha);
        paint.setAlpha(alpha);
    }

    @Override
    public int getWidth() {
        return sizeDS.width;
    }

    @Override
    public int getHeight() {
        return sizeDS.height;
    }

    @Override
    public void onSizeChanged(int w, int h) {
        sizeDS.set(w);
    }

    @Override
    public IDataController<Integer> getSizeDS() {
        return sizeDS;
    }

    @Override
    public IDataController<Integer> getAlphaDS() {
        return alphaDS;
    }
}
