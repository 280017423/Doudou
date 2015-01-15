package com.jnhlxd.doudou.activity;

import java.io.File;
import java.util.ArrayList;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jnhlxd.doudou.R;
import com.jnhlxd.doudou.adapter.ImageGridAdapter;
import com.jnhlxd.doudou.model.ImageItemModel;
import com.jnhlxd.doudou.util.AlbumHelper;
import com.jnhlxd.doudou.util.ConstantSet;

/**
 * 选择照片界面
 * 
 * @author zou.sq
 */
public class ChoosePhotoActivity extends ActivityBase implements OnClickListener {

	private ArrayList<ImageItemModel> mAllImageList;
	private GridView mGvPhoto;
	private ImageGridAdapter mAdapter; // 自定义的适配器
	private AlbumHelper mHelper;
	private LinearLayout mLlBack;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_choose_photo);
		initVariables();
		initView();
		setListener();
	}

	private void initVariables() {
		mHelper = AlbumHelper.getHelper();
		mHelper.init(this);
		mAllImageList = (ArrayList<ImageItemModel>) getIntent().getSerializableExtra(ConstantSet.EXTRA_IMAGE_LIST);
		mAllImageList = mAllImageList == null ? new ArrayList<ImageItemModel>() : mAllImageList;
	}

	@Override
	public void onBackPressed() {
		setResult(RESULT_CANCELED);
		finish();
	}

	private void setListener() {
		mLlBack.setOnClickListener(this);
	}

	private void initView() {

		mLlBack = (LinearLayout) findViewById(R.id.title_with_back_title_btn_left);
		TextView mTvBack = (TextView) findViewById(R.id.tv_title_with_back_left);
		mTvBack.setText(R.string.title_back_text);
		mTvBack.setBackgroundResource(R.drawable.btn_back_bg);
		TextView tvTitle = (TextView) findViewById(R.id.title_with_back_title_btn_mid);
		tvTitle.setText(R.string.choose_photo);

		mGvPhoto = (GridView) findViewById(R.id.gridview);
		mAdapter = new ImageGridAdapter(ChoosePhotoActivity.this, mAllImageList);
		mGvPhoto.setAdapter(mAdapter);

		mGvPhoto.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				ImageItemModel model = (ImageItemModel) parent.getAdapter().getItem(position);
				if (null != model) {
					Intent intent = new Intent(ChoosePhotoActivity.this, ImageAlbumActivity.class);
					File file = new File(model.imagePath);
					intent.setData(Uri.fromFile(file));
					setResult(RESULT_OK, intent);
					finish();
				}
			}

		});
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
