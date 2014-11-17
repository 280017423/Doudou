package com.jnhlxd.doudou.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;

import com.jnhlxd.doudou.R;
import com.jnhlxd.doudou.authentication.ActionResult;
import com.jnhlxd.doudou.authentication.LoginProcessor;
import com.jnhlxd.doudou.manager.UserMgr;
import com.jnhlxd.doudou.model.UserInfoModel;
import com.jnhlxd.doudou.req.UserReq;
import com.jnhlxd.doudou.util.ConstantSet;
import com.jnhlxd.doudou.util.SharedPreferenceUtil;
import com.qianjiang.framework.authentication.BaseLoginProcessor;
import com.qianjiang.framework.authentication.BaseLoginProcessor.LOGIN_TYPE;
import com.qianjiang.framework.util.ImeUtil;
import com.qianjiang.framework.util.NetUtil;
import com.qianjiang.framework.util.StringUtil;
import com.qianjiang.framework.widget.LoadingUpView;

/**
 * 登录界面
 * 
 * @version 1.0
 * @author zou.sq 2014-8-18 下午4:38:27 增加记住我和无法登录的逻辑</br>
 * 
 */
public class LoginActivity extends ActivityBase implements OnClickListener {
	private static final int DIALOG_EXIT_APP = 0;
	private static final int DELAY_TIME = 500;
	private static final String TAG = "LoginActivity";
	private boolean mIsLogining;
	private LOGIN_TYPE mLoginType;
	// 登录来源动作标记
	private String mIdentify;
	private EditText mEdtChildName;
	private EditText mEdtTel;
	private CheckBox mCbRememberMe;
	private LoadingUpView mLoadingUpView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		initVariable();
		initView();
	}

	private void initVariable() {
		mIdentify = getIntent().getStringExtra(BaseLoginProcessor.IDENTIFY);
		mLoginType = (LOGIN_TYPE) getIntent().getExtras().get(BaseLoginProcessor.KEY_LOGIN_TYPE);
	}

	private void initView() {
		mLoadingUpView = new LoadingUpView(this, true);
		mLoadingUpView.setCancelable(false);
		mEdtChildName = (EditText) findViewById(R.id.edt_child_name);
		mEdtTel = (EditText) findViewById(R.id.edt_tel);
		mCbRememberMe = (CheckBox) findViewById(R.id.cb_remember_me);
		mCbRememberMe.setChecked(SharedPreferenceUtil.getBooleanValueByKey(this,
				ConstantSet.KEY_APPLICATION_CONFIG_FILE, ConstantSet.KEY_IS_REMEMBER_ME, true));
		UserInfoModel userInfoModel = UserMgr.getUserInfoModel();
		if (userInfoModel != null && !StringUtil.isNullOrEmpty(userInfoModel.getTeacherId())) {
			if (mCbRememberMe.isChecked()) {
				// 如果有用户信息并且选择记住我，那么显示用户名和密码
				mEdtChildName.setText(userInfoModel.getName());
				mEdtTel.setText(userInfoModel.getPwd());
			} else {
				mEdtChildName.setText(userInfoModel.getName());
				mEdtTel.setText("");
			}
		}
	}

	private void checkAndLogin() {
		if (mIsLogining) {
			return;
		}
		String childName = mEdtChildName.getText().toString();
		String edtTel = mEdtTel.getText().toString();
		if (StringUtil.isNullOrEmpty(childName) || StringUtil.isNullOrEmpty(edtTel)) {
			toast(getString(R.string.toast_login_error));
			return;
		}
		ImeUtil.hideSoftInput(LoginActivity.this);
		if (!NetUtil.isNetworkAvailable()) {
			toast(getString(R.string.network_is_not_available));
			return;
		}
		if (null != mLoadingUpView && !mLoadingUpView.isShowing()) {
			mLoadingUpView.showPopup();
		}
		mIsLogining = true;
		new AsyncLogin().execute(childName, edtTel);
	}

	@Override
	public void onClick(final View v) {
		doActionAgain(TAG, DELAY_TIME, new ActionListener() {
			@Override
			public void doAction() {
				switch (v.getId()) {
					case R.id.btn_login:
						checkAndLogin();
						break;
					case R.id.ll_remember_me:
						mCbRememberMe.setChecked(!mCbRememberMe.isChecked());
						break;
					default:
						break;
				}
			}
		});
	}

	/**
	 * 异步登录
	 * 
	 * @author zeng.ww
	 * @since 2012-7-16 下午03:26:10
	 */
	class AsyncLogin extends AsyncTask<String, Void, ActionResult> {

		@Override
		protected ActionResult doInBackground(String... params) {
			String userName = "";
			String userTel = "";
			if (params != null) {
				if (params.length > 0) {
					userName = params[0];
				}
				if (params.length > 1) {
					userTel = params[1];
				}
			}
			return UserReq.login(userName, userTel);
		}

		@Override
		protected void onPostExecute(ActionResult result) {
			if (result != null && ActionResult.RESULT_CODE_SUCCESS.equals(result.ResultCode)) {
				// 增加登录成功提示语
				String loginInfo = (String) result.ResultObject;
				toast(loginInfo);
				LoginProcessor.getInstance().onLoginSuccess(LoginActivity.this, mIdentify);
				// 登录成功就记录记住我的状态，登出就清除该状态
				SharedPreferenceUtil.saveValue(LoginActivity.this, ConstantSet.KEY_APPLICATION_CONFIG_FILE,
						ConstantSet.KEY_IS_REMEMBER_ME, mCbRememberMe.isChecked());
			} else {
				// 登录失败
				LoginProcessor.getInstance().onLoginError(LoginActivity.this, mIdentify);
				showErrorMsg(result);
			}
			dismissLoadingUpView(mLoadingUpView);
			mIsLogining = false;
		}
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
			finish();
		}
		super.onNegativeBtnClick(id, dialog, which);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// 是退出APP的那个LoginType时，才退出
		if (keyCode == KeyEvent.KEYCODE_BACK && mLoginType != null && mLoginType.equals(LOGIN_TYPE.Exit_To_Cancel_Apk)) {
			showDialog(DIALOG_EXIT_APP);
			return false;
		} else if (keyCode == KeyEvent.KEYCODE_BACK) {
			toBack();
		}
		return super.onKeyDown(keyCode, event);
	}

	private void toBack() {
		doExitLoginActivity();
	}

	/**
	 * 退出操作
	 */
	private void doExitLoginActivity() {
		// 底层已做关闭处理
		LoginProcessor.getInstance().onLoginCancel(this, mIdentify, MainActivity.class);
	}
}
