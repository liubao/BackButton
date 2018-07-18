package com.example.liubao.backbutton;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.os.Build;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.AppCompatImageView;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.view.WindowManager;

/**
 * * Created by liubao on 2018/5/12.
 */

public class BBView extends AppCompatImageView {

    public static BBView instance;

    private Paint circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint paint;
    private GestureDetector gestureDetector;


    public static BBView getInstance() {
        if (instance == null) {
            instance = new BBView(MainApplication.context);
        }
        return instance;
    }

    private int touchSlop;
    private float oldY;
    private float oldX;
    private boolean moved;
    LocalBroadcastManager localBroadcastManager;

    final Intent backIntent = new Intent(BBCommon.ACTION_BACK);

    public BaseDataController doubleClickDS;
    public BaseDataController longClickDS;
    public AlphaDS alphaDS;
    public SizeDS sizeDS;
    public XYDS xds;
    public XYDS yds;

    public BBView(final Context context) {
        super(context);
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);//获得WindowManager对象
        sizeDS = new SizeDS();
        alphaDS = new AlphaDS();
        doubleClickDS = new BaseDataController(BBCommon.SHARED_PREFERENCES_DOUBLE);
        longClickDS = new BaseDataController(BBCommon.SHARED_PREFERENCES_LONG);
        Configuration configuration = getResources().getConfiguration(); //获取设置的配置信息
        boolean orientationPortrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT;

        xds = new XYDS(BBCommon.SHARED_PREFERENCES_X_PORTRAIT,
                BBCommon.SHARED_PREFERENCES_X_LANDSCAPE, BBCommon.DEFAULT_X);
        xds.set(orientationPortrait);
        yds = new XYDS(BBCommon.SHARED_PREFERENCES_Y_PORTRAIT,
                BBCommon.SHARED_PREFERENCES_Y_LANDSCAPE, BBCommon.DEFAULT_Y);
        yds.set(orientationPortrait);


        localBroadcastManager = LocalBroadcastManager.getInstance(context);
        touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.WHITE);
        paint.setAlpha(alphaDS.alpha);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(sizeDS.stokeWidth);
        paint.setPathEffect(new CornerPathEffect(4));
        circlePaint.setColor(0xFFB0C4DE);
        circlePaint.setAlpha(alphaDS.alpha);

    }

    private WindowManager windowManager;
    private WindowManager.LayoutParams mParams;
    private boolean isViewShow;

    public void show() {
        if (isViewShow) {
            return;
        }
        isViewShow = true;
        mParams = new WindowManager.LayoutParams();
        mParams.gravity = Gravity.TOP | Gravity.START;

        mParams.x = xds.getValue();
        mParams.y = yds.getValue();

        //总是出现在应用程序窗口之上
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            mParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        }
        //设置图片格式，效果为背景透明
        mParams.format = PixelFormat.RGBA_8888;
        mParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        mParams.width = sizeDS.width;
        mParams.height = sizeDS.height;
        windowManager.addView(BBView.this, mParams);

        //样式


        //交互
        gestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
            //此方法只有在双击enable情况下被触发。
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {//单击事件
                //在双击存在的情况下，这里响应单击。
                if (doubleClickDS.intent != null) {
                    dispatchClick(backIntent);
                }
                return super.onSingleTapConfirmed(e);
            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                //在双击不存在的情况下，这里响应单击。
                if (doubleClickDS.intent == null) {
                    dispatchClick(backIntent);
                }
                return super.onSingleTapUp(e);
            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {//双击事件
                dispatchClick(doubleClickDS.intent);
                return super.onDoubleTap(e);  //此处做双击具体业务逻辑
            }

            @Override
            public void onLongPress(MotionEvent e) {
                super.onLongPress(e);
                dispatchClick(longClickDS.intent);
            }
        });
    }

    int maxOffsetX;
    int maxOffsetY;
    float downX;
    float downY;
    float rawX;
    float rawY;


    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        //横屏
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            xds.set(false);
            yds.set(false);
            mParams.x = xds.getValue();
            mParams.y = yds.getValue();
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            xds.set(true);
            yds.set(true);
            mParams.x = xds.getValue();
            mParams.y = yds.getValue();
        }

        windowManager.updateViewLayout(BBView.this, mParams);
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
                windowManager.updateViewLayout(BBView.this, mParams);

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
                if (xds.getFromMemory()) {
                    if (oldX > BBCommon.screenWidth / 2) {
                        mParams.x = (int) (BBCommon.screenWidth - getWidth());
                    } else {
                        mParams.x = 0;
                    }
                } else {
                    if (oldX > BBCommon.screenHeight / 2) {
                        mParams.x = (int) (BBCommon.screenHeight - getWidth());
                    } else {
                        mParams.x = 0;
                    }
                }
                if (moved) {
                    xds.setValue(mParams.x);
                    yds.setValue(mParams.y);
                }
                windowManager.updateViewLayout(BBView.this, mParams);
                break;
        }
        return super.dispatchTouchEvent(event);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        int w = getWidth();
        int h = getHeight();
        sizeDS.onLayout(w, h);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(sizeDS.width / 2f, sizeDS.height / 2f,
                sizeDS.width / 2f, circlePaint);
        canvas.drawPath(sizeDS.path, paint);
    }

    public void updateView(int w) {
        if (mParams == null) {
            return;
        }
        sizeDS.set(w);
        mParams.width = sizeDS.width;
        mParams.height = sizeDS.height;
        windowManager.updateViewLayout(BBView.this, mParams);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (gestureDetector == null) {
            return super.onTouchEvent(event);
        }
        return gestureDetector.onTouchEvent(event);
    }


    public void dispatchClick(Intent intent) {
        if (moved || intent == null) {
            return;
        }
        localBroadcastManager.sendBroadcast(intent);
    }

    public void updateAlpha(int a) {
        alphaDS.set(a);
        paint.setAlpha(alphaDS.alpha);
        circlePaint.setAlpha(alphaDS.alpha);
        invalidate();
    }

}
