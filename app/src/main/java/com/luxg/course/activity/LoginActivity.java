package com.luxg.course.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.luxg.course.R;
import com.luxg.course.model.StudentInfo;
import com.luxg.course.util.NetWorkUtil;
import com.luxg.course.util.PreferenceUtil;
import com.luxg.course.widget.basewise.BaseWiseActivity;
import com.luxg.course.widget.basewise.CommonTitle;
import com.luxg.course.widget.basewise.Constant;

import java.lang.reflect.Type;


public class LoginActivity extends BaseWiseActivity implements OnClickListener {

	private Button login; // 登录按钮
	private EditText username, password; // 用户名，密码输入框
	private Animation shake;
	private SharedPreferences userSpf;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_activity);
		CommonTitle commonTitle = setTitleView("");
		commonTitle.setBackgroundColor(getResources().getColor(R.color.BackgroundColor));
		commonTitle.setLeftImageBg(R.drawable.personal_btn_cancle);

		userSpf = PreferenceUtil.getSharedPreferences(this);
		initView();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	private void initView() {
		username = (EditText) findViewById(R.id.login_username);
		password = (EditText) findViewById(R.id.login_password);
		ImageView headImageView = (ImageView) findViewById(R.id.user_icon);
		final ImageView iconUser = (ImageView) findViewById(R.id.icon_user); // 用户名前面的小图标
		final ImageView iconPassword = (ImageView) findViewById(R.id.icon_password); // 密码前面的小图标
		username.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (s.length() > 0) {
					iconUser.setImageResource(R.drawable.personal_icon_user_black);
					if (s.length() >= 4 && password.length() >= 6) {
						login.setEnabled(true);
					} else {
						login.setEnabled(false);
					}
				} else {
					iconUser.setImageResource(R.drawable.personal_icon_user_gray);
				}
			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
		password.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (s.length() > 0) {
					iconPassword.setImageResource(R.drawable.personal_icon_password_black);
					if (s.length() >= 6 && username.length() >= 4) {
						login.setEnabled(true);
					} else {
						login.setEnabled(false);
					}
				} else {
					iconPassword.setImageResource(R.drawable.personal_icon_password_gray);
				}
			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
		login = (Button) findViewById(R.id.login_login); // 登录按钮
		login.setOnClickListener(this);
		shake = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.personal_shake);

		// 忘记密码事件
		findViewById(R.id.personal_login_forget).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showToast("一会儿实现");
			}
		});
	}


	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.login_login) {
			if (username.getText().toString().trim().equals("")) {
				showToast("账号不能为空");
				username.requestFocus();
				username.startAnimation(shake);
				return;
			}
			if (password.getText().toString().trim().equals("")) {
				showToast("密码不能为空");
				password.requestFocus();
				password.startAnimation(shake);
				return;
			}

			login( username.getText().toString().trim(), password.getText().toString().trim());
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK && requestCode == 120) {
			if (data != null && data.getBooleanExtra("isRegist", false)) {
				finish();
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}


	private void login( final String userId, final String password) {
		showDialog("正在为您登录，请稍候...");
		if (NetWorkUtil.isNetworkAvailable(getContext())) {
			//网络可用
			HttpUtils http = new HttpUtils();
			String Url = Constant.URL + "findStudentByIdAndPsw?studentId=" + userId +"&studentPassword=" + password;
			System.out.println("网络请求地址——" +  Url );
			http.send(HttpRequest.HttpMethod.POST, Url, new RequestCallBack<String>(){

				@Override
				public void onSuccess(ResponseInfo<String> responseInfo) {
					dismissDialog();
					/**
					 * 解析从后台取回的json数据
					 */
					System.out.println("获取登录信息 : " + responseInfo.result.toString());
					String jsonString = responseInfo.result;
					Type type = new TypeToken<StudentInfo>() {
					}.getType();
					Gson json = new Gson();
					StudentInfo b = json.fromJson(jsonString, type);
					/**
					 * 解析从后台取回的json数据
					 */
					if (b != null) {
						loginSuccess(b);
					}else {
						dismissDialog();
						showToast("登录失败");
					}
				}

				@Override
				public void onFailure(HttpException e, String s) {
					dismissDialog();
					showToast("登录失败");
				}
			});
		} else {
			//网络不可用
			dismissDialog();
			showToast("网络不通畅，请连接网络。。。");
		}

	}

	private void loginSuccess(StudentInfo userInfo) {
		SharedPreferences.Editor editor = userSpf.edit();
		Constant.studentId = userInfo.getStudentId();
		Constant.studentPassword = userInfo.getStudentPassword();
		Constant.studentName = userInfo.getStudentName();
		Constant.studentBirth = userInfo.getStudentBirth();
		Constant.studentClass = userInfo.getStudentClass();//班级
		Constant.studentTime = userInfo.getStudentTime();//年级
		Constant.studentTel = userInfo.getStudentTel();
		Constant.studentSex = userInfo.getStudentSex();

		editor.putString("studentId", Constant.studentId);
		editor.putString("studentPassword", Constant.studentPassword);
		editor.putString("studentName", Constant.studentName);
		editor.putString("studentBirth", Constant.studentBirth);
		editor.putString("studentClass", Constant.studentClass);
		editor.putString("studentTime", Constant.studentTime);
		editor.putString("studentTel", Constant.studentTel);
		editor.putString("studentSex", Constant.studentSex);
		editor.putString("avatar", userInfo.getStudentImageInfo() == null ? "" : userInfo.getStudentImageInfo().get(0).getImagePath() );
		editor.commit();

		PreferenceUtil.putBoolean(getContext(), "isLogin", true);//将登录信息存入SharedPreferences；
		PreferenceUtil.putBoolean(getContext(), "module_refresh", true);

		showToast("登录成功");
		dismissDialog();
		setResult(200);//登录成功的返回值是200
		finish();
		overridePendingTransition(R.anim.personal_back_in, R.anim.personal_back_left_out);
	}


	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// 直接关闭本页面
			finish();
			overridePendingTransition(R.anim.personal_back_in, R.anim.personal_back_left_out);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}




	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}
