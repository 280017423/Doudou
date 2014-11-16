package com.jnhlxd.doudou.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;

import com.jnhlxd.doudou.R;
import com.jnhlxd.doudou.adapter.NewerAdapter;
import com.jnhlxd.doudou.widget.AutoScrollViewPager;
import com.qianjiang.framework.util.HttpClientUtil;

/**
 * 
 * 新手引导界面
 * 
 * @author zou.sq
 * 
 */
public class NewerGuidingActivity extends ActivityBase {
	private static final int DIALOG_EXIT_APP = 1;
	private AutoScrollViewPager mViewPager;
	private int mCurrentPosition;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.newer_guiding);
		initVariables();
		findView();
	}

	private void findView() {
		mViewPager = (AutoScrollViewPager) findViewById(R.id.vp_newer_guide);
		mViewPager.setAdapter(new NewerAdapter(this));
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				mCurrentPosition = position;
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
	}

	private void initVariables() {
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {

			if (mCurrentPosition <= 0) {
				showDialog(DIALOG_EXIT_APP);
			} else {
				mViewPager.setCurrentItem(mCurrentPosition - 1, true);
			}

			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
			case DIALOG_EXIT_APP:
				return createDialogBuilder(this, getString(R.string.button_text_tips),
						getString(R.string.exit_dialog_title), getString(R.string.button_text_no),
						getString(R.string.button_text_yes)).create(id);
			default:
				break;
		}
		return super.onCreateDialog(id);
	}

	@Override
	public void onNegativeBtnClick(int id, DialogInterface dialog, int which) {
		switch (id) {
			case DIALOG_EXIT_APP:
				HttpClientUtil.setCookieStore(null);
				finish();
				break;
			default:
				break;
		}
	}
}
