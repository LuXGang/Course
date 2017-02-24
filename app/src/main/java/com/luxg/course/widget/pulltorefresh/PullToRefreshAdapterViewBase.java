package com.luxg.course.widget.pulltorefresh;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListAdapter;

public abstract class PullToRefreshAdapterViewBase<T extends AbsListView> 
								extends PullToRefreshBaseView<T> implements OnScrollListener {
	private OnScrollListener mOnScrollListener;
	private View mEmptyView;
	private FrameLayout mRefreshableViewHolder;

	public PullToRefreshAdapterViewBase(Context context) {
		super(context);
		mRefreshableView.setOnScrollListener(this);
	}

	public PullToRefreshAdapterViewBase(Context context, AttributeSet attrs) {
		super(context, attrs);
		mRefreshableView.setOnScrollListener(this);
	}
	private boolean mLastItemVisible;
	private OnLastItemVisibleListener mOnLastItemVisibleListener;

	public final void onScroll(final AbsListView view, final int firstVisibleItem, final int visibleItemCount,
			final int totalItemCount) {
		if (null != mOnLastItemVisibleListener) {
			mLastItemVisible = (totalItemCount > 0) && (firstVisibleItem + visibleItemCount >= totalItemCount - 1);
		}
		if (null != mOnScrollListener) {
			mOnScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
		}
	}

	public final void onScrollStateChanged(final AbsListView view, final int scrollState) {
		if (scrollState == OnScrollListener.SCROLL_STATE_IDLE && null != mOnLastItemVisibleListener && mLastItemVisible) {
			if (!IS_LOADING_MORE) {
				IS_LOADING_MORE = true;
				mOnLastItemVisibleListener.onLastItemVisible();
			}
		}
		if (null != mOnScrollListener) {
			mOnScrollListener.onScrollStateChanged(view, scrollState);
		}
	}

	public final void setOnLastItemVisibleListener(OnLastItemVisibleListener listener) {
		mOnLastItemVisibleListener = listener;
	}

	public void setOnItemClickListener(AdapterView.OnItemClickListener listener) {
		((AbsListView)this.mRefreshableView).setOnItemClickListener(listener);
	}

	public void setAdapter(ListAdapter adapter) {
		((AdapterView<ListAdapter>) mRefreshableView).setAdapter(adapter);
	}

	/**
	 * 给Adapter View 设置一个空的view
	 * 
	 * @param newEmptyView
	 *            - Empty View to be used
	 */
	public final void setEmptyView(View newEmptyView) {
		// 如果已经存在，remove掉
		if (null != mEmptyView) {
			mRefreshableViewHolder.removeView(mEmptyView);
		}

		if (null != newEmptyView) {
			ViewParent newEmptyViewParent = newEmptyView.getParent();
			if (null != newEmptyViewParent && newEmptyViewParent instanceof ViewGroup) {
				((ViewGroup) newEmptyViewParent).removeView(newEmptyView);
			}

			this.mRefreshableViewHolder.addView(newEmptyView, ViewGroup.LayoutParams.FILL_PARENT,
					ViewGroup.LayoutParams.FILL_PARENT);
		}

		if (mRefreshableView instanceof EmptyViewMethodAccessor) {
			((EmptyViewMethodAccessor) mRefreshableView).setEmptyViewInternal(newEmptyView);
		} else {
			this.mRefreshableView.setEmptyView(newEmptyView);
		}
	}

	/**
	 * 添加刷新监听器
	 * @param listener
	 * 				- 监听器
	 */
	public final void setOnScrollListener(OnScrollListener listener) {
		mOnScrollListener = listener;
	}

	/**
	 * 加支持下拉刷新的控件添加进当前容器
	 */
	protected void addRefreshableView(Context context, T refreshableView) {
		mRefreshableViewHolder = new FrameLayout(context);
		mRefreshableViewHolder.addView(refreshableView, ViewGroup.LayoutParams.FILL_PARENT,
				ViewGroup.LayoutParams.FILL_PARENT);
		addView(mRefreshableViewHolder, new LayoutParams(LayoutParams.FILL_PARENT, 0, 1.0f));
	}

	/**
	 * 是否能够用下拉功能（处于ListView的顶端）
	 */
	protected boolean isReadyForPullDown() {
		return isFirstItemVisible();
	}

	/**
	 * 是否处于ListView的顶端 
	 */
	private boolean isFirstItemVisible() {
		if (this.mRefreshableView.getCount() == 0) {
			return true;
		} else if (mRefreshableView.getFirstVisiblePosition() == 0) {
			
			final View firstVisibleChild = mRefreshableView.getChildAt(0);
			
			if (firstVisibleChild != null) {
				return firstVisibleChild.getTop() >= mRefreshableView.getTop();
			}
		}
		
		return false;
	}

}
