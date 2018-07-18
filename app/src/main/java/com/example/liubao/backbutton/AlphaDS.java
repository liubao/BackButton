package com.example.liubao.backbutton;

/**
 * * Created by liubao on 2018/7/16.
 */
public class AlphaDS implements IDataController<Integer> {
    public static final int DEFAULT_ALPHA = 125;
    public static final int MAX_ALPHA = 255;

    public int alpha;

    public AlphaDS() {
        int a = getFromDisk();
        if (a <= 0) {
            a = DEFAULT_ALPHA;
        }
        alpha = a;
    }


    @Override
    public Integer getFromMemory() {
        return alpha;
    }

    @Override
    public Integer getFromDisk() {
        Integer key = SharedPreferencesUtils.getInt(BBCommon.SHARED_PREFERENCES_ALPHA, DEFAULT_ALPHA);
        return key;
    }

    @Override
    public void set(Integer p) {
        int a = p == null ? 125 : p;
        alpha = Math.min(255, Math.max(0, a));
    }

    @Override
    public void putToDisk() {
        SharedPreferencesUtils.putInt(BBCommon.SHARED_PREFERENCES_ALPHA, alpha);
    }

}
