package com.example.liubao.backbutton;

/**
 * * Created by liubao on 2018/7/18.
 */
public class XYBaseDS implements IDataController<Integer> {

    private final String fileKey;
    public Integer value;


    public XYBaseDS(String fileKey) {
        this.fileKey = fileKey;
        value = getFromDisk();
    }

    @Override
    public Integer getFromMemory() {
        return value;
    }

    @Override
    public Integer getFromDisk() {
        Integer key = SharedPreferencesUtils.getInt(fileKey, 0);
        return key;
    }


    @Override
    public void putToDisk() {
        SharedPreferencesUtils.putInt(fileKey, value);
    }

    @Override
    public void set(Integer newValue) {
        value = newValue;
        putToDisk();
    }

}