package com.luxg.course.adapter;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.luxg.course.R;
import com.luxg.course.model.CommentInfo;
import com.luxg.course.util.ImageUtil;

import java.util.LinkedList;


/**
 * 评论的二级菜单的形式
 *
 * @author Administrator
 */
public class CommentSectionAdapter extends BaseAdapter {

    // 上下文服务
    public Context mContext;
    private LayoutInflater inflater;
    // 传进Adapter中的数据
    private LinkedList<CommentInfo> mCommentList;
    private Handler mHandler;

    public CommentSectionAdapter() {
    }

    // 构造方法
    public CommentSectionAdapter(Context mContext,
                                 LinkedList<CommentInfo> mCommentList, Handler handle) {
        this.mContext = mContext;
        this.mCommentList = mCommentList;
        inflater = LayoutInflater.from(mContext);
        mHandler = handle;
    }

    class ViewCommentHoder {
        public TextView nameTextView;
        public TextView timeTextView;
        public TextView contentTextView;
        public ImageView usericon;
    }

    @Override
    public int getCount() {// 列表的大小
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
    public View getView(final int position, View convertView, ViewGroup parent) {
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
            mHodler.nameTextView.setText(mCommentList.get(position)
                    .getStudentInfo().getStudentName());
            mHodler.timeTextView.setText(mCommentList.get(position)
                    .getCommentTime());
            mHodler.contentTextView.setText(mCommentList.get(position)
                    .getCommentContent());
        }

        return convertView;
    }

}
