package com.luxg.course.widget.pulltorefresh;

import android.view.View;

/**
 * 允许 PullToRefreshBase 劫持 AdapterView.setEmptyView()的接口
 * 
 */
public interface EmptyViewMethodAccessor {

	/**
	 * 调用 AdapterView.setEmptyView()
	 * 
	 * @param View
	 *            to set as Empty View
	 */
	public void setEmptyViewInternal(View emptyView);

	/**
	 *调用setEmptyViewInternal()是自动调用 
	 * 
	 * @param View
	 *            to set as Empty View
	 */
	public void setEmptyView(View emptyView);
	
}
