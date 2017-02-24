package com.luxg.course.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.luxg.course.R;
import com.luxg.course.widget.basewise.BaseWiseActivity;


public class BaseAdWebActivity extends BaseWiseActivity {
	public static final String URLSTR = "URL";// 静态常量赋初值
	public static final String TITLE = "TITLE"; // titlebar 标题

	private String BASE_URL = "";
	private String mTitle = "";
	private String mContent = "";

	private WebView mWebView;
	private Context mContext;

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webview_layout);
		mContext = this;

		BASE_URL = getIntent().getStringExtra(URLSTR);// 获取传来的id关键
		mTitle = getIntent().getStringExtra(TITLE);
		if (TextUtils.isEmpty(mTitle)){
			mTitle = "";
		}
		setTitleView(mTitle);
		mWebView = (WebView) findViewById(R.id.webview);
		WebSettings setting = mWebView.getSettings();
		setting.setJavaScriptEnabled(true);
		mWebView.setWebChromeClient(new MyWebChromeClient());
		// mWebView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
		mWebView.loadUrl(BASE_URL);

		mWebView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				if (url.contains(".apk")) {
					Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
					BaseAdWebActivity.this.startActivity(intent);
				} else if (url.equals("http://login/")) {
					Intent intent = new Intent();
					intent.setClass(mContext, LoginActivity.class);
					mContext.startActivity(intent);
					((Activity) mContext).finish();
				} else {
					view.loadUrl(url);
				}

				return true;
			}
		});
	}

	class MyWebChromeClient extends WebChromeClient {

		@Override
		public void onProgressChanged(WebView view, int newProgress) {
			super.onProgressChanged(view, newProgress);
		}

		/**
		 * 处理alert弹出框
		 */
		@Override
		public boolean onJsAlert(WebView view, String url, String message,
		                         JsResult result) {
			Log.d("radom", "onJsAlert:" + message);
			// 对alert的简单封装
			new AlertDialog.Builder(BaseAdWebActivity.this)
					.setTitle("提示")
					.setMessage(message)
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface arg0,
								                    int arg1) {

								}
							}).create().show();
			result.confirm();
			return true;
		}

		@Override
		public boolean onJsConfirm(WebView view, String url, final String message,
		                           final JsResult result) {

			final AlertDialog.Builder builder = new AlertDialog.Builder(
					view.getContext());
			builder.setTitle("提示")
					.setMessage(message)
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
								                    int which) {
									if (message != null
											&& message.contains("12320")) {
										Intent intent = new Intent(
												Intent.ACTION_DIAL, Uri
												.parse("tel:12320"));
										BaseAdWebActivity.this
												.startActivity(intent);
									}
									result.confirm();
								}
							})
					.setNeutralButton("取消",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
								                    int which) {
									result.cancel();
								}
							});
			builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
				@Override
				public void onCancel(DialogInterface dialog) {
					result.cancel();
				}
			});

			// 屏蔽keycode等于84之类的按键，避免按键后导致对话框消息而页面无法再弹出对话框的问题
			builder.setOnKeyListener(new DialogInterface.OnKeyListener() {
				@Override
				public boolean onKey(DialogInterface dialog, int keyCode,
				                     KeyEvent event) {
					return true;
				}
			});
			AlertDialog dialog = builder.create();
			dialog.show();
			return true;
		}

		/**
		 * 处理prompt弹出框
		 */
		@Override
		public boolean onJsPrompt(WebView view, String url, String message,
		                          String defaultValue, JsPromptResult result) {
			Log.d("radom", "onJsPrompt:" + message);
			Toast.makeText(BaseAdWebActivity.this, message, Toast.LENGTH_SHORT)
					.show();
			result.confirm();
			return super.onJsPrompt(view, url, message, message, result);
		}

		@Override
		public boolean onJsBeforeUnload(WebView view, String url,
		                                String message, JsResult result) {
			Log.d("radom", "onJsBeforeUnload:" + url);
			return super.onJsBeforeUnload(view, url, message, result);
		}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mWebView != null) {
			mWebView.setVisibility(View.GONE);
			mWebView.destroy();
		}
	}
}
