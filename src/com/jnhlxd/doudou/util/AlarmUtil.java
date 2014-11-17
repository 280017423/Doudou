package com.jnhlxd.doudou.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

import com.jnhlxd.doudou.service.PunchReceiver;

/**
 * 
 * @version 1.0
 * @author zou.sq
 * 
 */
public class AlarmUtil {

	private static AlarmManager getAlarmManager(Context ctx) {
		return (AlarmManager) ctx.getSystemService(Context.ALARM_SERVICE);
	}

	/**
	 * 设置定时发送的闹钟
	 * 
	 * @param ctx
	 *            上下文对象
	 */
	public static void sendUpdateBroadcastRepeat(Context ctx) {
		Intent intent = new Intent(ctx, PunchReceiver.class);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(ctx, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

		// 立即开始发送闹钟，以后就是每隔10秒发送闹钟
		long firsAlarmTime = SystemClock.elapsedRealtime();
		AlarmManager am = getAlarmManager(ctx); // 60秒一个周期，不停的发送广播
		am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, firsAlarmTime, ConstantSet.INTERVAL_TIME, pendingIntent);
	}

	/**
	 * 取消上传数据广播
	 * 
	 * @param ctx
	 *            上下文对象
	 */
	public static void cancelUpdateBroadcast(Context ctx) {
		AlarmManager am = getAlarmManager(ctx);
		Intent intent = new Intent(ctx, PunchReceiver.class);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(ctx, 0, intent, 0);
		am.cancel(pendingIntent);
	}

}
