package com.luxg.course.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.luxg.course.R;
import com.luxg.course.model.TeachingInfo;
import com.luxg.course.util.NetWorkUtil;
import com.luxg.course.video.VideoViewDemoActivity;
import com.luxg.course.widget.basewise.BaseWiseActivity;
import com.luxg.course.widget.basewise.CommonTitle;
import com.luxg.course.widget.basewise.Constant;

import java.io.File;
import java.lang.reflect.Type;

/**
 * 我的选课详情
 * Created by LuXiaogang on 2016/3/4.
 */
public class MyCourseDetailActivity extends BaseWiseActivity {
    private TeachingInfo teachingInfo;
    private TextView tv_name, tv_address, tv_weekday, tv_time, tv_number, tv_comment ,tv_teacher , tv_hour , tv_credit;
    private CommonTitle mCommonTitle;
    private LinearLayout ll_video;
    private Button bt_cancel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_course_detail);

        mCommonTitle = new CommonTitle(getContext());
        mCommonTitle.setOnTitleClickListener(new CommonTitle.TitleClickListener() {
            @Override
            public void onLeftClicked(CommonTitle view, View v) {
                viewBack();
            }

            @Override
            public void onRightClicked(CommonTitle view, View v) {
                //评论
                Intent intent = new Intent(MyCourseDetailActivity.this, CourseCommentActivity.class);
                intent.putExtra("TeachingInfo", teachingInfo);
                startActivity(intent);
            }

            @Override
            public void onRight2Clicked(CommonTitle view, View v) {
            }
        });
        mCommonTitle.setTitleText("我的选课详情");
        CharSequence cs = "评论";
        mCommonTitle.setRightText(cs);
        setTitleView(mCommonTitle);
        getData();
        findView();
        initData();
    }

    private void getData() {
        teachingInfo = (TeachingInfo) getIntent().getSerializableExtra("TeachingInfo");
    }

    private void findView() {
        ll_video = (LinearLayout) findViewById(R.id.ll_video);
        bt_cancel = (Button) findViewById(R.id.bt_cancel);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_address = (TextView) findViewById(R.id.tv_address);
        tv_weekday = (TextView) findViewById(R.id.tv_weekday);
        tv_time = (TextView) findViewById(R.id.tv_time);
        tv_number = (TextView) findViewById(R.id.tv_number);
        tv_comment = (TextView) findViewById(R.id.tv_comment);
        tv_teacher = (TextView) findViewById(R.id.tv_teacher);
        tv_hour = (TextView) findViewById(R.id.tv_hour);
        tv_credit = (TextView) findViewById(R.id.tv_credit);
        /**
         * 点击事件
         */
        ll_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyCourseDetailActivity.this, VideoViewDemoActivity.class);
                if (teachingInfo.getCourse().getCourseVideo().startsWith("http://")) {
                    intent.putExtra("VIDEO_URL",teachingInfo.getCourse().getCourseVideo() == null ? "" : teachingInfo.getCourse().getCourseVideo() );
                }else {
                    //Environment.getExternalStorageDirectory().getPath()+"/Test_Movie.m4v"
                    File file=new File(Environment.getExternalStorageDirectory(),teachingInfo.getCourse().getCourseVideo());
                    intent.putExtra("VIDEO_URL", file.getPath() );
                }
                startActivity(intent);
            }
        });
        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //删除按钮
                CancelSignUp(Constant.studentId, teachingInfo.getCourseId() ,teachingInfo.getTeacherId() );
            }
        });
    }

    /**
     * 删除按钮
     */
    private void CancelSignUp(String studentId, String courseId, String teacherId) {
        showDialog();
        if (NetWorkUtil.isNetworkAvailable(getContext())) {
            //网络可用
            HttpUtils http = new HttpUtils();
            String Url = Constant.URL + "deleteElectiveByAllId?studentId=" + studentId +"&courseId=" + courseId + "&teacherId="+teacherId ;
            System.out.println("网络请求地址——" +  Url );
            http.send(HttpRequest.HttpMethod.POST, Url, new RequestCallBack<String>(){

                @Override
                public void onSuccess(ResponseInfo<String> responseInfo) {
                    dismissDialog();
                    /**
                     * 解析从后台取回的json数据
                     */
                    System.out.println("获取退课结果信息 : " + responseInfo.result.toString());
                    String jsonString = responseInfo.result;
                    Type type = new TypeToken<Boolean>() {
                    }.getType();
                    Gson json = new Gson();
                    Boolean b = json.fromJson(jsonString, type);
                    /**
                     * 解析从后台取回的json数据
                     */
                    if (b == true) {
                        dismissDialog();
                        showToast("退课成功");
                        finish();
                    }else {
                        dismissDialog();
                        showToast("退课失败");
                    }
                }

                @Override
                public void onFailure(HttpException e, String s) {
                    dismissDialog();
                    showToast("退课失败");
                }
            });
        } else {
            //网络不可用
            dismissDialog();
            showToast("网络不通畅，请连接网络。。。");
        }


    }

    private void initData() {
        tv_name.setText(teachingInfo.getCourse().getCourseName() == null ? "" : teachingInfo.getCourse().getCourseName());
        tv_address.setText(teachingInfo.getCourse().getCourseAddress() == null ? "" : teachingInfo.getCourse().getCourseAddress()) ;
        tv_number.setText(teachingInfo.getCourse().getCourseNumber() == null ? "" : teachingInfo.getCourse().getCourseNumber()) ;
        tv_comment.setText(teachingInfo.getCourse().getCourseIntroduce() == null ? "" : teachingInfo.getCourse().getCourseIntroduce()) ;
        tv_hour.setText(teachingInfo.getCourse().getCourseHour() == null ? "" : teachingInfo.getCourse().getCourseHour() ) ;
        tv_credit.setText(teachingInfo.getCourse().getCourseCredit() + "" ) ;
        tv_teacher.setText(teachingInfo.getTeacher().getTeacherName() == null ? "" : teachingInfo.getTeacher().getTeacherName()) ;
        /**
         * 转换
         */
        String day = "",time = "";
        switch (teachingInfo.getCourse().getCourseTimeDay()) {
            case "1": day = "星期一"; break;
            case "2": day = "星期二"; break;
            case "3": day = "星期三"; break;
            case "4": day = "星期四"; break;
            case "5": day = "星期五"; break;
            case "6": day = "星期六"; break;
            case "7": day = "星期天"; break;
        }
        switch (teachingInfo.getCourse().getCourseTime()) {
            case "1": time = "第一大节"; break;
            case "2": time = "第二大节"; break;
            case "3": time = "第三大节"; break;
            case "4": time = "第四大节"; break;
            case "5": time = "晚间自习"; break;
        }
        tv_weekday.setText(day) ;
        tv_time.setText(time) ;

    }

}
