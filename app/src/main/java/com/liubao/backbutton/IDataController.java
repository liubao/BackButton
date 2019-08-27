package com.liubao.backbutton;

/**
 * * Created by liubao on 2018/7/13.
 */
public interface IDataController<T> {

    T getFromMemory();

    T getFromDisk();

    void set(T newValue);

    void putToDisk();
}
