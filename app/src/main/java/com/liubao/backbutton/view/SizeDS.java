package com.liubao.backbutton.view;

import com.liubao.backbutton.IDataController;
import com.liubao.backbutton.SharedPreferencesUtils;
import com.liubao.backbutton.common.BBCommon;

/**
 * * Created by liubao on 2018/7/16.
 */
public class SizeDS implements IDataController<Integer> {
    public static final int DEFAULT_MAX_WIDTH = (int) (BBCommon.screenWidth / 2);
    public int width;
    public int height;

    public SizeDS() {
        int w = getFromDisk();
        if (w <= 0) {
            w = DEFAULT_MAX_WIDTH;
        }
        width = w;
        height = w;
    }

    @Override
    public Integer getFromMemory() {
        return width;
    }

    @Override
    public Integer getFromDisk() {
        Integer key = SharedPreferencesUtils.getInt(BBCommon.SHARED_PREFERENCES_SIZE, DEFAULT_MAX_WIDTH / 2);
        return key;
    }

    @Override
    public void set(Integer w) {
        width = w == null ? DEFAULT_MAX_WIDTH : w;
        height = width;
    }

    @Override
    public void putToDisk() {
        SharedPreferencesUtils.putInt(BBCommon.SHARED_PREFERENCES_SIZE, width);
    }

}
