package com.example.liubao.backbutton;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * * Created by liubao on 2018/5/21.
 */
public class LogoDrawable extends Drawable {
    private Paint paint;
    private Paint circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Path path;
    private float stokeWidth = 20;
    private float radius;
    private float triangleSideLength;
    private float bianXinJu;
    private String circleColor = "#B0C4DE";
    private int width = 180;
    private int height = width;
    private int touchSlop;
    private float oldY;
    private float oldX;
    private boolean moved;

    public LogoDrawable() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.WHITE);
        paint.setAlpha(125);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(stokeWidth);
        paint.setPathEffect(new CornerPathEffect(4));

        circlePaint.setColor(Color.parseColor(circleColor));
        circlePaint.setAlpha(125);

        final Intent backIntent = new Intent(MyAccessibilityService.ACTION_BACK);
        final Intent homeIntent = new Intent(MyAccessibilityService.ACTION_HOME);

        path = new Path();
        radius = width / 2f - stokeWidth;
        bianXinJu = (float) (radius * Math.cos(Math.PI / 3f));
        triangleSideLength = (float) ((radius * Math.cos(Math.PI / 6f)) * 2);
    }

    @Override
    public void setBounds(@NonNull Rect bounds) {
        super.setBounds(bounds);

    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        int w = canvas.getWidth();
        int h = canvas.getHeight();
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

        canvas.drawCircle(width / 2f, height / 2f, width / 2f, circlePaint);
        canvas.drawPath(path, paint);
    }

    @Override
    public void setAlpha(int alpha) {

    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {

    }

    @Override
    public int getOpacity() {
        return 0;
    }
}
