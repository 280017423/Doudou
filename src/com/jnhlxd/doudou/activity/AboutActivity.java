package com.jnhlxd.doudou.activity;

import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jnhlxd.doudou.R;
import com.qianjiang.framework.util.PackageUtil;

public class AboutActivity extends ActivityBase implements OnClickListener {

	private LinearLayout mLlBack;
	private String mVersionName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		initVariables();
		initView();
		setListener();
	}

	private void initVariables() {
		try {
			mVersionName = PackageUtil.getVersionName();
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
	}

	private void setListener() {
		mLlBack.setOnClickListener(this);
	}

	private void initView() {

		TextView titleTextView = (TextView) findViewById(R.id.title_with_back_title_btn_mid);
		titleTextView.setText(R.string.about);
		mLlBack = (LinearLayout) findViewById(R.id.title_with_back_title_btn_left);
		TextView mTvBack = (TextView) findViewById(R.id.tv_title_with_back_left);
		mTvBack.setText(R.string.title_back_text);
		mTvBack.setBackgroundResource(R.drawable.btn_back_bg);
		TextView tvVersion = (TextView) findViewById(R.id.tv_version_code);
		tvVersion.setText(getString(R.string.text_version_code, mVersionName));
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.title_with_back_title_btn_left:
				finish();
				break;
			default:
				break;
		}
	}

}