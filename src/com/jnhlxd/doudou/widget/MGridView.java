package com.jnhlxd.doudou.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.GridView;

/**
 * 
 * Description the class
 * 解决不能在一个拥有Scrollbar的组件中嵌入另一个拥有Scrollbar的组件，因为这不科学，会混淆滑动事件，导致只显示一到两行数据
 * 
 * @version 1.0
 * @author zou.sq
 * 
 */
public class MGridView extends GridView {

	private int mColumns;

	/**
	 * 构造方法
	 * 
	 * @param context
	 *            上下文对象
	 * @param attrs
	 *            属性
	 */
	public MGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mColumns = getAttriIntVal(attrs, "numColumns");

	}

	/**
	 * 
	 * @Method: onMeasure
	 * @param widthMeasureSpec
	 *            参数
	 * @param heightMeasureSpec
	 *            参数
	 * @see android.widget.GridView#onMeasure(int, int)
	 */
	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}

	/**
	 * 
	 * @return 列数
	 */
	public int getColumns() {
		return mColumns;
	}

	private int getAttriIntVal(AttributeSet attrs, String attrName) {
		int attrCount = attrs.getAttributeCount();
		for (int i = 0; i < attrCount; ++i) {
			String name = attrs.getAttributeName(i);
			if (name.equals(attrName)) {
				return Integer.parseInt(attrs.getAttributeValue(i));
			}
		}
		return 0;
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		// 由于内嵌在listview中的gridview，当手指在gridview上滑动时，listview不会滑动了，所以要屏蔽gridview的滑动事件
		if (ev.getAction() == MotionEvent.ACTION_MOVE) {
			return true;
		}
		return super.dispatchTouchEvent(ev);
	}

}
