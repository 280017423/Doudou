package com.jnhlxd.doudou.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.jnhlxd.doudou.util.AlarmUtil;
import com.qianjiang.framework.util.EvtLog;

/**
 * 
 * Description the class 广播发送服务类
 * 
 * @version 1.0
 * @author zou.sq
 * 
 */
public class PunchService extends Service {

	private static final String TAG = "PunchService";
	private IBinder mBinder = new PunchBinder();

	@Override
	public IBinder onBind(Intent intent) {
		EvtLog.d(TAG, "onBind");
		AlarmUtil.sendUpdateBroadcastRepeat(this);
		return mBinder;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		EvtLog.d(TAG, "onCreate");
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		EvtLog.d(TAG, "onStartCommand");
		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		EvtLog.d(TAG, "onDestroy");
	}

	@Override
	public void onRebind(Intent intent) {
		super.onRebind(intent);
		EvtLog.d(TAG, "onRebind");
	}

	@Override
	public boolean onUnbind(Intent intent) {
		AlarmUtil.cancelUpdateBroadcast(this);
		EvtLog.d(TAG, "onUnbind");
		return true;
	}

	/**
	 * Binder类
	 * 
	 * @version 1.0
	 * @author zou.sq
	 */
	public class PunchBinder extends Binder {

		/**
		 * 获取当前服务
		 * 
		 * @return PunchService 当前服务类
		 */
		public PunchService getService() {
			return PunchService.this;
		}
	}

}
