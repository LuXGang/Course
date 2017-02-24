package com.luxg.course.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.luxg.course.R;
import com.luxg.course.widget.basewise.BaseWiseActivity;

/**
 * Created by LuXiaogang on 2016/4/29.
 */
public class SplashActivity extends BaseWiseActivity {
    private final static int GO_TO_MAINPAGE = 99;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        handler.sendEmptyMessageDelayed(GO_TO_MAINPAGE, 3 * 1000);
    }

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message message) {
            switch (message.what) {
                // 跳转主页面
                case GO_TO_MAINPAGE:
                    gotoMainPage();
                    break;

            }
        }

    };

    /**
     * 跳转到主页
     */
    private void gotoMainPage() {
            Intent intent = new Intent(getContext(), MainActivity.class);
            startActivity(intent);
            finish();
    }
}
