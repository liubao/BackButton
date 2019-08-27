package com.liubao.backbutton.view;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;

import com.liubao.backbutton.IDataController;

/**
 * * Created by liubao on 2018/7/23.
 */
public class StarPainter implements Painter {
    private Paint circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public AlphaDS alphaDS;
    public SizeDS sizeDS;
    public Path path;
    public float stokeWidth = 20;

    public StarPainter() {
        sizeDS = new SizeDS();
        alphaDS = new AlphaDS();
        paint.setColor(Color.WHITE);
        paint.setAlpha(alphaDS.alpha);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(stokeWidth);
        paint.setPathEffect(new CornerPathEffect(4));
        circlePaint.setColor(0xFFB0C4DE);
        circlePaint.setAlpha(alphaDS.alpha);
        path = new Path();
        onSizeChanged(sizeDS.width, sizeDS.height);
    }


    @Override
    public void onDraw(Canvas canvas) {
        int width = canvas.getWidth();
        int height = canvas.getHeight();
        canvas.drawCircle(width / 2f, height / 2f, width / 2f, circlePaint);
        canvas.drawPath(path, paint);
    }

    @Override
    public void setAlpha(int alpha) {
        alphaDS.set(alpha);
        paint.setAlpha(alpha);
        circlePaint.setAlpha(alpha);
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
        float radius = w / 2f - stokeWidth;
        float bianXinJu = (float) (radius * Math.cos(Math.PI / 3f));
        float triangleSideLength = (float) ((radius * Math.cos(Math.PI / 6f)) * 2);

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

    @Override
    public IDataController<Integer> getSizeDS() {
        return sizeDS;
    }

    @Override
    public IDataController<Integer> getAlphaDS() {
        return alphaDS;
    }

}
