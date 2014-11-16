package com.jnhlxd.doudou.widget;

import java.lang.reflect.Field;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.Interpolator;

/**
 * Auto Scroll View Pager
 * <ul>
 * <strong>Basic Setting and Usage</strong>
 * <li>{@link #startAutoScroll()} start auto scroll, or
 * {@link #startAutoScroll(int)} start auto scroll delayed</li>
 * <li>{@link #stopAutoScroll()} stop auto scroll</li>
 * <li>{@link #setInterval(long)} set auto scroll time in milliseconds, default
 * is {@link #DEFAULT_INTERVAL}</li>
 * </ul>
 * <ul>
 * <strong>Advanced Settings and Usage</strong>
 * <li>{@link #setDirection(int)} set auto scroll direction</li>
 * <li>{@link #setCycle(boolean)} set whether automatic cycle when auto scroll
 * reaching the last or first item, default is true</li>
 * <li>{@link #setSlideBorderMode(int)} set how to process when sliding at the
 * last or first item</li>
 * <li>{@link #setStopScrollWhenTouch(boolean)} set whether stop auto scroll
 * when touching, default is true</li>
 * </ul>
 * 
 * @author <a href="http://www.trinea.cn" target="_blank">Trinea</a> 2013-12-30
 */
public class AutoScrollViewPager extends ViewPager {

	public static final int DEFAULT_INTERVAL = 3000;
	public static final int SCROLL_WHAT = 0;
	public static final int LEFT = 0;
	public static final int RIGHT = 1;

	/** do nothing when sliding at the last or first item **/
	public static final int SLIDE_BORDER_MODE_NONE = 0;
	/** cycle when sliding at the last or first item **/
	public static final int SLIDE_BORDER_MODE_CYCLE = 1;
	/** deliver event to parent when sliding at the last or first item **/
	public static final int SLIDE_BORDER_MODE_TO_PARENT = 2;

	/** auto scroll time in milliseconds, default is {@link #DEFAULT_INTERVAL} **/
	private long mInterval = DEFAULT_INTERVAL;
	/** auto scroll direction, default is {@link #RIGHT} **/
	private int mDirection = RIGHT;
	/**
	 * whether automatic cycle when auto scroll reaching the last or first item,
	 * default is true
	 **/
	private boolean mIsCycle = true;
	/** whether stop auto scroll when touching, default is true **/
	private boolean mStopScrollWhenTouch = true;
	/**
	 * how to process when sliding at the last or first item, default is
	 * {@link #SLIDE_BORDER_MODE_NONE}
	 **/
	private int mSlideBorderMode = SLIDE_BORDER_MODE_NONE;
	/** whether animating when auto scroll at the last or first item **/
	private boolean mIsBorderAnimation = true;

	private Handler mHandler;
	private boolean mIsAutoScroll;
	private boolean mIsStopByTouch;
	private float mTouchX;
	private float mDownX;
	private CustomDurationScroller mScroller;

	/**
	 * 构造函数
	 * 
	 * @param context
	 *            上下文对象
	 * 
	 */
	public AutoScrollViewPager(Context context) {
		super(context);
		init();
	}

	/**
	 * 构造函数
	 * 
	 * @param context
	 *            上下文对象
	 * @param attributeSet
	 *            属性集合
	 * 
	 */
	public AutoScrollViewPager(Context context, AttributeSet attributeSet) {
		super(context, attributeSet);
		init();
	}

	private void init() {
		mHandler = new MyHandler();
		setViewPagerScroller();
	}

	/**
	 * start auto scroll, first scroll delay time is {@link #getInterval()}
	 */
	public void startAutoScroll() {
		mIsAutoScroll = true;
		sendScrollMessage(mInterval);
	}

	/**
	 * start auto scroll
	 * 
	 * @param delayTimeInMills
	 *            first scroll delay time
	 */
	public void startAutoScroll(int delayTimeInMills) {
		mIsAutoScroll = true;
		sendScrollMessage(delayTimeInMills);
	}

