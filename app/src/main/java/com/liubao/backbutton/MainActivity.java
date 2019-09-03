package com.liubao.backbutton;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.os.Debug;
import android.provider.Settings;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatCheckBox;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.liubao.backbutton.common.BBCommon;
import com.liubao.backbutton.event.EventCode;
import com.liubao.backbutton.event.EventController;
import com.liubao.backbutton.utils.DensityUtil;
import com.liubao.backbutton.view.AlphaDS;
import com.liubao.backbutton.view.BBView;
import com.liubao.backbutton.view.LinePainter;
import com.liubao.backbutton.view.SizeDS;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    public final static int PERMISSION_REQ_CODE_OVERLAY = 111;
    public final static int PERMISSION_REQ_CODE_SERVICE = 222;

    private AppCompatCheckBox drawOverlaysSwitch;
    private AppCompatCheckBox accessibilityServiceSwitch;
    private Resources resources;
    private boolean hasPermission;
    private TextView sizeSeekBarHint;
    private SeekBar sizeSeekBar;
    private TextView alphaSeekBarHint;
    private SeekBar alphaSeekBar;
    private TextView doubleFunHint;
    private RadioGroup doubleRG;
    private TextView longFunHint;
    private RadioGroup longRG;
    private RadioGroup styleRG;
    private TextView styleHint;
    private HashMap<View, Float> viewHM = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initTest();
        resources = getResources();
        hasPermission = initPermissionCheckBox();
        sizeSeekBarHint = findViewById(R.id.size_hint);
        sizeSeekBar = findViewById(R.id.size_seek);
        alphaSeekBarHint = findViewById(R.id.alpha_hint);
        alphaSeekBar = findViewById(R.id.alpha_seek);
        doubleFunHint = findViewById(R.id.doubleHint);
        doubleRG = findViewById(R.id.doubleRG);
        longFunHint = findViewById(R.id.longHint);
        longRG = findViewById(R.id.longRG);
        styleRG = findViewById(R.id.styleRG);
        styleHint = findViewById(R.id.styleHint);
        float s = 1;
        float a = 0.2f;
        viewHM.put(sizeSeekBarHint, s);
        viewHM.put(sizeSeekBar, s);
        viewHM.put(alphaSeekBarHint, s + a * 1);
        viewHM.put(alphaSeekBar, s + a * 1);
        viewHM.put(doubleFunHint, s + a * 2);
        viewHM.put(doubleRG, s + a * 2);
        viewHM.put(longFunHint, s + a * 3);
        viewHM.put(longRG, s + a * 3);
        viewHM.put(styleHint, s + a * 4);
        viewHM.put(styleRG, s + a * 4);
        initSeekBar();
        initFunction();
        initVersionHint();
        initHideMode();
        for (final View view : viewHM.keySet()) {
            view.setVisibility(View.INVISIBLE);
        }
        if (hasPermission) {
            setFunctionViewVisibility(View.VISIBLE);
        }
        initGoogleAD();
    }

    private void initGoogleAD() {
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {

            }
        });
        AdView adView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }

    private void initTest() {
        TextView textView = findViewById(R.id.test);
        if (!BBCommon.DEBUG) {
            textView.setVisibility(View.GONE);
            return;
        }
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventController.send(EventCode.APP_LAUNCHED);
            }
        });

    }

    private void initHideMode() {
        styleRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                BBView.getInstance().painterDS.set(
                        checkedId == R.id.lineStyle ? BBCommon.STYLE_LINE : BBCommon.STYLE_STAR);
                BBView.getInstance().setPainter(BBView.getInstance().painterDS.getPainter());
            }
        });
        styleRG.check(BBView.getInstance().painter instanceof LinePainter ? R.id.lineStyle : R.id.roundStyle);
    }


    public void setFunctionViewVisibility(final int visibility) {
        int startAlpha = visibility == View.VISIBLE ? 0 : 1;
        int endAlpha = visibility == View.VISIBLE ? 1 : 0;
        Interpolator interpolator = new LinearInterpolator();
        int d = 300;
        for (final View view : viewHM.keySet()) {
            final AlphaAnimation alphaAnimation = new AlphaAnimation(startAlpha, endAlpha);
            alphaAnimation.setFillAfter(true);
            alphaAnimation.setDuration(d);
            alphaAnimation.setAnimationListener(new SimpleAnimationListener() {
                @Override
                public void onAnimationEnd(Animation animation) {
                    view.setVisibility(visibility);
                }
            });
            alphaAnimation.setInterpolator(interpolator);
            view.postDelayed(new Runnable() {
                @Override
                public void run() {
                    view.startAnimation(alphaAnimation);
                }
            }, (long) (viewHM.get(view) * d));
        }
    }

    private boolean initPermissionCheckBox() {
        View.OnClickListener permissionClickLis = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == drawOverlaysSwitch) {
                    boolean canDrawOverlays = Settings.canDrawOverlays(MainActivity.this);
                    drawOverlaysSwitch.setChecked(canDrawOverlays);
                    openOverlaysActivity();
                }
                if (v == accessibilityServiceSwitch) {
                    boolean canDrawOverlays = Settings.canDrawOverlays(MainActivity.this);
                    if (!canDrawOverlays) {
                        drawOverlaysSwitch.setChecked(canDrawOverlays);
                        openOverlaysActivity();
                        return;
                    }
                    boolean isOn = Utils.isAccessibilitySettingsOn(MainActivity.this, BBCommon.serviceName);
                    accessibilityServiceSwitch.setChecked(isOn);
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
        return drawOverlaysSwitch.isChecked() && accessibilityServiceSwitch.isChecked();
    }

    private void initSeekBar() {
        sizeSeekBar.setProgress(BBView.getInstance().getSizeDS().getFromDisk() * sizeSeekBar.getMax()
                / SizeDS.DEFAULT_MAX_WIDTH
        );
        sizeSeekBar.setOnSeekBarChangeListener(new SimpleOnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    int w = progress * SizeDS.DEFAULT_MAX_WIDTH / sizeSeekBar.getMax();
                    BBView.getInstance().updateView(w, w);
                }
            }
        });
        alphaSeekBar.setProgress(BBView.getInstance().getAlphaDS().getFromDisk()
                * alphaSeekBar.getMax() / AlphaDS.MAX_ALPHA);
        alphaSeekBar.setOnSeekBarChangeListener(new SimpleOnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    BBView.getInstance().updateAlpha(AlphaDS.MAX_ALPHA * progress
                            / alphaSeekBar.getMax());
                }
            }
        });
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

    @Override
    protected void onStop() {
        super.onStop();
        BBView.getInstance().doubleClickDS.putToDisk();
        BBView.getInstance().longClickDS.putToDisk();
        BBView.getInstance().getAlphaDS().putToDisk();
        BBView.getInstance().getSizeDS().putToDisk();
        BBView.getInstance().xds.putToDisk();
        BBView.getInstance().yds.putToDisk();
        BBView.getInstance().painterDS.putToDisk();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void initFunction() {
        String doubleIntent = BBView.getInstance().doubleClickDS.getFromDisk();
        doubleRG.check(findKey(DOUBLE_HASH_MAP, doubleIntent));
        doubleRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                BBView.getInstance().doubleClickDS.set(DOUBLE_HASH_MAP.get(checkedId));
            }
        });

        String longIntent = BBView.getInstance().longClickDS.getFromDisk();
        longRG.check(findKey(LONG_HASH_MAP, longIntent));
        longRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                BBView.getInstance().longClickDS.set(LONG_HASH_MAP.get(checkedId));
            }
        });
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
        Toast.makeText(MainActivity.this, "打开浮窗权限", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:" + getPackageName()));
        startActivityForResult(intent, PERMISSION_REQ_CODE_OVERLAY);
    }

    public void openServiceActivity() {
        Toast.makeText(MainActivity.this, "打开服务权限", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
        startActivityForResult(intent, PERMISSION_REQ_CODE_SERVICE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        boolean canDraw = Settings.canDrawOverlays(this);
        drawOverlaysSwitch.setChecked(canDraw);
        boolean isAccessibilitySettingsOn = Utils.isAccessibilitySettingsOn(this,
                BBCommon.serviceName);
        hasPermission = canDraw && isAccessibilitySettingsOn;
        accessibilityServiceSwitch.setChecked(isAccessibilitySettingsOn);
        if (hasPermission) {
            setFunctionViewVisibility(View.VISIBLE);
        } else {
            setFunctionViewVisibility(View.INVISIBLE);
        }
    }
}