package com.luxg.course.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.luxg.course.R;
import com.luxg.course.db.SyllabusDatabase;
import com.luxg.course.model.SyllabusDayInfo;
import com.luxg.course.widget.basewise.BaseWiseActivity;

/**
 * Created by LuXiaogang on 2016/2/22.
 * 课程表的修改跟添加
 */
public class TimeTableDetailEditActivity extends BaseWiseActivity implements OnClickListener {
    public TextView tv_weekday, tv_time;
    public Button bt_commit;
    public EditText et_name, et_address, et_comment;
    public String weekday, time, name, address, comment, day, timeCN;
    public SyllabusDatabase db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable_edit);
        getData();
        if ("add".equals(getIntent().getStringExtra("type")) ) {
            setTitleView("添加");
        }else {
            setTitleView("课表详情编辑");
        }
        findView();
        initView();

    }

    private void getData() {
        db = new SyllabusDatabase(getContext());
        weekday = getIntent().getStringExtra("weekday");
        time = getIntent().getStringExtra("time");
        name = getIntent().getStringExtra("name");
        address = getIntent().getStringExtra("address");
        comment = getIntent().getStringExtra("comment");
        switch (weekday) {
            case "1":day = "星期一";break;
            case "2":day = "星期二";break;
            case "3":day = "星期三";break;
            case "4":day = "星期四";break;
            case "5":day = "星期五";break;
            case "6":day = "星期六";break;
            case "7":day = "星期天";break;
        }
        switch (time) {
            case "1":timeCN = "第一大节";break;
            case "2":timeCN = "第二大节";break;
            case "3":timeCN = "第三大节";break;
            case "4":timeCN = "第四大节";break;
            case "5":timeCN = "晚间自习";break;
        }
    }

    private void findView() {
        tv_weekday = (TextView) findViewById(R.id.tv_weekday);
        tv_time = (TextView) findViewById(R.id.tv_time);
        et_name = (EditText) findViewById(R.id.et_name);
        et_address = (EditText) findViewById(R.id.et_address);
        et_comment = (EditText) findViewById(R.id.et_comment);
        bt_commit = (Button) findViewById(R.id.bt_commit);
        bt_commit.setOnClickListener(this);
    }

    private void initView() {
        tv_weekday.setText(day);
        tv_time.setText(timeCN);
        et_name.setText(name);
        et_address.setText(address);
        et_comment.setText(comment);
    }



    @Override
    public void onClick(View v) {
        /**
         * 完成按钮点击事件 添加或更新
         */
        if (v.getId() == R.id.bt_commit) {
            checkCommit();
        }

    }

    private void checkCommit() {
        if (TextUtils.isEmpty(et_name.getText())){
            showToast("请输入课程名称");
            return;
        }
        if (TextUtils.isEmpty(et_address.getText())){
            showToast("请输入上课地点");
            return;
        }
        if ( (name == null ? "" : name ).equals(et_name.getText().toString())
                && (address == null ? "" : address ).equals(et_address.getText().toString())
                && (comment == null ? "" : comment ).equals(et_comment.getText() == null ? "" : et_comment.getText().toString() )) {
            showToast("没有任何数据修改");
            return;
        }
        SyllabusDayInfo data = new SyllabusDayInfo();
        data.setWeek_day_no(weekday);
        data.setCourse_time(time);
        data.setCourse_name(et_name.getText().toString());
        data.setCourse_address(et_address.getText().toString());
        data.setCourse_comments(et_comment.getText() == null ? "" : et_comment.getText().toString());
        db.insertCourseTimeTable(data);
        if ("add".equals(getIntent().getStringExtra("type")) ) {
            finish();
        }else {
            setResult(RESULT_OK);
            finish();
        }
    }
    /**
     *
     */


}
