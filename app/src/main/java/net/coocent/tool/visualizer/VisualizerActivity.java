package net.coocent.tool.visualizer;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.provider.MediaStore;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.jasonjan.animdemo.R;

import net.coocent.tool.visualizer.ui.BgButton;
import net.coocent.tool.visualizer.ui.BgColorStateList;
import net.coocent.tool.visualizer.ui.ColorDrawable;
import net.coocent.tool.visualizer.ui.CustomContextMenu;
import net.coocent.tool.visualizer.ui.TextIconDrawable;
import net.coocent.tool.visualizer.ui.UI;
import net.coocent.tool.visualizer.util.MainHandler;


/**
 * start VisualizerActivity时需要传入两个参数
 * <p/>
 * VISUALIZER_TYPE   需要展示的频谱图的效果的类型
 * ----TYPE_SPECTRUM
 * ----TYPE_LIQUID
 * ----TYPE_SPIN
 * ----TYPE_PARTICLE
 * ----TYPE_IMMERSIVE_PARTICLE
 * ----TYPE_IMMERSIVE_PARTICLE_VR
 * ----TYPE_CLASSIC
 * <p/>
 * AUDIOSESSION_ID  MediaPlayer的audiosession_id 默认为-1(没频谱)
 */

/**
 * 频谱展示活动页
 */
public class VisualizerActivity extends Activity implements View.OnClickListener, MenuItem.OnMenuItemClickListener {

    /**
     * 展示消息代号
     */
    public static final int MSG_WHAT_SHOW = 0x0007;
    /**
     * 隐藏消息代号
     */
    public static final int MSG_WHAT_HIDE = 0x0008;
    /**
     *  控制操作栏的隐藏延迟时间
     */
    public static final int HIDE_DELAY_TIME = 3000;
    /**
     * 内容区域，顶层布局
     */
    private RelativeLayout contentLayout;
    /**
     * 控制面板
     */
    private RelativeLayout panelLayout;
    //    private View lock_layout, unlock_visualizer_btn;

    /**
     * 更多按钮
     */
    private BgButton moreBtn;
    /**
     * 返回按钮
     */
    private BgButton backBtn;
    /**
     * 标题和艺术家字样
     */
    private TextView title, artist;
    /**
     * 左侧按钮
     */
    private ImageButton leftBtn;
    /**
     * 右侧按钮
     */
    private ImageButton rightBtn;
    /**
     * 暂停和下一首歌曲按钮
     */
    private ImageView pauseBtn, nextBtn;
    /**
     * 频谱抽象类
     */
    private Visualizer visualizer;
    /**
     * 频谱伴随时间改变的封装类
     */
    private FxVisualizer fxVisualizer;
    /**
     * 请求水平展示
     */
    int requiredOrientation;
    /**
     * 是否需要隐藏控制面板
     */
    boolean visualizerRequiresHiddenControls;
    /**
     * 频谱是否暂停
     */
    boolean visualizerPaused;
    /**
     * 音乐素材ID
     */
    private int audioID;
    /**
     * 类型数组
     */
    private int[] types;
    /**
     * 是否完成改变频谱
     */
    private boolean done = true;

//    private IMediaPlaybackService mService = null;

//    private MusicUtils.ServiceToken mToken;


