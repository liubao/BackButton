package com.example.liubao.backbutton;

/**
 * * Created by liubao on 2018/6/8.
 */
public class BBCommon {
    public static final String ACTION_BACK = "b";
    public static final String ACTION_HOME = "h";
    public static final String ACTION_RECENT = "r";

    public static final String SHARED_PREFERENCES_PREFIX = "s_p_";

    public static final String SHARED_PREFERENCES_DOUBLE = SHARED_PREFERENCES_PREFIX + "double";
    public static final String SHARED_PREFERENCES_LONG = SHARED_PREFERENCES_PREFIX + "long";
    public static final String SHARED_PREFERENCES_ALPHA = SHARED_PREFERENCES_PREFIX + "alpha";
    public static final String SHARED_PREFERENCES_SIZE = SHARED_PREFERENCES_PREFIX + "size";
    public static final String SHARED_PREFERENCES_X = SHARED_PREFERENCES_PREFIX + "x";
    public static final String SHARED_PREFERENCES_Y = SHARED_PREFERENCES_PREFIX + "y";


    public static final boolean DEBUG = false;
    public static String serviceName;
    public static float screenWidth;
    public static float screenHeight;
    public static String versionName;
    public static int versionCode;
}
