
package com.luxg.course.widget.basewise;

import com.luxg.course.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


@SuppressLint("ResourceAsColor")
public class CommonTitle extends RelativeLayout {

	private View rootView;
	private ImageView backImageView;
	private Button leftButton; // 左边按钮
	private Button rightButton; // 右边第一个按钮
	private ImageButton rightButton2; // 右边第二个按钮
	private TextView titleTextView; // 中间标题

	int bgColor;
	int backBtnSrc;
	int rightBtnSrc;
	int rightBtn2Src;
	int titleText;
	int titleColor;
	int rightText;
	int rightText2;

	private TitleClickListener titleClickListener;

	public CommonTitle(Context context) {
		this(context, null);
	}

	public CommonTitle(Context context, AttributeSet attrs) {
		super(context, attrs);

		LayoutInflater.from(context).inflate(R.layout.common_title, this, true);
		rootView = findViewById(R.id.common_title_root);
		backImageView = (ImageView) findViewById(R.id.common_title_left_img);
		leftButton = (Button) findViewById(R.id.common_title_right_btn); // 左边按钮
		rightButton = (Button) findViewById(R.id.common_title_right_img); // 右边第一个按钮
		rightButton2 = (ImageButton) findViewById(R.id.common_title_right_img2); // 右边第二个按钮
		titleTextView = (TextView) findViewById(R.id.common_title_title_text); // 中间标题
		rightButton.setMinHeight(0);
		rightButton.setMinWidth(0);

		backImageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (titleClickListener != null) {
					titleClickListener.onLeftClicked(CommonTitle.this, v);
				}
			}
		});
		rightButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (titleClickListener != null) {
					titleClickListener.onRightClicked(CommonTitle.this, v);
				}
			}
		});
		rightButton2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (titleClickListener != null) {
					titleClickListener.onRight2Clicked(CommonTitle.this, v);
				}
			}
		});
	}

	public void setOnTitleClickListener(TitleClickListener clickListener) {
		this.titleClickListener = clickListener;
	}

	public void setBackgroundColor(int colorId) {
		rootView.setBackgroundColor(colorId);
	}

	public void setBackgroundResource(int resId) {
		rootView.setBackgroundResource(resId);
	}

	/**
	 * set title text
	 *
	 * @param resId in string.xml
	 */
	public void setTitleText(int resId) {
		titleTextView.setVisibility(View.VISIBLE);
		titleTextView.setText(getResources().getText(resId));
	}

	/**
	 * set title text
	 *
	 * @param text
	 */
	public void setTitleText(String text) {
		titleTextView.setVisibility(View.VISIBLE);
		setText(titleTextView, text);
	}

	public void setLeftText(int resId) {
		backImageView.setVisibility(GONE);
		leftButton.setVisibility(View.VISIBLE);
		leftButton.setText(getResources().getText(resId));
		leftButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (titleClickListener != null) {
					titleClickListener.onLeftClicked(CommonTitle.this, v);
				}
			}
		});
	}

	public void setLeftText(CharSequence text) {
		backImageView.setVisibility(GONE);
		leftButton.setVisibility(View.VISIBLE);
		leftButton.setText(text);
		leftButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (titleClickListener != null) {
					titleClickListener.onLeftClicked(CommonTitle.this, v);
				}
			}
		});
	}

	public void setRightText(int resId) {
		rightButton.setVisibility(View.VISIBLE);
		rightButton.setText(getResources().getText(resId));
	}

	public void setRightText(CharSequence text) {
		rightButton.setVisibility(View.VISIBLE);
		rightButton.setText(text);
	}

	public void setLeftImageBg(int resId) {
		backImageView.setVisibility(View.VISIBLE);
		backImageView.setImageResource(resId);
	}
	// 设置右边第一张图片
	public void setRightBg(int resId) {
		rightButton.setVisibility(View.VISIBLE);
		rightButton.setBackgroundResource(resId);
	}

	// 设置右边第二张图片
	public void setRight2Bg(int resId) {
		rightButton2.setVisibility(View.VISIBLE);
		rightButton2.setImageResource(resId);
	}

	public TextView getTitleView() {
		return titleTextView;
	}

	public Button getLeftButton() {
		return leftButton;
	}

	public Button getRightButton() {
		return rightButton;
	}

	public ImageButton getRightButton2() {
		return rightButton2;
	}

	/**
	 * set title text color
	 *
	 * @param resId
	 */
	public void setTitleTextColor(int resId) {
		titleTextView.setTextColor(getResources().getColor(resId));
	}

	public void setRightTextColor(int resId) {
		rightButton.setTextColor(getResources().getColor(resId));
	}

	/**
	 * @param clickable
	 */
	public void setnRightClickable(boolean clickable) {
		if (rightBtnSrc != -1) {
			rightButton.setClickable(clickable);
		}
	}

	/**
	 * @param visibility
	 */
	public void setRightVisibility(int visibility) {
		if (rightBtnSrc != -1) {
			rightButton.setVisibility(visibility);
		}
	}

	/**
	 * @param visibility
	 */
	public void setRight2Visibility(int visibility) {
		if (rightBtn2Src != -1) {
			rightButton2.setVisibility(visibility);
		}
	}

	public void setRightImageViewBackgroundResource(int resid) {
		rightButton.setBackgroundResource(resid);
	}

	protected void setText(TextView textview, String title) {
		String s;
		if (TextUtils.isEmpty(title)) {
			s = "";
		} else {
			s = title;
		}
		textview.setText(s);
	}

	@Deprecated
	public void initSecondView() {
		setBackgroundResource(R.drawable.bus_detail_titlebar_bg);
		setLeftImageBg(R.drawable.titlebar_back_second);
		setTitleTextColor(R.color.main_color);
		setRightTextColor(R.color.main_color);
	}

	public interface TitleClickListener {
		void onLeftClicked(CommonTitle view, View v);

		void onRightClicked(CommonTitle view, View v);

		void onRight2Clicked(CommonTitle view, View v);
	}
}
