package com.luxg.course.widget.pulltorefresh;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.luxg.course.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;


/**
 * 下拉刷新控件，显示在listView的上部，下拉后才可见，达到阈值并释放方可刷新
 *
 */
public class PullToRefreshHeaderView extends FrameLayout {

	static final int DEFAULT_ROTATION_ANIMATION_DURATION = 150;

	private static SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("MM-dd HH:mm");
	
	private final ImageView mHeaderImage;
	private final ProgressBar mHeaderProgress;
	private final TextView mHeaderText;
	private final TextView mLatestRefreshDateText;

	private String mPullLabel;
	private String mRefreshingLabel;
	private String mReleaseLabel;

	private final Animation mRotateAnimation;
	private final Animation mResetRotateAnimation;

	/**
	 * 构造方法
	 * 
	 * @param context
	 * 				- 上下文实例
	 * @param releaseLabel
	 * 				- 显示释放刷新的文字
	 * @param pullLabel
	 * 				- 显示继续下拉的文字
	 * @param refreshingLabel
	 * 				- 处于刷新状态时显示的文字
	 */
	public PullToRefreshHeaderView(Context context, String releaseLabel, String pullLabel, String refreshingLabel) {
		super(context);
		ViewGroup header = (ViewGroup) LayoutInflater.from(context).inflate(R.layout.pull_to_refresh_header_view, this);
		mHeaderText = (TextView) header.findViewById(R.id.pull_to_refresh_text);
		mLatestRefreshDateText = (TextView) header.findViewById(R.id.refresh_date);
		mHeaderImage = (ImageView) header.findViewById(R.id.pull_to_refresh_image);
		mHeaderProgress = (ProgressBar) header.findViewById(R.id.pull_to_refresh_progress);

		final Interpolator interpolator = new LinearInterpolator();
		mRotateAnimation = new RotateAnimation(0, -180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
		        0.5f);
		mRotateAnimation.setInterpolator(interpolator);
		mRotateAnimation.setDuration(DEFAULT_ROTATION_ANIMATION_DURATION);
		mRotateAnimation.setFillAfter(true);

		mResetRotateAnimation = new RotateAnimation(-180, 0, Animation.RELATIVE_TO_SELF, 0.5f,
		        Animation.RELATIVE_TO_SELF, 0.5f);
		mResetRotateAnimation.setInterpolator(interpolator);
		mResetRotateAnimation.setDuration(DEFAULT_ROTATION_ANIMATION_DURATION);
		mResetRotateAnimation.setFillAfter(true);

		this.mReleaseLabel = releaseLabel;
		this.mPullLabel = pullLabel;
		this.mRefreshingLabel = refreshingLabel;

		mHeaderImage.setImageResource(R.drawable.pull_to_refresh_arrow);
	}

	/**
	 * 重置状态
	 */
	public void reset() {
		mHeaderText.setText(mPullLabel);
		mHeaderImage.setVisibility(View.VISIBLE);
		mHeaderProgress.setVisibility(View.GONE);
	}

	/**
	 * 当达到下拉阈值显示“释放”
	 */
	public void releaseToRefresh() {
		mHeaderText.setText(mReleaseLabel);
		mHeaderImage.clearAnimation();
		mHeaderImage.startAnimation(mRotateAnimation);
	}

	/**
	 * 设置下拉时显示的信息
	 * @param pullLabel
	 * 						- 文字信息
	 */
	public void setPullLabel(String pullLabel) {
		this.mPullLabel = pullLabel;
	}

	/**
	 * 处于刷新状态时显示的样式
	 */
	public void refreshing() {
		mHeaderText.setText(mRefreshingLabel);
		mHeaderImage.clearAnimation();
		mHeaderImage.setVisibility(View.INVISIBLE);
		mHeaderProgress.setVisibility(View.VISIBLE);
	}

	/**
	 * 设置刷新时显示的信息
	 * @param refreshingLabel
	 * 						- 文字信息
	 */
	public void setRefreshingLabel(String refreshingLabel) {
		this.mRefreshingLabel = refreshingLabel;
	}

	/**
	 * 设置需要释放时显示的信息
	 * @param releaseLabel
	 * 						- 文字信息
	 */
	public void setReleaseLabel(String releaseLabel) {
		this.mReleaseLabel = releaseLabel;
	}

	/**
	 * 当未达到下拉阈值显示“下拉”
	 */
	public void pullToRefresh() {
		mHeaderText.setText(mPullLabel);
		mHeaderImage.clearAnimation();
		mHeaderImage.startAnimation(mResetRotateAnimation);
	}

	/**
	 * 设置文字字体颜色
	 */
	public void setTextColor(int color) {
		mHeaderText.setTextColor(color);
	}
	
	/**
	 * 显示上次刷新的时间
	 */
	public void setRefreshDate() {
		mLatestRefreshDateText.setText(
				String.format(getResources().getString(R.string.pull_to_refresh_refreshing_date), 
						mSimpleDateFormat.format(new Date())));
		mLatestRefreshDateText.setVisibility(View.VISIBLE);
	}

}
