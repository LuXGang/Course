package com.luxg.course.widget.basewise;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;



import java.util.HashMap;
import java.util.Map;

import com.luxg.course.R;

public class ViewContainer {
	public static final String KEY_CONTENT = "content";
	public static final String KEY_NONET = "nonet";
	public static final String KEY_ERROR = "error";
	public static final String KEY_EMPTY = "empty";
	public static final String KEY_LOADING = "loading";

	private Context context;
	private RelativeLayout rootView;
	private View bodyView;
	private String keyOfShownView;
	private OnPageRefreshClickListener onPageRefreshClickListener;
	private HashMap<String, View> viewMap = new HashMap<String, View>();
	private ProgressDialog dialog;

	public ViewContainer(Context mContext, int layout) {
		this.context = mContext;
		rootView = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.view_container_layout, null);

		bodyView = View.inflate(context, layout, null);
		rootView.addView(bodyView, 0);
		RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
		rlp.addRule(RelativeLayout.BELOW, R.id.layout_head);
		rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		bodyView.setLayoutParams(rlp);
		keyOfShownView = KEY_CONTENT;
		viewMap.put(KEY_CONTENT, bodyView);

		setNonetView(R.id.container_nonet);
		setErrorView(R.id.container_error);
		setEmptyView(R.id.container_empty);
		setLoadingView(R.id.container_loading);
	}

	public View getContentView() {
		return rootView;
	}

	public View getBodyView() {
		return bodyView;
	}

	protected void setBackgroundColor(int colorId) {
		rootView.setBackgroundColor(colorId);
	}

	public ViewContainer setNonetView(int layout) {
		View view = rootView.findViewById(layout);
		view.setTag("Nonet");
		view.findViewById(R.id.btn_refresh).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (onPageRefreshClickListener != null)
					onPageRefreshClickListener.onPageRefresh(v);
			}
		});
		viewMap.put(KEY_NONET, view);
		return this;
	}

	public ViewContainer setErrorView(int layout) {
		View view = rootView.findViewById(layout);
		view.setTag("Error");
		view.findViewById(R.id.btn_refresh).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (onPageRefreshClickListener != null)
					onPageRefreshClickListener.onPageRefresh(v);
			}
		});
		view.findViewById(R.id.btn_back).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (onPageRefreshClickListener != null)
					onPageRefreshClickListener.onPageBack(v);
			}
		});
		viewMap.put(KEY_ERROR, view);
		return this;
	}

	public ViewContainer setEmptyView(int layout) {
		View view = rootView.findViewById(layout);
		view.setTag("Empty");
		viewMap.put(KEY_EMPTY, view);
		return this;
	}

	public ViewContainer setLoadingView(int layout) {
//        View view = View.inflate(context, layout, null);
		View view = rootView.findViewById(layout);
		view.setTag("Loading");
		viewMap.put(KEY_LOADING, view);
		return this;
	}

	public ViewContainer setOnPageRefreshClickListener(OnPageRefreshClickListener onClickListener) {
		this.onPageRefreshClickListener = onClickListener;
		return this;
	}

	public ViewContainer addView(String key, View view) {
		viewMap.put(key, view);
		return this;
	}

	public View build(Object o) {
		return build(o, ViewContainer.KEY_CONTENT);
	}

	public View build(Object o, String defaultShowKey) {
		for (Map.Entry<String, View> entry : viewMap.entrySet()) {
			String key = entry.getKey();
			View view = entry.getValue();
			if (!defaultShowKey.equals(key)) {
				if (!KEY_CONTENT.equals(key))
					view.setVisibility(View.GONE);
			}
//            bodyView.addView(view, new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
		}
		if (o instanceof Activity) {
			((Activity) o).setContentView(rootView);
		}
//        if (onClickListener != null) {
//            viewMap.get(KEY_NONET).setOnClickListener(onClickListener);
//            viewMap.get(KEY_ERROR).setOnClickListener(onClickListener);
//            viewMap.get(KEY_EMPTY).setOnClickListener(onClickListener);
//        }
		return rootView;
	}

	public void showContent() {
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				show(KEY_CONTENT);
			}
		}, 500);
	}

	public void showNonet() {
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				show(KEY_NONET);
			}
		}, 500);
	}

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {

		}
	};

	public void showError() {
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				show(KEY_ERROR);
			}
		}, 500);
	}

	public void showEmpty() {
		show(KEY_EMPTY);
	}

	public void showLoading() {
		show(KEY_LOADING);
	}

	public void showView(String key) {
		show(key);
	}

	public void showProgressDialog() {
		if (dialog == null)
			dialog = showProgressDialog(context, "加载中...", null, null, null);
		else
			dialog.show();
	}

	public void dismissProgressDialog() {
		if (dialog != null)
			dialog.dismiss();
	}

	private ProgressDialog showProgressDialog(Context ctx, String title,
	                                          String message, DialogInterface.OnClickListener listen,
	                                          String button_name) {
		ProgressDialog dialog = new ProgressDialog(ctx);
		dialog.setTitle("");
		dialog.setMessage("加载中...");
		dialog.setIndeterminate(true);
		dialog.setCancelable(true);
		dialog.show();
		return dialog;
	}


	private void show(String key) {
		if (!key.equals(keyOfShownView)) {
			View v_shown = viewMap.get(keyOfShownView);
			View view = viewMap.get(key);
			if (!KEY_CONTENT.equals(keyOfShownView))
				v_shown.setVisibility(View.GONE);
			if (!KEY_CONTENT.equals(key))
				view.setVisibility(View.VISIBLE);
			keyOfShownView = key;
		}
	}

	// save this if you need to SaveInstanceState
	public String getKeyOfShownView() {
		return keyOfShownView;
	}

	public void setTitleView(View view) {
		RelativeLayout layout_head = (RelativeLayout) rootView.findViewById(R.id.layout_head);
		layout_head.addView(view, new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
	}

	public void setTitleView2(View view) {
		rootView.addView(view);
	}
}
