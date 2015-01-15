package com.jnhlxd.doudou.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.FloatMath;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jnhlxd.doudou.R;
import com.jnhlxd.doudou.util.ImageUtil;
import com.jnhlxd.doudou.widget.ClipView;
import com.qianjiang.framework.util.EvtLog;

/**
 * 图片浏览、缩放、拖动、自动居中
 */
public class ClipPictureActivity extends ActivityBase implements OnTouchListener, OnClickListener {

	private static final String TAG = "ClipPictureActivity";
	private static final int FRAME_WIDTH = 300;
	private static final int FRAME_HIGTN = 300;
	private static final int NONE = 0; // 初始状态
	private static final int DRAG = 1; // 拖动
	private static final int ZOOM = 2; // 缩放
	private static final int ARRAY_SIZE = 9;
	private static final float MAX_SCALE = 20f; // 最大缩放比例

	private Matrix mMatrix = new Matrix();
	private Matrix mSavedMatrix = new Matrix();
	private DisplayMetrics mDm;
	private ImageView mSrcPicIv;
	private ClipView mClipview;
	private Bitmap mBitmap;

	private int mMode = NONE;
	private int mStatusBarHeight;
	private int mTitleBarHeight;
	private int mOutputX = FRAME_WIDTH;
	private int mOutputY = FRAME_HIGTN;

