package com.luxg.course.activity;

import android.os.Bundle;
import com.luxg.course.R;
import com.luxg.course.widget.basewise.BaseWiseActivity;


/**
 * Created by Administrator on 2016/4/27.
 */
public class SchoolCalendarActivity extends BaseWiseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_calendar);
        setTitleView("校历");
    }
}
