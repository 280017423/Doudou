package com.jnhlxd.doudou.activity;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.KeyEvent;

import com.jnhlxd.doudou.R;
import com.jnhlxd.doudou.authentication.ActionProcessor;
import com.jnhlxd.doudou.db.DbDao;
import com.jnhlxd.doudou.manager.PunchMgr;
import com.jnhlxd.doudou.model.DropPickModel;
import com.jnhlxd.doudou.util.ConstantSet;
import com.jnhlxd.doudou.util.SharedPreferenceUtil;
import com.qianjiang.framework.authentication.BaseLoginProcessor.LOGIN_TYPE;
import com.qianjiang.framework.util.EvtLog;
import com.qianjiang.framework.util.PackageUtil;
import com.qianjiang.framework.util.StringUtil;
import com.umeng.analytics.MobclickAgent;

/**
 * 
 * 启动界面
 * 
 * @version 1.0
 * @author zou.sq 2014-8-18 下午4:11:37 取消程序启动的第一次自动登录，启动就跳转到登录界面v1.2.0</br>
 * 
 */
public class LoadingActivity extends ActivityBase {
	private static final int DISPLAY_TIME = 3000;
	private static final String TAG = "LoadingActivity";
	private static final int SIGN_MODULE_MODEL_SIZE = 6;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_loading);
		initSignModule();
		MobclickAgent.updateOnlineConfig(this);
		testJump();
	}

	private void testJump() {
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				// if (!isJumpToNewerGuiding()) {
				// 跳过新手引导
				// 取消程序启动的第一次自动登录，启动就跳转到登录界面v1.2.0
				new ActionProcessor(true).startActivity(LoadingActivity.this, new Intent(
						LoadingActivity.this, MainActivity.class), LOGIN_TYPE.Exit_To_Cancel_Apk);
				// } else {
				// // 跳转到新手引导
				// startActivity(new Intent(LoadingActivity.this,
				// NewerGuidingActivity.class));
				// }
				finish();
			}
		}, DISPLAY_TIME);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private boolean isJumpToNewerGuiding() {

		String versionString = SharedPreferenceUtil.getStringValueByKey(LoadingActivity.this,
				ConstantSet.KEY_APPLICATION_CONFIG_FILE, ConstantSet.KEY_NEWER_GUIDING_FINISH);
		int code = -1;
		try {
			code = PackageUtil.getVersionCode();
		} catch (NameNotFoundException e) {
			EvtLog.w(TAG, e);
		}
		if (!StringUtil.isNullOrEmpty(versionString) && versionString.equals(code + "")) {
			return false;
		}
		return true;
	}

	/**
	 * 第一次启动，初始化打卡模块数据
	 * 
	 * @return void
	 **/
	private void initSignModule() {
		List<DropPickModel> models = PunchMgr.getSignModules();
		String[] defaultSpeechArray = getResources().getStringArray(R.array.default_speech_msg);
		String[] dropPickNameArray = getResources().getStringArray(R.array.drop_pick_name);
		if (null == models || SIGN_MODULE_MODEL_SIZE != models.size()) {
			for (int i = 0; i < SIGN_MODULE_MODEL_SIZE; i++) {
				DropPickModel model = new DropPickModel();
				model.setSignMode(i + 1);
				model.setCurrentModel(DropPickModel.SIGN_TYPE_IN_SCHOOL == model.getSignMode() ? 1 : 0); // 默认设置为入园模式
				model.setSignToast(defaultSpeechArray[i]);
				model.setSignModeName(dropPickNameArray[i]);
				DbDao.saveModel(model);
			}
		}
	}
}