	private PointF mPrev = new PointF();
	private PointF mMid = new PointF();
	private float mDist = 1f;
	private Uri mUri;
	private LinearLayout mLlBack;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_cut);
		initVariables();
		initViews();
		setListener();
	}

	private void setListener() {
		mLlBack.setOnClickListener(this);
	}

	/**
	 * 初始化控件
	 * 
	 * @param
	 * @return void
	 * @throws
	 */
	private void initViews() {
		TextView titleTextView = (TextView) findViewById(R.id.title_with_back_title_btn_mid);
		titleTextView.setText(R.string.upload_head_icon);
		mLlBack = (LinearLayout) findViewById(R.id.title_with_back_title_btn_left);
		TextView mTvBack = (TextView) findViewById(R.id.tv_title_with_back_left);
		mTvBack.setText(R.string.title_back_text);

		mTvBack.setBackgroundResource(R.drawable.btn_back_bg);
		TextView tvRight = (TextView) findViewById(R.id.tv_title_with_right);
		tvRight.setBackgroundResource(R.drawable.tongyong_button_bg);
		findViewById(R.id.title_with_back_title_btn_right).setOnClickListener(this);
		tvRight.setText(R.string.text_ensure);

		mSrcPicIv = (ImageView) findViewById(R.id.src_pic); // 获取控件
		mBitmap = ImageUtil.getBitmapFromUri(mUri);
		if (null == mBitmap) {
			toast(getString(R.string.user_image_load_fail));
			finish();
		}
		mSrcPicIv.setImageBitmap(mBitmap);
		mSrcPicIv.setOnTouchListener(this);

		mDm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(mDm); // 获取分辨率
		center();
		mSrcPicIv.setImageMatrix(mMatrix);
	}

	private void initVariables() {
		mUri = getIntent().getData();
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {

		switch (event.getAction() & MotionEvent.ACTION_MASK) {
		// 主点按下
			case MotionEvent.ACTION_DOWN:
				mSavedMatrix.set(mMatrix);
				mPrev.set(event.getX(), event.getY());
				mMode = DRAG;
				break;
			// 副点按下
			case MotionEvent.ACTION_POINTER_DOWN:
				mDist = spacing(event);
				// 如果连续两点距离大于10，则判定为多点模式
				if (spacing(event) > 10f) {
					mSavedMatrix.set(mMatrix);
					midPoint(mMid, event);
					mMode = ZOOM;
				}
				break;
			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_POINTER_UP:
				mMode = NONE;
				break;
			case MotionEvent.ACTION_MOVE:
				if (mMode == DRAG) {
					mMatrix.set(mSavedMatrix);
					mMatrix.postTranslate(event.getX() - mPrev.x, event.getY() - mPrev.y);
				} else if (mMode == ZOOM) {
					float newDist = spacing(event);
					if (newDist > 10f) {
						mMatrix.set(mSavedMatrix);
						float tScale = newDist / mDist;
						mMatrix.postScale(tScale, tScale, mMid.x, mMid.y);
					}
				}
				break;
			default:
				break;
		}
		mSrcPicIv.setImageMatrix(mMatrix);
		checkView();
		return true;
	}

	/**
	 * 限制最大最小缩放比例，自动居中
	 */
	private void checkView() {
		float[] p = new float[ARRAY_SIZE];
		mMatrix.getValues(p);
		if (mMode == ZOOM) {
			// if (p[0] < 1) {
			// mMatrix.setScale(mMinScaleR, mMinScaleR);
			// }
			if (p[0] > MAX_SCALE) {
				mMatrix.set(mSavedMatrix);
			}
		}
		// center();
	}

	private void center() {
		center(true, true);
	}

	/**
	 * 横向、纵向居中
	 */
	protected void center(boolean horizontal, boolean vertical) {
		RectF rect = null;
		Matrix m = new Matrix();
		m.set(mMatrix);
		try {
			rect = new RectF(0, 0, mBitmap.getWidth(), mBitmap.getHeight());
			m.mapRect(rect);

			float height = rect.height();
			float width = rect.width();

			float deltaX = 0;
			float deltaY = 0;

			if (vertical) {
				// 图片小于屏幕大小，则居中显示。大于屏幕，上方留空则往上移，下放留空则往下移
				int screenHeight = mDm.heightPixels;
				if (height < screenHeight) {
					deltaY = (screenHeight - height) / 2 - rect.top;
				} else if (rect.top > 0) {
					deltaY = -rect.top;
				} else if (rect.bottom < screenHeight) {
					deltaY = mSrcPicIv.getHeight() - rect.bottom;
				}
			}

			if (horizontal) {
				int screenWidth = mDm.widthPixels;
				if (width < screenWidth) {
					deltaX = (screenWidth - width) / 2 - rect.left;
				} else if (rect.left > 0) {
					deltaX = -rect.left;
				} else if (rect.right < screenWidth) {
					deltaX = screenWidth - rect.right;
				}
			}
			mMatrix.postTranslate(deltaX, deltaY);
		} catch (Exception e) {
			finish();
		}
	}

	/**
	 * 两点的距离
	 */
	private float spacing(MotionEvent event) {
		float x = event.getX(0) - event.getX(1);
		float y = event.getY(0) - event.getY(1);
		return FloatMath.sqrt(x * x + y * y);
	}

	/**
	 * 两点的中点
	 */
	private void midPoint(PointF point, MotionEvent event) {
		float x = event.getX(0) + event.getX(1);
		float y = event.getY(0) + event.getY(1);
		point.set(x / 2, y / 2);
	}

	private Bitmap getBitmap() {
		Bitmap finalBitmap = null;
		getBarHeight();
		Bitmap screenShoot = takeScreenShot();
		if (null == screenShoot) {
			EvtLog.d(TAG, "获取缓存图片失败，再次调用方法获取图片");
			screenShoot = takeScreenShot();
		}
		if (null == screenShoot) {
			return finalBitmap;
		} else {
			mClipview = (ClipView) this.findViewById(R.id.clipview);
			mClipview.setmOutputX(mOutputX);
			mClipview.setmOutputY(mOutputY);
			int width = mClipview.getWidth();
			int height = mClipview.getHeight();
			try {
				finalBitmap = Bitmap.createBitmap(screenShoot, (width - mOutputX) / 2, (height - mOutputY) / 2
						+ mTitleBarHeight + mStatusBarHeight, mOutputX, mOutputY);
			} catch (Exception e) {
				finish();
			}
			return finalBitmap;
		}
	}

	private void getBarHeight() {
		Rect frame = new Rect();
		this.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
		mStatusBarHeight = frame.top;

		int contenttop = this.getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop();
		mTitleBarHeight = contenttop - mStatusBarHeight;
	}

	private Bitmap takeScreenShot() {
		View view = this.getWindow().getDecorView();
		if (view != null) {
			view.setDrawingCacheEnabled(true);
			view.buildDrawingCache();
			return view.getDrawingCache();
		}
		return null;
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
			case R.id.title_with_back_title_btn_left:
				finish();
				break;
			case R.id.title_with_back_title_btn_right:
				Bitmap fianBitmap = getBitmap();
				if (null != fianBitmap) {
					Intent intent = new Intent();
					intent.putExtra("data", fianBitmap);
					setResult(RESULT_OK, intent);
				}
				finish();
				break;

			default:
				break;
		}
	}
}
