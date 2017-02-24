package com.luxg.course.video;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;


import com.luxg.course.R;
import com.luxg.course.util.DensityUtil;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class VideoViewDemoActivity extends Activity implements OnClickListener {

    // 自定义VideoView
    private FullScreenVideoView mVideo;

    // 头部View
    private View mTopView;

    // 底部View
    private View mBottomView;
    // 视频播放拖动条
    private SeekBar mSeekBar;
    private ImageView mPlay;
    private TextView mPlayTime;
    private TextView mDurationTime;

    // 音频管理器
    private AudioManager mAudioManager;

    // 屏幕宽高
    private float width;
    private float height;

    // 视频播放时间
    private int playTime;

    private String videoUrl = "http://www.ydtsystem.com/CardImage/21/video/20140305/20140305124807_37734.mp4";
    // 自动隐藏顶部和底部View的时间
    private static final int HIDE_TIME = 5000;

    // 声音调节Toast
    private VolumnController volumnController;

    // 原始屏幕亮度
    private int orginalLight;
    private String VIDEO_NAME;
    private String VIDEO_URL;
    private String TYPE;
    private LinearLayout progressbar;
    private ImageView mVoiceImg;
    private VerticalSeekBar mVerticalSeekbar;
    private TextView mNameView;
    private MyVolumeReceiver mVolumeReceiver;
    private boolean isPlaying;
    private ConnectionChangeReceiver myReceiver;
    private int mCurrentPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏
        setContentView(R.layout.videoview_demo);

        VIDEO_URL = getIntent().getStringExtra("VIDEO_URL");
        VIDEO_NAME = getIntent().getStringExtra("VIDEO_NAME");
//        VIDEO_URL = "http://live.habctv.com/xwzh/playlist.m3u8?_upt=de9adf9e1456974870";
        TYPE = getIntent().getStringExtra("TYPE");
        videoUrl = VIDEO_URL;

        volumnController = new VolumnController(this);
        mVideo = (FullScreenVideoView) findViewById(R.id.videoview);
        progressbar = (LinearLayout) findViewById(R.id.progressbar);
        mPlayTime = (TextView) findViewById(R.id.play_time);
        mDurationTime = (TextView) findViewById(R.id.total_time);
        mPlay = (ImageView) findViewById(R.id.play_btn);
        mSeekBar = (SeekBar) findViewById(R.id.seekbar);
        mTopView = findViewById(R.id.top_layout);
        mBottomView = findViewById(R.id.bottom_layout);
        mNameView = (TextView) findViewById(R.id.name_text);
        findViewById(R.id.back_btn).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        mVoiceImg = (ImageView) findViewById(R.id.voice);
        mVerticalSeekbar = (VerticalSeekBar) findViewById(R.id.vertical_Seekbar);
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int maxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);  //获取系统最大音量
        mVerticalSeekbar.setMax(maxVolume);   //拖动条最高值与系统最大声匹配
        int currentVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);  //获取当前值
        mVerticalSeekbar.setProgress(currentVolume);
        mVerticalSeekbar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
                mHandler.removeCallbacks(hideRunnable);
                mHandler.postDelayed(hideRunnable, HIDE_TIME);
