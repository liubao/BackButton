package com.liubao.backbutton;

import android.content.Intent;

/**
 * * Created by liubao on 2018/7/13.
 */
public class BaseClickDataController implements IDataController<String> {

    public String action;

    public Intent intent;
    public String fileKey;

    public BaseClickDataController(String fileKey) {
        intent = new Intent();
        this.fileKey = fileKey;
        action = getFromDisk();
        intent.setAction(action);
    }

    @Override
    public String getFromMemory() {
        return action;
    }

    @Override
    public String getFromDisk() {
        String key = SharedPreferencesUtils.getString(fileKey, null);
        return key;
    }


    @Override
    public void putToDisk() {
        SharedPreferencesUtils.putString(fileKey, action);
    }

    @Override
    public void set(String newValue) {
        action = newValue;
        intent.setAction(action);
    }

}
