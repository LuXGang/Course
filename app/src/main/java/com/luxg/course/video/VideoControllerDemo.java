package com.luxg.course.video;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.luxg.course.R;


/**
 * Created by LuXiaogang on 2016/4/28.
 */
public class VideoControllerDemo extends Activity {

    private FullScreenVideoView mVideo;
    private String videoUrl = "http://upload.2500city.com/20160322wh4.mp4";
    private MController mController;
    private MyVolumeReceiver mVolumeReceiver;
    private int orginalLight;
    private String VIDEO_URL;
    private String VIDEO_NAME;
    private String TYPE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏
        setContentView(R.layout.video_controller_demo);

        VIDEO_URL = getIntent().getStringExtra("VIDEO_URL");
        VIDEO_NAME = getIntent().getStringExtra("VIDEO_NAME");
//        VIDEO_URL = "http://live.habctv.com/xwzh/playlist.m3u8?_upt=de9adf9e1456974870";
        TYPE = getIntent().getStringExtra("TYPE");
        videoUrl = VIDEO_URL;

        volumeRegisterReceiver();
//        orginalLight = LightnessController.getLightness(this);

        mVideo = (FullScreenVideoView) findViewById(R.id.videoview);
        mController = (MController) findViewById(R.id.controller);
        mVideo.setMediaController(mController);
        mVideo.playVideo(videoUrl);
        mVideo.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                finish();
            }
        });

        if ("live".equals(TYPE)) {
            mController.findViewById(R.id.seekbar).setEnabled(false);
        }
        TextView mNameView = (TextView) mController.findViewById(R.id.name_text);
        if (!TextUtils.isEmpty(VIDEO_NAME)) {
            mNameView.setText(VIDEO_NAME);
        }
        mController.findViewById(R.id.back_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
//        LightnessController.setLightness(this, orginalLight);
        if (mVideo.isPlaying()) {
            mVideo.pause();
            mController.hideProgressBar();
            mController.hideController();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!mVideo.isPlaying()) {
            if (mVideo.getMyCurrentPosition() > 0 && TYPE != null && !TYPE.contains("live")) {
                mVideo.seekTo(mVideo.getMyCurrentPosition());
            }
            mVideo.start();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mVideo.stopPlayback();
        if (mVolumeReceiver != null) {
            unregisterReceiver(mVolumeReceiver);
        }
    }

    /**
     * 注册当音量发生变化时接收的广播
     */
    private void volumeRegisterReceiver() {
        mVolumeReceiver = new MyVolumeReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.media.VOLUME_CHANGED_ACTION");
        registerReceiver(mVolumeReceiver, filter);
    }

    /**
     * 处理音量变化时的界面显示
     *
     * @author long
     */
    private class MyVolumeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            //如果音量发生变化则更改seekbar的位置
            if (intent.getAction().equals("android.media.VOLUME_CHANGED_ACTION")) {
                mController.setCurrentVolume();
            }
        }
    }
}
