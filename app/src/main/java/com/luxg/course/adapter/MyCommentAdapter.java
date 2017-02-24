package com.luxg.course.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.luxg.course.R;
import com.luxg.course.model.CommentInfo;
import com.luxg.course.util.ImageUtil;
import com.luxg.course.util.PreferenceUtil;
import com.luxg.course.widget.basewise.Constant;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by Administrator on 2016/4/13.
 */
public class MyCommentAdapter extends BaseAdapter {
    // 上下文服务
    public Context mContext;
    private LayoutInflater inflater;
    // 传进Adapter中的数据
    private ArrayList<CommentInfo> mCommentList;

    // 构造方法
    public MyCommentAdapter() {
    }

    public MyCommentAdapter(Context mContext, ArrayList<CommentInfo> mCommentList) {
        this.mContext = mContext;
        this.mCommentList = mCommentList;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        if (mCommentList != null) {
            return mCommentList.size();
        }
        return 0;
    }

    @Override
    public CommentInfo getItem(int position) {
        return mCommentList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewCommentHoder mHodler;
        mHodler = new ViewCommentHoder();
        convertView = inflater.inflate(R.layout.course_comment_item, null);
        mHodler.nameTextView = (TextView) convertView
                .findViewById(R.id.news_comment_first_item_title);
        mHodler.timeTextView = (TextView) convertView
                .findViewById(R.id.news_comment_first_item_time);
        mHodler.contentTextView = (TextView) convertView
                .findViewById(R.id.news_comment_first_item_content);
        mHodler.usericon = (ImageView) convertView.findViewById(R.id.news_comment_first_item_usericon);

        if (mCommentList != null) {
            mHodler.nameTextView.setText(Constant.studentName + "");
            mHodler.timeTextView.setText(mCommentList.get(position)
                    .getCommentTime());
            mHodler.contentTextView.setText(mCommentList.get(position)
                    .getCommentContent());
        }
        ImageUtil.getInstance().displayImage(mContext, mHodler.usericon, PreferenceUtil.getString(mContext,"avatar"));
        return convertView;
    }

    class ViewCommentHoder {
        public TextView nameTextView;
        public TextView timeTextView;
        public TextView contentTextView;
        public ImageView usericon;
    }
}
