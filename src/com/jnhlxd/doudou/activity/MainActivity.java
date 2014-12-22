package com.jnhlxd.doudou.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.IBinder;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.jnhlxd.doudou.R;
import com.jnhlxd.doudou.adapter.PopClassAdapter;
import com.jnhlxd.doudou.adapter.PopDropPickAdapter;
import com.jnhlxd.doudou.adapter.StudentAdapter;
import com.jnhlxd.doudou.db.ClassDao;
import com.jnhlxd.doudou.db.PunchDao;
import com.jnhlxd.doudou.db.StudentDao;
import com.jnhlxd.doudou.manager.PunchMgr;
import com.jnhlxd.doudou.manager.UserMgr;
import com.jnhlxd.doudou.model.ClassInfoModel;
import com.jnhlxd.doudou.model.DropPickModel;
import com.jnhlxd.doudou.model.SchoolInfoModel;
import com.jnhlxd.doudou.model.SignModel;
import com.jnhlxd.doudou.model.StudentModel;
import com.jnhlxd.doudou.service.PunchService;
import com.jnhlxd.doudou.service.PunchService.PunchBinder;
import com.jnhlxd.doudou.util.ConstantSet;
import com.jnhlxd.doudou.util.PopWindowUtil;
import com.qianjiang.framework.util.EvtLog;
import com.qianjiang.framework.util.NetUtil;
import com.qianjiang.framework.util.StringUtil;

/**
 * 应用主界面
 * 
 * @author zou.sq
 */
