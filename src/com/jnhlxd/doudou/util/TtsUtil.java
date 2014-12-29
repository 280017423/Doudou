package com.jnhlxd.doudou.util;

import java.util.List;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.os.RemoteException;
import android.widget.Toast;

import com.iflytek.speech.ISpeechModule;
import com.iflytek.speech.InitListener;
import com.iflytek.speech.SpeechConstant;
import com.iflytek.speech.SpeechSynthesizer;
import com.iflytek.speech.SpeechUtility;
import com.iflytek.speech.SynthesizerListener;
import com.qianjiang.framework.util.EvtLog;
import com.qianjiang.framework.util.StringUtil;

/**
 * 语音工具类
 * 
 * @author zou.sq
 **/
public class TtsUtil {

	private static String TAG;
	private SpeechSynthesizer mSpeechSynthesizer;

	/**
	 * 构造函数
	 * 
	 * @param context
	 *            上下文对象
	 */
	public TtsUtil(Context context) {
		init(context);
	}

	/**
	 * 获取语音合成实例
	 * 
	 * @param context
	 *            上下文对象
	 */
	private void init(Context context) {
		// 设置你申请的应用appid
		SpeechUtility.getUtility(context).setAppid("53cc8a9c");
		mSpeechSynthesizer = new SpeechSynthesizer(context, new InitListener() {

			@Override
			public void onInit(ISpeechModule arg0, int code) {
				EvtLog.d(TAG, "InitListener init() code = " + code);
			}
		});
		setTtsParam();
	}

	private void setTtsParam() {
		mSpeechSynthesizer.setParameter(SpeechConstant.ENGINE_TYPE, "local");
		mSpeechSynthesizer.setParameter(SpeechSynthesizer.VOICE_NAME, "xiaoyan");
		mSpeechSynthesizer.setParameter(SpeechSynthesizer.SPEED, "50");
		mSpeechSynthesizer.setParameter(SpeechSynthesizer.PITCH, "50");
		mSpeechSynthesizer.setParameter(SpeechSynthesizer.VOLUME, "50");
	}

	/**
	 * 播报声音
	 * 
	 * @param speakInfo
	 *            声音内容
	 */
	public void startSpeak(String speakInfo) {
		if (!StringUtil.isNullOrEmpty(speakInfo) && null != mSpeechSynthesizer) {
			mSpeechSynthesizer.startSpeaking(speakInfo, new SynthesizerListener.Stub() {
				@Override
				public void onBufferProgress(int progress) throws RemoteException {
					EvtLog.d(TAG, "onBufferProgress :" + progress);
				}

				@Override
				public void onCompleted(int code) throws RemoteException {
					EvtLog.d(TAG, "onCompleted code =" + code);
				}

				@Override
				public void onSpeakBegin() throws RemoteException {
					EvtLog.d(TAG, "onSpeakBegin");
				}

				@Override
				public void onSpeakPaused() throws RemoteException {
					EvtLog.d(TAG, "onSpeakPaused.");
				}

				@Override
				public void onSpeakProgress(int progress) throws RemoteException {
					EvtLog.d(TAG, "onSpeakProgress :" + progress);
				}

				@Override
				public void onSpeakResumed() throws RemoteException {
					EvtLog.d(TAG, "onSpeakResumed.");
				}
			});
		}

	}

	/**
	 * @Description 判断手机中是否安装了讯飞语音+
	 * @param context
	 *            上下文对象
	 * @return boolean true表示已经安装
	 */
	public static boolean isSpeechServiceInstalled(Context context) {
		String packageName = "com.iflytek.speechcloud";
		List<PackageInfo> packages = context.getPackageManager().getInstalledPackages(0);
		for (int i = 0; i < packages.size(); i++) {
			PackageInfo packageInfo = packages.get(i);
			if (packageInfo.packageName.equals(packageName)) {
				return true;
			} else {
				continue;
			}
		}
		return false;
	}

	/**
	 * 
	 * @Description 把服务组件apk安装包放在assets中，然后copy到SDcard中进行安装。
	 * @param context
	 *            上下文对象
	 * @param assetsApk
	 *            资源文件路径
	 * @return boolean true表示安装成功
	 */
	public static boolean processInstall(Context context, String assetsApk) {
		// 本地安装方式
		if (!ApkInstaller.installFromAssets(context, assetsApk)) {
			Toast.makeText(context, "安装失败", Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}

}
