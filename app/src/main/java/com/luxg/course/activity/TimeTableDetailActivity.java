package com.luxg.course.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.luxg.course.R;
import com.luxg.course.widget.basewise.BaseWiseActivity;
import com.luxg.course.widget.basewise.CommonTitle;

/**
 * Created by LuXiaogang on 2016/2/22.
 */
public class TimeTableDetailActivity extends BaseWiseActivity {
    private CommonTitle commonTitle;
    public TextView tv_weekday , tv_time ;
    public EditText et_name, et_address, et_comment ;
    public String weekday ,time ,name ,address ,comment , day ,timeCN;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable_detail);
        getData();
        commonTitle = new CommonTitle(getContext());
        commonTitle.setOnTitleClickListener(new CommonTitle.TitleClickListener(){

            @Override
            public void onLeftClicked(CommonTitle view, View v) {
                finish();
            }

            @Override
            public void onRightClicked(CommonTitle view, View v) {
                Intent intent = new Intent(getActivity(), TimeTableDetailEditActivity.class);
                intent.putExtra("weekday",weekday);
                intent.putExtra("time",time);
                intent.putExtra("name",name);
                intent.putExtra("address",address);
                intent.putExtra("comment",comment);
                startActivityForResult(intent,200);
            }

            @Override
            public void onRight2Clicked(CommonTitle view, View v) {

            }
        });
        CharSequence cs = "编辑";
        commonTitle.setRightText(cs);
        commonTitle.setTitleText("课表详情");
        setTitleView(commonTitle);
        findView();
        initView();

    }

    private void getData() {
        weekday = getIntent().getStringExtra("weekday");
        time = getIntent().getStringExtra("time");
        name = getIntent().getStringExtra("name");
        address = getIntent().getStringExtra("address");
        comment = getIntent().getStringExtra("comment");
        switch (weekday) {
            case "1": day = "星期一"; break;
            case "2": day = "星期二"; break;
            case "3": day = "星期三"; break;
            case "4": day = "星期四"; break;
            case "5": day = "星期五"; break;
            case "6": day = "星期六"; break;
            case "7": day = "星期天"; break;
        }
        switch (time) {
            case "1": timeCN = "第一大节"; break;
            case "2": timeCN = "第二大节"; break;
            case "3": timeCN = "第三大节"; break;
            case "4": timeCN = "第四大节"; break;
            case "5": timeCN = "晚间自习"; break;
        }
    }

    private void findView() {
        tv_weekday = (TextView) findViewById(R.id.tv_weekday);
        tv_time = (TextView) findViewById(R.id.tv_time);
        et_name = (EditText) findViewById(R.id.et_name);
        et_address = (EditText) findViewById(R.id.et_address);
        et_comment = (EditText) findViewById(R.id.et_comment);
    }

    private void initView() {
        tv_weekday.setText(day);
        tv_time.setText(timeCN);
        et_name.setText(name);
        et_address.setText(address);
        et_comment.setText(comment);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == RESULT_OK && requestCode == 200) {
            finish();
        }
    }
}