    /**
     * 控制OperateBar的延迟显示与延迟隐藏
     */
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_WHAT_SHOW:
                    panelLayout.setVisibility(View.VISIBLE);
                    leftBtn.setVisibility(View.VISIBLE);
                    rightBtn.setVisibility(View.VISIBLE);
                    break;
                case MSG_WHAT_HIDE:
                    panelLayout.setVisibility(View.GONE);
                    leftBtn.setVisibility(View.GONE);
                    rightBtn.setVisibility(View.GONE);
                    break;
                default:
                    break;
            }
        }

        ;
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.overridePendingTransition(0, 0);
        hideNavigationBar();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.visualizer_layout);

        //初始化相关
        MainHandler.initialize();
        UI.initialize(VisualizerActivity.this, VisualizerActivity.this);
        UI.loadFPlayTheme();

        //动态注册广播
        IntentFilter f = new IntentFilter();
        registerReceiver(mStatusListener, new IntentFilter(f));

        //视图相关
        contentLayout = (RelativeLayout) findViewById(R.id.contentLayout);
        contentLayout.setOnClickListener(this);
        leftBtn = (ImageButton) findViewById(R.id.leftBtn);
        leftBtn.setOnClickListener(this);
        rightBtn = (ImageButton) findViewById(R.id.rightBtn);
        rightBtn.setOnClickListener(this);
        pauseBtn = (ImageView) findViewById(R.id.visualizer_play);
        nextBtn = (ImageView) findViewById(R.id.visualizer_next);
        pauseBtn.setOnClickListener(this);
        nextBtn.setOnClickListener(this);
        panelLayout = (RelativeLayout) findViewById(R.id.panelLayout);
        ColorDrawable panelTopBackground = new ColorDrawable(UI.color_visualizer565);
        panelTopBackground.setAlpha(255 >> 1);
        panelLayout.setBackgroundDrawable(panelTopBackground);
        BgColorStateList buttonColor = new BgColorStateList(UI.color_btn, UI.color_text_selected);
        backBtn = (BgButton) findViewById(R.id.backBtn);
        backBtn.setIcon(UI.ICON_GOBACK);
        backBtn.setTextColor(buttonColor);
        backBtn.setOnClickListener(this);
        title = (TextView) findViewById(R.id.title_tv);
        artist = (TextView) findViewById(R.id.artist_tv);
        moreBtn = (BgButton) findViewById(R.id.moreBtn);
        moreBtn.setOnClickListener(this);
        moreBtn.setIcon(UI.ICON_MENU);
        moreBtn.setTextColor(buttonColor);

        //音量相关
        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        //intent中获取相关类型参数
        final Intent intent = getIntent();
        if (intent != null) {
            int[] _types = intent.getIntArrayExtra(VisualParams.VISUALIZER_TYPE);
            if (_types != null) {
                types = _types;
            } else {
                goDefaultVisual();
            }
            audioID = intent.getIntExtra(VisualParams.AUDIOSESSION_ID, -1);
            String titlestr = intent.getStringExtra(VisualParams.TITLE);
            String artistStr = intent.getStringExtra(VisualParams.ARTIST);
            if (titlestr != null)
                title.setText(titlestr);
            if (artistStr != null)
                artist.setText(artistStr);
        } else {
            goDefaultVisual();//走默认的频谱流程
        }

        //初始化频谱类型
        int index = UI.index % types.length;
        index = index < 0 ? types.length + index : index;
        int type = types[index >= types.length ? 0 : index < 0 ? 0 : index];
        if (type == VisualParams.TYPE_CLASSIC) {
            visualizer = new SimpleVisualizerJni(VisualizerActivity.this);
        } else {
            visualizer = new OpenGLVisualizerJni(VisualizerActivity.this, true, type);
        }

        //获取频谱相关参数，是否开启线程，是否竖直方向，是否隐藏控制面板
        boolean visualizerRequiresThread=false;
        if (visualizer != null) {
            requiredOrientation = visualizer.requiredOrientation();
            visualizerRequiresHiddenControls = visualizer.requiresHiddenControls();
        } else {
            requiredOrientation = Visualizer.ORIENTATION_NONE;
            visualizerRequiresHiddenControls = false;
        }

        //UI上增加频谱
        if (visualizer != null) {
            contentLayout.addView((View) visualizer);
            panelLayout.bringToFront();
            leftBtn.bringToFront();
            rightBtn.bringToFront();
        }

        //频谱是否需要线程
        if (visualizer != null) {
            visualizerPaused = false;
            visualizer.onActivityResume();
            if (!visualizerRequiresThread)
                visualizer.load(getApplication());//不需要
            else {
                fxVisualizer = new FxVisualizer(getApplication(), visualizer, audioID);
                fxVisualizer.start();//需要，和音乐关联
            }
        }

        //频谱布局相关
        RelativeLayout.LayoutParams ip;
        final Point pt = visualizer.getDesiredSize(UI.screenWidth, UI.screenHeight);
        ip = new RelativeLayout.LayoutParams(pt.x, pt.y);
        ip.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        ((View) visualizer).setLayoutParams(ip);

        //保证不锁屏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN |
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS |
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        //发送3秒隐藏操作面板消息
        mHandler.removeMessages(MSG_WHAT_HIDE);
        mHandler.sendEmptyMessageDelayed(MSG_WHAT_HIDE, HIDE_DELAY_TIME);
    }

    /**
     * 走默认频谱流程
     */
    private void goDefaultVisual() {
         OpenGLVisualizerSensorManager[] sensorManager = {new OpenGLVisualizerSensorManager(this, false)};
        if (sensorManager[0] != null) {//判断设备是否有重力传感器
            if (!sensorManager[0].isCapable) {
                sensorManager[0] = null;
                types = new int[4];
                types[0] = VisualParams.TYPE_LIQUID;
                types[1] = VisualParams.TYPE_PARTICLE;
                types[2] = VisualParams.TYPE_SPECTRUM;
                types[3] = VisualParams.TYPE_SPIN;
            } else {
                types = new int[6];
                types[0] = VisualParams.TYPE_LIQUID;
                types[1] = VisualParams.TYPE_IMMERSIVE_PARTICLE;
                types[2] = VisualParams.TYPE_PARTICLE;
                types[3] = VisualParams.TYPE_HORN;
                types[4] = VisualParams.TYPE_SPECTRUM;
                types[5] = VisualParams.TYPE_SPIN;
            }
        }


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.clear();
    }

    /**
     * 创建菜单#水平和竖直情况下更改菜单首项
     * @param menu
     * @param v
     * @param menuInfo
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        if (UI.forcedLocale != UI.LOCALE_NONE){
            UI.reapplyForcedLocale(getApplication(), this);
        }
        UI.prepare(menu);
        menu.add(0, 100, 0, UI.isLandscape ? "PORTRAIT" : "LANDSCAPE")
                .setOnMenuItemClickListener(this)
                .setIcon(new TextIconDrawable(UI.ICON_ORIENTATION));
        if (visualizer != null) {
            visualizer.onCreateContextMenu(menu);
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case 100:
                UI.isLandscape = !UI.isLandscape;
                setRequestedOrientation(UI.isLandscape ?
                        ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE : ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                break;
        }
        return true;
    }

    /**
     * 窗口焦点发送变化，隐藏操作面板
     * @param hasFocus
     */
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (hasFocus) {
            mHandler.removeMessages(MSG_WHAT_HIDE);
            mHandler.sendEmptyMessageDelayed(MSG_WHAT_HIDE, HIDE_DELAY_TIME);
        }
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideNavigationBar();
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(0, R.anim.menu_out);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (visualizer != null) {
            visualizer.onActivityPause();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.backBtn:
                finish();
                overridePendingTransition(0, R.anim.menu_out);
                break;

            case R.id.moreBtn:
                if (moreBtn != null)
                    onPrepareOptionsMenu(null);
                mHandler.removeMessages(MSG_WHAT_HIDE);
                mHandler.sendEmptyMessage(MSG_WHAT_SHOW);
                break;

            case R.id.contentLayout:
                if (panelLayout.getVisibility() == View.GONE) {
                    mHandler.removeMessages(MSG_WHAT_HIDE);
                    mHandler.sendEmptyMessage(MSG_WHAT_SHOW);
                    mHandler.sendEmptyMessageDelayed(MSG_WHAT_HIDE, HIDE_DELAY_TIME);
                } else {
                    mHandler.removeMessages(MSG_WHAT_HIDE);
                    mHandler.removeMessages(MSG_WHAT_SHOW);
                    mHandler.sendEmptyMessage(MSG_WHAT_HIDE);
                }
                break;

            case R.id.leftBtn:
                UI.index--;
                changeVisualType();
                break;

            case R.id.rightBtn:
                UI.index++;
                changeVisualType();
                break;

            case R.id.visualizer_play:
//            final ComponentName serviceName = new ComponentName(this,
//                    MediaPlaybackService.class);
//            Intent intent = new Intent(MediaPlaybackService.TOGGLEPAUSE_ACTION);
//            intent.setComponent(serviceName);
//            startService(intent);
//            mHandler.removeMessages(MSG_WHAT_HIDE);
//            mHandler.sendEmptyMessageDelayed(MSG_WHAT_HIDE, HIDE_DELAY_TIME);
                break;

            case R.id.visualizer_next:
//            final ComponentName serviceName = new ComponentName(this,
//                    MediaPlaybackService.class);
//            Intent intent = new Intent(MediaPlaybackService.NEXT_ACTION);
//            intent.setComponent(serviceName);
//            startService(intent);
//            mHandler.removeMessages(MSG_WHAT_HIDE);
//            mHandler.sendEmptyMessageDelayed(MSG_WHAT_HIDE, HIDE_DELAY_TIME);
                break;

        }
    }

    /**
     * 改变频谱类型
     */
    private void changeVisualType() {
        if (!done) {
            return;
        }
        done = false;

        //移除之前的
        if (fxVisualizer != null) {
            fxVisualizer.destroy();
            fxVisualizer = null;
        }
        if (visualizer != null) {
            contentLayout.removeView((View) visualizer);
            visualizer.release();
            visualizer = null;
        }

        //生产频谱类型
        int index = UI.index % types.length;
        index = index < 0 ? types.length + index : index;
        int type = types[index >= types.length ? 0 : index < 0 ? 0 : index];
        if (type == VisualParams.TYPE_CLASSIC) {
            visualizer = new SimpleVisualizerJni(VisualizerActivity.this);
        } else {
            visualizer = new OpenGLVisualizerJni(VisualizerActivity.this, true, type);
        }

        //频谱布局相关
        RelativeLayout.LayoutParams ip;
        final Point pt = visualizer.getDesiredSize(UI.screenWidth, UI.screenHeight);
        ip = new RelativeLayout.LayoutParams(pt.x, pt.y);
        ip.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        ((View) visualizer).setLayoutParams(ip);

        //UI上增加频谱
        contentLayout.addView((View) visualizer);
        panelLayout.bringToFront();
        leftBtn.bringToFront();
        rightBtn.bringToFront();

        //关联音乐
        fxVisualizer = new FxVisualizer(getApplication(), visualizer, audioID);
        fxVisualizer.start();

        done = true;

        //发送隐藏操作面板消息
        mHandler.removeMessages(MSG_WHAT_HIDE);
        mHandler.sendEmptyMessageDelayed(MSG_WHAT_HIDE, HIDE_DELAY_TIME);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (moreBtn != null)
            CustomContextMenu.openContextMenu(moreBtn, this);
        return false;
    }

    /**
     * 清理频谱
     */
    private void finalCleanup() {
        if (fxVisualizer != null) {
            fxVisualizer.destroy();
            fxVisualizer = null;
        } else if (visualizer != null) {
            visualizer.cancelLoading();
            visualizer.release();
            visualizer = null;
        }
    }

    private BroadcastReceiver mStatusListener = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finalCleanup();
        if (mStatusListener != null){
            unregisterReceiver(mStatusListener);
        }
        if (mHandler!=null){
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
        }
    }

    /**
     * 隐藏标题栏
     */
    public void hideNavigationBar() {
        int uiFlags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                | View.SYSTEM_UI_FLAG_FULLSCREEN; // hide status bar

        if (Build.VERSION.SDK_INT >= 19) {
            uiFlags |= 0x00001000;    //SYSTEM_UI_FLAG_IMMERSIVE_STICKY: hide navigation bars - compatibility: building API level is lower thatn 19, use magic number directly for higher API target level
        } else {
            uiFlags |= View.SYSTEM_UI_FLAG_LOW_PROFILE;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            getWindow().getDecorView().setSystemUiVisibility(uiFlags);
        }
    }
}