//				int currentVolume = mAudioManage.getStreamVolume(AudioManager.STREAM_MUSIC);  //获取当前值
//				mVerticalSeekbar.setProgress(currentVolume);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        mVoiceImg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mVerticalSeekbar.setVisibility(mVerticalSeekbar.getVisibility() == View.VISIBLE ? View.INVISIBLE : View.VISIBLE);
                int currentVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);  //获取当前值
                mVerticalSeekbar.setProgress(currentVolume);
                mVerticalSeekbar.setSecondaryProgress(currentVolume);
            }
        });

        width = DensityUtil.getWidthInPx(this);
        height = DensityUtil.getHeightInPx(this);
        threshold = DensityUtil.dip2px(this, 18);

        orginalLight = LightnessController.getLightness(this);

        mPlay.setOnClickListener(this);
        mSeekBar.setOnSeekBarChangeListener(mSeekBarChangeListener);

        playVideo();
        myRegisterReceiver();
        if ("live".equals(TYPE)) {
            findViewById(R.id.seekbar).setEnabled(false);
        }
        if (!TextUtils.isEmpty(VIDEO_NAME)) {
            mNameView.setText(VIDEO_NAME);
        }
        registerConnectionReceiver();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            height = DensityUtil.getWidthInPx(this);
            width = DensityUtil.getHeightInPx(this);
        } else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            width = DensityUtil.getWidthInPx(this);
            height = DensityUtil.getHeightInPx(this);
        }
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onPause() {
        super.onPause();
        LightnessController.setLightness(this, orginalLight);
        if (mVideo.isPlaying()) {
            mVideo.pause();
            mPlay.setImageResource(R.drawable.video_play_btn);
            mCurrentPosition = mVideo.getCurrentPosition();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!mVideo.isPlaying()) {
            mVideo.start();
            mPlay.setImageResource(R.drawable.video_pause_btn);
            mVideo.seekTo(mCurrentPosition);
        }
    }

    private OnSeekBarChangeListener mSeekBarChangeListener = new OnSeekBarChangeListener() {

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            mHandler.postDelayed(hideRunnable, HIDE_TIME);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            mHandler.removeCallbacks(hideRunnable);
        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress,
                                      boolean fromUser) {
            if (fromUser) {
                int time = progress * mVideo.getDuration() / 100;
                mVideo.seekTo(time);
            }
        }
    };

    private void backward(float delataX) {
        int current = mVideo.getCurrentPosition();
        int backwardTime = (int) (delataX / width * mVideo.getDuration());
        int currentTime = current - backwardTime;
        mVideo.seekTo(currentTime);
        mSeekBar.setProgress(currentTime * 100 / mVideo.getDuration());
        mPlayTime.setText(formatTime(currentTime));
    }

    private void forward(float delataX) {
        int current = mVideo.getCurrentPosition();
        int forwardTime = (int) (delataX / width * mVideo.getDuration());
        int currentTime = current + forwardTime;
        mVideo.seekTo(currentTime);
        mSeekBar.setProgress(currentTime * 100 / mVideo.getDuration());
        mPlayTime.setText(formatTime(currentTime));
    }

    private void volumeDown(float delatY) {
        int max = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int current = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        int down = (int) (delatY / height * max * 3);
        int volume = Math.max(current - down, 0);
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, 0);
        int transformatVolume = volume * 100 / max;
        volumnController.show(transformatVolume);
    }

    private void volumeUp(float delatY) {
        int max = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int current = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        int up = (int) ((delatY / height) * max * 3);
        int volume = Math.min(current + up, max);
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, 0);
        int transformatVolume = volume * 100 / max;
        volumnController.show(transformatVolume);
    }

    private void lightDown(float delatY) {
        int down = (int) (delatY / height * 255 * 3);
        int transformatLight = LightnessController.getLightness(this) - down;
        LightnessController.setLightness(this, transformatLight);
    }

    private void lightUp(float delatY) {
        int up = (int) (delatY / height * 255 * 3);
        int transformatLight = LightnessController.getLightness(this) + up;
        LightnessController.setLightness(this, transformatLight);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeMessages(0);
        mHandler.removeCallbacksAndMessages(null);
        mVideo.stopPlayback();
        if (mVolumeReceiver != null) {
            unregisterReceiver(mVolumeReceiver);
        }
        unregisterConnectionReceiver();
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    if (mVideo.getCurrentPosition() > 0) {

                        mPlayTime.setText(formatTime(mVideo.getCurrentPosition()));
                        if (mVideo.getDuration() > 0) {
                            int progress = mVideo.getCurrentPosition() * 100 / mVideo.getDuration();
                            mSeekBar.setProgress(progress);
                            if (mVideo.getCurrentPosition() > mVideo.getDuration() - 100) {
                                mPlayTime.setText("00:00");
                                mSeekBar.setProgress(0);
                            }
                            mSeekBar.setSecondaryProgress(mVideo.getBufferPercentage());
                        }
                    } else {
                        mPlayTime.setText("00:00");
                        mSeekBar.setProgress(0);
                    }

                    break;
                case 2:
                    showOrHide();
                    break;

                default:
                    break;
            }
        }
    };

    private void playVideo() {
        mVideo.setVideoPath(videoUrl);
        mVideo.requestFocus();
        mVideo.setOnPreparedListener(new OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mVideo.setVideoWidth(mp.getVideoWidth());
                mVideo.setVideoHeight(mp.getVideoHeight());
                progressbar.setVisibility(View.GONE);
                mp.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                    @Override
                    public boolean onInfo(MediaPlayer mp, int what, int extra) {
                        if (what == MediaPlayer.MEDIA_INFO_BUFFERING_START) {
                            progressbar.setVisibility(View.VISIBLE);
                        } else if (what == MediaPlayer.MEDIA_INFO_BUFFERING_END) {
                            //此接口每次回调完START就回调END,若不加上判断就会出现缓冲图标一闪一闪的卡顿现象
                            if (mp.isPlaying()) {
                                progressbar.setVisibility(View.GONE);
                            }
                        }
                        isPlaying = true;
                        return true;
                    }
                });
                mp.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
                    @Override
                    public void onBufferingUpdate(MediaPlayer mp, int percent) {
                        mSeekBar.setSecondaryProgress(percent);
                    }
                });

                mp.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                    @Override
                    public boolean onError(MediaPlayer mp, int what, int extra) {
                        return false;
                    }
                });

                mVideo.start();
                if (playTime != 0) {
                    mVideo.seekTo(playTime);
                }

                mHandler.removeCallbacks(hideRunnable);
                mHandler.postDelayed(hideRunnable, HIDE_TIME);
                mDurationTime.setText(formatTime(mVideo.getDuration()));
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {

                    @Override
                    public void run() {
                        mHandler.sendEmptyMessage(1);
                    }
                }, 0, 1000);
            }
        });
        mVideo.setOnCompletionListener(new OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mPlay.setImageResource(R.drawable.video_play_btn);
                mPlayTime.setText("00:00");
                mSeekBar.setProgress(0);
                finish();
            }
        });
        mVideo.setOnTouchListener(mTouchListener);
    }

    private Runnable hideRunnable = new Runnable() {

        @Override
        public void run() {
            showOrHide();
        }
    };

    @SuppressLint("SimpleDateFormat")
    private String formatTime(long time) {
        DateFormat formatter = new SimpleDateFormat("mm:ss");
        return formatter.format(new Date(time));
    }

    private float mLastMotionX;
    private float mLastMotionY;
    private int startX;
    private int startY;
    private int threshold;
    private boolean isClick = true;

    private OnTouchListener mTouchListener = new OnTouchListener() {
        @SuppressLint("ClickableViewAccessibility")
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                showOrHide();
            }
            return true;
        }
    };

