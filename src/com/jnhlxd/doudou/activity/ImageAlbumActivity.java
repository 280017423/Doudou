package com.jnhlxd.doudou.activity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.jnhlxd.doudou.R;
import com.jnhlxd.doudou.adapter.ImageBucketAdapter;
import com.jnhlxd.doudou.model.ImageAlbumModel;
import com.jnhlxd.doudou.util.AlbumHelper;
import com.jnhlxd.doudou.util.ConstantSet;

/**
 * 相册界面
 * 
 * @version 1.0
 * @author zou.sq
 */
public class ImageAlbumActivity extends ActivityBase implements OnClickListener {

	public static final int REQUEST_CODE_SELECT_IMAGES = 1004; // 自定义相册，可多选
	private List<ImageAlbumModel> mDataList;
	private ListView mListView;
	private ImageBucketAdapter mAdapter;
	private AlbumHelper mAlbumHelper;
	private LinearLayout mLlBack;
	private TextView mTvEmptyContent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_album);
		initVariables();
		initView();
		setListener();
	}

	private void initVariables() {
		mAlbumHelper = AlbumHelper.getHelper();
		mAlbumHelper.init(this);
		mDataList = new ArrayList<ImageAlbumModel>();
	}

	private void setListener() {
		mLlBack.setOnClickListener(this);
	}

	@Override
	public void onResume() {
		super.onResume();
		List<ImageAlbumModel> tmpList = mAlbumHelper.getImagesBucketList(true);
		if (tmpList != null) {
			if (tmpList.isEmpty()) {
				mTvEmptyContent.setVisibility(View.VISIBLE);
				mListView.setVisibility(View.GONE);
			} else {
				mTvEmptyContent.setVisibility(View.GONE);
				mListView.setVisibility(View.VISIBLE);
			}
			mDataList.clear();
			mDataList.addAll(tmpList);
			mAdapter.notifyDataSetChanged();
		}
	}

	private void initView() {
		mLlBack = (LinearLayout) findViewById(R.id.title_with_back_title_btn_left);
		TextView mTvBack = (TextView) findViewById(R.id.tv_title_with_back_left);
		mTvBack.setText(R.string.title_back_text);
		mTvBack.setBackgroundResource(R.drawable.btn_back_bg);

		TextView tvTitle = (TextView) findViewById(R.id.title_with_back_title_btn_mid);
		tvTitle.setText(R.string.choose_photo_album);
		mListView = (ListView) findViewById(R.id.lv_photo_album);
		mAdapter = new ImageBucketAdapter(ImageAlbumActivity.this, mDataList);
		mTvEmptyContent = (TextView) findViewById(R.id.tv_empty_content);
		mListView.setAdapter(mAdapter);

		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent(ImageAlbumActivity.this, ChoosePhotoActivity.class);
				intent.putExtra(ConstantSet.EXTRA_IMAGE_LIST, (Serializable) mDataList.get(position).imageList);
				startActivityForResult(intent, REQUEST_CODE_SELECT_IMAGES);
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode != RESULT_OK) {
			return;
		}
		if (REQUEST_CODE_SELECT_IMAGES == requestCode) {
			setResult(RESULT_OK, data);
			finish();
		}
	}

	@Override
	public void onBackPressed() {
		setResult(RESULT_CANCELED);
		finish();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.title_with_back_title_btn_left:
				setResult(RESULT_CANCELED);
				finish();
				break;
			default:
				break;
		}
	}

}
