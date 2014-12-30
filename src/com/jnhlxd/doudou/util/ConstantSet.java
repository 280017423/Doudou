package com.jnhlxd.doudou.util;

/**
 * 保存常量的类
 * 
 * @author zou.sq
 */
public class ConstantSet {
	public static final int MILLI_SECONDS = 1000;
	public static final int INFO_NUM_IN_ONE_PAGE = 20;

	public static final long INTERVAL_TIME = 10 * MILLI_SECONDS;

	public static final String ACTION_DEFAULT_BROAD = "ACTION_DEFAULT_BROAD";
	public static final String APP_SIGN = "Doudou";
	// 应用配置信息（退出登录时不需要清除的）
	public static final String KEY_APPLICATION_CONFIG_FILE = "KEY_APPLICATION_GUIDING_FILE";
	public static final String KEY_NEWER_GUIDING_FINISH = "KEY_NEWER_GUIDING_FINISH";
	public static final String KEY_IS_REMEMBER_ME = "KEY_IS_REMEMBER_ME";
	public static final String KEY_STUDENT_MODEL = "KEY_STUDENT_MODEL";
	// 用户配置信息（退出登录时要清除的）
	public static final String KEY_FILE_DOUDOU_CONFIG_FILE = "FILE_DOUDOU_CONFIG";
}
