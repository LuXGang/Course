package com.luxg.course.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.luxg.course.R;
import com.luxg.course.adapter.SignUpCourseAdapter;
import com.luxg.course.model.TeachingInfo;
import com.luxg.course.util.DensityUtil;
import com.luxg.course.util.NetWorkUtil;
import com.luxg.course.widget.basewise.BaseWiseActivity;
import com.luxg.course.widget.basewise.Constant;
import com.luxg.course.widget.pulltorefresh.PullToRefreshBaseView;
import com.luxg.course.widget.pulltorefresh.PullToRefreshListView;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by LuXiaogang on 2016/2/23.
 */
public class SignUpCourseActivity extends BaseWiseActivity {
    private PullToRefreshListView sign_up_course_listview;
    private LinearLayout ll_no_course;
    private Button bt_refresh;
    private View mFootView;
    private SignUpCourseAdapter adapter;
    private int pageNum = 1;
    private ArrayList<TeachingInfo> list = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_course);
        setTitleView("选课");
        findView();
        getCourse(pageNum);
    }

    private void findView() {
        mFootView = LayoutInflater.from(this).inflate(R.layout.course_more_footer, null);
        sign_up_course_listview = (PullToRefreshListView) findViewById(R.id.sign_up_course_listview);
        ll_no_course = (LinearLayout) findViewById(R.id.ll_no_course);
        bt_refresh = (Button) findViewById(R.id.bt_refresh);
        sign_up_course_listview.setPullToRefreshEnabled(false);
        /**
         * 对sign_up_course_listview 进行设置
         */
        adapter = new SignUpCourseAdapter(list, getContext());
        sign_up_course_listview.getRefreshableView().addFooterView(mFootView);
        sign_up_course_listview.getRefreshableView().setDividerHeight(DensityUtil.dip2px(getContext(), 10));
        sign_up_course_listview.getRefreshableView().setCacheColorHint(0);
        sign_up_course_listview.getRefreshableView().setSelector(R.color.transparent);
        sign_up_course_listview.setAdapter(adapter);
        /**
         * 当条目到底时，加载更多
         */
        sign_up_course_listview.setOnLastItemVisibleListener(new PullToRefreshBaseView.OnLastItemVisibleListener() {
            @Override
            public void onLastItemVisible() {
                if (sign_up_course_listview.getRefreshableView().getFooterViewsCount() > 0) {
                    pageNum = pageNum + 1;
                    getCourse(pageNum);
                }
            }
        });
        /**
         * PullToRefreshListView的条目点击
         */
        sign_up_course_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TeachingInfo info = (TeachingInfo) parent.getAdapter().getItem(position);
                Intent intent = new Intent(SignUpCourseActivity.this, CourseDetailActivity.class);
                intent.putExtra("TeachingInfo", info);
                startActivity(intent);
            }
        });
        /**
         * 当出现“暂无选课”时，点击 “点击刷新”，进行刷新
         */
        bt_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCourse(pageNum);
            }
        });
    }

    private void getCourse(final int page) {
        showDialog();
        if (NetWorkUtil.isNetworkAvailable(getContext())) {
            //网络可用
            HttpUtils http = new HttpUtils();
            String pageString = String.valueOf(page);
            System.out.println("网络请求地址——" + Constant.URL + "findTeachingDetail?pageNum=" + pageString);
            http.send(HttpRequest.HttpMethod.POST, Constant.URL + "findTeachingDetail?pageNum=" + pageString, new RequestCallBack<String>() {

                @Override
                public void onSuccess(ResponseInfo<String> responseInfo) {
                    dismissDialog();
                    /**
                     * 解析从后台取回的json数据
                     */
                    System.out.println("获取课程信息 : " + responseInfo.result.toString());
                    String jsonString = responseInfo.result;
                    Type type = new TypeToken<ArrayList<TeachingInfo>>() {
                    }.getType();
                    Gson json = new Gson();
                    ArrayList<TeachingInfo> morelist = json.fromJson(jsonString, type);
                    /**
                     * 解析从后台取回的json数据
                     */
                    int size = 6; //每页数量
                    if (morelist.size() < size) {
                        sign_up_course_listview.getRefreshableView().removeFooterView(mFootView);
                        showToast("加载完毕！");
                    }
                    list.addAll(morelist);
                    if (list.size() <= 0) {
                        sign_up_course_listview.getRefreshableView().removeFooterView(mFootView);
                        sign_up_course_listview.setEmptyView(ll_no_course);
                    }
                    adapter.notifyDataSetChanged();
                    sign_up_course_listview.onLoadingMoreComplete();
                }

                @Override
                public void onFailure(HttpException e, String s) {
                    dismissDialog();
                    if (list.size() <= 0) {
                        sign_up_course_listview.setEmptyView(ll_no_course);
                    }
                    sign_up_course_listview.getRefreshableView().removeFooterView(mFootView);
                    sign_up_course_listview.onLoadingMoreComplete();
                }
            });
        } else {
            //网络不可用
            dismissDialog();
            if (list.size() <= 0) {
                sign_up_course_listview.getRefreshableView().removeFooterView(mFootView);
                sign_up_course_listview.setEmptyView(ll_no_course);
            }
            showToast("网络不通畅，请连接网络。。。");
        }
    }
    /**
     *
     */

}
