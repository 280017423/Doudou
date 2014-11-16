package com.jnhlxd.doudou.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

public class MultiGridView extends GridView {

	public MultiGridView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public MultiGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MultiGridView(Context context) {
		super(context);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}
}
