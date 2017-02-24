package com.luxg.course.video;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Handler;
import android.util.AttributeSet;
import android.widget.VideoView;

/**
 * 自动全屏的VideoView
 */
public class FullScreenVideoView extends VideoView implements MController.MediaPlayerControl {

    private int videoWidth;
    private int videoHeight;
    private MController mMediaController;
    private int mCurrentPosition;
    private int mCurrentBufferPercentage;

    public FullScreenVideoView(Context context) {
        super(context);
    }

    public FullScreenVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FullScreenVideoView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setMediaController(MController controller) {
        if (mMediaController != null) {
            mMediaController.hide();
        }
        mMediaController = controller;
        attachMediaController();
    }

    private void attachMediaController() {
        if (mMediaController != null) {
            mMediaController.setMediaPlayer(this);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = getDefaultSize(videoWidth, widthMeasureSpec);
        int height = getDefaultSize(videoHeight, heightMeasureSpec);
        if (videoWidth > 0 && videoHeight > 0) {
            if (videoWidth * height > width * videoHeight) {
                height = width * videoHeight / videoWidth;
            } else if (videoWidth * height < width * videoHeight) {
                width = height * videoWidth / videoHeight;
            }
        }
        setMeasuredDimension(width, height);
    }

    public int getVideoWidth() {
        return videoWidth;
    }

    public void setVideoWidth(int videoWidth) {
        this.videoWidth = videoWidth;
    }

    public int getVideoHeight() {
        return videoHeight;
    }

    public void setVideoHeight(int videoHeight) {
        this.videoHeight = videoHeight;
    }


    @Override
    public void start() {
        super.start();
    }

    @Override
    public void pause() {
        super.pause();
        mCurrentPosition = getCurrentPosition();
    }

    public int getMyCurrentPosition() {
        return mCurrentPosition;
    }

    @Override
    public int getDuration() {
        return super.getDuration();
    }

    @Override
    public int getCurrentPosition() {
        return super.getCurrentPosition();
    }

    @Override
    public void seekTo(int pos) {
        super.seekTo(pos);
    }

    @Override
    public boolean isPlaying() {
        return super.isPlaying();
    }

    @Override
    public int getBufferPercentage() {
        return mCurrentBufferPercentage;
    }

    @Override
    public boolean canPause() {
        return super.canPause();
    }

    @Override
    public boolean canSeekBackward() {
        return super.canSeekBackward();
    }

    @Override
    public boolean canSeekForward() {
        return super.canSeekForward();
    }

    @Override
    public int getAudioSessionId() {
        return super.getAudioSessionId();
    }

    public void playVideo(String videoUrl) {
        setVideoPath(videoUrl);
        requestFocus();

        setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mMediaController.show();
                mMediaController.showOrHide();
                setVideoWidth(mp.getVideoWidth());
                setVideoHeight(mp.getVideoHeight());

                mp.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                    @Override
                    public boolean onInfo(MediaPlayer mp, int what, int extra) {
                        boolean handled = false;
                        switch (what) {
                            case MediaPlayer.MEDIA_INFO_BUFFERING_START:
                                if (mMediaController != null) {
                                    mMediaController.showProgressBar();
                                }
                                handled = true;
                                break;
                            case MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START:
                            case MediaPlayer.MEDIA_INFO_BUFFERING_END:
                                //此接口每次回调完START就回调END,若不加上判断就会出现缓冲图标一闪一闪的卡顿现象
                                if (mMediaController != null) {
                                    if (mp.isPlaying()) {
                                        mMediaController.hideProgressBar();
                                    }
                                }
                                handled = true;
                                break;
                        }
                        return handled;
                    }
                });
                mp.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
                    @Override
                    public void onBufferingUpdate(MediaPlayer mp, int percent) {
                        mCurrentBufferPercentage = percent;
                    }
                });
                start();
            }
        });

        setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
//                mPlay.setImageResource(R.drawable.video_play_btn);
//                mPlayTime.setText("00:00");
//                mSeekBar.setProgress(0);
            }
        });
    }
}
