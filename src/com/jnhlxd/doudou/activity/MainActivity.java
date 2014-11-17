package com.jnhlxd.doudou.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.jnhlxd.doudou.R;
import com.jnhlxd.doudou.adapter.PopClassAdapter;
import com.jnhlxd.doudou.adapter.StudentAdapter;
import com.jnhlxd.doudou.manager.PunchMgr;
import com.jnhlxd.doudou.manager.UserMgr;
import com.jnhlxd.doudou.model.StudentModel;
import com.jnhlxd.doudou.service.PunchService;
import com.jnhlxd.doudou.service.PunchService.PunchBinder;
import com.jnhlxd.doudou.util.PopWindowUtil;
import com.qianjiang.framework.util.EvtLog;

/**
 * 应用主界面
 * 
 * @author zou.sq
 */
public class MainActivity extends ActivityBase implements OnClickListener, OnItemClickListener, OnItemLongClickListener {
	private static final String TAG = "MainActivity";
	private static final int DIALOG_EXIT_APP = 0;
	private Button mBtnManualSign;
	private GridView mGvStudent;
	private List<StudentModel> mStudentModels;
	private List<StudentModel> mSelectModels;
	private StudentAdapter mAdapter;
	private PunchService mService;
	private PopWindowUtil mChooseClassPopUtil;
	private PopWindowUtil mDropPickPopUtil;
	private PopWindowUtil mSubModePopUtil;
	private TextView mTvChooseClass;
	private TextView mTvDropPick;
	private TextView mTvSubMode;
	private int mCurrentModel;

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
	}

	private void getStudents() {
		for (int i = 0; i < 50; i++) {
			StudentModel model = new StudentModel();
			model.setName("测试同学" + i);
			mStudentModels.add(model);
		}
		mAdapter.notifyDataSetChanged();
	}

	private void initVariables() {
		mStudentModels = new ArrayList<StudentModel>();
		mSelectModels = new ArrayList<StudentModel>();
		mAdapter = new StudentAdapter(this, mStudentModels);

	}

	private void initView() {
		initTitle();
		mTvChooseClass = (TextView) findViewById(R.id.tv_choose_class);
		mTvDropPick = (TextView) findViewById(R.id.tv_drop_pick_mode);
		mTvSubMode = (TextView) findViewById(R.id.tv_choose_sub_mode);
		mBtnManualSign = (Button) findViewById(R.id.btn_manual_sign);
		mGvStudent = (GridView) findViewById(R.id.gv_student);
		mGvStudent.setAdapter(mAdapter);
		initClassPop();
	}

	private void initClassPop() {
		View contentView = View.inflate(this, R.layout.view_pop_teacher, null);
		mChooseClassPopUtil = new PopWindowUtil(contentView, null);
		GridView gridView = (GridView) contentView.findViewById(R.id.gv_pop_teacher);
		gridView.setAdapter(new PopClassAdapter(this));
		gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				mChooseClassPopUtil.dismiss();
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
				break;
			case R.id.ll_choose_sub_mode:
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
		return true;
	}

	private void refreash() {
		if (null == mSelectModels || mSelectModels.isEmpty()) {
			mBtnManualSign.setVisibility(View.GONE);
		} else {
			mBtnManualSign.setVisibility(View.VISIBLE);
		}
	}

	@Override
	protected void onDestroy() {
		unbindPunchService();
		super.onDestroy();
	}
}
