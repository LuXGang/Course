package com.luxg.course.video;

import android.content.Context;
import android.content.res.Resources;
import android.media.AudioManager;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;


import com.luxg.course.R;

import java.util.Formatter;
import java.util.Locale;

/**
 * Created by lc5450 on 2016/3/22.
 */
public class MController extends FrameLayout {
    private MediaPlayerControl mPlayer;
    private final Context mContext;
    private View mRoot;
    private LinearLayout mProgressbar;
    private TextView mCurrentTime;
    private TextView mEndTime;
    private ImageView mPlay;
    private SeekBar mProgress;
    private View mTopView;
    private View mBottomView;
    private TextView mNameView;

    private static final int sDefaultTimeout = 5000;
    private static final int FADE_OUT = 1;
    private static final int SHOW_PROGRESS = 2;
    private StringBuilder mFormatBuilder;
    private Formatter mFormatter;
    private boolean mShowing;
    private boolean mDragging;
    private AudioManager mAudioManager;
    private VDVideoSoundSeekBar mVerticalSeekbar;
    private ImageView mVoiceImg;
    private int maxVolume;

    public MController(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public MController(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    public MController(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    private void init() {
        LayoutParams frameParams = new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );

        removeAllViews();
        View v = makeControllerView();
        addView(v, frameParams);
    }

    /**
     * Create the view that holds the widgets that control playback.
     * Derived classes can override this to create their own.
     *
     * @return The controller view.
     * @hide This doesn't work as advertised
     */
    protected View makeControllerView() {
        LayoutInflater inflate = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mRoot = inflate.inflate(R.layout.media_controller_layout, null);

        initControllerView(mRoot);

        return mRoot;
    }

    private void initControllerView(View v) {
        Resources res = mContext.getResources();

        mProgressbar = (LinearLayout) v.findViewById(R.id.progressbar);
        mCurrentTime = (TextView) v.findViewById(R.id.time_current);
        mEndTime = (TextView) v.findViewById(R.id.time);
        mPlay = (ImageView) v.findViewById(R.id.play_btn);
        mProgress = (SeekBar) v.findViewById(R.id.seekbar);
        mProgress.setOnSeekBarChangeListener(mSeekListener);
        mProgress.setMax(1000);
        mTopView = v.findViewById(R.id.top_layout);
        mBottomView = v.findViewById(R.id.bottom_layout);
        mNameView = (TextView) v.findViewById(R.id.name_text);
        mVoiceImg = (ImageView) v.findViewById(R.id.voice);
        mVerticalSeekbar = (VDVideoSoundSeekBar) v.findViewById(R.id.vertical_Seekbar);

        mPlay.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                doPauseResume();
                show(sDefaultTimeout);
            }
        });

        mAudioManager = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
        maxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);  //获取系统最大音量
        mVerticalSeekbar.setMax(maxVolume);   //拖动条最高值与系统最大声匹配
        int currentVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);  //获取当前值
        mVerticalSeekbar.setProgress(currentVolume);
        mVerticalSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
                mHandler.removeCallbacks(hideRunnable);
                mHandler.postDelayed(hideRunnable, sDefaultTimeout);
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
//                mVerticalSeekbar.setProgress(currentVolume);
//                mVerticalSeekbar.setSecondaryProgress(currentVolume);
                mVerticalSeekbar.onSetCurVolume(currentVolume);
            }
        });


        mFormatBuilder = new StringBuilder();
        mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());