public class MainActivity extends ActivityBase implements OnKeyListener, OnClickListener, OnItemClickListener,
		OnItemLongClickListener {
	private static final String TAG = "MainActivity";
	private static final int DIALOG_EXIT_APP = 0;
	private Button mBtnManualSign;
	private GridView mGvStudent;
	private List<StudentModel> mStudentModels;
	private List<StudentModel> mSelectModels;
	private List<DropPickModel> mDropPickModels;
	private List<ClassInfoModel> mClassInfoModels;
	private StudentAdapter mAdapter;
	private PopDropPickAdapter mPopDropPickAdapter;
	private PopClassAdapter mPopClassAdapter;
	private PunchService mService;
	private PopWindowUtil mSignPopUtil;
	private PopWindowUtil mChooseClassPopUtil;
	private PopWindowUtil mDropPickPopUtil;
	private TextView mTvChooseClass;
	private TextView mTvDropPick;
	private ClassInfoModel mClassInfoModel;
	private DropPickModel mDropPickModel;
	private TextView mTvToastInfo;
	private EditText mEdtPunchNo;
	private PopupWindow mPopupWindow;

	private ServiceConnection mConnection = new ServiceConnection() {

		@Override
		public void onServiceDisconnected(ComponentName name) {
			mService = null;
		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			PunchBinder binder = (PunchBinder) service;
			mService = binder.getService();
		}
	};

	private void bindPunchService() {
		EvtLog.d(TAG, "bindPunchService");
		Intent mIntent = new Intent(this, PunchService.class);
		bindService(mIntent, mConnection, Context.BIND_AUTO_CREATE);
	}

	private void unbindPunchService() {
		try {
			unbindService(mConnection);
			EvtLog.d(TAG, "unbindPunchService");
		} catch (IllegalArgumentException e) {
			EvtLog.d(TAG, "Service wasn't bound!");
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		mIntentFilter.addAction(ConstantSet.ACTION_DEFAULT_BROAD);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initVariables();
		initView();
		setListener();
		bindPunchService();
	}

	private void setListener() {
		mBtnManualSign.setOnClickListener(this);
		mGvStudent.setOnItemClickListener(this);
		mGvStudent.setOnItemLongClickListener(this);
		mEdtPunchNo.setOnKeyListener(this);
	}

	private void initVariables() {
		mDropPickModel = PunchDao.getCurrentModule();
		mClassInfoModels = ClassDao.getClassInfoModels();
		mStudentModels = new ArrayList<StudentModel>();
		mSelectModels = new ArrayList<StudentModel>();
		mAdapter = new StudentAdapter(this, mStudentModels);
		mDropPickModels = PunchMgr.getSignModules();
		mPopDropPickAdapter = new PopDropPickAdapter(this, mDropPickModels);
		mPopClassAdapter = new PopClassAdapter(this, mClassInfoModels);
	}

	private void initView() {
		initTitle();
		mTvChooseClass = (TextView) findViewById(R.id.tv_choose_class);
		mTvToastInfo = (TextView) findViewById(R.id.tv_toast_info);
		mTvDropPick = (TextView) findViewById(R.id.tv_drop_pick_mode);
		mBtnManualSign = (Button) findViewById(R.id.btn_manual_sign);
		mGvStudent = (GridView) findViewById(R.id.gv_student);
		mGvStudent.setAdapter(mAdapter);
		initClassPop();
		initDropPickPop();
		if (null != mDropPickModel) {
			mTvDropPick.setText(mDropPickModel.getSignModeName());
		}
		if (null != mClassInfoModels && !mClassInfoModels.isEmpty()) {
			ClassInfoModel model = mClassInfoModels.get(0);
			if (null != model) {
				mClassInfoModel = model;
				model.setCurrentModel(1);
				mTvChooseClass.setText(model.getClassName());
				mPopClassAdapter.notifyDataSetChanged();
				getStudents(model.getClassId());
			}
		}
		mEdtPunchNo = (EditText) findViewById(R.id.edt_input_punch_no);
		mEdtPunchNo.setFocusable(true);
		// 控制EditText点击时不弹出键盘
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(mEdtPunchNo.getWindowToken(), 0);
		mEdtPunchNo.setInputType(0);
		mEdtPunchNo.requestFocus();
	}

	private void getStudents(String classId) {
		List<StudentModel> models = StudentDao.getStudentModels(classId);
		int stuSize = models.size();
		// 恢复状态的逻辑
		List<SignModel> signModels = PunchDao.getRefreashData(mDropPickModel.getSignMode());
		int size = signModels.size();
		for (int i = 0; i < stuSize; i++) {
			StudentModel studentModel = models.get(i);
			for (int j = 0; j < size; j++) {
				SignModel signModel = signModels.get(j);
				if (studentModel.getSignId().equals(signModel.getSignId())) {
					// 同步发送状态和签到类型
					studentModel.setSignMode(signModel.getSignMode());
					studentModel.setSignModelStatus(StudentModel.SIGN_TYPE_SIGNED);
				}
			}
		}
		if (null != models) {
			mStudentModels.clear();
			mStudentModels.addAll(models);
			mAdapter.notifyDataSetChanged();
		}
		refreash();
	}

	private void initClassPop() {
		View contentView = View.inflate(this, R.layout.view_pop_teacher, null);
		contentView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mChooseClassPopUtil.dismiss();
			}
		});
		mChooseClassPopUtil = new PopWindowUtil(contentView, null);
		GridView gridView = (GridView) contentView.findViewById(R.id.gv_pop_teacher);
		gridView.setAdapter(mPopClassAdapter);
		gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				mChooseClassPopUtil.dismiss();
				mClassInfoModel = (ClassInfoModel) parent.getAdapter().getItem(position);
				if (null == mClassInfoModel) {
					return;
				}
				int size = mClassInfoModels.size();
				for (int i = 0; i < size; i++) {
					ClassInfoModel model = mClassInfoModels.get(i);
					if (mClassInfoModel.getClassId() == model.getClassId()) {
						model.setCurrentModel(1);
					} else {
						model.setCurrentModel(0);
					}
				}
				getStudents(mClassInfoModel.getClassId());
				mPopClassAdapter.notifyDataSetChanged();
				mTvChooseClass.setText(mClassInfoModel.getClassName());
			}
		});
	}

	private void initDropPickPop() {
		View contentView = View.inflate(this, R.layout.view_pop_teacher, null);
		contentView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mDropPickPopUtil.dismiss();
			}
		});
		mDropPickPopUtil = new PopWindowUtil(contentView, null);
		GridView gridView = (GridView) contentView.findViewById(R.id.gv_pop_teacher);
		gridView.setAdapter(mPopDropPickAdapter);
		gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				mDropPickPopUtil.dismiss();
				mDropPickModel = (DropPickModel) parent.getAdapter().getItem(position);
				if (null != mDropPickModel) {
					mTvDropPick.setText(mDropPickModel.getSignModeName());
					DropPickModel.updateCurrentMode(mDropPickModel.getSignMode());
					mDropPickModels.clear();
					mDropPickModels.addAll(PunchMgr.getSignModules());
					mPopDropPickAdapter.notifyDataSetChanged();
					// 重置学生状态
					mSelectModels.clear();
					getStudents(mClassInfoModel.getClassId());
				}
			}
		});
	}

	private void initTitle() {
		SchoolInfoModel model = UserMgr.getSchoolInfoModel();
		TextView tvTitle = (TextView) findViewById(R.id.title_with_back_title_btn_mid);
		tvTitle.setText(model.getName());
		TextView tvRight = (TextView) findViewById(R.id.tv_title_with_right);
		tvRight.setBackgroundResource(R.drawable.tongyong_button_bg);
		findViewById(R.id.title_with_back_title_btn_right).setOnClickListener(this);
		tvRight.setText(R.string.about);
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
		if (DIALOG_EXIT_APP == id) {
			logout();
		}
		super.onNegativeBtnClick(id, dialog, which);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			showDialog(DIALOG_EXIT_APP);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void logout() {
		UserMgr.logout();
		finish();
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
			case R.id.title_with_back_title_btn_right:
				startActivity(new Intent(MainActivity.this, AboutActivity.class));
				break;
			case R.id.btn_manual_sign:
				submitSignInfo();
				break;
			case R.id.ll_choose_class:
				mChooseClassPopUtil.show();
				break;
			case R.id.ll_drop_pick_mode:
				mDropPickPopUtil.show();
				break;
			case R.id.tv_casual_leave:
				dismissPopupwindow();
				StudentModel casualLeaveModel = (StudentModel) v.getTag();
				if (null == casualLeaveModel) {
					return;
				}
				casualLeaveModel.setSignMode(DropPickModel.SIGN_TYPE_CASUAL_LEAVE);
				casualLeaveModel.setSignModelStatus(StudentModel.SIGN_TYPE_SIGNING);
				mSelectModels.add(casualLeaveModel);
				refreash();
				mAdapter.notifyDataSetChanged();
				break;
			case R.id.tv_sick_leave:
				dismissPopupwindow();
				StudentModel sickLeaveModel = (StudentModel) v.getTag();
				if (null == sickLeaveModel) {
					return;
				}
				sickLeaveModel.setSignMode(DropPickModel.SIGN_TYPE_SICK_LEAVE);
				sickLeaveModel.setSignModelStatus(StudentModel.SIGN_TYPE_SIGNING);
				mSelectModels.add(sickLeaveModel);
				refreash();
				mAdapter.notifyDataSetChanged();
				break;

			default:
				break;
		}
	}

	private void submitSignInfo() {
		if (null == mSelectModels || mSelectModels.isEmpty()) {
			toast(getString(R.string.choose_student));
			return;
		}
		int size = mSelectModels.size();
		for (int i = 0; i < size; i++) {
			StudentModel model = mSelectModels.get(i);
			String signId = model.getSignId();
			PunchMgr.savePunchModel2Db(signId, model.getSignMode());
			model.setSignModelStatus(StudentModel.SIGN_TYPE_SIGNED);
		}
		mSelectModels.clear();
		refreash();
		mAdapter.notifyDataSetChanged();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		StudentModel model = (StudentModel) parent.getAdapter().getItem(position);
		if (null == model) {
			return;
		}
		int status = model.getSignModelStatus();
		switch (status) {
			case StudentModel.SIGN_TYPE_NOT_SIGN:
				model.setSignMode(mDropPickModel.getSignMode());
				model.setSignModelStatus(StudentModel.SIGN_TYPE_SIGNING);
				mSelectModels.add(model);
				break;
			case StudentModel.SIGN_TYPE_SIGNING:
				model.setSignMode(0);
				model.setSignModelStatus(StudentModel.SIGN_TYPE_NOT_SIGN);
				mSelectModels.remove(model);
				break;
			default:
				break;
		}
		refreash();
		mAdapter.notifyDataSetChanged();
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
		StudentModel model = (StudentModel) parent.getAdapter().getItem(position);
		if (null != model && StudentModel.SIGN_TYPE_SIGNED != model.getSignModelStatus()) {
			// 已经打卡成功的不弹框
			showPopupWindow(view, model);
		}
		return true;
	}

	private void showPopupWindow(View relativeView, StudentModel model) {
		// 懒加载
		if (mPopupWindow == null) {
			View contentView = View.inflate(this, R.layout.send_msg_popwindow_layout, null);
			mPopupWindow = new PopupWindow(contentView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);
			mPopupWindow.setTouchable(true);
			mPopupWindow.setOutsideTouchable(true);
			ColorDrawable dw = new ColorDrawable(0x00);
			mPopupWindow.setBackgroundDrawable(dw);
		}
		View contentView = mPopupWindow.getContentView();
		TextView tvCasualLeave = (TextView) contentView.findViewById(R.id.tv_casual_leave);
		TextView tvSickLeave = (TextView) contentView.findViewById(R.id.tv_sick_leave);
		ImageView ivArrowUp = (ImageView) contentView.findViewById(R.id.iv_arrow_up);
		ImageView ivArrowDown = (ImageView) contentView.findViewById(R.id.iv_arrow_down);
		// 事件监听优化,如果没有TAG，说明是第一次进入，需要设置
		if (tvCasualLeave != null && tvCasualLeave.getTag() == null) {
			tvCasualLeave.setOnClickListener(this);
		}
		if (tvSickLeave != null && tvSickLeave.getTag() == null) {
			tvSickLeave.setOnClickListener(this);
		}
		tvCasualLeave.setTag(model);
		tvSickLeave.setTag(model);

		int[] locationOnScreen = new int[2];
		relativeView.getLocationOnScreen(locationOnScreen);
		int height = relativeView.getMeasuredHeight();
		int width = relativeView.getMeasuredWidth();
		int contentViewWidth = contentView.getMeasuredWidth();
		int offsetX = locationOnScreen[0];
		int offsetY = locationOnScreen[1];
		if (0 != contentViewWidth) {
			offsetX = (int) (locationOnScreen[0] - (contentViewWidth - width) / 2.0);
		}
		if (relativeView.getTop() < 0) {
			offsetY += height;
			ivArrowDown.setVisibility(View.GONE);
			ivArrowUp.setVisibility(View.VISIBLE);
		} else {
			ivArrowDown.setVisibility(View.VISIBLE);
			ivArrowUp.setVisibility(View.GONE);
			offsetY -= height;
		}
		mPopupWindow.showAtLocation(relativeView, Gravity.NO_GRAVITY, offsetX, offsetY);
	}

	private void dismissPopupwindow() {
		if (mPopupWindow != null && mPopupWindow.isShowing()) {
			mPopupWindow.dismiss();
		}
	}

	private void refreash() {
		if (null == mSelectModels || mSelectModels.isEmpty()) {
			mBtnManualSign.setVisibility(View.GONE);
		} else {
			mBtnManualSign.setVisibility(View.VISIBLE);
		}
	}

	private void refreashToast() {
		int size = PunchMgr.getNoSendDataSize();
		String netInfo = "";
		if (!NetUtil.isNetworkAvailable()) {
			netInfo = getString(R.string.network_is_not_found);
		}
		if (size <= 0) {
			mTvToastInfo.setVisibility(View.GONE);
		} else {
			mTvToastInfo.setVisibility(View.VISIBLE);
			if (!StringUtil.isNullOrEmpty(netInfo)) {
				netInfo = netInfo + ",";
			}
			mTvToastInfo.setText(netInfo + getString(R.string.left_student_count, size));
		}
	}

	@Override
	protected void processBroadReceiver(String action, Object data) {
		if (StringUtil.isNullOrEmpty(action)) {
			return;
		}
		if (ConstantSet.ACTION_DEFAULT_BROAD.equals(action)) {
			refreashToast();
		}
		super.processBroadReceiver(action, data);
	}

	@Override
	protected void onDestroy() {
		unbindPunchService();
		super.onDestroy();
	}

	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {

		if (KeyEvent.KEYCODE_ENTER == keyCode && KeyEvent.ACTION_DOWN == event.getAction()) {
			doActionAgain(TAG, new ActionListener() {

				@Override
				public void doAction() {
					final String punchNo = mEdtPunchNo.getText().toString();
					sendData(punchNo);
				}
			});
			mEdtPunchNo.setText("");
			return true;
		}
		return false;
	}

	private void sendData(String punchNo) {
		if (StringUtil.isNullOrEmpty(punchNo)) {
			return;
		}
		if (null != mStudentModels && !mStudentModels.isEmpty()) {
			int size = mStudentModels.size();
			for (int i = 0; i < size; i++) {
				StudentModel model = mStudentModels.get(i);
				if (punchNo.equals(model.getSignId())) {
					model.setSignModelStatus(StudentModel.SIGN_TYPE_SIGNED);
					mSelectModels.remove(model);
					refreash();
					mAdapter.notifyDataSetChanged();
					// 用来弹窗
					initSignPop(model);
					break;
				}
			}
		}
		// 用来发送考勤数据
		PunchMgr.savePunchModel2Db(punchNo, mDropPickModel.getSignMode());
	}

	private void initSignPop(StudentModel model) {
		View contentView = null;
		if (null == contentView) {
			contentView = View.inflate(this, R.layout.view_pop_sign, null);
		}
		contentView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mSignPopUtil.dismiss();
			}
		});
		mSignPopUtil = new PopWindowUtil(contentView, null);
		TextView tvClass = (TextView) contentView.findViewById(R.id.tv_student_class);
		TextView tvName = (TextView) contentView.findViewById(R.id.tv_student_name);
		ImageView ivIcon = (ImageView) contentView.findViewById(R.id.iv_student_icon);
		tvClass.setText(mClassInfoModel.getClassName());
		tvName.setText(model.getName());
		String imgUrl = model.getHeadIcon();
		if (!StringUtil.isNullOrEmpty(imgUrl)) {
			mImageLoader.displayImage(imgUrl, ivIcon, mOptions);
		}
		mSignPopUtil.showAndDismiss();
	}
}
