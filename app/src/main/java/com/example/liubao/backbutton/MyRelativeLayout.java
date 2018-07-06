package com.example.liubao.backbutton;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

/**
 * * Created by liubao on 2018/7/6.
 */
public class MyRelativeLayout extends BaseRelativeLayout {

    @Override
    public void init() {
        super.init();
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);

    }

    public MyRelativeLayout(Context context) {
        super(context);
    }

    public MyRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


}
