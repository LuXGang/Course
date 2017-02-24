package com.luxg.course.activity;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.luxg.course.adapter.CommentSectionAdapter;
import com.luxg.course.model.CommentInfo;
import com.luxg.course.model.TeachingInfo;
import com.luxg.course.util.NetWorkUtil;
import com.luxg.course.widget.basewise.BaseWiseActivity;
import com.luxg.course.widget.basewise.Constant;
import com.luxg.course.widget.basewise.NewToast;
import com.luxg.course.widget.pulltorefresh.PullToRefreshBaseView;
import com.luxg.course.widget.pulltorefresh.PullToRefreshListView;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;

/**
 * Created by LuXiaogang on 2016/3/4.
 */
public class CourseCommentActivity extends BaseWiseActivity implements
        View.OnClickListener, View.OnTouchListener {

    private static final String TAG = "NewsCommentActivity";
    private EditText mEdit;
    private TextView mSubmit;
    private RelativeLayout mSubmitBar;
    private boolean isRefreshFoot;
    // 评论的二级菜单的控件
    private PullToRefreshListView mPulltoListView;
    // 评论的页数的变量,默认是第一页
    private int pageNumber = 1;
    // 适配器的声明
    private CommentSectionAdapter mCommentSectionAdapter;
    // 上下文
    private Context mContext;
    // 页脚
    private View mFootView;
    private ImageView mSafaView;
    private RelativeLayout mBottomLayout;
    private boolean isEditTextShow = false;// 输入面板是否弹出
    private Dialog mDialog;

    /**
     *
     */
    private TeachingInfo teachingInfo;
    /**
     *
     */

    // 传进Adapter中的数据
    private LinkedList<CommentInfo> mCommentList;
    /**
     * adapte按钮相应事件
     */
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                // 起点输入框为空
                case 0: {
                    Bundle bundle = msg.getData();
                    showCommentBar();
                    mBottomLayout.setVisibility(View.GONE);
                    mSubmitBar.setVisibility(View.VISIBLE);
                    isEditTextShow = true;
                    break;
                }

            }
        }
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course_comment_activity);
        setTitleView("评论");
        mContext = this;
        teachingInfo = (TeachingInfo) getIntent().getSerializableExtra("TeachingInfo");
        findViews();
        // 初始化Comment的list
        mCommentList = new LinkedList<>();
        mCommentSectionAdapter = new CommentSectionAdapter(mContext, mCommentList, handler);
        mPulltoListView.setAdapter(mCommentSectionAdapter);
        initCommentList();
        mPulltoListView.setOnLastItemVisibleListener(new PullToRefreshBaseView.OnLastItemVisibleListener() {
            @Override
            public void onLastItemVisible() {
                if (mPulltoListView.getRefreshableView().getFooterViewsCount() > 0) {
                    initCommentList();
                }
            }
        });
        hideCommentBar();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
            mDialog = null;
        }
        super.onDestroy();
    }

    /**
     * find views
     */
    private void findViews() {
        mPulltoListView = (PullToRefreshListView) findViewById(R.id.news_comment_headlistvieww);
        mFootView = LayoutInflater.from(this).inflate(R.layout.course_more_footer, null);
        mPulltoListView.getRefreshableView().addFooterView(mFootView);
        mPulltoListView.setPullToRefreshEnabled(false);
        mEdit = (EditText) findViewById(R.id.news_comment_edit);
        mSubmit = (TextView) findViewById(R.id.news_comment_submit);
        mSubmit.setOnClickListener(this);
        mSubmitBar = (RelativeLayout) findViewById(R.id.news_comment_submitbar);
        mBottomLayout = (RelativeLayout) findViewById(R.id.news_content_buttom_button_layout);
        mSafaView = (ImageView) findViewById(R.id.news_comment_safa);

        findViewById(R.id.news_comment_button).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mBottomLayout.setVisibility(View.GONE);
                mSubmitBar.setVisibility(View.VISIBLE);
                showCommentBar();
                isEditTextShow = true;
            }
        });

        // 监听输入框的的文字输入事件
        mEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                if (mEdit != null && mEdit.getText().toString().length() > 0) {
                    mSubmit.setBackgroundResource(R.drawable.news_content_submit_back_selector);
                } else {
                    mSubmit.setBackgroundResource(R.drawable.news_content_submit_selector);
                }
            }
        });

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (isEditTextShow) {// 显示底部按钮
                    mBottomLayout.setVisibility(View.VISIBLE);
                    mSubmitBar.setVisibility(View.GONE);
                    isEditTextShow = false;
                } else {
                    finish();
                }
                return true;

            default:
                return super.onKeyDown(keyCode, event);
        }
    }

    @Override
    public void viewRefresh() {

    }

    @Override
    public void viewLoadMore() {

    }

    // 重新加载评论列表
    private void reloadCommentList() {
        pageNumber = 1;
        mPulltoListView.getRefreshableView().removeFooterView(mFootView);
        mPulltoListView.getRefreshableView().addFooterView(mFootView);
        mCommentList.clear();
        initCommentList();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.news_comment_submit) {
            if (!Constant.isLogin()) {
                showToast("登录后才可以发表评论");
            } else {
                String str = mEdit.getText().toString().trim();
                if (str == null || "".equals(str)) {
                } else if (str.length() > 495) {
                    showToast("输入内容不能超过500字");
                } else {
                    if (mDialog == null)
                        mDialog = NewToast.makeDialog(getContext(), "提交评论中，请稍候...", false, null);
                    mDialog.show();
                    commentSubmit(Constant.studentId,teachingInfo.getCourseId(),teachingInfo.getTeacherId());
                }
            }
        }
    }

    private void showCommentBar() {
        mEdit.requestFocus();
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.SHOW_IMPLICIT);
    }

    private void hideCommentBar() {
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        mEdit.clearFocus();
        mPulltoListView.requestFocus();
        if (imm != null) {
            imm.hideSoftInputFromWindow(mEdit.getWindowToken(), 0);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (v.getId() == R.id.news_comment_headlistvieww) {
            hideCommentBar();
        }
        return true;
    }

    // 方法操作 获取评论列表
    private void initCommentList() {
        /**
         *
         */
        if (!NetWorkUtil.isNetworkAvailable(getContext())) {
            //网络不可用
            dismissDialog();
            showToast("网络不通畅，请连接网络。。。");
            return;
        }

        //网络可用
        HttpUtils http = new HttpUtils();
        String Url = Constant.URL + "findCommentByCidTid?courseId=" + teachingInfo.getCourseId() + "&teacherId=" + teachingInfo.getTeacherId() + "&pageNum=" + pageNumber;
        System.out.println("网络请求地址——" + Url);
        http.send(HttpRequest.HttpMethod.POST, Url, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                dismissDialog();
                /**
                 * 解析从后台取回的json数据
                 */
                System.out.println("获取评论信息 : " + responseInfo.result.toString());
                String jsonString = responseInfo.result;
                Type type = new TypeToken<ArrayList<CommentInfo>>() {
                }.getType();
                Gson json = new Gson();
                ArrayList<CommentInfo> clist = json.fromJson(jsonString, type);
                /**
                 * 解析从后台取回的json数据
                 */
                int size = 6; //每页数量
                if (clist.size() < size) {
                    if (mFootView != null) {
                        // 若有需求再add上FooterView
                        mPulltoListView.getRefreshableView().removeFooterView(mFootView);
                    }
                }
                mCommentList.addAll(clist);
                mCommentSectionAdapter.notifyDataSetChanged();
                pageNumber++;
                if (mCommentList.size() <= 0) {
                    mSafaView.setVisibility(View.VISIBLE);
                } else {
                    mSafaView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                dismissDialog();
                showToast("获取失败");
                if (mCommentList.size() <= 0) {
                    mSafaView.setVisibility(View.VISIBLE);
                } else {
                    mSafaView.setVisibility(View.GONE);
                }
                if (mFootView != null) {
                    // 若有需求再add上FooterView
                    mPulltoListView.getRefreshableView().removeFooterView(mFootView);
                }
            }
        });

    }

    // 方法操作 发表评论   尚未完成
    private void commentSubmit(String sId ,String cId ,String tId) {

        /**
         *
         */
        if (!NetWorkUtil.isNetworkAvailable(getContext())) {
            //网络不可用
            dismissDialog();
            showToast("网络不通畅，请连接网络。。。");
            return;
        }
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
        String time=  sdf.format( new Date());
        //网络可用
        HttpUtils http = new HttpUtils();
        String Url = Constant.URL + "addCommentByAllId?studentId=" + sId
                +"&courseId=" + cId
                + "&teacherId="+ tId
                + "&commentContent="+ mEdit.getText().toString()
                + "&commentTime="+ time;
        System.out.println("网络请求地址——" + Url);
        http.send(HttpRequest.HttpMethod.POST, Url, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                dismissDialog();
                /**
                 * 解析从后台取回的json数据
                 */
                System.out.println("获取发布评论信息 : " + responseInfo.result.toString());
                String jsonString = responseInfo.result;
                Type type = new TypeToken<Boolean>() {
                }.getType();
                Gson json = new Gson();
                Boolean b = json.fromJson(jsonString, type);
                /**
                 * 解析从后台取回的json数据
                 */
                if (b) {
                    setResult(RESULT_OK);
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (null != imm) {
                        imm.hideSoftInputFromWindow(mEdit.getWindowToken(), 0);
                    }
                    showToast("评论发表成功!");
                    mEdit.setText("");
                    reloadCommentList();
                    if (mDialog != null) {
                        mDialog.dismiss();
                    }
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                showToast("评论失败");
                if (mDialog != null){
                    mDialog.dismiss();
                }
            }
        });


        hideCommentBar();
        mSafaView.setVisibility(View.GONE);
    }
}

