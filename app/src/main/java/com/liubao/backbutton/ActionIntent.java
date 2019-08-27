package com.liubao.backbutton;

public class ActionIntent {
    private String action;

    public ActionIntent(String action) {
        this.action = action;
    }

    public ActionIntent() {

    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getAction() {
        return action;
    }
}
