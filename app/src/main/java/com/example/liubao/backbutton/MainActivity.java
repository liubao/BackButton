package com.example.liubao.backbutton;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatCheckBox;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    public final static int PERMISSION_REQ_CODE_OVERLAY = 111;
    public final static int PERMISSION_REQ_CODE_SERVICE = 222;

    private AppCompatCheckBox drawOverlaysSwitch;
    private AppCompatCheckBox accessibilityServiceSwitch;
    private Resources resources;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        resources = getResources();
        initPermissionCheckBox();
        initSeekBar();
        initFunction();
        initVersionHint();
    }

    private void initPermissionCheckBox() {
        View.OnClickListener permissionClickLis = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Settings.canDrawOverlays(MainActivity.this)) {
                    openOverlaysActivity();
                    return;
                }
                boolean isOn = Utils.isAccessibilitySettingsOn(MainActivity.this, BBCommon.serviceName);
                accessibilityServiceSwitch.setChecked(isOn);
                if (!isOn) {
                    openServiceActivity();
                }
            }
        };
        drawOverlaysSwitch = findViewById(R.id.full_screen_check);
        drawOverlaysSwitch.setOnClickListener(permissionClickLis);
        drawOverlaysSwitch.setChecked(Settings.canDrawOverlays(this));

        accessibilityServiceSwitch = findViewById(R.id.service_check);
        accessibilityServiceSwitch.setOnClickListener(permissionClickLis);
        accessibilityServiceSwitch.setChecked(Utils.isAccessibilitySettingsOn(this, BBCommon.serviceName));
    }

    private void initSeekBar() {
        final SeekBar sizeSeekBar = findViewById(R.id.size_seek);
        sizeSeekBar.setOnSeekBarChangeListener(new SimpleOnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    int w = progress * SizeDS.DEFAULT_MAX_WIDTH / sizeSeekBar.getMax();
                    BBView.getInstance().updateView(w);
                }
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                BBView.getInstance().sizeDS.putToDisk();
            }
        });
        sizeSeekBar.setProgress(BBView.getInstance().sizeDS.getFromDisk() * sizeSeekBar.getMax()
                / SizeDS.DEFAULT_MAX_WIDTH
        );


        final SeekBar alphaSeekBar = findViewById(R.id.alpha_seek);
        alphaSeekBar.setOnSeekBarChangeListener(new SimpleOnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    BBView.getInstance().updateAlpha(AlphaDS.MAX_ALPHA * progress
                            / alphaSeekBar.getMax());
                }
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                BBView.getInstance().alphaDS.putToDisk();
            }
        });
        alphaSeekBar.setProgress(BBView.getInstance().alphaDS.getFromDisk()
                * alphaSeekBar.getMax() / AlphaDS.MAX_ALPHA);
    }

    public static final HashMap<Integer, String> DOUBLE_HASH_MAP = new HashMap<Integer, String>() {
        {
            put(R.id.doubleRG_home, BBCommon.ACTION_HOME);
            put(R.id.doubleRG_recent, BBCommon.ACTION_RECENT);
            put(R.id.doubleRG_disable, null);
        }
    };
    public static final HashMap<Integer, String> LONG_HASH_MAP = new HashMap<Integer, String>() {
        {
            put(R.id.longRG_home, BBCommon.ACTION_HOME);
            put(R.id.longRG_recent, BBCommon.ACTION_RECENT);
            put(R.id.longRG_disable, null);
        }
    };


    private void initFunction() {
        RadioGroup doubleRG = findViewById(R.id.doubleRG);
        doubleRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                BBView.getInstance().doubleClickDS.set(DOUBLE_HASH_MAP.get(checkedId));
            }
        });
        String doubleIntent = BBView.getInstance().doubleClickDS.getFromDisk();
        doubleRG.check(findKey(DOUBLE_HASH_MAP, doubleIntent));

        RadioGroup longRG = findViewById(R.id.longRG);
        longRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                BBView.getInstance().longClickDS.set(LONG_HASH_MAP.get(checkedId));
            }
        });
        String longIntent = BBView.getInstance().longClickDS.getFromDisk();
        longRG.check(findKey(LONG_HASH_MAP, longIntent));
    }

    private void initVersionHint() {
        TextView hintTV = findViewById(R.id.hint);
        SpannableBuilder spannableBuilder = new SpannableBuilder();
        spannableBuilder.append("当前版本", new AbsoluteSizeSpan(DensityUtil.dip2px(13)));
        spannableBuilder.append("v" + BBCommon.versionName, new AbsoluteSizeSpan(DensityUtil.dip2px(13)),
                new ForegroundColorSpan(resources.getColor(R.color.colorAccent)));
        hintTV.setText(spannableBuilder.build());
    }

    public int findKey(HashMap<Integer, String> map, String value) {
        for (Integer key : map.keySet()) {
            if (TextUtils.equals(map.get(key), value)) {
                return key;
            }
        }
        return 0;
    }

    public void openOverlaysActivity() {
        Toast.makeText(MainActivity.this, "打开浮窗权限", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:" + getPackageName()));
        startActivityForResult(intent, PERMISSION_REQ_CODE_OVERLAY);
    }

    public void openServiceActivity() {
        Toast.makeText(MainActivity.this, "打开服务权限", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
        startActivityForResult(intent, PERMISSION_REQ_CODE_SERVICE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        boolean canDraw = Settings.canDrawOverlays(this);
        drawOverlaysSwitch.setChecked(canDraw);
        accessibilityServiceSwitch.setChecked(canDraw && Utils.isAccessibilitySettingsOn(this,
                BBCommon.serviceName));
    }

}