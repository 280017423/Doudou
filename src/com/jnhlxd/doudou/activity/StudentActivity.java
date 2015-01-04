package com.jnhlxd.doudou.activity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.List;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jnhlxd.doudou.R;
import com.jnhlxd.doudou.authentication.ActionProcessor;
import com.jnhlxd.doudou.authentication.ActionResult;
import com.jnhlxd.doudou.db.ClassDao;
import com.jnhlxd.doudou.listener.IActionListener;
import com.jnhlxd.doudou.model.ClassInfoModel;
import com.jnhlxd.doudou.model.StudentModel;
import com.jnhlxd.doudou.req.UserReq;
import com.jnhlxd.doudou.util.ConstantSet;
import com.jnhlxd.doudou.util.ImageUtil;
import com.jnhlxd.doudou.util.PopWindowUtil;
import com.jnhlxd.doudou.util.ServerAPIConstant;
import com.jnhlxd.doudou.widget.RoundImageView;
import com.qianjiang.framework.util.FileUtil;
import com.qianjiang.framework.util.StringUtil;
import com.qianjiang.framework.widget.LoadingUpView;

public class StudentActivity extends ActivityBase implements OnClickListener {

	public static final int PHOTO_REQUEST_TAKEPHOTO = 1; // 拍照
	public static final int PHOTO_REQUEST_GALLERY = 2; // 从相册中选择
	public static final int PHOTO_REQUEST_CUT = 3; // 结果
	public static final int MSG_PHOTO_UPLOAD_SUCCESS = 4; // 结果
	public static final int MSG_PHOTO_UPLOAD_FAIL = 5; // 结果
	public static final int UPDATE_SUCCESS = 6; // 结果
	public static final int UPDATE_FAIL = 7; // 结果

