package com.jnhlxd.doudou.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.GridView;
import android.widget.TextView;

import com.jnhlxd.doudou.R;
import com.jnhlxd.doudou.adapter.StudentAdapter;
import com.jnhlxd.doudou.manager.UserMgr;
import com.jnhlxd.doudou.model.StudentModel;

/**
 * 应用主界面
 * 
 * @author zou.sq
 */
public class MainActivity extends ActivityBase {
	private static final int DIALOG_EXIT_APP = 0;
	private GridView mGvStudent;
	private List<StudentModel> mStudentModels;
	private StudentAdapter mAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initVariables();
		initView();
		getStudents();
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
		mAdapter = new StudentAdapter(this, mStudentModels);
	}

	private void initView() {
		initTitle();
		mGvStudent = (GridView) findViewById(R.id.gv_student);
		mGvStudent.setAdapter(mAdapter);
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
}
