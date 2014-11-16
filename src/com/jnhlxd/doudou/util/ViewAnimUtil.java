package com.jnhlxd.doudou.util;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.qianjiang.framework.util.EvtLog;

/**
 * 
 * @author Administrator
 * 
 */
public class ViewAnimUtil implements AnimationListener, OnTouchListener {
	// 动画层
	private ViewGroup mAnimLayout;
	// 要展示动画的视图
	private View mAnimView;
	private Activity mActivity;
	private ImageView mIvAnim;
	private boolean mFillAfter;
	private int[] mLocation;
	private AnimationListener mListener;

	/**
	 * @param activity
	 *            上下文对象
	 */
	public ViewAnimUtil(Activity activity) {
		this.mActivity = activity;
		mIvAnim = new ImageView(activity);
		createAnimLayout();
	}

	private void createAnimLayout() {
		ViewGroup rootView = (ViewGroup) mActivity.getWindow().getDecorView();
		mAnimLayout = new LinearLayout(mActivity);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		mAnimLayout.setLayoutParams(lp);
		mAnimLayout.setBackgroundResource(android.R.color.transparent);
		mAnimLayout.setVisibility(View.GONE);
		rootView.addView(mAnimLayout);
		mAnimLayout.setOnTouchListener(this);
	}

	/**
	 * 
	 * @param view
	 *            动画view
	 * @param animSet
	 *            动画
	 * @param listener
	 *            动画回调
	 */
	public void startAnim(final View view, AnimationSet animSet, AnimationListener listener) {
		mLocation = new int[2];
		mAnimView = view;
		this.mListener = listener;
		animSet.setAnimationListener(this);
		// 打开舞台,表演开始(显示动画层)
		mAnimLayout.setVisibility(View.VISIBLE);
		// 添加动画视图到动画层
		addViewToAnimLayout();
		mIvAnim.startAnimation(animSet);
	}

	/**
	 * 
	 * @param fillAfter
	 *            是否需要fillAfter
	 */
	public void setFillAfter(boolean fillAfter) {
		this.mFillAfter = fillAfter;
	}

	private boolean addViewToAnimLayout() {
		int[] startLocation = new int[2];
		mAnimView.getLocationInWindow(startLocation);
		// 从现在的父容器移出
		// animViewParent.removeView(animView);
		Bitmap b = loadBitmapFromView(mAnimView);
		if (b == null) {
			return false;
		}
		mIvAnim.setImageBitmap(b);
		ViewGroup vg = (ViewGroup) mIvAnim.getParent();
		if (null != vg) {
			vg.removeView(mIvAnim);
		}
		// 添到动画层
		mAnimLayout.addView(mIvAnim);
		// 设置视图在动画层位置
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		if (0 == startLocation[0] && 0 == startLocation[1]) {
			lp.leftMargin = mLocation[0];
			lp.topMargin = mLocation[1];
		} else {
			lp.leftMargin = startLocation[0];
			lp.topMargin = startLocation[1];
		}
		mIvAnim.setLayoutParams(lp);
		return true;
	}

	/**
	 * 
	 * @Title: loadBitmapFromView
	 * @Description: 把View转换成Bitmap
	 * @param v
	 *            需要转换的视图
	 * @return Bitmap 返回类型
	 */
	private static Bitmap loadBitmapFromView(View v) {
		try {
			v.setDrawingCacheEnabled(true);
			Bitmap bit = v.getDrawingCache();
			if (!bit.isRecycled()) {
				v.setDrawingCacheEnabled(false);
				if (v.getMeasuredHeight() <= 0) {
					v.measure(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				}
				Bitmap b = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888);
				if (!b.isRecycled()) {
					Canvas c = new Canvas(b);
					v.draw(c);
					return b;
				} else {
					return null;
				}
			} else {
				v.setDrawingCacheEnabled(false);
			}
		} catch (Exception e) {
			EvtLog.d("qd", "表示异常了。。 " + e);
		}
		return null;
	}

	/**
	 * 手动移除动画层
	 */
	public void removeAnimationLayout() {
		mIvAnim.clearAnimation(); // 此处一定要写。不然view第二次点击没反应了。(释放对该组件的锁定)
		// ViewUtil.recycleImageViewBitMap(mIvAnim);
		// 舞台谢幕(隐藏动画层)
		mAnimLayout.setVisibility(View.GONE);
	}

	@Override
	public void onAnimationEnd(Animation animation) {
		// / 此处一定要写。不然view第二次点击没反应了。(释放对该组件的锁定)
		// // 舞台谢幕(隐藏动画层)
		if (!this.mFillAfter) {
			removeAnimationLayout();
		}
		if (null != mListener) {
			mListener.onAnimationEnd(animation);
		}
	}

	@Override
	public void onAnimationRepeat(Animation animation) {
		if (null != mListener) {
			mListener.onAnimationRepeat(animation);
		}
	}

	@Override
	public void onAnimationStart(Animation animation) {
		if (null != mListener) {
			mListener.onAnimationStart(animation);
		}
	}

	@Override
	public boolean onTouch(View arg0, MotionEvent arg1) {
		// 这里什么都不实现，只是为了在执行动画的时候，点击动画层事件不会传递到下面view
		return true;
	}
}
