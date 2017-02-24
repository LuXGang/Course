package com.luxg.course.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.luxg.course.R;
import com.luxg.course.util.DataCleanManager;
import com.luxg.course.util.DialogHelper;
import com.luxg.course.util.PreferenceUtil;
import com.luxg.course.widget.basewise.BaseWiseActivity;
import com.luxg.course.widget.basewise.Constant;

/**
 * Created by LuXiaogang on 2016/3/1.
 */
public class PersonalSettingActivity extends BaseWiseActivity {
    private RelativeLayout setting_flow_control,setting_upgrade,setting_help,setting_about,setting_feedback;
    private Button setting_out_login;
    private TextView tv_flow;

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message message) {
            switch (message.what) {
                case 110:
                    try {
                        tv_flow.setText("0M");
                        showToast("清除缓存完成");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }

    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_personal);
        setTitleView("设置");
        findView();
        setClick();
    }

    private void setClick() {
        setting_flow_control.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 清楚缓存
                AlertDialog.Builder builderOffline = new AlertDialog.Builder(getContext());
                builderOffline.setTitle("提醒");
                builderOffline.setMessage("您确定要清除缓存?");
                builderOffline.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DataCleanManager.clearAllCache(getContext());
                        handler.sendEmptyMessage(110);
                    }
                });
                builderOffline.setNegativeButton("取消", null);
                builderOffline.show();
            }
        });

        setting_out_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //退出登录
                DialogHelper.showAlert(getContext(), "确定退出登录吗？", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }

                }, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences userInfo = PreferenceUtil.getSharedPreferences(getContext());
                        SharedPreferences.Editor editor = userInfo.edit();
                        Constant.studentId = "";
                        Constant.studentPassword = "";
                        Constant.studentName = "";
                        Constant.studentBirth = "";
                        Constant.studentClass = "";//班级
                        Constant.studentTime = "";//年级
                        Constant.studentTel = "";
                        Constant.studentSex = "";

                        editor.putString("studentId", Constant.studentId);
                        editor.putString("studentPassword", Constant.studentPassword);
                        editor.putString("studentName", Constant.studentName);
                        editor.putString("studentBirth", Constant.studentBirth);
                        editor.putString("studentClass", Constant.studentClass);
                        editor.putString("studentTime", Constant.studentTime);
                        editor.putString("studentTel", Constant.studentTel);
                        editor.putString("studentSex", Constant.studentSex);
                        editor.putString("avatar", "" );
                        editor.commit();

                        PreferenceUtil.putBoolean(getContext(), "isLogin", false);//将登录信息存入SharedPreferences；
                        PreferenceUtil.putBoolean(getContext(), "module_refresh", true);
                        setResult(300);//传个值回去关闭上一页
                        finish();
                        overridePendingTransition(R.anim.personal_back_in, R.anim.personal_back_left_out);
                    }
                });
            }
        });

        setting_upgrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast("没有版本更新！");
            }
        });

        setting_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PersonalSettingActivity.this,BaseAdWebActivity.class);
                intent.putExtra(BaseAdWebActivity.TITLE,"用户帮助");
                intent.putExtra(BaseAdWebActivity.URLSTR,"http://yb.xzit.edu.cn/");
                startActivity(intent);
            }
        });
        setting_about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PersonalSettingActivity.this,PersonalAboutActivity.class));
            }
        });
        setting_feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PersonalSettingActivity.this,BaseAdWebActivity.class);
                intent.putExtra(BaseAdWebActivity.TITLE,"意见反馈");
                intent.putExtra(BaseAdWebActivity.URLSTR,"http://www.xzit.edu.cn/xxgk/index.asp");
                startActivity(intent);
            }
        });
    }

    private void findView() {
        setting_flow_control = (RelativeLayout) findViewById(R.id.setting_flow_control);
        setting_upgrade = (RelativeLayout) findViewById(R.id.setting_upgrade);
        setting_help = (RelativeLayout) findViewById(R.id.setting_help);
        setting_about = (RelativeLayout) findViewById(R.id.setting_about);
        setting_feedback = (RelativeLayout) findViewById(R.id.setting_feedback);
        setting_out_login = (Button) findViewById(R.id.setting_out_login);
        tv_flow = (TextView) findViewById(R.id.tv_flow);
        try {
            tv_flow.setText(DataCleanManager.getTotalCacheSize(getContext()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
