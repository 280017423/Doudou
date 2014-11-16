package com.jnhlxd.doudou.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * 宽度设置好了之后，自动计算高度，让高度与宽度一样，成一个正方形
 * 
 * @author Administrator
 * 
 */
// For simple implementation, or internal size is always 0.
// We depend on the container to specify the layout size of
// our view. We can't really know what it is since we will be
// adding and removing different arbitrary views and do not
// want the layout to change as this happens.
// Children are just made to fill our space.
// 高度和宽度一样
/**
 * 宽度设置好了之后，自动计算高度，让高度与宽度一样，成一个正方形
 * 
 * @author Administrator
 * 
 */
public class SquareRelativeLayout extends RelativeLayout {

	/**
	 * 构造函数
	 * 
	 * @param context
	 *            上下文对象
	 * @param attrs
	 *            属性集合
	 * @param defStyle
	 *            defStyle
	 */
	public SquareRelativeLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	/**
	 * 构造函数
	 * 
	 * @param context
	 *            上下文对象
	 * @param attrs
	 *            属性集合
	 */
	public SquareRelativeLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/**
	 * 构造函数
	 * 
	 * @param context
	 *            上下文对象
	 */
	public SquareRelativeLayout(Context context) {
		super(context);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// For simple implementation, or internal size is always 0.
		// We depend on the container to specify the layout size of
		// our view. We can't really know what it is since we will be
		// adding and removing different arbitrary views and do not
		// want the layout to change as this happens.
		setMeasuredDimension(getDefaultSize(0, widthMeasureSpec), getDefaultSize(0, heightMeasureSpec));

		// Children are just made to fill our space.
		int childWidthSize = getMeasuredWidth();
		// 高度和宽度一样
		widthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidthSize, MeasureSpec.EXACTLY);
		heightMeasureSpec = widthMeasureSpec;
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
}
