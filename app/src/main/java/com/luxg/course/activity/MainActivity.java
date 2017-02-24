package com.luxg.course.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.luxg.course.R;
import com.luxg.course.video.VideoViewDemoActivity;
import com.luxg.course.widget.base.MyImageView;
import com.luxg.course.widget.basewise.BaseWiseActivity;
import com.luxg.course.widget.basewise.CommonTitle;
import com.luxg.course.widget.basewise.Constant;
import com.umeng.comm.core.CommunitySDK;
import com.umeng.comm.core.beans.CommUser;
import com.umeng.comm.core.constants.ErrorCode;
import com.umeng.comm.core.impl.CommunityFactory;
import com.umeng.comm.core.login.LoginListener;

import java.io.File;

public class MainActivity extends BaseWiseActivity {
    private CommonTitle commonTitle;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        commonTitle =new CommonTitle(getContext());
        commonTitle.setOnTitleClickListener(new CommonTitle.TitleClickListener() {
            @Override
            public void onLeftClicked(CommonTitle view, View v) {

            }

            @Override
            public void onRightClicked(CommonTitle view, View v) {

            }

            @Override
            public void onRight2Clicked(CommonTitle view, View v) {

            }
        });
        commonTitle.setLeftImageBg(R.color.White);
        commonTitle.setTitleText("课程管理系统");
        setTitleView(commonTitle);

        MyImageView c_person = (MyImageView) findViewById(R.id.c_person);
        MyImageView c_course = (MyImageView) findViewById(R.id.c_course);
        MyImageView c_signup = (MyImageView) findViewById(R.id.c_signup);
        MyImageView c_comment = (MyImageView) findViewById(R.id.c_comment);
        MyImageView c_school = (MyImageView) findViewById(R.id.c_school);
        /**
         * 查看校历
         */
        c_school.setOnClickIntent(new MyImageView.OnViewClickListener() {
            @Override
            public void onViewClick(MyImageView view) {
                Intent intent = new Intent(MainActivity.this, SchoolCalendarActivity.class);
                startActivity(intent);
            }
        });
        /**
         * 课程表
         */
        c_course.setOnClickIntent(new MyImageView.OnViewClickListener() {
            @Override
            public void onViewClick(MyImageView view) {
                Intent intent = new Intent(MainActivity.this, TimeTableActivity.class);
                startActivity(intent);
            }
        });
        /**
         * 个人中心
         */
        c_person.setOnClickIntent(new MyImageView.OnViewClickListener() {
            @Override
            public void onViewClick(MyImageView view) {
                if ("".equals(Constant.studentId) || Constant.studentId == null) {
                    Intent intent = new Intent(getContext(), LoginActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(MainActivity.this, PersonalMainActivity.class);
                    startActivity(intent);
                }

            }
        });
        /**
         * 选课
         */
        c_signup.setOnClickIntent(new MyImageView.OnViewClickListener() {
            @Override
            public void onViewClick(MyImageView view) {
                if ("".equals(Constant.studentId) || Constant.studentId == null) {
                    needLogin();
                } else {
                    Intent intent = new Intent(getContext(), SignUpCourseActivity.class);
                    startActivity(intent);
                }
            }
        });

        /**
         * 友盟微社区
         */
        c_comment.setOnClickIntent(new MyImageView.OnViewClickListener() {
            @Override
            public void onViewClick(MyImageView view) {
                /**
                 * 测试友盟微社区  已经登录的同时，登录友盟
                 */
                if (Constant.isLogin()) {
                    // 获取CommunitySDK实例, 参数1为Context类型
                    CommunitySDK mCommSDK = CommunityFactory.getCommSDK(getContext());
                    //创建CommUser前必须先初始化CommunitySDK
                    CommUser user = new CommUser();
                    user.name = Constant.studentName;
                    user.id = Constant.studentId;
                    mCommSDK.loginToUmengServer(getContext(), user, new LoginListener() {
                        @Override
                        public void onStart() {
                            showDialog();
                        }
                        @Override
                        public void onComplete(int stCode, CommUser commUser) {
                            dismissDialog();
                            if (ErrorCode.NO_ERROR == stCode) {
                                //在此处可以跳转到任何一个你想要的activity或者任意的操作
                                System.out.println("微社区登录成功 --- [ CommUser ] == " + "[ name = " + commUser.name + ", id = " + commUser.id + " ]");
                            }

                        }
                    });
                    // 打开微社区 的接口, 参数1为Context类型
                    mCommSDK.openCommunity(getContext());
                } else {
                    needLogin();
                }
            }
        });
        /**
         * iphone风格的Dialog
         */
//        MyImageView c_idea1 = (MyImageView) findViewById(R.id.c_idea1);
//        c_idea1.setOnClickIntent(new MyImageView.OnViewClickListener() {
//            @Override
//            public void onViewClick(MyImageView view) {
//                showCustomMessage("Alert", "Are you sure you want continue?");
//            }
//        });

    }

    /**
     * 判断是否需要登录，需要时显示 [ 您还没有登录，请登录后继续 ] 界面
     */
    private void needLogin() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("提示");
        builder.setMessage("您还没有登录，请登录后继续");
        builder.setPositiveButton("登录",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getContext(), LoginActivity.class);
                        startActivity(intent);
                    }
                });
        builder.setNegativeButton("取消", null);
        builder.show();
    }

    /**
     * it will show the OK/CANCEL dialog like iphone, make sure no keyboard is visible
     *
     * @param pTitle title for dialog
     * @param pMsg   msg for body
     */
    private void showCustomMessage(String pTitle, final String pMsg) {
        final Dialog lDialog = new Dialog(MainActivity.this,
                android.R.style.Theme_Translucent_NoTitleBar);
        lDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        lDialog.setContentView(R.layout.r_okcanceldialogview);
        ((TextView) lDialog.findViewById(R.id.dialog_title)).setText(pTitle);
        ((TextView) lDialog.findViewById(R.id.dialog_message)).setText(pMsg);
        ((Button) lDialog.findViewById(R.id.ok)).setText("Ok");
        ((Button) lDialog.findViewById(R.id.cancel))
                .setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // write your code to do things after users clicks CANCEL
                        lDialog.dismiss();
                    }
                });
        ((Button) lDialog.findViewById(R.id.ok))
                .setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // write your code to do things after users clicks OK

                        lDialog.dismiss();
                    }
                });
        lDialog.show();

    }

    /**
     * it will show the OK dialog like iphone, make sure no keyboard is visible
     *
     * @param pTitle title for dialog
     * @param pMsg   msg for body
     */
    private void showCustomMessageOK(String pTitle, final String pMsg) {
        final Dialog lDialog = new Dialog(MainActivity.this,
                android.R.style.Theme_Translucent_NoTitleBar);
        lDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        lDialog.setContentView(R.layout.r_okdialogview);
        ((TextView) lDialog.findViewById(R.id.dialog_title)).setText(pTitle);
        ((TextView) lDialog.findViewById(R.id.dialog_message)).setText(pMsg);
        ((Button) lDialog.findViewById(R.id.ok)).setText("Ok");
        ((Button) lDialog.findViewById(R.id.ok))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // write your code to do things after users clicks OK
                        lDialog.dismiss();
                    }
                });
        lDialog.show();

    }

    @Override
    protected void onResume() {
        super.onResume();
        dismissDialog();
    }
}
