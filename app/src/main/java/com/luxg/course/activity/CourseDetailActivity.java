package com.luxg.course.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
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
import com.luxg.course.widget.basewise.Constant;

import java.io.File;
import java.lang.reflect.Type;

/**
 * Created by LuXiaogang on 2016/2/26.
 */
public class CourseDetailActivity extends BaseWiseActivity implements OnClickListener{
    private TeachingInfo teachingInfo;
    private TextView tv_name, tv_address, tv_weekday, tv_time, tv_number, tv_comment ,tv_teacher , tv_hour , tv_credit;
    private Button bt_commit;
    private LinearLayout ll_video ;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);
        setTitleView("课程详情");
        getData();
        findView();
        initData();
    }

    private void getData() {
        teachingInfo = (TeachingInfo) getIntent().getSerializableExtra("TeachingInfo");
    }

    private void findView() {
        ll_video = (LinearLayout) findViewById(R.id.ll_video);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_address = (TextView) findViewById(R.id.tv_address);
        tv_weekday = (TextView) findViewById(R.id.tv_weekday);
        tv_time = (TextView) findViewById(R.id.tv_time);
        tv_number = (TextView) findViewById(R.id.tv_number);
        tv_comment = (TextView) findViewById(R.id.tv_comment);
        tv_teacher = (TextView) findViewById(R.id.tv_teacher);
        tv_hour = (TextView) findViewById(R.id.tv_hour);
        tv_credit = (TextView) findViewById(R.id.tv_credit);
        bt_commit = (Button) findViewById(R.id.bt_commit);
        bt_commit.setOnClickListener(this);
        ll_video.setOnClickListener(this);
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

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.bt_commit) {
            //确定按钮
            CommitSignUp(Constant.studentId, teachingInfo.getCourseId() ,teachingInfo.getTeacherId() );
        } else if (v.getId() == R.id.ll_video) {
            //videoIntroduce
            Intent intent = new Intent(CourseDetailActivity.this, VideoViewDemoActivity.class);
            if (teachingInfo.getCourse().getCourseVideo().startsWith("http://")) {
                intent.putExtra("VIDEO_URL",teachingInfo.getCourse().getCourseVideo() == null ? "" : teachingInfo.getCourse().getCourseVideo() );
            }else {
                //Environment.getExternalStorageDirectory().getPath()+"/Test_Movie.m4v"
                File file=new File(Environment.getExternalStorageDirectory(),teachingInfo.getCourse().getCourseVideo());
                intent.putExtra("VIDEO_URL", file.getPath() );
            }

            startActivity(intent);
        }
    }

    private void CommitSignUp(String sId ,String cId ,String tId) {
        showDialog();
        if (NetWorkUtil.isNetworkAvailable(getContext())) {
            //网络可用
            HttpUtils http = new HttpUtils();
            String Url = Constant.URL + "addElectiveByAllId?studentId=" + sId +"&courseId=" + cId + "&teacherId="+tId ;
            System.out.println("网络请求地址——" +  Url );
            http.send(HttpRequest.HttpMethod.POST, Url, new RequestCallBack<String>(){

                @Override
                public void onSuccess(ResponseInfo<String> responseInfo) {
                    dismissDialog();
                    /**
                     * 解析从后台取回的json数据
                     */
                    System.out.println("获取选课结果信息 : " + responseInfo.result.toString());
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
                        showPop();
                    }else {
                        dismissDialog();
                        showToast("选课失败");
                    }
                }

                @Override
                public void onFailure(HttpException e, String s) {
                    dismissDialog();
                    showToast("选课失败");
                }
            });
        } else {
            //网络不可用
            dismissDialog();
            showToast("网络不通畅，请连接网络。。。");
        }

    }

    /**
     * PopupWindow 创建  将这节课的选择信息显示在 PopupWindow 上
     */
    private void showPop() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.course_pop_window, null);
        final PopupWindow mPop = new PopupWindow(view, ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT, true);
        mPop.setOutsideTouchable(false);
        view.findViewById(R.id.tv_success).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mPop.dismiss();
                CourseDetailActivity.this.finish();
            }
        });
        mPop.showAtLocation(getContentView(), Gravity.BOTTOM, 0, 0);
    }

}