	/**
	 * stop auto scroll
	 */
	public void stopAutoScroll() {
		mIsAutoScroll = false;
		mHandler.removeMessages(SCROLL_WHAT);
	}

	/**
	 * set the factor by which the duration of sliding animation will change
	 * 
	 * @param scrollFactor
	 *            切换动画时间
	 */
	public void setScrollDurationFactor(double scrollFactor) {
		mScroller.setScrollDurationFactor(scrollFactor);
	}

	private void sendScrollMessage(long delayTimeInMills) {
		/** remove messages before, keeps one message is running at most **/
		mHandler.removeMessages(SCROLL_WHAT);
		mHandler.sendEmptyMessageDelayed(SCROLL_WHAT, delayTimeInMills);
	}

	/**
	 * set ViewPager scroller to change animation duration when sliding
	 */
	private void setViewPagerScroller() {
		try {
			Field scrollerField = ViewPager.class.getDeclaredField("mScroller");
			scrollerField.setAccessible(true);
			Field interpolatorField = ViewPager.class.getDeclaredField("sInterpolator");
			interpolatorField.setAccessible(true);

			mScroller = new CustomDurationScroller(getContext(), (Interpolator) interpolatorField.get(null));
			scrollerField.set(this, mScroller);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * scroll only once
	 */
	public void scrollOnce() {
		PagerAdapter adapter = getAdapter();
		int currentItem = getCurrentItem();
		int totalCount;
		if (adapter == null || adapter.getCount() <= 1) {
			return;
		}
		totalCount = adapter.getCount();

		int nextItem = (mDirection == LEFT) ? --currentItem : ++currentItem;
		if (nextItem < 0) {
			if (mIsCycle) {
				setCurrentItem(totalCount - 1, mIsBorderAnimation);
			}
		} else if (nextItem == totalCount) {
			if (mIsCycle) {
				setCurrentItem(0, mIsBorderAnimation);
			}
		} else {
			setCurrentItem(nextItem, true);
		}
	}

	/**
	 * <ul>
	 * if stopScrollWhenTouch is true
	 * <li>if event is down, stop auto scroll.</li>
	 * <li>if event is up, start auto scroll again.</li>
	 * </ul>
	 * 
	 * @param ev
	 *            MotionEvent
	 * @return boolean ""
	 */
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (mStopScrollWhenTouch) {
			if (ev.getAction() == MotionEvent.ACTION_DOWN && mIsAutoScroll) {
				mIsStopByTouch = true;
				stopAutoScroll();
			} else if (ev.getAction() == MotionEvent.ACTION_UP && mIsStopByTouch) {
				startAutoScroll();
			}
		}

		if (mSlideBorderMode == SLIDE_BORDER_MODE_TO_PARENT || mSlideBorderMode == SLIDE_BORDER_MODE_CYCLE) {
			mTouchX = ev.getX();
			if (ev.getAction() == MotionEvent.ACTION_DOWN) {
				mDownX = mTouchX;
			}
			int currentItem = getCurrentItem();
			PagerAdapter adapter = getAdapter();
			int pageCount = adapter == null ? 0 : adapter.getCount();
			/**
			 * current index is first one and slide to right or current index is
			 * last one and slide to left.<br/>
			 * if slide border mode is to parent, then
			 * requestDisallowInterceptTouchEvent false.<br/>
			 * else scroll to last one when current item is first one, scroll to
			 * first one when current item is last one.
			 */
			if ((currentItem == 0 && mDownX <= mTouchX) || (currentItem == pageCount - 1 && mDownX >= mTouchX)) {
				if (mSlideBorderMode == SLIDE_BORDER_MODE_TO_PARENT) {
					getParent().requestDisallowInterceptTouchEvent(false);
				} else {
					if (pageCount > 1) {
						setCurrentItem(pageCount - currentItem - 1, mIsBorderAnimation);
					}
					getParent().requestDisallowInterceptTouchEvent(true);
				}
				return super.onTouchEvent(ev);
			}
		}
		getParent().requestDisallowInterceptTouchEvent(true);
		return super.onTouchEvent(ev);
	}

	private class MyHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			switch (msg.what) {
				case SCROLL_WHAT:
					scrollOnce();
					sendScrollMessage(mInterval);
					break;
				default:
					break;
			}
		}
	}

	/**
	 * get auto scroll time in milliseconds, default is
	 * {@link #DEFAULT_INTERVAL}
	 * 
	 * @return the interval
	 */
	public long getInterval() {
		return mInterval;
	}

	/**
	 * set auto scroll time in milliseconds, default is
	 * {@link #DEFAULT_INTERVAL}
	 * 
	 * @param interval
	 *            the interval to set
	 */
	public void setInterval(long interval) {
		this.mInterval = interval;
	}

	/**
	 * get auto scroll direction
	 * 
	 * @return {@link #LEFT} or {@link #RIGHT}, default is {@link #RIGHT}
	 */
	public int getDirection() {
		return (mDirection == LEFT) ? LEFT : RIGHT;
	}

	/**
	 * set auto scroll direction
	 * 
	 * @param direction
	 *            {@link #LEFT} or {@link #RIGHT}, default is {@link #RIGHT}
	 */
	public void setDirection(int direction) {
		this.mDirection = direction;
	}

	/**
	 * whether automatic cycle when auto scroll reaching the last or first item,
	 * default is true
	 * 
	 * @return the isCycle
	 */
	public boolean isCycle() {
		return mIsCycle;
	}

	/**
	 * set whether automatic cycle when auto scroll reaching the last or first
	 * item, default is true
	 * 
	 * @param isCycle
	 *            the isCycle to set
	 */
	public void setCycle(boolean isCycle) {
		this.mIsCycle = isCycle;
	}

	/**
	 * whether stop auto scroll when touching, default is true
	 * 
	 * @return the stopScrollWhenTouch
	 */
	public boolean isStopScrollWhenTouch() {
		return mStopScrollWhenTouch;
	}

	/**
	 * set whether stop auto scroll when touching, default is true
	 * 
	 * @param stopScrollWhenTouch
	 *            是否触摸停止滚动
	 */
	public void setStopScrollWhenTouch(boolean stopScrollWhenTouch) {
		this.mStopScrollWhenTouch = stopScrollWhenTouch;
	}

	/**
	 * get how to process when sliding at the last or first item
	 * 
	 * @return the slideBorderMode {@link #SLIDE_BORDER_MODE_NONE},
	 *         {@link #SLIDE_BORDER_MODE_TO_PARENT},
	 *         {@link #SLIDE_BORDER_MODE_CYCLE}, default is
	 *         {@link #SLIDE_BORDER_MODE_NONE}
	 */
	public int getSlideBorderMode() {
		return mSlideBorderMode;
	}

	/**
	 * set how to process when sliding at the last or first item
	 * 
	 * @param slideBorderMode
	 *            {@link #SLIDE_BORDER_MODE_NONE},
	 *            {@link #SLIDE_BORDER_MODE_TO_PARENT},
	 *            {@link #SLIDE_BORDER_MODE_CYCLE}, default is
	 *            {@link #SLIDE_BORDER_MODE_NONE}
	 */
	public void setSlideBorderMode(int slideBorderMode) {
		this.mSlideBorderMode = slideBorderMode;
	}

	/**
	 * whether animating when auto scroll at the last or first item, default is
	 * true
	 * 
	 * @return 是否有切换动画
	 */
	public boolean isBorderAnimation() {
		return mIsBorderAnimation;
	}

	/**
	 * set whether animating when auto scroll at the last or first item, default
	 * is true
	 * 
	 * @param isBorderAnimation
	 *            是否有切换动画
	 */
	public void setBorderAnimation(boolean isBorderAnimation) {
		this.mIsBorderAnimation = isBorderAnimation;
	}
}
