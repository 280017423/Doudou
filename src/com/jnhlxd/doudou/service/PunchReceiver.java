package com.jnhlxd.doudou.service;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.jnhlxd.doudou.authentication.ActionResult;
import com.jnhlxd.doudou.manager.PunchMgr;
import com.jnhlxd.doudou.model.SignModel;
import com.jnhlxd.doudou.req.PunchReq;
import com.jnhlxd.doudou.util.ConstantSet;
import com.qianjiang.framework.util.EvtLog;
import com.qianjiang.framework.util.NetUtil;

/**
 * 定时发送打卡数据处理类
 * 
 * @version 1.0
 * @author zou.sq
 */
public class PunchReceiver extends BroadcastReceiver {

	private static final String TAG = "PunchReceiver";
	ExecutorService mThreadPool = Executors.newFixedThreadPool(2);

	/**
	 * @Description 接受广播处理方法
	 * @param context
	 *            上下文对象
	 * @param intent
	 *            数据传输intent
	 */
	public void onReceive(final Context context, Intent intent) {
		if (NetUtil.isNetworkAvailable()) {
			EvtLog.d(TAG, "当前网络是可用的");
			final List<SignModel> models = PunchMgr.getLocalPunchInfo();
			if (null == models || models.isEmpty()) {
				EvtLog.d(TAG, "数据库没有记录了");
			} else {
				mThreadPool.execute(new Thread(new Runnable() {

					@Override
					public void run() {
						ActionResult result = PunchReq.submitSignInfo(models);
						if (ActionResult.RESULT_CODE_SUCCESS.equals(result.ResultCode)) {
							EvtLog.d(TAG, "提交学生数据成功");
						} else {
							EvtLog.d(TAG, "提交学生数据失败");
						}
					}
				}));
			}
		} else {
			EvtLog.d(TAG, "当前网络不可用");
		}
		sendBroadCastV(context, ConstantSet.ACTION_DEFAULT_BROAD, "");
	}

	/**
	 * 
	 * @param context
	 *            上下文对象
	 * @param action
	 *            广播类型
	 * @param data
	 *            广播发送的数据
	 */
	public void sendBroadCastV(Context context, String action, String data) {
		Intent intent = new Intent();
		intent.setAction(action);
		intent.putExtra("data", data);
		context.sendBroadcast(intent);
		EvtLog.d(TAG, "发送广播.......");
	}
}