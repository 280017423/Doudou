package com.jnhlxd.doudou.util;

import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;

import com.jnhlxd.doudou.listener.OnPopDismissListener;

/**
 * PoPWindow 弹出窗口控件
 * 
 * @author zou.sq
 * 
 */
public class PopWindowUtil {
	private View mMenuView;
	private PopupWindow mPopupWindow;
	private View mView;
	private OnPopDismissListener mListener;

	/**
	 * @param contentView
	 *            需要显示的view
	 * @param view
	 *            PopWindow相对位置的视图
	 * @param listener
	 *            popwindow消失监听
	 */
	public PopWindowUtil(View contentView, View view, OnPopDismissListener listener) {
		this.mMenuView = contentView;
		mView = view;
		mListener = listener;
		initView();
	}

	/**
	 * 初始化
	 */
	private void initView() {
		mPopupWindow = new PopupWindow(mMenuView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		mPopupWindow.setContentView(mMenuView);
		mPopupWindow.setFocusable(true);
		// 点击popupwindow窗口之外的区域popupwindow消失
		ColorDrawable dw = new ColorDrawable(0x00);
		mPopupWindow.setBackgroundDrawable(dw);
		mPopupWindow.setOutsideTouchable(true);
		mPopupWindow.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				if (null != mListener) {
					mListener.onDismiss();
				}
			}
		});
	}

	/**
	 * 用来显示和关闭PopWindow
	 */
	public void changeStatus() {
		if (null != mPopupWindow) {
			if (mPopupWindow.isShowing()) {
				mPopupWindow.dismiss();
			} else {
				mPopupWindow.showAsDropDown(mView, 0, 0);
			}
		}
	}

	/**
	 * 
	 * @Description 消失
	 * @Author admin
	 * @Date 2014-8-26 下午4:11:10
	 * 
	 */
	public void dissmiss() {
		if (mPopupWindow != null && mPopupWindow.isShowing()) {
			mPopupWindow.dismiss();
		}
	}

}