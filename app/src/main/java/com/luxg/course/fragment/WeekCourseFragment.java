package com.luxg.course.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.luxg.course.R;
import com.luxg.course.activity.TimeTableDetailActivity;
import com.luxg.course.activity.TimeTableDetailEditActivity;
import com.luxg.course.db.SyllabusDatabase;
import com.luxg.course.model.SyllabusDayInfo;
import com.luxg.course.util.DialogHelper;

import java.util.ArrayList;

/**
 * Created by LuXiaogang on 2016/1/31.
 */
public class WeekCourseFragment extends Fragment implements
        OnClickListener {
    private LinearLayout ll_first, ll_second, ll_third, ll_fourth, ll_fifth;
    private TextView tv_first_name, tv_first_address, tv_second_name, tv_second_address, tv_third_name, tv_third_address, tv_fourth_name, tv_fourth_address, tv_fifth_name, tv_fifth_address;
    private ArrayList<SyllabusDayInfo> list;
    private String day , comment1, comment2 ,comment3 ,comment4 ,comment5;
    private SyllabusDatabase db;

    public static WeekCourseFragment newInstance( String weekday) {
        WeekCourseFragment fragment = new WeekCourseFragment();
        Bundle args = new Bundle();
        args.putString("day", weekday);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            /**
             * "1","2"代表周一，周二 以此类推
             * 表示这是周几的课程
             */
            day = getArguments().getString("day");

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.week_course_data_fragment, null);
        initView(view);
        return view;
    }

    private void initView(View view) {
        ll_first = (LinearLayout) view.findViewById(R.id.ll_first);
        ll_second = (LinearLayout) view.findViewById(R.id.ll_second);
        ll_third = (LinearLayout) view.findViewById(R.id.ll_third);
        ll_fourth = (LinearLayout) view.findViewById(R.id.ll_fourth);
        ll_fifth = (LinearLayout) view.findViewById(R.id.ll_fifth);

        tv_first_name = (TextView) view.findViewById(R.id.tv_first_name);
        tv_first_address = (TextView) view.findViewById(R.id.tv_first_address);
        tv_second_name = (TextView) view.findViewById(R.id.tv_second_name);
        tv_second_address = (TextView) view.findViewById(R.id.tv_second_address);
        tv_third_name = (TextView) view.findViewById(R.id.tv_third_name);
        tv_third_address = (TextView) view.findViewById(R.id.tv_third_address);
        tv_fourth_name = (TextView) view.findViewById(R.id.tv_fourth_name);
        tv_fourth_address = (TextView) view.findViewById(R.id.tv_fourth_address);
        tv_fifth_name = (TextView) view.findViewById(R.id.tv_fifth_name);
        tv_fifth_address = (TextView) view.findViewById(R.id.tv_fifth_address);

        ll_first.setOnClickListener(this);
        ll_second.setOnClickListener(this);
        ll_third.setOnClickListener(this);
        ll_fourth.setOnClickListener(this);
        ll_fifth.setOnClickListener(this);
        deletelongClick();

    }

    private void initData() {
        db = new SyllabusDatabase(getActivity().getBaseContext());
        list = (ArrayList<SyllabusDayInfo>) db.selectCourseTimeTableByDay(day);
        /**
         * "1","2"代表第一大节，第二大节 以此类推
         * 表示这是第几节课
         */
        if (list != null) {
            for (SyllabusDayInfo info : list) {
                switch (info.getCourse_time()) {
                    case "1":
                        tv_first_name.setText(info.getCourse_name());
                        tv_first_address.setText(info.getCourse_address());
                        comment1 = info.getCourse_comments();
                        break;
                    case "2":
                        tv_second_name.setText(info.getCourse_name());
                        tv_second_address.setText(info.getCourse_address());
                        comment2 = info.getCourse_comments();
                        break;
                    case "3":
                        tv_third_name.setText(info.getCourse_name());
                        tv_third_address.setText(info.getCourse_address());
                        comment3 = info.getCourse_comments();
                        break;
                    case "4":
                        tv_fourth_name.setText(info.getCourse_name());
                        tv_fourth_address.setText(info.getCourse_address());
                        comment4 = info.getCourse_comments();
                        break;
                    case "5":
                        tv_fifth_name.setText(info.getCourse_name());
                        tv_fifth_address.setText(info.getCourse_address());
                        comment5 = info.getCourse_comments();
                        break;
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_first:
                if (!TextUtils.isEmpty(tv_first_name.getText())) {
                    Intent intent = new Intent(getActivity(), TimeTableDetailActivity.class);
                    intent.putExtra("weekday",day);
                    intent.putExtra("time","1");
                    intent.putExtra("name",tv_first_name.getText() == null ? "" : tv_first_name.getText().toString());
                    intent.putExtra("address",tv_first_address.getText() == null ? "" : tv_first_address.getText().toString());
                    intent.putExtra("comment",comment1);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(getActivity(), TimeTableDetailEditActivity.class);
                    intent.putExtra("type","add");
                    intent.putExtra("weekday",day);
                    intent.putExtra("time","1");
                    startActivity(intent);
                }
                break;
            case R.id.ll_second:
                if (!TextUtils.isEmpty(tv_second_name.getText())) {
                    Intent intent = new Intent(getActivity(), TimeTableDetailActivity.class);
                    intent.putExtra("weekday",day);
                    intent.putExtra("time","2");
                    intent.putExtra("name",tv_second_name.getText() == null ? "" : tv_second_name.getText().toString());
                    intent.putExtra("address",tv_second_address.getText() == null ? "" : tv_second_address.getText().toString());
                    intent.putExtra("comment",comment2);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(getActivity(), TimeTableDetailEditActivity.class);
                    intent.putExtra("type","add");
                    intent.putExtra("weekday",day);
                    intent.putExtra("time","2");
                    startActivity(intent);
                }
                break;
            case R.id.ll_third:
                if (!TextUtils.isEmpty(tv_third_name.getText())) {
                    Intent intent = new Intent(getActivity(), TimeTableDetailActivity.class);
                    intent.putExtra("weekday",day);
                    intent.putExtra("time","3");
                    intent.putExtra("name",tv_third_name.getText() == null ? "" : tv_third_name.getText().toString());
                    intent.putExtra("address",tv_third_address.getText() == null ? "" : tv_third_address.getText().toString());
                    intent.putExtra("comment",comment3);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(getActivity(), TimeTableDetailEditActivity.class);
                    intent.putExtra("type","add");
                    intent.putExtra("weekday",day);
                    intent.putExtra("time","3");
                    startActivity(intent);
                }
                break;
            case R.id.ll_fourth:
                if (!TextUtils.isEmpty(tv_fourth_name.getText())) {
                    Intent intent = new Intent(getActivity(), TimeTableDetailActivity.class);
                    intent.putExtra("weekday",day);
                    intent.putExtra("time","4");
                    intent.putExtra("name",tv_fourth_name.getText() == null ? "" : tv_fourth_name.getText().toString());
                    intent.putExtra("address",tv_fourth_address.getText() == null ? "" : tv_fourth_address.getText().toString());
                    intent.putExtra("comment",comment4);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(getActivity(), TimeTableDetailEditActivity.class);
                    intent.putExtra("type","add");
                    intent.putExtra("weekday",day);
                    intent.putExtra("time","4");
                    startActivity(intent);
                }
                break;
            case R.id.ll_fifth:
                if (!TextUtils.isEmpty(tv_fifth_name.getText())) {
                    Intent intent = new Intent(getActivity(), TimeTableDetailActivity.class);
                    intent.putExtra("weekday",day);
                    intent.putExtra("time","5");
                    intent.putExtra("name",tv_fifth_name.getText() == null ? "" : tv_fifth_name.getText().toString());
                    intent.putExtra("address",tv_fifth_address.getText() == null ? "" : tv_fifth_address.getText().toString());
                    intent.putExtra("comment",comment5);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(getActivity(), TimeTableDetailEditActivity.class);
                    intent.putExtra("type","add");
                    intent.putExtra("weekday",day);
                    intent.putExtra("time","5");
                    startActivity(intent);
                }
                break;
        }
    }

    /**
     * PopupWindow 创建  将这节课的详细信息显示在 PopupWindow 上    没有用到 由于界面设计改变
     */
    private void showPop(String weekday, String name, String time, String address, String comment) {
        View view = LayoutInflater.from(getActivity().getBaseContext()).inflate(R.layout.timetable_popup_window, null);
        final PopupWindow mPop = new PopupWindow(view, ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT, true);
        mPop.setOutsideTouchable(true);
        TextView tv_name_course = (TextView) view.findViewById(R.id.tv_name_course);
        TextView tv_address_course = (TextView) view.findViewById(R.id.tv_address_course);
        TextView tv_comment_course = (TextView) view.findViewById(R.id.tv_comment_course);
        TextView tv_time_course = (TextView) view.findViewById(R.id.tv_time_course);
        tv_name_course.setText(name);
        tv_address_course.setText(address);
        tv_comment_course.setText(comment);
        switch (weekday) {
            case "1": weekday = "星期一"; break;
            case "2": weekday = "星期二"; break;
            case "3": weekday = "星期三"; break;
            case "4": weekday = "星期四"; break;
            case "5": weekday = "星期五"; break;
            case "6": weekday = "星期六"; break;
            case "7": weekday = "星期天"; break;
        }
        switch (time) {
            case "1": time = "第一大节"; break;
            case "2": time = "第二大节"; break;
            case "3": time = "第三大节"; break;
            case "4": time = "第四大节"; break;
            case "5": time = "晚间自习"; break;
        }
        tv_time_course.setText(weekday + "  :  " + time);
        view.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mPop.dismiss();
            }
        });
        mPop.showAtLocation(getView(), Gravity.BOTTOM, 0, 0);
    }

    /**
     * 长按删除  db.deleteCourseTimeTable( day , "1" ); 表示删除这天的第一大节课，以此类推
     * 长按添加   跳转其他界面
     */
    private void deletelongClick() {
        db = new SyllabusDatabase(getActivity().getBaseContext());
        ll_first.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (!TextUtils.isEmpty(tv_first_name.getText())) {
                    DialogHelper.showAlert(getActivity(), "确定删除吗？", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //取消
                        }

                    }, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //确定
                            db.deleteCourseTimeTable(day, "1");
                            tv_first_name.setText("");
                            tv_first_address.setText("");
                        }
                    });
                }
                return true;
            }
        });
        ll_second.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (!TextUtils.isEmpty(tv_second_name.getText())) {
                    DialogHelper.showAlert(getActivity(), "确定删除吗？", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //取消
                        }

                    }, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //确定
                            db.deleteCourseTimeTable(day, "2");
                            tv_second_name.setText("");
                            tv_second_address.setText("");
                        }
                    });
                }
                return true;
            }
        });
        ll_third.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (!TextUtils.isEmpty(tv_third_name.getText())) {
                    DialogHelper.showAlert(getActivity(), "确定删除吗？", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //取消
                        }

                    }, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //确定
                            db.deleteCourseTimeTable(day, "3");
                            tv_third_name.setText("");
                            tv_third_address.setText("");
                        }
                    });
                }
                return true;
            }
        });
        ll_fourth.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (!TextUtils.isEmpty(tv_fourth_name.getText())) {
                    DialogHelper.showAlert(getActivity(), "确定删除吗？", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //取消
                        }

                    }, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //确定
                            db.deleteCourseTimeTable(day, "4");
                            tv_fourth_name.setText("");
                            tv_fourth_address.setText("");
                        }
                    });
                }
                return true;
            }
        });
        ll_fifth.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (!TextUtils.isEmpty(tv_fifth_name.getText())) {
                    DialogHelper.showAlert(getActivity(), "确定删除吗？", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //取消
                        }

                    }, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //确定
                            db.deleteCourseTimeTable(day, "5");
                            tv_fifth_name.setText("");
                            tv_fifth_address.setText("");
                        }
                    });
                }
                return true;
            }
        });
    }
    /**
     *
     */

}