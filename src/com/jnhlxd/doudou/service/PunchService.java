package com.jnhlxd.doudou.service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.jnhlxd.doudou.db.PunchDao;
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
		executeEightAtNightPerDay();
		AlarmUtil.sendUpdateBroadcastRepeat(this);
		return mBinder;
	}

	/**
	 * 每天早上6点执行一次清除先前一天的数据
	 */
	private static void executeEightAtNightPerDay() {
		ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
		long oneDay = 24 * 60 * 60 * 1000;
		long initDelay = getTimeMillis("06:00:00") - System.currentTimeMillis();
		initDelay = initDelay > 0 ? initDelay : oneDay + initDelay;
		executor.scheduleAtFixedRate(new Runnable() {

			@Override
			public void run() {
				PunchDao.deleteHistoryData();
			}
		}, initDelay, oneDay, TimeUnit.MILLISECONDS);
	}

	/**
	 * 获取指定时间对应的毫秒数
	 * 
	 * @param time
	 *            "HH:mm:ss"
	 * @return
	 */
	private static long getTimeMillis(String time) {
		try {
			DateFormat dateFormat = new SimpleDateFormat("yy-MM-dd HH:mm:ss", Locale.getDefault());
			DateFormat dayFormat = new SimpleDateFormat("yy-MM-dd", Locale.getDefault());
			Date curDate = dateFormat.parse(dayFormat.format(new Date()) + " " + time);
			return curDate.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;
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
