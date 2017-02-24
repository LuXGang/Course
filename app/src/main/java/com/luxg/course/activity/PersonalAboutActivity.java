package com.luxg.course.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.luxg.course.R;
import com.luxg.course.widget.basewise.BaseWiseActivity;
import com.luxg.course.widget.basewise.Constant;


public class PersonalAboutActivity extends BaseWiseActivity {
	private TextView tv_version, text_kefu , text_web;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about_page);
		setTitleView("关于我们");
		findView();
	}

	private void findView() {
		tv_version = (TextView) findViewById(R.id.tv_version);
		text_kefu = (TextView) findViewById(R.id.text_kefu);
		text_web = (TextView) findViewById(R.id.text_web);
		setText(tv_version, "V " + Constant.getVersionName());
		setText(text_web, Html.fromHtml("<u>www.xzit.edu.cn</u>"));
		setText(text_kefu, "150-5141-3372");
		text_kefu.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				handleEvent("tel:15051413372");
			}
		});
		text_web.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				handleUrl("http://www.xzit.edu.cn");
			}
		});
	}

	/**
	 * 处理网站跳转
	 * 
	 * @param url
	 */
	private void handleUrl(String url) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setData(Uri.parse(url));
		startActivity(intent);
	}

	/**
	 * 处理打电话
	 * 
	 * @param url
	 */
	private void handleEvent(String url) {
		Uri uri = Uri.parse(url);
		Intent intent = new Intent(Intent.ACTION_DIAL, uri);
		startActivity(intent);
	}

}
