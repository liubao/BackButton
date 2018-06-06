package com.example.liubao.backbutton;

import android.Manifest;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.widget.SeekBar;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final String APP_PACKAGE_NAME = "com.example.liubao.backbutton";
    private static final String TAG = MainActivity.class.getSimpleName();
    public final static int OVERLAY_PERMISSION_REQ_CODE = 0;
    public final static int SERVICE_PERMISSION_REQ_CODE = 111;
    private static final int MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE = 222;
    private static final int MY_PERMISSIONS_INTERNET = 333;
    private SeekBar seekBar;
    private AppCompatCheckBox drawOverlaysSwitch;
    private AppCompatCheckBox accessibilityServiceSwitch;
    public static final boolean DEBUG = true;
    private String serviceName;
    private View bigV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bigV = findViewById(R.id.big);
        bigV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeCrash();
            }
        });

        serviceName = getPackageName() + "/." + "MyAccessibilityService";
        seekBar = findViewById(R.id.seek);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                FloatingView.getInstance(MainActivity.this).updateView(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        drawOverlaysSwitch = findViewById(R.id.full_screen_check);
        drawOverlaysSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean can = Settings.canDrawOverlays(MainActivity.this);
                drawOverlaysSwitch.setChecked(can);
                if (!can) {
                    openOverlaysActivity();
                }
            }
        });
        drawOverlaysSwitch.setChecked(Settings.canDrawOverlays(this));

        accessibilityServiceSwitch = findViewById(R.id.service_check);
        accessibilityServiceSwitch.setOnClickListener(new View.OnClickListener() {
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
        });
        accessibilityServiceSwitch.setChecked(isAccessibilitySettingsOn(serviceName));
        askPer();
    }

    private void makeCrash() {
        String s = null;
        s.substring(1);
    }

    public void askPer() {
        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.INTERNET);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.INTERNET},
                    MY_PERMISSIONS_INTERNET);
        }

    }


    public void openOverlaysActivity() {
        Toast.makeText(MainActivity.this, "打开浮窗权限", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:" + getPackageName()));
        startActivityForResult(intent, OVERLAY_PERMISSION_REQ_CODE);
    }

    public void openServiceActivity() {
        Toast.makeText(MainActivity.this, "打开服务权限", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
        startActivityForResult(intent, SERVICE_PERMISSION_REQ_CODE);
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

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == OVERLAY_PERMISSION_REQ_CODE) {
            drawOverlaysSwitch.setChecked(Settings.canDrawOverlays(this));
        } else if (requestCode == SERVICE_PERMISSION_REQ_CODE) {
            accessibilityServiceSwitch.setChecked(isAccessibilitySettingsOn(serviceName));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_INTERNET: {
                // If request is cancelled, the result arrays are empty.

                if (grantResults.length > 0) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    }
                } else {

                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}