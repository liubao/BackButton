package com.example.liubao.backbutton.view;

import com.example.liubao.backbutton.IDataController;

/**
 * * Created by liubao on 2018/7/18.
 */
public class XYDS implements IDataController<Boolean> {
    private boolean orientationPortrait;
    public XYBaseDS portraitDS;
    public XYBaseDS landscapeDS;

    public XYDS(String sharedPreferencesPortrait, String sharedPreferencesLandscape, int d) {
        portraitDS = new XYBaseDS(sharedPreferencesPortrait, d);
        landscapeDS = new XYBaseDS(sharedPreferencesLandscape, d);
    }

    @Override
    public Boolean getFromMemory() {
        return orientationPortrait;
    }

    @Override
    public Boolean getFromDisk() {
        return null;
    }

    @Override
    public void set(Boolean newValue) {
        orientationPortrait = newValue;
    }

    @Override
    public void putToDisk() {
        portraitDS.putToDisk();
        landscapeDS.putToDisk();
    }

    public void setValue(int v) {
        if (orientationPortrait) {
            portraitDS.set(v);
        } else {
            landscapeDS.set(v);
        }
    }

    public int getValue() {
        return orientationPortrait ? portraitDS.value : landscapeDS.value;
    }

}
