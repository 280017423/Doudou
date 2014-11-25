package com.jnhlxd.doudou.adapter;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;

import com.jnhlxd.doudou.R;
import com.jnhlxd.doudou.activity.MainActivity;
import com.jnhlxd.doudou.authentication.ActionProcessor;
import com.jnhlxd.doudou.util.ConstantSet;
import com.qianjiang.framework.authentication.BaseLoginProcessor.LOGIN_TYPE;
import com.qianjiang.framework.util.EvtLog;
import com.qianjiang.framework.util.PackageUtil;
import com.qianjiang.framework.util.SharedPreferenceUtil;
import com.qianjiang.framework.util.UIUtil;

/**
 * 新手引导界面适配器
 * 
 * @version 1.0
 * @author zou.sq
 * 
 */
public class NewerAdapter extends PagerAdapter {

	private static final String TAG = "NewerAdapter";
	private static final int BOTTOM_MARGIN = 45;
	private Activity mContext;
	private int[] mImages;
	private int mScreenWidth;

	/**
	 * 构造函数
	 * 
	 * @param activity
	 *            上下文
	 */
	public NewerAdapter(Activity activity) {
		this.mContext = activity;
		this.mImages = new int[] { R.drawable.newer_guiding_1, R.drawable.newer_guiding_2, R.drawable.newer_guiding_3 };
		mScreenWidth = activity.getWindowManager().getDefaultDisplay().getWidth();
	}

	/**
	 * 
	 * @return 返回默认大小
	 */
	@Override
	public int getCount() {
		if (null == mImages) {
			return 0;
		}
		return mImages.length;
	}

	/**
	 * 
	 * @return 返回真实大小
	 */
	public int getSize() {
		if (mImages != null) {
			return mImages.length;
		} else {
			return 0;
		}
	}

	protected class Item {
		Button mBtnJump;
	}

	@Override
	public Object instantiateItem(View viewPager, int position) {

		final Item mItem = new Item();
		View itemView = View.inflate(mContext, R.layout.newer_guiding_item, null);
		mItem.mBtnJump = (Button) itemView.findViewById(R.id.btn_jump_to_main);
		if (mImages.length > 0) {
			FrameLayout.LayoutParams params = (LayoutParams) mItem.mBtnJump.getLayoutParams();
			params.width = mScreenWidth / 2;
			params.height = params.width / 4;
			params.bottomMargin = UIUtil.dip2px(mContext, BOTTOM_MARGIN);
			mItem.mBtnJump.setLayoutParams(params);
			mItem.mBtnJump.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// 开启线程保存用戶是否完成新手引导的状态
					saveNewerGuidingStatus();
					EvtLog.d("newer_guiding", "点击按钮，准备跳转至主界面时的时间:  " + System.currentTimeMillis());
					jumpToLogin();
				}
			});
			if (mImages.length - 1 == position) {
				mItem.mBtnJump.setEnabled(true);
				mItem.mBtnJump.setBackgroundResource(R.drawable.btn_newer_guiding_start_selector);
			} else {
				mItem.mBtnJump.setEnabled(false);
				mItem.mBtnJump.setBackgroundColor(mContext.getResources().getColor(R.color.transparent));
			}
			itemView.setBackgroundResource(mImages[position % mImages.length]);
		}
		((ViewPager) viewPager).addView(itemView);
		return itemView;
	}

	@Override
	public void destroyItem(View arg0, int arg1, Object arg2) {
		((ViewPager) arg0).removeView((View) arg2);
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	@Override
	public void restoreState(Parcelable arg0, ClassLoader arg1) {

	}

	@Override
	public Parcelable saveState() {
		return null;
	}

	@Override
	public void startUpdate(View arg0) {

	}

	@Override
	public void finishUpdate(View arg0) {

	}

	private void saveNewerGuidingStatus() {
		new Thread() {
			@Override
			public void run() {
				int code = -1;
				try {
					code = PackageUtil.getVersionCode();
				} catch (NameNotFoundException e) {
					EvtLog.e(TAG, e);
				}
				SharedPreferenceUtil.saveValue(mContext, ConstantSet.KEY_APPLICATION_CONFIG_FILE,
						ConstantSet.KEY_NEWER_GUIDING_FINISH, code + "");
			}
		}.start();
	}

	private void jumpToLogin() {
		// 直接跳转到登录界面
		new ActionProcessor(true).startActivity(mContext, new Intent(mContext, MainActivity.class),
				LOGIN_TYPE.Exit_To_Cancel_Apk);
		mContext.finish();
	}

}