	private LinearLayout mLlBack;
	private StudentModel mStudentModel;
	private TextView mTvClassName;
	private TextView mTvStudentName;
	private RoundImageView mIvIcon;
	private List<ClassInfoModel> mClassInfoModels;
	private PopWindowUtil mPopWindowUtil;
	public static String TEMP_PHONE_FILENAME = "";
	private LoadingUpView mLoadingUpView;
	private File mGzipFile;

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			ActionResult result = (ActionResult) msg.obj;
			dismissLoadingUpView(mLoadingUpView);
			switch (msg.what) {
				case UPDATE_SUCCESS:
					String headUrl = (String) result.ResultObject;
					mStudentModel.setHeadIcon(headUrl);
					Intent intent = new Intent(StudentActivity.this, MainActivity.class);
					intent.putExtra(ConstantSet.KEY_STUDENT_MODEL, mStudentModel);
					setResult(RESULT_OK, intent);
					toast(getString(R.string.upload_head_icon_success));
					finish();
					break;
				case UPDATE_FAIL:
					showErrorMsg(result);
					break;
				default:
					break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_student);
		initVariables();
		initView();
		setListener();
	}

	private void initVariables() {
		mLoadingUpView = new LoadingUpView(this);
		mStudentModel = (StudentModel) getIntent().getSerializableExtra(ConstantSet.KEY_STUDENT_MODEL);
		if (null == mStudentModel) {
			finish();
		}
		mClassInfoModels = ClassDao.getClassInfoModels();
		if (ImageUtil.hasSdcard()) {
			mGzipFile = new File(Environment.getExternalStorageDirectory(), "TempHeadImage.gz");
		} else {
			toast(getString(R.string.user_image_no_sdcard));
			finish();
		}
	}

	private void setListener() {
		mLlBack.setOnClickListener(this);
	}

	private void initView() {
		TextView titleTextView = (TextView) findViewById(R.id.title_with_back_title_btn_mid);
		titleTextView.setText(R.string.upload_head_icon);
		mLlBack = (LinearLayout) findViewById(R.id.title_with_back_title_btn_left);
		TextView mTvBack = (TextView) findViewById(R.id.tv_title_with_back_left);
		mTvBack.setText(R.string.title_back_text);
		mTvBack.setBackgroundResource(R.drawable.btn_back_bg);

		mIvIcon = (RoundImageView) findViewById(R.id.iv_student_icon);
		mTvClassName = (TextView) findViewById(R.id.tv_student_class);
		mTvStudentName = (TextView) findViewById(R.id.tv_student_name);
		mTvStudentName.setText(mStudentModel.getName());
		String classId = mStudentModel.getClassId();
		if (StringUtil.isNullOrEmpty(classId)) {
			mTvClassName.setVisibility(View.GONE);
		} else {
			if (null != mClassInfoModels) {
				for (int i = 0; i < mClassInfoModels.size(); i++) {
					if (classId.equals(mClassInfoModels.get(i).getClassId())) {
						mTvClassName.setText(mClassInfoModels.get(i).getClassName());
						break;
					}
				}
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.title_with_back_title_btn_left:
				finish();
				break;
			case R.id.iv_student_icon:
				showHeadImageWindow(v);
				mPopWindowUtil.show(Gravity.BOTTOM);
				break;
			case R.id.tv_cancel:
				mPopWindowUtil.dismiss();
				break;
			case R.id.tv_take_photo:
				mPopWindowUtil.dismiss();
				if (ImageUtil.hasSdcard()) {
					FileUtil.prepareFile(Environment.getExternalStorageDirectory() + "/DCIM/"); // 准备文件夹
					String fileName = ImageUtil.getPhotoFileName();
					File mTempFile = new File(Environment.getExternalStorageDirectory() + "/DCIM/", fileName);
					TEMP_PHONE_FILENAME = fileName;
					try {
						Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); // 调用系统的拍照功能
						// 判断存储卡是否可以用，可用进行存储
						intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mTempFile)); // 指定调用相机拍照后照片的储存路径
						startActivityForResult(intent, PHOTO_REQUEST_TAKEPHOTO);
					} catch (Exception e) {
						toast(getString(R.string.user_image_not_support));
					}
				} else {
					toast(getString(R.string.user_image_no_sdcard));
				}
				break;
			case R.id.tv_choose_photo:
				mPopWindowUtil.dismiss();
				Intent intent = new Intent(StudentActivity.this, ImageAlbumActivity.class);
				startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
				break;
			default:
				break;
		}
	}

	private void showHeadImageWindow(View parent) {
		View contentView = View.inflate(this, R.layout.header_list_item, null);
		mPopWindowUtil = new PopWindowUtil(contentView, null);
		contentView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mPopWindowUtil.dismiss();
			}
		});
		contentView.findViewById(R.id.tv_cancel).setOnClickListener(this);
		contentView.findViewById(R.id.tv_take_photo).setOnClickListener(this);
		contentView.findViewById(R.id.tv_choose_photo).setOnClickListener(this);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// 因为照相根本不需要使用data，所以不判断
		if (resultCode != RESULT_OK) {
			return;
		}
		switch (requestCode) {
			case PHOTO_REQUEST_TAKEPHOTO:// 当选择拍照时调用
				startPhotoZoom(Uri.fromFile(new File(
						Environment.getExternalStorageDirectory() + "/DCIM/", TEMP_PHONE_FILENAME)));
				break;
			case PHOTO_REQUEST_GALLERY:// 当选择从本地获取图片时
				startPhotoZoom(data.getData());
				break;
			case PHOTO_REQUEST_CUT:// 返回的结果
				File file = getCutImage(data);
				if (file == null || !file.exists()) {
					// 小米手机上出现SD卡已经卸载，但是还能显示图片，当裁剪图片后读取的时候文件没有发现
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							toast(getResources().getString(R.string.center_no_find_file));
						}
					});
					ImageUtil.doCompressFile(file, mGzipFile); // 压缩文件
					FileUtil.delete(file);
					uploadPhoto();
					return;
				}
				break;
			default:
				break;
		}
	}

	private void uploadPhoto() {
		showLoadingUpView(mLoadingUpView);
		new ActionProcessor().startAction(this, new IActionListener() {

			@Override
			public void onSuccess(ActionResult result) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onError(ActionResult result) {
				// TODO Auto-generated method stub

			}

			@Override
			public ActionResult onAsyncRun() {
				ActionResult actionResult = UserReq.uploadHeadImage(mGzipFile, mStudentModel.getChildId());
				FileUtil.delete(mGzipFile); // 删除文件
				return actionResult;
			}
		});

	}

	private File getCutImage(Intent picdata) {
		File file = null;
		if (null != picdata) {
			Bundle bundle = picdata.getExtras();
			if (bundle != null) {
				Bitmap photo = bundle.getParcelable("data");
				Bitmap newPhoto = ImageUtil.zoomImg(photo, 150, 150);
				mIvIcon.setImageBitmap(newPhoto);
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				newPhoto.compress(Bitmap.CompressFormat.JPEG, 100, stream);
				byte[] b = stream.toByteArray();
				file = ImageUtil.getFile(b, Environment.getExternalStorageDirectory().getPath(), "CutHeadImage.jpg");
			}
		}
		return file;
	}

	/**
	 * 跳转到截屏的界面
	 * 
	 * @param uri
	 *            需要裁剪图片的uri
	 */
	private void startPhotoZoom(Uri uri) {
		Intent intent = new Intent(this, ClipPictureActivity.class);
		intent.setDataAndType(uri, "image/*");
		startActivityForResult(intent, PHOTO_REQUEST_CUT);
	}

}