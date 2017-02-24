package com.luxg.course.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
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
import com.luxg.course.util.BaseUtil;
import com.luxg.course.util.NetWorkUtil;
import com.luxg.course.util.PreferenceUtil;
import com.luxg.course.widget.basewise.BaseWiseActivity;
import com.luxg.course.widget.basewise.Constant;

import java.lang.reflect.Type;

/**
 * Created by LuXiaogang on 2016/4/13.   用户修改密码
 */
public class PersonalSecurityActivity extends BaseWiseActivity {
    private EditText mOldPassword;
    private EditText mNewPassword;
    private EditText mNewPasswordAgain;
    private Button mCommit;
    private String mOld;
    private String mNew;
    private String mNewAgain;
    private Context mContext;
    private Animation shake;
    private ImageView password_icon, password2_icon, password3_icon;
    private SharedPreferences userSpf;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security_personal);
        setTitleView("账户与安全");
        mContext = PersonalSecurityActivity.this;
        userSpf = PreferenceUtil.getSharedPreferences(mContext);
        findView();
        initView();
        findView();
    }

    private void findView() {
        mOldPassword = (EditText) findViewById(R.id.personal_login_old_password);
        mNewPassword = (EditText) findViewById(R.id.personal_login_password);
        mNewPasswordAgain = (EditText) findViewById(R.id.personal_login_password_again);
        mCommit = (Button) findViewById(R.id.personal_login_password_button);
        shake = AnimationUtils.loadAnimation(mContext, R.anim.personal_shake);
        password_icon = (ImageView) findViewById(R.id.password_icon);
        password2_icon = (ImageView) findViewById(R.id.password2_icon);
        password3_icon = (ImageView) findViewById(R.id.password3_icon);
    }

    private void initView() {
        mOldPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    password_icon.setImageResource(R.drawable.personal_icon_password_black);
                    if (s.length() > 0 && mNewPasswordAgain.length() >= 6 && mNewPassword.length() >= 6 ) {
                        mCommit.setEnabled(true);
                    } else {
                        mCommit.setEnabled(false);
                    }
                } else {
                    password_icon.setImageResource(R.drawable.personal_icon_password_gray);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mNewPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    password2_icon.setImageResource(R.drawable.personal_icon_password_black);
                    if (s.length() >= 6  && mOldPassword.length() > 0 && mNewPasswordAgain.length() >= 6 ) {
                        mCommit.setEnabled(true);
                    } else {
                        mCommit.setEnabled(false);
                    }
                } else {
                    password2_icon.setImageResource(R.drawable.personal_icon_password_gray);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mNewPasswordAgain.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    password3_icon.setImageResource(R.drawable.personal_icon_password_black);
                    if (s.length() >= 6  && mOldPassword.length() > 0 && mNewPassword.length() >= 6 ) {
                        mCommit.setEnabled(true);
                    } else {
                        mCommit.setEnabled(false);
                    }
                } else {
                    password3_icon.setImageResource(R.drawable.personal_icon_password_gray);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mCommit.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                mOld = mOldPassword.getText().toString().trim();
                mNew = mNewPassword.getText().toString().trim();
                mNewAgain = mNewPasswordAgain.getText().toString();
                if (!BaseUtil.isPassword(mNew)) {
                    showToast("密码不符合规范，请重新输入");
                    mNewPassword.requestFocus();
                    mNewPassword.startAnimation(shake);
                    return;
                }
                if (!mNew.equals(mNewAgain)) {
                    showToast("两次输入的密码不一致，请重新输入");
                    return;
                }
                changePassword(mOld,mNew);
            }

        });
    }

    /**
     * 修改密码的网络请求
     */
    private void changePassword(String oldpsw , final String newpsw) {
        showDialog();
        if (NetWorkUtil.isNetworkAvailable(getContext())) {
            //网络可用
            HttpUtils http = new HttpUtils();
            String Url = Constant.URL + "changeStudentPswById?studentId=" + Constant.studentId +"&oldstudentPassword=" + oldpsw +"&newstudentPassword=" + newpsw;
            System.out.println("网络请求地址——" +  Url );
            http.send(HttpRequest.HttpMethod.POST, Url, new RequestCallBack<String>(){

                @Override
                public void onSuccess(ResponseInfo<String> responseInfo) {
                    dismissDialog();
                    /**
                     * 解析从后台取回的json数据
                     */
                    System.out.println("获取传回信息 : " + responseInfo.result.toString());
                    String jsonString = responseInfo.result;
                    Type type = new TypeToken<Boolean>() {
                    }.getType();
                    Gson json = new Gson();
                    boolean b = json.fromJson(jsonString, type);
                    /**
                     * 解析从后台取回的json数据
                     */
                    if (b) {
                        SharedPreferences.Editor editor = userSpf.edit();
                        Constant.studentPassword = newpsw;
                        editor.putString("studentPassword", Constant.studentPassword);
                        editor.commit();
                        dismissDialog();
                        showToast("修改成功");
                        finish();
                        overridePendingTransition(R.anim.personal_back_in, R.anim.personal_back_left_out);
                    }else {
                        dismissDialog();
                        showToast("修改失败");
                    }
                }

                @Override
                public void onFailure(HttpException e, String s) {
                    dismissDialog();
                    showToast("修改失败");
                }
            });
        } else {
            //网络不可用
            dismissDialog();
            showToast("网络不通畅，请连接网络。。。");
        }

    }
}
