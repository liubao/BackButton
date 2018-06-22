package com.example.liubao.backbutton;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.os.Build;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.AppCompatImageView;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;

import java.util.HashMap;

/**
 * * Created by liubao on 2018/5/12.
 */

public class FloatingView extends AppCompatImageView {

    public static final int TAG_DOUBLE = 0;
    public static final int TAG_LONG = 1;
    public static FloatingView instance;
    private Intent doubleClickIntent;
    private Intent longClickIntent;

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
    LocalBroadcastManager localBroadcastManager;

    final Intent backIntent = new Intent(MyAccessibilityService.ACTION_BACK);
    final Intent homeIntent = new Intent(MyAccessibilityService.ACTION_HOME);
    final Intent recentsIntent = new Intent(MyAccessibilityService.ACTION_RECENTS);


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


        path = new Path();
        radius = width / 2f - stokeWidth;
        bianXinJu = (float) (radius * Math.cos(Math.PI / 3f));
        triangleSideLength = (float) ((radius * Math.cos(Math.PI / 6f)) * 2);
        localBroadcastManager = LocalBroadcastManager.getInstance(context);
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
        screenW = BBCommon.screenWidth;
    }

    int maxOffsetX;
    int maxOffsetY;
    float downX;
    float downY;
    float rawX;
    float rawY;


    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        float oldH = 0;
        float newH = 0;
        //横屏
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            screenW = BBCommon.screenHeight;
            if (mParams.x > BBCommon.screenWidth / 2) {
                mParams.x = (int) screenW;
            } else {
                mParams.x = 0;
            }
            newH = BBCommon.screenWidth;
            oldH = BBCommon.screenHeight;
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            screenW = BBCommon.screenWidth;
            if (mParams.x > BBCommon.screenHeight / 2) {
                mParams.x = (int) screenW;
            } else {
                mParams.x = 0;
            }
            newH = BBCommon.screenHeight;
            oldH = BBCommon.screenWidth;
        }
        if (oldH != 0) {
            mParams.y = (int) (mParams.y / oldH * newH);
        }
        windowManager.updateViewLayout(FloatingView.this, mParams);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        int action = event.getActionMasked();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                moved = false;
                oldX = event.getRawX();
                oldY = event.getRawY();
                maxOffsetX = 0;
                maxOffsetY = 0;
                downX = event.getRawX();
                downY = event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                rawX = event.getRawX();
                rawY = event.getRawY();

                mParams.x += rawX - oldX;//相对于屏幕左上角的位置
                mParams.y += rawY - oldY;//相对于屏幕左上角的位置
                windowManager.updateViewLayout(FloatingView.this, mParams);

                maxOffsetX = (int) Math.max(maxOffsetX, Math.abs(rawX - downX));
                maxOffsetY = (int) Math.max(maxOffsetY, Math.abs(rawY - downY));
                if (maxOffsetY > touchSlop || maxOffsetX > touchSlop) {
                    moved = true;
                }
                oldX = rawX;
                oldY = rawY;
                break;
            case MotionEvent.ACTION_UP:
                oldX = event.getRawX();
                oldY = event.getRawY();

                //纵向
                if (oldX < screenW / 2) {
                    mParams.x = 0;
                } else {
                    mParams.x = (int) (screenW - getWidth());
                }
                windowManager.updateViewLayout(FloatingView.this, mParams);
                break;
        }
        return super.dispatchTouchEvent(event);
    }

    private float screenW;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (doubleClickFunDisabled || gestureDetector == null) {
            gestureDetector = null;
            return super.onTouchEvent(event);
        }
        return gestureDetector.onTouchEvent(event);
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

    GestureDetector gestureDetector;

    public HashMap<Integer, Intent> LONG_CLICK_ACTION = new HashMap<Integer, Intent>() {
        {
            put(R.id.longRG_home, homeIntent);
            put(R.id.longRG_recent, recentsIntent);
            put(R.id.longRG_disable, null);
        }
    };
    public HashMap<Integer, Intent> DOUBLE_CLICK_ACTION = new HashMap<Integer, Intent>() {
        {
            put(R.id.doubleRG_home, homeIntent);
            put(R.id.doubleRG_recent, recentsIntent);
            put(R.id.doubleRG_disable, null);
        }
    };

    public void dispatchClick(Intent intent) {
        if (moved || intent == null) {
            return;
        }
        localBroadcastManager.sendBroadcast(intent);
    }

    public boolean doubleClickFunDisabled;

    public void disableDoubleClickFun(boolean disabled) {
        doubleClickFunDisabled = disabled;
        if (disabled) {
            OnClickListener onClickListener = new OnClickListener() {
                @Override
                public void onClick(View v) {
                    dispatchClick(backIntent);
                }
            };

            setOnClickListener(onClickListener);

            OnLongClickListener onLongClickListener = new OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    dispatchClick(longClickIntent);
                    return true;
                }
            };
            setOnLongClickListener(onLongClickListener);
        } else {
            setOnClickListener(null);
            setOnLongClickListener(null);
            gestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {

                @Override
                public boolean onSingleTapConfirmed(MotionEvent e) {//单击事件
                    //表示单击，此处也可以做单击的操作
                    dispatchClick(backIntent);
                    return super.onSingleTapConfirmed(e);
                }

                @Override
                public boolean onDoubleTap(MotionEvent e) {//双击事件
                    dispatchClick(doubleClickIntent);
                    return super.onDoubleTap(e);  //此处做双击具体业务逻辑
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    super.onLongPress(e);
                    dispatchClick(longClickIntent);
                }
            });
        }
    }

    public void changeDoubleClickAction(int checkedId) {
        doubleClickIntent = DOUBLE_CLICK_ACTION.get(checkedId);
        disableDoubleClickFun(doubleClickIntent == null);
    }

    public void changeLongClickAction(int checkedId) {
        longClickIntent = LONG_CLICK_ACTION.get(checkedId);
    }

    public int getCheckedId(int tag) {
        Integer idI = null;
        switch (tag) {
            case TAG_DOUBLE: //
                idI = getKey(DOUBLE_CLICK_ACTION, doubleClickIntent);
                break;
            case TAG_LONG: //
                idI = getKey(LONG_CLICK_ACTION, longClickIntent);
                break;
        }
        return idI == null ? 0 : idI;
    }

    public static Integer getKey(HashMap<Integer, Intent> map, Intent value) {
        for (Integer key : map.keySet()) {
            if (map.get(key) == value) {
                return key;
            }
        }
        return null;
    }
}
