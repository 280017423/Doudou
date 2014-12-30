package com.jnhlxd.doudou.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.View;

/**
 * 裁剪头像的工具
 * 
 * @author zou.sq
 * @since 2012-11-1
 * @version 时间，作者，更改内容
 */
public class ClipView extends View {

	private static final int COLOR = 0xaa000000;
	private static final int THREE = 3;
	private static final int SIX = 6;
	private static final int FRAME_WIDTH = 300;
	private static final int FRAME_HIGTN = 300;

	private int mOutputX = FRAME_WIDTH;
	private int mOutputY = FRAME_HIGTN;

	/**
	 * 
	 * @param context
	 *            上下文对象
	 */
	public ClipView(Context context) {
		super(context);
	}

	/**
	 * 
	 * @param context
	 *            上下文对象
	 * @param attrs
	 *            属性集
	 */
	public ClipView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/**
	 * 
	 * @param context
	 *            上下文对象
	 * @param attrs
	 *            属性集
	 * @param defStyle
	 *            类型
	 */
	public ClipView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	/**
	 * 
	 * @param outputX
	 *            the mOutputX to set
	 */
	public void setmOutputX(int outputX) {
		this.mOutputX = outputX;
	}

	/**
	 * @param outputY
	 *            the mOutputY to set
	 */
	public void setmOutputY(int outputY) {
		this.mOutputY = outputY;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		int width = this.getWidth();
		int height = this.getHeight();

		// 画选取框
		Paint mPaint = new Paint();
		mPaint.setStyle(Style.STROKE);
		mPaint.setStrokeMiter(SIX);
		mPaint.setStrokeWidth(THREE);
		mPaint.setColor(Color.WHITE);
		canvas.drawRect((width - mOutputX) / 2, (height - mOutputY) / 2, (width + mOutputX) / 2,
				(height + mOutputY) / 2, mPaint);

		Paint paint = new Paint();
		paint.setColor(COLOR);
		canvas.drawRect(0, 0, width, (height - mOutputY) / 2, paint);
		canvas.drawRect(0, (height - mOutputY) / 2, (width - mOutputX) / 2, (height + mOutputY) / 2, paint);
		canvas.drawRect((width + mOutputX) / 2, (height - mOutputY) / 2, width, (height + mOutputY) / 2, paint);
		canvas.drawRect(0, (height + mOutputY) / 2, width, height, paint);
	}

}
