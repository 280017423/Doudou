package com.jnhlxd.doudou.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.qianjiang.framework.imageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;

/**
 * Fragment基类
 * 
 * @author zou.sq
 */
public class FragmentBase extends Fragment {

	protected ImageLoader mImageLoader = ImageLoader.getInstance();

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MobclickAgent.onPageStart("");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		MobclickAgent.onPageEnd("");
	}

	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(getActivity());
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(getActivity());
	}

	/**
	 * 
	 * @Method: onPositiveBtnClick
	 * @Description: 确定按钮回调
	 * @param id
	 *            当前对话框对象的ID
	 * @param dialog
	 *            DialogInterface 对象
	 * @param which
	 *            dialog ID
	 */
	public void onPositiveBtnClick(int id, DialogInterface dialog, int which) {
	}

	/**
	 * 
	 * @Method: onPositiveBtnClick
	 * @Description: 取消按钮回调
	 * @param id
	 *            当前对话框对象的ID
	 * @param dialog
	 *            DialogInterface 对象
	 * @param which
	 *            dialog ID
	 */
	public void onNegativeBtnClick(int id, DialogInterface dialog, int which) {
	}

}
