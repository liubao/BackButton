package com.example.liubao.backbutton.view;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.os.Build;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.AppCompatImageView;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.view.WindowManager;

import com.example.liubao.backbutton.BaseClickDataController;
import com.example.liubao.backbutton.IDataController;
import com.example.liubao.backbutton.MainApplication;
import com.example.liubao.backbutton.common.BBCommon;

/**
 * * Created by liubao on 2018/5/12.
 */

public class BBView extends AppCompatImageView {

    public static BBView instance;
    private GestureDetector gestureDetector;


    public synchronized static BBView getInstance() {
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

    public BaseClickDataController doubleClickDS;
    public BaseClickDataController longClickDS;
    public Painter painter;
    public PainterDS painterDS;

    public XYDS xds;
    public XYDS yds;

    public BBView(final Context context) {
        super(context);
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);//获得WindowManager对象
        painterDS = new PainterDS(BBCommon.SHARED_PREFERENCES_STYLE, this);
        painter = painterDS.getPainter();

        doubleClickDS = new BaseClickDataController(BBCommon.SHARED_PREFERENCES_DOUBLE);
        longClickDS = new BaseClickDataController(BBCommon.SHARED_PREFERENCES_LONG);
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


    }

    private WindowManager windowManager;
    public WindowManager.LayoutParams mParams;
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
        windowManager.addView(BBView.this, mParams);
        //样式

        updateView(painter.getWidth(), painter.getHeight());

        //交互
        gestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
            //此方法只有在双击enable情况下被触发。
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {//单击事件
                //在双击存在的情况下，这里响应单击。
                if (doubleClickDS.action != null) {
                    dispatchClick(backIntent);
                }
                return super.onSingleTapConfirmed(e);
            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                //在双击不存在的情况下，这里响应单击。
                if (doubleClickDS.action == null) {
                    dispatchClick(backIntent);
                }
                return super.onSingleTapUp(e);
            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {//双击事件
                if (doubleClickDS.action == null) {
                    dispatchClick(backIntent);
                } else {
                    dispatchClick(doubleClickDS.intent);
                }
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
        invalidate();
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
                float w;
                //纵向
                if (xds.getFromMemory()) {
                    w = BBCommon.screenWidth;
                } else {
                    w = BBCommon.screenHeight;
                }
                if (oldX > w / 2) {
                    mParams.x = (int) (w - getWidth());
                } else {
                    mParams.x = 0;
                }
                if (moved) {
                    if (xds.getValue() != mParams.x) {
                        invalidate();
                        xds.setValue(mParams.x);
                    }
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
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        painter.onSizeChanged(w, h);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        painter.onDraw(canvas);
    }

    public void updateView(int w, int h) {
        if (mParams == null) {
            return;
        }
        painter.onSizeChanged(w, h);
        mParams.width = painter.getWidth();
        mParams.height = painter.getHeight();
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
        painter.setAlpha(a);
        invalidate();
    }

    public IDataController<Integer> getSizeDS() {
        return painter.getSizeDS();
    }

    public IDataController<Integer> getAlphaDS() {
        return painter.getAlphaDS();
    }

    public void setPainter(Painter painter) {
        this.painter = painter;
        int w = getWidth() == 0 ? painter.getWidth() : getWidth();
        int h = getHeight() == 0 ? painter.getHeight() : getHeight();
        updateView(w, h);
        invalidate();
    }
}
