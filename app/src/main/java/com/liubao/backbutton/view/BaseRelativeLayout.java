package com.liubao.backbutton.view;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * * Created by liubao on 2018/7/6.
 */
public class BaseRelativeLayout extends RelativeLayout {
    public Context context;
    public Resources resources;

    public void init() {
        context = getContext();
        resources = getResources();
    }

    public BaseRelativeLayout(Context context) {
        super(context);
        init();
    }

    public BaseRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BaseRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
}
