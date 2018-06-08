package com.example.liubao.backbutton;

import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;

/**
 * * Created by liubao on 2018/6/8.
 */
public class SpannableBuilder {
    SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();

    public SpannableBuilder() {
    }

    public void append(String s, Object... o) {
        if (TextUtils.isEmpty(s) || o == null) {
            return;
        }
        int length = spannableStringBuilder.length();
        spannableStringBuilder.append(s);
        int size = o.length;
        for (int i = 0; i < size; i++) {
            Object object = o[i];
            if (object == null) {
                continue;
            }
            spannableStringBuilder.setSpan(object, length, length + s.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        }
    }

    public void append(String s) {
        if (TextUtils.isEmpty(s)) {
            return;
        }
        spannableStringBuilder.append(s);
    }

    public SpannableStringBuilder build() {
        return spannableStringBuilder;
    }
}
