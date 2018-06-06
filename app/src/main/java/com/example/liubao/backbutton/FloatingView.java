package com.example.liubao.backbutton;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.os.Build;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.AppCompatImageView;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;

/**
 * * Created by liubao on 2018/5/12.
 */

public class FloatingView extends AppCompatImageView {
    public static FloatingView instance;

    public static FloatingView getInstance(Context context) {
        if (instance == null) {
            instance = new FloatingView(context);
        }
        return instance;
    }

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
//    GestureDetector gestureDetector;

    public FloatingView(final Context context) {
        super(context);
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);//获得WindowManager对象
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
        final Intent recentsIntent = new Intent(MyAccessibilityService.ACTION_RECENTS);

        path = new Path();
        radius = width / 2f - stokeWidth;
        bianXinJu = (float) (radius * Math.cos(Math.PI / 3f));
        triangleSideLength = (float) ((radius * Math.cos(Math.PI / 6f)) * 2);


        final LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(context);
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (moved) {
                    return;
                }
                localBroadcastManager.sendBroadcast(backIntent);
            }
        });
        setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (moved) {
                    return true;
                }
                localBroadcastManager.sendBroadcast(homeIntent);
                return true;
            }
        });
//        gestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
//
//            @Override
//            public boolean onSingleTapConfirmed(MotionEvent e) {//单击事件
//                //表示单击，此处也可以做单击的操作
//                localBroadcastManager.sendBroadcast(backIntent);
//                return super.onSingleTapConfirmed(e);
//            }
//
//            @Override
//            public boolean onDoubleTap(MotionEvent e) {//双击事件
//                localBroadcastManager.sendBroadcast(homeIntent);
//                return super.onDoubleTap(e);  //此处做双击具体业务逻辑
//            }
//
//            @Override
//            public void onLongPress(MotionEvent e) {
//                super.onLongPress(e);
//                localBroadcastManager.sendBroadcast(recentsIntent);
//            }
//
//        });
        touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    private void compute() {
        radius = width / 2f - stokeWidth;
        bianXinJu = (float) (radius * Math.cos(Math.PI / 3f));
        triangleSideLength = (float) ((radius * Math.cos(Math.PI / 6f)) * 2);
    }

    private WindowManager windowManager;
    private WindowManager.LayoutParams mParams;
    private boolean isViewAdded;

    public void show() {
        if (isViewAdded) {
            return;
        }
        isViewAdded = true;
        mParams = new WindowManager.LayoutParams();
        mParams.gravity = Gravity.TOP | Gravity.START;
        mParams.x = 0;
        mParams.y = 100;
        //总是出现在应用程序窗口之上
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            mParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        }
        //设置图片格式，效果为背景透明
        mParams.format = PixelFormat.RGBA_8888;
        mParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        mParams.width = width;
        mParams.height = height;
        windowManager.addView(FloatingView.this, mParams);
    }

    int offsetY;
    int offsetX;
    int maxOffsetY;
    int maxOffsetX;

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        int action = event.getActionMasked();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                oldY = event.getRawY();
                oldX = event.getRawX();
                moved = false;
                maxOffsetY = 0;
                maxOffsetX = 0;
                break;
            case MotionEvent.ACTION_MOVE:
                offsetY = (int) (event.getRawY() - oldY);
                offsetX = (int) (event.getRawX() - oldX);
                mParams.y += offsetY;//相对于屏幕左上角的位置
                mParams.x += offsetX;//相对于屏幕左上角的位置
                windowManager.updateViewLayout(FloatingView.this, mParams);
                maxOffsetY = Math.max(maxOffsetY, Math.abs(offsetY));
                maxOffsetX = Math.max(maxOffsetX, Math.abs(offsetX));
                if (maxOffsetY > touchSlop || maxOffsetX > touchSlop) {
                    moved = true;
                }
                oldY = event.getRawY();
                oldX = event.getRawX();
                break;
            case MotionEvent.ACTION_UP:
                oldY = event.getRawY();
                oldX = event.getRawX();
                if (oldX < Utils.screenWidth / 2) {
                    mParams.x = 0;
                } else {
                    mParams.x = (int) (Utils.screenWidth - getWidth());
                }
                windowManager.updateViewLayout(FloatingView.this, mParams);
                break;
        }
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        int w = getWidth();
        int h = getHeight();
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
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(width / 2f, height / 2f, width / 2f, circlePaint);
        canvas.drawPath(path, paint);

    }

    public void updateView(int progress) {
        if (mParams == null) {
            return;
        }
        width = 10 * progress;
        height = width;
        compute();
        mParams.width = width;
        mParams.height = height;
        windowManager.updateViewLayout(FloatingView.this, mParams);
    }
}
