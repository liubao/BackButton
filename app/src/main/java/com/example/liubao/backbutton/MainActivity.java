package com.example.liubao.backbutton;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatCheckBox;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final String APP_PACKAGE_NAME = "com.example.liubao.backbutton";
    private static final String TAG = MainActivity.class.getSimpleName();
    public final static int PERMISSION_REQ_CODE_OVERLAY = 111;
    public final static int PERMISSION_REQ_CODE_SERVICE = 222;

    private AppCompatCheckBox drawOverlaysSwitch;
    private AppCompatCheckBox accessibilityServiceSwitch;
    public static final boolean DEBUG = true;
    private String serviceName;
    private Resources resources;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        resources = getResources();

        serviceName = getPackageName() + "/." + "MyAccessibilityService";

        initPermissionCheckBox();
        initSeekBar();
        initFunction();
        initVersionHint();
    }

    private void initPermissionCheckBox() {
        View.OnClickListener permissionClickLis = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isOn = isAccessibilitySettingsOn(serviceName);
                accessibilityServiceSwitch.setChecked(isOn);
                if (!Settings.canDrawOverlays(MainActivity.this)) {
                    openOverlaysActivity();
                    return;
                }
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
        accessibilityServiceSwitch.setChecked(isAccessibilitySettingsOn(serviceName));
    }

    private void initSeekBar() {
        SeekBar sizeSeekBar = findViewById(R.id.size_seek);
        sizeSeekBar.setOnSeekBarChangeListener(new SimpleOnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                FloatingView.getInstance().updateView(progress);
            }
        });
        SeekBar alphaSeekBar = findViewById(R.id.alpha_seek);
        alphaSeekBar.setOnSeekBarChangeListener(new SimpleOnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                FloatingView.getInstance().updateAlpha(progress);
            }
        });
    }

    private void initFunction() {
        RadioGroup doubleRG = findViewById(R.id.doubleRG);
        doubleRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                FloatingView.getInstance().changeDoubleClickAction(checkedId);
                FloatingView.getInstance().putCheckedIdToFile(FloatingView.TAG_DOUBLE);

            }
        });
        int doubleCheckedId = FloatingView.getInstance().getCheckedIdFromFile(FloatingView.TAG_DOUBLE);
        if (doubleCheckedId == 0) {
            doubleCheckedId = FloatingView.getInstance().getCheckedId(FloatingView.TAG_DOUBLE);
        }
        doubleRG.check(doubleCheckedId);

        RadioGroup longRG = findViewById(R.id.longRG);
        longRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                FloatingView.getInstance().changeLongClickAction(checkedId);
                FloatingView.getInstance().putCheckedIdToFile(FloatingView.TAG_LONG);
            }
        });
        int longCheckedId = FloatingView.getInstance().getCheckedIdFromFile(FloatingView.TAG_LONG);
        if (longCheckedId == 0) {
            longCheckedId = FloatingView.getInstance().getCheckedId(FloatingView.TAG_LONG);
        }
        longRG.check(longCheckedId);
    }

    private void initVersionHint() {
        TextView hintTV = findViewById(R.id.hint);
        SpannableBuilder spannableBuilder = new SpannableBuilder();
        spannableBuilder.append("当前版本", new AbsoluteSizeSpan(DensityUtil.dip2px(13)));
        spannableBuilder.append("v" + BBCommon.versionName, new AbsoluteSizeSpan(DensityUtil.dip2px(13)),
                new ForegroundColorSpan(resources.getColor(R.color.colorAccent)));
        hintTV.setText(spannableBuilder.build());
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

    private boolean isAccessibilitySettingsOn(String accessibilityServiceName) {
        AccessibilityManager manager = (AccessibilityManager) getSystemService(ACCESSIBILITY_SERVICE);
        if (manager == null) {
            return false;
        }
        List<AccessibilityServiceInfo> list = manager.getEnabledAccessibilityServiceList(
                AccessibilityServiceInfo.FEEDBACK_ALL_MASK);
        for (int i = 0; i < list.size(); i++) {
            AccessibilityServiceInfo accessibilityServiceInfo = list.get(i);
            if (accessibilityServiceInfo == null) {
                continue;
            }
            if (accessibilityServiceName.equals(accessibilityServiceInfo.getId())) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        boolean canDraw = Settings.canDrawOverlays(this);
        drawOverlaysSwitch.setChecked(canDraw);
        accessibilityServiceSwitch.setChecked(canDraw && isAccessibilitySettingsOn(serviceName));
    }

}