//        mPlayDescription = res
//                .getText(com.android.internal.R.string.lockscreen_transport_play_description);
//        mPauseDescription = res
//                .getText(com.android.internal.R.string.lockscreen_transport_pause_description);
//        mPauseButton = (ImageButton) v.findViewById(com.android.internal.R.id.pause);
//        if (mPauseButton != null) {
//            mPauseButton.requestFocus();
//            mPauseButton.setOnClickListener(mPauseListener);
//        }
//
//        mFfwdButton = (ImageButton) v.findViewById(com.android.internal.R.id.ffwd);
//        if (mFfwdButton != null) {
//            mFfwdButton.setOnClickListener(mFfwdListener);
//            if (!mFromXml) {
//                mFfwdButton.setVisibility(mUseFastForward ? View.VISIBLE : View.GONE);
//            }
//        }
//
//        mRewButton = (ImageButton) v.findViewById(com.android.internal.R.id.rew);
//        if (mRewButton != null) {
//            mRewButton.setOnClickListener(mRewListener);
//            if (!mFromXml) {
//                mRewButton.setVisibility(mUseFastForward ? View.VISIBLE : View.GONE);
//            }
//        }
//
//        // By default these are hidden. They will be enabled when setPrevNextListeners() is called
//        mNextButton = (ImageButton) v.findViewById(com.android.internal.R.id.next);
//        if (mNextButton != null && !mFromXml && !mListenersSet) {
//            mNextButton.setVisibility(View.GONE);
//        }
//        mPrevButton = (ImageButton) v.findViewById(com.android.internal.R.id.prev);
//        if (mPrevButton != null && !mFromXml && !mListenersSet) {
//            mPrevButton.setVisibility(View.GONE);
//        }
//
//        mProgress = (ProgressBar) v.findViewById(com.android.internal.R.id.mediacontroller_progress);
//        if (mProgress != null) {
//            if (mProgress instanceof SeekBar) {
//                SeekBar seeker = (SeekBar) mProgress;
//                seeker.setOnSeekBarChangeListener(mSeekListener);
//            }
//            mProgress.setMax(1000);
//        }
//
//        mEndTime = (TextView) v.findViewById(com.android.internal.R.id.time);
//        mCurrentTime = (TextView) v.findViewById(com.android.internal.R.id.time_current);
//        mFormatBuilder = new StringBuilder();
//        mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());
//
//        installPrevNextListeners();
    }

    public void setCurrentVolume() {
        int currentVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);  //获取当前值
        mVerticalSeekbar.onSetCurVolume(currentVolume);
    }

    public void setMediaPlayer(MediaPlayerControl player) {
        mPlayer = player;
        updatePausePlay();
    }

    public void showProgressBar() {
        if (mProgressbar != null)
            mProgressbar.setVisibility(VISIBLE);
    }

    public void hideProgressBar() {
        if (mProgressbar != null)
            mProgressbar.setVisibility(GONE);
    }

    public void show() {
        show(sDefaultTimeout);
    }

    public void show(int timeout) {
        if (!mShowing) {
            setProgress();
//            if (mPauseButton != null) {
//                mPauseButton.requestFocus();
//            }
//            disableUnsupportedButtons();
//            updateFloatingWindowLayout();
//            mWindowManager.addView(mDecor, mDecorLayoutParams);
            mShowing = true;
        }
        updatePausePlay();

        // cause the progress bar to be updated even if mShowing
        // was already true.  This happens, for example, if we're
        // paused with the progress bar showing the user hits play.
        mHandler.sendEmptyMessage(SHOW_PROGRESS);

        Message msg = mHandler.obtainMessage(FADE_OUT);
        if (timeout != 0) {
            mHandler.removeMessages(FADE_OUT);
            mHandler.sendMessageDelayed(msg, timeout);
        }
    }

    private void updatePausePlay() {
        if (mPlay == null)
            return;

        if (mPlayer.isPlaying()) {
            mPlay.setImageResource(R.drawable.video_pause_btn);
        } else {
            mPlay.setImageResource(R.drawable.video_play_btn);
        }
    }

    public void doPauseResume() {
        if (mPlayer.isPlaying()) {
            mPlayer.pause();
        } else {
            mPlayer.start();
        }
        updatePausePlay();
    }

    public boolean isShowing() {
        return mShowing;
    }

    public void hide() {
//        if (mAnchor == null)
//            return;
//
        if (mShowing) {
            try {
                mHandler.removeMessages(SHOW_PROGRESS);
            } catch (IllegalArgumentException ex) {
                Log.w("MediaController", "already removed");
            }
            mShowing = false;
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int pos;
            switch (msg.what) {
                case FADE_OUT:
                    hide();
                    break;
                case SHOW_PROGRESS:
                    pos = setProgress();
                    if (!mDragging && mShowing && mPlayer.isPlaying()) {
                        msg = obtainMessage(SHOW_PROGRESS);
                        sendMessageDelayed(msg, 1000 - (pos % 1000));
                    }
                    break;
            }
        }
    };

    private String stringForTime(int timeMs) {
        int totalSeconds = timeMs / 1000;

        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;

        mFormatBuilder.setLength(0);
        if (hours > 0) {
            return mFormatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();
        } else {
            return mFormatter.format("%02d:%02d", minutes, seconds).toString();
        }
    }

    private int setProgress() {
        if (mPlayer == null || mDragging) {
            return 0;
        }
        int position = mPlayer.getCurrentPosition();
        int duration = mPlayer.getDuration();
        System.out.println("setProgress position ------------- " + position);
        System.out.println("setProgress duration ------------- " + duration);
        if (mProgress != null) {
            if (duration > 0) {
                // use long to avoid overflow
                long pos = 1000L * position / duration;
                System.out.println("setProgress pos ------------- " + pos);
                mProgress.setProgress((int) pos);
            } else {
                mProgress.setProgress(0);
            }
            int percent = mPlayer.getBufferPercentage();
            System.out.println("setProgress percent ------------- " + percent);
            mProgress.setSecondaryProgress(percent * 10);
        }

        if (mEndTime != null)
            mEndTime.setText(stringForTime(duration));
        if (mCurrentTime != null)
            mCurrentTime.setText(stringForTime(position));

        return position;
    }

    public void reset() {
        mProgress.setProgress(0);
        mProgress.setSecondaryProgress(0);
        mCurrentTime.setText("00:00");
        mPlay.setImageResource(R.drawable.video_play_btn);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                show(0); // show until hide is called
                showOrHide();
                break;
            case MotionEvent.ACTION_UP:
                show(sDefaultTimeout); // start timeout
                break;
            case MotionEvent.ACTION_CANCEL:
                hide();
                break;
            default:
                break;
        }
        return true;
    }

    private SeekBar.OnSeekBarChangeListener mSeekListener = new SeekBar.OnSeekBarChangeListener() {
        public void onStartTrackingTouch(SeekBar bar) {
            show(3600000);

            mDragging = true;

            // By removing these pending progress messages we make sure
            // that a) we won't update the progress while the user adjusts
            // the seekbar and b) once the user is done dragging the thumb
            // we will post one of these messages to the queue again and
            // this ensures that there will be exactly one message queued up.
            mHandler.removeMessages(SHOW_PROGRESS);
        }

        public void onProgressChanged(SeekBar bar, int progress, boolean fromuser) {
            if (!fromuser) {
                // We're not interested in programmatically generated changes to
                // the progress bar's position.
                return;
            }

            long duration = mPlayer.getDuration();
            long newposition = (duration * progress) / 1000L;
            mPlayer.seekTo((int) newposition);
            if (mCurrentTime != null)
                mCurrentTime.setText(stringForTime((int) newposition));
        }

        public void onStopTrackingTouch(SeekBar bar) {
            mDragging = false;
            setProgress();
            updatePausePlay();
            show(sDefaultTimeout);

            // Ensure that progress is properly updated in the future,
            // the call to show() does not guarantee this because it is a
            // no-op if we are already showing.
            mHandler.sendEmptyMessage(SHOW_PROGRESS);
        }
    };
