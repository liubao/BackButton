package com.example.liubao.backbutton.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import com.example.liubao.backbutton.AndroidIdUtil;
import com.example.liubao.backbutton.event.ExtraGenerator;

import java.util.ArrayList;

/**
 * * Created by liubao on 2019/1/13.
 */
public class OpenUDID {
    private final static String TAG = OpenUDID.class.getSimpleName();
    private final static String PREF_KEY = "openudid";
    private final static String PREFS_NAME = "openudid_prefs";

    private static String _openUdid;
    private final static boolean _UseImeiFailback = false;// false if you don't wanna include READ_PHONE_STATE permission
    private final static boolean _UseBlueToothFailback = false; // false if you don't wanna include BT permission or android 1.6
    private final static boolean LOG = true; //Display or not debug message

    private final static ArrayList<String> blackList = new ArrayList<>();

    static {
        blackList.add("02:00:00:00:00:00");
        blackList.add("00:02:00:00:00:00");
        blackList.add("01:23:45:67:89:ab");
        blackList.add("ff:ff:ff:ff:ff:ff");
    }

    private static void _debugLog(String msg) {
        if (LOG) {
            Log.d(TAG, msg);
        }
    }

    public static void syncContext(Context ctx, ExtraGenerator extra) {
        if (TextUtils.isEmpty(_openUdid)) {
            SharedPreferences pref = ctx.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            String _keyInPref = pref.getString(PREF_KEY, null);

            if ("02:00:00:00:00:00".equals(_keyInPref)) {
                String uuid = null;
                if (extra != null) {
                    uuid = extra.invalidOpenUDID(ctx);
                }
                if (!TextUtils.isEmpty(uuid)) {
                    _openUdid = uuid;
                    SharedPreferences.Editor e = pref.edit();
                    e.putString(PREF_KEY, uuid);
                    e.apply();
                }
            } else if (TextUtils.isEmpty(_keyInPref) || _keyInPref.startsWith("00:00:00:00") || inBlackList(_keyInPref)) {
                generateOpenUDIDInContext(ctx);
                SharedPreferences.Editor e = pref.edit();
                e.putString(PREF_KEY, _openUdid);
                e.apply();
            } else {
                _openUdid = _keyInPref;
            }
        }
    }

    public static String getOpenUDIDInContext() {
        if (_openUdid != null) {
            if (_openUdid.contains("\\x3a")) {
                return _openUdid.replaceAll("\\x3a", ":");
            } else if (_openUdid.contains("\\x3A")) {
                return _openUdid.replaceAll("\\x3A", ":");
            }
        }
        return _openUdid;
    }

    /*
     * Generate a new OpenUDID
     */
    private static void generateOpenUDIDInContext(Context context) {
        _debugLog("Generating openUDID");

        //Try to get WIFI MAC
        _openUdid = WifiIdUtil.generateWifiId(context);
        if (!TextUtils.isEmpty(_openUdid) && !_openUdid.startsWith("00:00:00:00:00") && !_openUdid.startsWith(":::") && !inBlackList(_openUdid)) {
            return;
        }
        _openUdid = null;

        //Try to get the ANDROID_ID
        String _androidId = AndroidIdUtil.getAndroidId(context);
        if (!TextUtils.isEmpty(_androidId) && !_androidId.startsWith("000000000000") && _androidId.length() > 14 && !_androidId.equals("9774d56d682e549c")/*android 2.2*/) {
            _openUdid = _androidId;
            return;
        }

        if (_UseImeiFailback) {
            _openUdid = null;
            _openUdid = ImeiIdUtil.generateImeiId(context);

            if (!TextUtils.isEmpty(_openUdid) && !_openUdid.startsWith("0000000000") && !_openUdid.startsWith(":::")) {
                return;
            }
        }

        _openUdid = null;
        _openUdid = RandomIdUtil.generateRandomId();

        _debugLog(_openUdid);
        _debugLog("done");
    }

    private static boolean inBlackList(String id) {
        boolean result = false;

        if (!TextUtils.isEmpty(id)) {
            for (String s : blackList) {
                if (id.equals(s)) {
                    result = true;
                    break;
                }
            }
        }

        return result;
    }

}
