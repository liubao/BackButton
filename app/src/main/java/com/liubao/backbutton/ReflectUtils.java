package com.liubao.backbutton;

import java.lang.reflect.Field;
import java.util.HashMap;

/**
 * * Created by liubao on 2019/1/13.
 */
public class ReflectUtils {

    public static void loopFields(Class clazz, LoopListener<Field> loopListener) {
        Field[] fields = clazz.getFields();
        if (fields == null) {
            return;
        }
        int size = fields.length;
        for (int i = 0; i < size; i++) {
            Field field = fields[i];
            if (field == null) {
                continue;
            }
            field.setAccessible(true);
            if (loopListener != null) {
                loopListener.onLoop(field, i);
            }
        }
    }

    public static HashMap<String, String> getFieldsKV(final Class clazz) {
        final HashMap<String, String> hashMap = new HashMap<>();
        ReflectUtils.loopFields(clazz, new LoopListener<Field>() {
            @Override
            public void onLoop(Field field, int index) {
                try {
                    Object object = field.get(clazz);
                    hashMap.put(field.getName(), object == null ? "" : object.toString());
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });
        return hashMap;
    }
}