//    private OnTouchListener mTouchListener = new OnTouchListener() {
//
//        @Override
//        public boolean onTouch(View v, MotionEvent event) {
//            final float x = event.getX();
//            final float y = event.getY();
//
//            switch (event.getAction()) {
//                case MotionEvent.ACTION_DOWN:
//                    mLastMotionX = x;
//                    mLastMotionY = y;
//                    startX = (int) x;
//                    startY = (int) y;
//                    break;
//                case MotionEvent.ACTION_MOVE:
//                    float deltaX = x - mLastMotionX;
//                    float deltaY = y - mLastMotionY;
//                    float absDeltaX = Math.abs(deltaX);
//                    float absDeltaY = Math.abs(deltaY);
//                    // 声音调节标识
//                    boolean isAdjustAudio = false;
//                    if (absDeltaX > threshold && absDeltaY > threshold) {
//                        if (absDeltaX < absDeltaY) {
//                            isAdjustAudio = true;
//                        } else {
//                            isAdjustAudio = false;
//                        }
//                    } else if (absDeltaX < threshold && absDeltaY > threshold) {
//                        isAdjustAudio = true;
//                    } else if (absDeltaX > threshold && absDeltaY < threshold) {
//                        isAdjustAudio = false;
//                    } else {
//                        return true;
//                    }
//                    if (isAdjustAudio) {
//                        if (x < width / 2) {
//                            if (deltaY > 0) {
//                                lightDown(absDeltaY);
//                            } else if (deltaY < 0) {
//                                lightUp(absDeltaY);
//                            }
//                        } else {
//                            if (deltaY > 0) {
//                                volumeDown(absDeltaY);
//                            } else if (deltaY < 0) {
//                                volumeUp(absDeltaY);
//                            }
//                        }
//
//                    } else {
//                        if (deltaX > 0) {
//                            forward(absDeltaX);
//                        } else if (deltaX < 0) {
//                            backward(absDeltaX);
//                        }
//                    }
//                    mLastMotionX = x;
//                    mLastMotionY = y;
//                    break;
//                case MotionEvent.ACTION_UP:
//                    if (Math.abs(x - startX) > threshold
//                            || Math.abs(y - startY) > threshold) {
//                        isClick = false;
//                    }
//                    mLastMotionX = 0;
//                    mLastMotionY = 0;
//                    startX = (int) 0;
//                    if (isClick) {
//                        showOrHide();
//                    }
//                    isClick = true;
//                    break;
//
//                default:
//                    break;
//            }
//            return true;
//        }
//
//    };

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.play_btn) {
            if (mVideo.isPlaying()) {
                isPlaying = false;
                mVideo.pause();
                mPlay.setImageResource(R.drawable.video_play_btn);
            } else {
                isPlaying = true;
                mVideo.start();
                mPlay.setImageResource(R.drawable.video_pause_btn);
            }
        }
    }

    private void showOrHide() {
        if (mBottomView.getVisibility() == View.VISIBLE) {
            mTopView.clearAnimation();
            Animation animation = AnimationUtils.loadAnimation(this, R.anim.option_leave_from_top);
            animation.setAnimationListener(new AnimationImp() {
                @Override
                public void onAnimationEnd(Animation animation) {
                    super.onAnimationEnd(animation);
                    mTopView.setVisibility(View.GONE);
                }
            });
            mTopView.startAnimation(animation);

            mBottomView.clearAnimation();
            Animation animation1 = AnimationUtils.loadAnimation(this, R.anim.option_leave_from_bottom);
            animation1.setAnimationListener(new AnimationImp() {
                @Override
                public void onAnimationEnd(Animation animation) {
                    super.onAnimationEnd(animation);
                    mBottomView.setVisibility(View.GONE);
                }
            });
            mBottomView.startAnimation(animation1);
            mVerticalSeekbar.setVisibility(View.GONE);
        } else {
            mTopView.setVisibility(View.VISIBLE);
            mTopView.clearAnimation();
            Animation animation = AnimationUtils.loadAnimation(this, R.anim.option_entry_from_top);
            mTopView.startAnimation(animation);

            mBottomView.setVisibility(View.VISIBLE);
            mBottomView.clearAnimation();
            Animation animation1 = AnimationUtils.loadAnimation(this, R.anim.option_entry_from_bottom);
            mBottomView.startAnimation(animation1);
            mHandler.removeCallbacks(hideRunnable);
            mHandler.postDelayed(hideRunnable, HIDE_TIME);
        }
    }

    private class AnimationImp implements AnimationListener {

        @Override
        public void onAnimationEnd(Animation animation) {

        }

        @Override
        public void onAnimationRepeat(Animation animation) {
        }

        @Override
        public void onAnimationStart(Animation animation) {
        }

    }

    /**
     * 注册当音量发生变化时接收的广播
     */
    private void myRegisterReceiver() {
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
                int currentVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);  //获取当前值
                mVerticalSeekbar.setProgress(currentVolume);
            }
        }
    }

    private void registerConnectionReceiver() {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        myReceiver = new ConnectionChangeReceiver();
        this.registerReceiver(myReceiver, filter);
    }

    private void unregisterConnectionReceiver() {
        this.unregisterReceiver(myReceiver);
    }

    public class ConnectionChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mobNetInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            NetworkInfo wifiNetInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

            if (!mobNetInfo.isConnected() && !wifiNetInfo.isConnected()) {
                if (!mVideo.isPlaying() && !isPlaying)
                    mVideo.start();
                //改变背景或者 处理网络的全局变量
            } else {
                //改变背景或者 处理网络的全局变量
            }
        }
    }
}
