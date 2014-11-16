package com.jnhlxd.doudou.listener;

import android.view.View;
import android.widget.TextView;

/**
 * ExpandableTextView显示隐藏回调接口
 * 
 * @author zou.sq
 */
public interface OnChangedListener {

	/**
	 * 显示隐藏状态发生改变时回调方法
	 * 
	 * @param canClick
	 *            是否能点击
	 * @param view
	 *            当前显示view
	 * @param contentView
	 *            当前内容view
	 */
	void onInit(View view, boolean canClick, TextView contentView);

	/**
	 * 显示隐藏状态发生改变时回调方法
	 * 
	 * @param isCollapsed
	 *            是否收起
	 */
	void onChanged(boolean isCollapsed);

}
