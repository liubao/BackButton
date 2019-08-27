package com.liubao.backbutton;

import java.util.Observable;

public class ActionObservable extends Observable {

    private static volatile ActionObservable instance;

    public static ActionObservable getInstance() {
        if (instance == null) {
            synchronized (ActionObservable.class) {
                if (instance == null) {
                    instance = new ActionObservable();
                }
            }
        }
        return instance;
    }

    @Override
    public void notifyObservers() {
        super.notifyObservers();
    }

    @Override
    public void notifyObservers(Object arg) {
        setChanged();
        super.notifyObservers(arg);
    }
}
