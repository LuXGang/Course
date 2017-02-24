package com.luxg.course.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.luxg.course.R;
import com.luxg.course.model.StudentImageInfo;
import com.luxg.course.model.StudentInfo;
import com.luxg.course.util.ImageUtil;
import com.luxg.course.util.NetWorkUtil;
import com.luxg.course.util.PreferenceUtil;
import com.luxg.course.widget.base.BezelImageView;
import com.luxg.course.widget.basewise.BaseWiseActivity;
import com.luxg.course.widget.basewise.CommonTitle;
import com.luxg.course.widget.basewise.Constant;

import java.lang.reflect.Type;

/**
 * Created by Administrator on 2016/2/29.
 */
public class PersonalMainActivity extends BaseWiseActivity implements
        View.OnClickListener {
    private BezelImageView personal_main_pic;
    private TextView tv_student_name;
    private RelativeLayout rl_timetable, rl_my_course, rl_comment, rl_signup_course, rl_safe, rl_setting;
    private SharedPreferences userSpf;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_personal);
        CommonTitle commonTitle = (CommonTitle) findViewById(R.id.titleView);
        commonTitle.setLeftImageBg(R.drawable.personal_btn_back_white);
        commonTitle.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        commonTitle.setOnTitleClickListener(new CommonTitle.TitleClickListener() {
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
        userSpf = PreferenceUtil.getSharedPreferences(this);
        findView();
        initView();

    }

    private void initView() {
        tv_student_name.setText(Constant.studentName);
    }

    private void findView() {
        personal_main_pic = (BezelImageView) findViewById(R.id.personal_main_pic);
        tv_student_name = (TextView) findViewById(R.id.tv_student_name);
        rl_timetable = (RelativeLayout) findViewById(R.id.rl_timetable);
        rl_my_course = (RelativeLayout) findViewById(R.id.rl_my_course);
        rl_comment = (RelativeLayout) findViewById(R.id.rl_comment);
        rl_signup_course = (RelativeLayout) findViewById(R.id.rl_signup_course);
        rl_safe = (RelativeLayout) findViewById(R.id.rl_safe);
        rl_setting = (RelativeLayout) findViewById(R.id.rl_setting);
        personal_main_pic.setOnClickListener(this);
        rl_timetable.setOnClickListener(this);
        rl_my_course.setOnClickListener(this);
        rl_comment.setOnClickListener(this);
        rl_signup_course.setOnClickListener(this);
        rl_safe.setOnClickListener(this);
        rl_setting.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getStudentPic();

    }

    /**
     * 获取头像
     */
    private void getStudentPic() {
        showDialog();
        if (NetWorkUtil.isNetworkAvailable(getContext())) {
            //网络可用
            HttpUtils http = new HttpUtils();
            String Url = Constant.URL + "findStudentImageByStuId?studentId=" + Constant.studentId;
            System.out.println("网络请求地址——" + Url);
            http.send(HttpRequest.HttpMethod.POST, Url, new RequestCallBack<String>() {

                @Override
                public void onSuccess(ResponseInfo<String> responseInfo) {

                    /**
                     * 解析从后台取回的json数据
                     */
                    System.out.println("获取返回信息 : " + responseInfo.result.toString());
                    String jsonString = responseInfo.result;
                    Type type = new TypeToken<StudentImageInfo>() {
                    }.getType();
                    Gson json = new Gson();
                    StudentImageInfo b = json.fromJson(jsonString, type);
                    /**
                     * 解析从后台取回的json数据
                     */
                    if (b != null) {
                        dismissDialog();
                        String str = b.getImagePath();
                        if (!TextUtils.isEmpty(str)) {
                            SharedPreferences.Editor editor = userSpf.edit();
                            Constant.avatar = str;
                            editor.putString("avatar", Constant.avatar);
                            editor.commit();
                            ImageUtil.getInstance().displayImage(getContext(), personal_main_pic, str);
                        }
                    } else {
                        dismissDialog();
                        showToast("获取头像失败");
                    }
                }

                @Override
                public void onFailure(HttpException e, String s) {
                    dismissDialog();
                    showToast("获取头像失败");
                }
            });
        } else {
            //网络不可用
            dismissDialog();
            showToast("网络不通畅，请连接网络。。。");
        }

    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        if (v.getId() == R.id.personal_main_pic) {//头像 ——》个人资料
            intent = new Intent(this, PersonalDetailActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.rl_timetable) {//我的课程表 ——》本地课表模块
            intent = new Intent(this, TimeTableActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.rl_my_course) {//我的选课 ——》已选课程
            intent = new Intent(this, MyCourseActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.rl_comment) {//我的评论 ——》评论
            intent = new Intent(this, MyCourseCommentActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.rl_signup_course) {//我要选课 ——》选课模块
            intent = new Intent(this, SignUpCourseActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.rl_safe) {//账户安全 ——》更改密码等
            intent = new Intent(this, PersonalSecurityActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.rl_setting) {//设置 ——》清除缓存，用户帮助，关于我们等
            intent = new Intent(this, PersonalSettingActivity.class);
            startActivityForResult(intent, 66);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == 66 && resultCode == 300) {// 退出登录 直接回到首页
            intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
