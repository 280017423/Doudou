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
import com.jnhlxd.doudou.db.PunchDao;
import com.jnhlxd.doudou.manager.PunchMgr;
import com.jnhlxd.doudou.manager.UserMgr;
import com.jnhlxd.doudou.model.ClassInfoModel;
import com.jnhlxd.doudou.model.DropPickModel;
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
	private static final int DISPLAY_TIME = 2000;
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
	private PopWindowUtil mChooseClassPopUtil;
	private PopWindowUtil mDropPickPopUtil;
	private TextView mTvChooseClass;
	private TextView mTvDropPick;
	private int mCurrentModel;
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
		getStudents();
		bindPunchService();
	}

	private void setListener() {
		mBtnManualSign.setOnClickListener(this);
		mGvStudent.setOnItemClickListener(this);
		mGvStudent.setOnItemLongClickListener(this);
		mEdtPunchNo.setOnKeyListener(this);
	}

	private void getStudents() {
		for (int i = 0; i < 50; i++) {
			StudentModel model = new StudentModel();
			model.setChild_id(i + "");
			model.setClass_id(6 + "");
			if (5 == i) {
				model.setSignId("0277160416");
			} else {
				model.setSignId("" + i);
			}
			model.setName("测试同学" + i);
			mStudentModels.add(model);
		}
		mAdapter.notifyDataSetChanged();
	}

	private void initVariables() {
		mDropPickModel = PunchDao.getCurrentModule();
		mClassInfoModels = UserMgr.getClassInfoModels();
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
				model.setCurrentModel(1);
				mTvChooseClass.setText(model.getClass_name());
				mPopClassAdapter.notifyDataSetChanged();
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
					if (mClassInfoModel.getClass_id() == model.getClass_id()) {
						model.setCurrentModel(1);
					} else {
						model.setCurrentModel(0);
					}
				}
				mPopClassAdapter.notifyDataSetChanged();
				mTvChooseClass.setText(mClassInfoModel.getClass_name());
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
				}
			}
		});
	}

	private void initTitle() {
		TextView tvTitle = (TextView) findViewById(R.id.title_with_back_title_btn_mid);
		tvTitle.setText("宝贝幼儿园");
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
			case R.id.btn_manual_sign:
				submitSignInfo();
				break;
			case R.id.ll_choose_class:
				mChooseClassPopUtil.show();
				break;
			case R.id.ll_drop_pick_mode:
				mDropPickPopUtil.show();
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
			PunchMgr.savePunchModel2Db(signId, mCurrentModel);
			model.setSignModel(StudentModel.SIGN_TYPE_SIGNED);
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
		int status = model.getSignModel();
		switch (status) {
			case StudentModel.SIGN_TYPE_NOT_SIGN:
				model.setSignModel(StudentModel.SIGN_TYPE_SIGNING);
				mSelectModels.add(model);
				break;
			case StudentModel.SIGN_TYPE_SIGNING:
				model.setSignModel(StudentModel.SIGN_TYPE_NOT_SIGN);
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
		showPopupWindow(view, model);
		return true;
	}

	private void showPopupWindow(View relativeView, StudentModel model) {
		if (model == null || relativeView == null) {
			return;
		}
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
		TextView tvResend = (TextView) contentView.findViewById(R.id.tv_is_resend);
		TextView tvDelete = (TextView) contentView.findViewById(R.id.tv_delete_fail_msg);
		ImageView ivArrowUp = (ImageView) contentView.findViewById(R.id.iv_arrow_up);
		ImageView ivArrowDown = (ImageView) contentView.findViewById(R.id.iv_arrow_down);
		// 事件监听优化,如果没有TAG，说明是第一次进入，需要设置
		if (tvResend != null && tvResend.getTag() == null) {
			tvResend.setOnClickListener(this);
		}
		if (tvDelete != null && tvDelete.getTag() == null) {
			tvDelete.setOnClickListener(this);
		}
		tvResend.setTag(model);
		tvDelete.setTag(model);

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
		final List<SignModel> models = PunchMgr.getLocalPunchInfo();
		if (null == models) {
			return;
		}
		int size = models.size();
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
			doActionAgain(TAG, DISPLAY_TIME, new ActionListener() {

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
		// 用来发送考勤数据
		PunchMgr.savePunchModel2Db(punchNo, mCurrentModel);
		if (null != mStudentModels && !mStudentModels.isEmpty()) {
			int size = mStudentModels.size();
			for (int i = 0; i < size; i++) {
				StudentModel model = mStudentModels.get(i);
				if (punchNo.equals(model.getSignId())) {
					model.setSignModel(StudentModel.SIGN_TYPE_SIGNED);
					break;
				}
			}
		}
	}
}
