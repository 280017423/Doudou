package com.jnhlxd.doudou.app;

import android.content.pm.PackageManager.NameNotFoundException;

import com.jnhlxd.doudou.util.ConstantSet;
import com.jnhlxd.doudou.util.DBUtil;
import com.qianjiang.framework.app.QJApplicationBase;
import com.qianjiang.framework.imageloader.cache.disc.naming.Md5FileNameGenerator;
import com.qianjiang.framework.imageloader.core.ImageLoader;
import com.qianjiang.framework.imageloader.core.ImageLoaderConfiguration;
import com.qianjiang.framework.util.PackageUtil;
import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.bugly.crashreport.CrashReport.UserStrategy;

/**
 * 全局应用程序
 * 
 * @author zou.sq
 */
public class DoudouApplication extends QJApplicationBase {
	public static final String TAG = "QianJiangApplication";
	public static final int MEMORY_CACHE_SIZE = 1500000;
	public static final int THREAD_POOL_SIZE = 3;

	@Override
	// 建议在您app的退出之前调用mapadpi的destroy()函数，避免重复初始化带来的时间消耗
	public void onTerminate() {
		super.onTerminate();
	}

	@Override
	public void onCreate() {
		super.onCreate();
		CONTEXT = this;
		initCrashReport();
		initImageLoader();
		// 打开数据库
		new Thread(new Runnable() {
			@Override
			public void run() {
				DBUtil.getDataManager().firstOpen();

			}
		}).start();
	}

	private void initCrashReport() {
		UserStrategy strategy = new UserStrategy(getApplicationContext());
		strategy.setAppChannel("");
		try {
			strategy.setAppVersion(PackageUtil.getVersionName());
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		strategy.setAppReportDelay(5000); // 设置SDK处理延时，毫秒
		CrashReport.initCrashReport(getApplicationContext(), "900001484", true, strategy);
	}

	@Override
	protected void setAppSign() {
		APP_SIGN = ConstantSet.APP_SIGN;
	}

	@Override
	protected void setClientType() {
	}

	/**
	 * This configuration tuning is custom. You can tune every option, you may
	 * tune some of them, or you can create default configuration by
	 * ImageLoaderConfiguration.createDefault(this); method.
	 * 
	 * @Name initImageLoader
	 * @Description 初始化图片加载器
	 * @return void
	 * @Author Administrator
	 * @Date 2014-3-21 上午11:42:06
	 * 
	 */
	private void initImageLoader() {
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
				.threadPoolSize(THREAD_POOL_SIZE).threadPriority(Thread.NORM_PRIORITY - 2)
				.memoryCacheSize(MEMORY_CACHE_SIZE).denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator()).enableLogging().build();
		ImageLoader.getInstance().init(config);
	}

}
