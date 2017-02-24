package com.luxg.course.widget.pulltorefresh;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View;
import android.widget.ListView;

public class PullToRefreshListView extends
		PullToRefreshAdapterViewBase<ListView> {

	class InternalListView extends ListView implements EmptyViewMethodAccessor {

		public InternalListView(Context context, AttributeSet attrs) {
			super(context, attrs);
		}

		@Override
		public void setEmptyView(View emptyView) {
			PullToRefreshListView.this.setEmptyView(emptyView);
		}

		@Override
		public void setEmptyViewInternal(View emptyView) {
			super.setEmptyView(emptyView);
		}

		public ContextMenuInfo getContextMenuInfo() {
			return super.getContextMenuInfo();
		}
	}

	public PullToRefreshListView(Context context) {
		super(context);
		this.setDisableScrollingWhileRefreshing(true);
	}

	public PullToRefreshListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.setDisableScrollingWhileRefreshing(false);
	}

	/**
	 * 创建一个支持下拉的ListView
	 */
	@Override
	protected final ListView createRefreshableView(Context context,
			AttributeSet attrs) {
		ListView lv = new InternalListView(context, attrs);
		lv.setDivider(null);
		// 这样设置 可以方便在使用 ListActivity 或 ListFragment是调用
		lv.setId(android.R.id.list);
		return lv;
	}

}
