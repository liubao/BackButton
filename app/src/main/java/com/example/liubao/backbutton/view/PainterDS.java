package com.example.liubao.backbutton.view;

import android.text.TextUtils;

import com.example.liubao.backbutton.IDataController;
import com.example.liubao.backbutton.SharedPreferencesUtils;
import com.example.liubao.backbutton.common.BBCommon;

/**
 * * Created by liubao on 2018/8/17.
 */
class PainterDS implements IDataController<String> {

    public String style;

    public String fileKey;
    public BBView bbView;

    public PainterDS(String fileKey) {
        this.fileKey = fileKey;
        style = getFromDisk();
    }

    public PainterDS(String fileKey, BBView bbView) {
        this.fileKey = fileKey;
        this.bbView = bbView;
        style = getFromDisk();
    }

    public Painter getPainter() {
        if (TextUtils.equals(style, BBCommon.STYLE_STAR)) {
            return new StarPainter();
        } else if (TextUtils.equals(style, BBCommon.STYLE_LINE)) {
            return new LinePainter(bbView);
        }
        return new StarPainter();
    }

    @Override
    public String getFromMemory() {
        return style;
    }

    @Override
    public String getFromDisk() {
        String key = SharedPreferencesUtils.getString(fileKey, null);
        return key;
    }


    @Override
    public void putToDisk() {
        SharedPreferencesUtils.putString(fileKey, style);
    }

    @Override
    public void set(String newValue) {
        style = newValue;
    }

}
