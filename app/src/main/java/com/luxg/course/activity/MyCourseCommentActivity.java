package com.luxg.course.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.luxg.course.R;
import com.luxg.course.adapter.MyCommentAdapter;
import com.luxg.course.model.CommentInfo;
import com.luxg.course.util.NetWorkUtil;
import com.luxg.course.widget.basewise.BaseWiseActivity;
import com.luxg.course.widget.basewise.Constant;
import com.luxg.course.widget.pulltorefresh.PullToRefreshBaseView;
import com.luxg.course.widget.pulltorefresh.PullToRefreshListView;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by Administrator on 2016/4/13.
 */
public class MyCourseCommentActivity extends BaseWiseActivity{
    private PullToRefreshListView listView;
    private View mFootView;
    private ImageView mSafaView;
    private boolean isRefreshFoot;
    // 评论的页数的变量,默认是第一页
    private int pageNumber = 1;
    // 传进Adapter中的数据
    private ArrayList<CommentInfo> mCommentList;
    private MyCommentAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_comment_personal);
        setTitleView("我的评论");

        findView();
        initCommentList();
    }

    private void findView() {
        mSafaView = (ImageView) findViewById(R.id.news_comment_safa);
        listView = (PullToRefreshListView) findViewById(R.id.listView);
        mFootView = LayoutInflater.from(this).inflate(R.layout.course_more_footer, null);
        listView.setPullToRefreshEnabled(false);
        listView.getRefreshableView().addFooterView(mFootView);
        // 初始化Comment的list
        mCommentList = new ArrayList<>();
        mAdapter = new MyCommentAdapter(getContext(), mCommentList);
        listView.setAdapter(mAdapter);
        listView.setOnLastItemVisibleListener(new PullToRefreshBaseView.OnLastItemVisibleListener() {
            @Override
            public void onLastItemVisible() {
                if (listView.getRefreshableView().getFooterViewsCount() > 0) {
                    initCommentList();
                }
            }
        });

    }


    // 方法操作 获取评论列表
    private void initCommentList() {
        showDialog();
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
        String Url = Constant.URL + "findCommentByStuid?studentId=" + Constant.studentId + "&pageNum=" + pageNumber;
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
                        listView.getRefreshableView().removeFooterView(mFootView);
                    }
                }
                mCommentList.addAll(clist);
                mAdapter.notifyDataSetChanged();
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
                    listView.getRefreshableView().removeFooterView(mFootView);
                }
            }
        });

    }



}