//    @Override
//    public boolean onTrackballEvent(MotionEvent ev) {
//        show(sDefaultTimeout);
//        return false;
//    }
//
//    @Override
//    public boolean dispatchKeyEvent(KeyEvent event) {
//        int keyCode = event.getKeyCode();
//        final boolean uniqueDown = event.getRepeatCount() == 0
//                && event.getAction() == KeyEvent.ACTION_DOWN;
//        if (keyCode ==  KeyEvent.KEYCODE_HEADSETHOOK
//                || keyCode == KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE
//                || keyCode == KeyEvent.KEYCODE_SPACE) {
//            if (uniqueDown) {
////                doPauseResume();
////                show(sDefaultTimeout);
////                if (mPauseButton != null) {
////                    mPauseButton.requestFocus();
////                }
//            }
//            return true;
//        } else if (keyCode == KeyEvent.KEYCODE_MEDIA_PLAY) {
//            if (uniqueDown && !mPlayer.isPlaying()) {
//                mPlayer.start();
////                updatePausePlay();
//                show(sDefaultTimeout);
//            }
//            return true;
//        } else if (keyCode == KeyEvent.KEYCODE_MEDIA_STOP
//                || keyCode == KeyEvent.KEYCODE_MEDIA_PAUSE) {
//            if (uniqueDown && mPlayer.isPlaying()) {
//                mPlayer.pause();
////                updatePausePlay();
//                show(sDefaultTimeout);
//            }
//            return true;
//        } else if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN
//                || keyCode == KeyEvent.KEYCODE_VOLUME_UP
//                || keyCode == KeyEvent.KEYCODE_VOLUME_MUTE
//                || keyCode == KeyEvent.KEYCODE_CAMERA) {
//            // don't show the controls for volume adjustment
//            return super.dispatchKeyEvent(event);
//        } else if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_MENU) {
//            if (uniqueDown) {
//                hide();
//            }
//            return true;
//        }
//
//        show(sDefaultTimeout);
//        return super.dispatchKeyEvent(event);
//    }

    public void showOrHide() {
        if (mBottomView.getVisibility() == View.VISIBLE) {
            mTopView.clearAnimation();
            Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.option_leave_from_top);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    mTopView.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            mTopView.startAnimation(animation);

            mBottomView.clearAnimation();
            Animation animation1 = AnimationUtils.loadAnimation(getContext(), R.anim.option_leave_from_bottom);
            animation1.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    mBottomView.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            mBottomView.startAnimation(animation1);
            mVerticalSeekbar.setVisibility(View.GONE);
        } else {
            mTopView.setVisibility(View.VISIBLE);
            mTopView.clearAnimation();
            Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.option_entry_from_top);
            mTopView.startAnimation(animation);

            mBottomView.setVisibility(View.VISIBLE);
            mBottomView.clearAnimation();
            Animation animation1 = AnimationUtils.loadAnimation(getContext(), R.anim.option_entry_from_bottom);
            mBottomView.startAnimation(animation1);
            mHandler.removeCallbacks(hideRunnable);
            mHandler.postDelayed(hideRunnable, sDefaultTimeout);
        }
    }

    public void hideController() {
        mHandler.removeCallbacks(hideRunnable);
        mHandler.postDelayed(hideRunnable, sDefaultTimeout);
    }

    private Runnable hideRunnable = new Runnable() {

        @Override
        public void run() {
            showOrHide();
        }
    };

    public interface MediaPlayerControl {
        void start();

        void pause();

        int getDuration();

        int getCurrentPosition();

        void seekTo(int pos);

        boolean isPlaying();

        int getBufferPercentage();

        boolean canPause();

        boolean canSeekBackward();

        boolean canSeekForward();

        /**
         * Get the audio session id for the player used by this VideoView. This can be used to
         * apply audio effects to the audio track of a video.
         *
         * @return The audio session, or 0 if there was an error.
         */
        int getAudioSessionId();
    }
}
