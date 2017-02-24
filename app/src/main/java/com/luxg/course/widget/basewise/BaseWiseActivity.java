package com.luxg.course.widget.basewise;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;



public abstract class BaseWiseActivity extends FragmentActivity implements IView {
	public final static int RESULT_FOR_LOGIN = 1001;
	public String TAG;
	private ViewContainer vc;
	private FragmentActivity mActivity;
	private Context mContext;

	public Context getContext() {
		return mContext;
	}

	public FragmentActivity getActivity() {
		return mActivity;
	}

	public String getTAG() {
		return TAG;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		TAG = this.getClass().getSimpleName();
		ActivityTaskManager.getInstance().putActivity(TAG + "-" + this.toString(), this);
		mActivity = this;
		mContext = this;
//		initSystemBar(this);
		initViewContainer();
//        setContentView(R.layout.base_layout);
//        init();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		ActivityTaskManager.getInstance().removeActivity(TAG + "-" + this.toString());
	}

	private void initViewContainer() {

	}



	protected void setText(TextView textview, CharSequence title) {
		if (TextUtils.isEmpty(title)) {
			textview.setText("");
		} else {
			if (textview instanceof EditText)
				((EditText) textview).setSelection(title.length());
			textview.setText(title);
		}
	}

	protected void setText(EditText textview, CharSequence title) {
		if (TextUtils.isEmpty(title)) {
			textview.setText("");
		} else {
			textview.setText(title);
			textview.setSelection(title.length());
		}
	}

	protected void setBackgroundColor(int colorId) {
		getVc().setBackgroundColor(colorId);
	}

	@Override
	public void setContentView(int layoutResID) {
		vc = new ViewContainer(this, layoutResID);
		vc.build(this);
//		vc.setBackgroundColor(getResources().getColor(R.color.allbackgroud));
	}

	public void setContentView(int layoutResID, String key) {
		vc = new ViewContainer(this, layoutResID);
		vc.build(this, key);
	}

	/**
	 * setContentView() 后调用
	 */
	public void setTitleView(int titleResID) {
		View view = LayoutInflater.from(this).inflate(titleResID, null);
		vc.setTitleView(view);
	}

	public void setTitleView(View view) {
		vc.setTitleView(view);
	}

	public CommonTitle setTitleView(String title) {
		CommonTitle mCommonTitle = new CommonTitle(getContext());
		mCommonTitle.setOnTitleClickListener(new CommonTitle.TitleClickListener() {
			@Override
			public void onLeftClicked(CommonTitle view, View v) {
				viewBack();
			}

			@Override
			public void onRightClicked(CommonTitle view, View v) {
			}

			@Override
			public void onRight2Clicked(CommonTitle view, View v) {
			}
		});
		mCommonTitle.setTitleText(title);
		vc.setTitleView(mCommonTitle);
		return mCommonTitle;
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode,
	                                Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
	}

	protected boolean checkLogin() {
		return Constant.studentId != null && !"".equals(Constant.studentId);
	}

	public View getContentView() {
		return vc.getContentView();
	}

	// titlebar以下
	public View getBodyView() {
		return vc.getBodyView();
	}

	public ViewContainer getVc() {
		return vc;
	}

	@Override
	public void viewBack() {
		finish();
	}

	private Toast toast;

	public void makeText(Context context, CharSequence s) {
		try {
			if (null == context || null == s) {
				return;
			}
			if (toast == null) {
				toast = Toast.makeText(context, s, Toast.LENGTH_SHORT);
			}
			toast.setText(s);
			toast.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void showToast(String msg) {
		if (!TextUtils.isEmpty(msg))
			makeText(getContext(), msg);
	}

	private Dialog mDialog;

	public void showDialog() {
		showDialog("");
	}

	public void showDialog(String msg) {
		if (!this.isFinishing()) {
			if (mDialog == null)
				mDialog = NewToast.makeDialog(this, msg);
			if (!mDialog.isShowing())
				mDialog.show();
		}
	}

	public void dismissDialog() {
		if (!this.isFinishing() && mDialog != null) {
			mDialog.dismiss();
			mDialog = null;
		}
	}

	@Override
	public void viewRefresh() {

	}

	@Override
	public void viewLoadMore() {

	}
}