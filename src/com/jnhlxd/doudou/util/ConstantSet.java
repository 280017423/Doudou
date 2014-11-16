package com.jnhlxd.doudou.util;

/**
 * 保存常量的类
 * 
 * @author zou.sq
 */
public class ConstantSet {
	public static final String STUDENT_PREFIX_FLAG = "s_";
	public static final String TEACHER_PREFIX_FLAG = "t_";
	public static final int DELAY_TIME = 50;
	public static final int MILLI_SECONDS = 1000;
	public static final int SECONDS_TO_MINUTES = 60;
	public static final String APP_SIGN = "XYT";
	public static final String TIME_PATTERN = "yyyy-MM-dd HH:mm";
	public static final String MY_MSG_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
	public static final String MIN_DATE = "1970-01-01 00:00:00";
	// sharepereference文件KEY
	public static String USER_INFO_SHAREDPREFERENCE_NAME = "USER_INFO_MODEL";
	public static String SCHOOL_INFO_SHAREDPREFERENCE_NAME = "SCHOOL_INFO_MODEL";
	public static String SYSTEM_INFO_SHAREDPREFERENCE_NAME = "SYSTEM_INFO";
	public static String KEY_SYSTEM_INFO_MY_MSG_LAST_SERVER_TIME = "KEY_SYSTEM_INFO_MY_MSG_LAST_SERVER_TIME";
	public static String KEY_MY_MSG_MAX_ID = "KEY_MY_MSG_MAX_ID";
	// 其他变量
	public static final int INFO_NUM_IN_ONE_PAGE = 20;
	public static final int HISTORY_MSG_NUM_IN_ONE_PAGE = 10;

	public static final int KEY_INTENT_MSG_TYPE_MY_MSG = 1; // 默认我的消息界面
	public static final int KEY_INTENT_MSG_TYPE_CLASS_NEWS = 2; // 默认班级新闻
	public static final int KEY_INTENT_MSG_TYPE_SCHOLL_NEWS = 3; // 默认学校新闻
	public static final int KEY_INTENT_MSG_TYPE_NOT_REVIEW = 4; // 审核等待

	public static final int KEY_INTENT_MY_MSG_FLAG_DELETE = 1;
	public static final int KEY_INTENT_MY_MSG_FLAG_UPDATE = 2;
	public static final String KEY_PUSH_MSG_MODEL = "KEY_PUSH_MSG_MODEL";
	public static final String KEY_MSG_TYPE = "MsgType";
	public static final String KEY_MSG_CONTENT = "MsgContent";
	public static final String KEY_MSG_ID = "MsgId";
	public static final String KEY_CLASS_ID = "ClassId";
	public static final String KEY_DATA = "Data";
	public static final String KEY_DATA_MSG_DETAIL_MODEL = "MsgDetailModel";
	public static final String KEY_CHAT_USERNAME = "KEY_CHAT_USERNAME";
	public static final String KEY_SMS_ID = "KEY_SMS_ID";
	public static final String KEY_TYPE = "KEY_TYPE";
	public static final String KEY_INTENT_IS_JUMP_FROM_NEW_SEND = "KEY_INTENT_IS_JUMP_FROM_NEW_SEND";
	public static final String KEY_IS_SENDING_SCHOLL_NEWS = "KEY_IS_SENDING_SCHOLL_NEWS";
	public static final String KEY_INTENT_FROM_ID = "KEY_INTENT_FROM_ID";
	public static final String KEY_INTENT_MSG_DETAIL_MODEL = "KEY_INTENT_MSG_DETAIL_MODEL";
	public static final String KEY_INTENT_MSG_TYPE = "KEY_INTENT_MSG_TYPE";
	public static final String KEY_INTENT_MY_MSG_FLAG = "KEY_INTENT_MY_MSG_FLAG";
	public static final String KEY_INTENT_SEND_TO_LIST = "KEY_INTENT_SEND_TO_LIST";
	public static final String INTENT_ACTION_PUSH_DATA_MY_MSG = "INTENT_ACTION_PUSH_DATA_MY_MSG";
	public static final String INTENT_ACTION_PUSH_DATA_MY_REVIEW = "INTENT_ACTION_PUSH_DATA_MY_REVIEW";
	public static final String INTENT_ACTION_UPDATE_DATA_MY_MSG = "INTENT_ACTION_JUMP_TO_MY_MSG";
	public static final String INTENT_ACTION_UPDATE_DATA_MSG_DETAIL = "INTENT_ACTION_JUMP_TO_MSG_DETAIL";
	public static final String KEY_IS_DEVELOPING = "is_developing";
	public static final String KEY_API_URL_FILE = "KEY_API_URL_FILE";
	public static final String KEY_API_URL_INDEX = "KEY_API_URL_INDEX";

	public static final String EXTRA_IMAGE_MAX_NUM = "max image number";
	public static final String EXTRA_IMAGE_ITEM_MODEL = "EXTRA_IMAGE_ITEM_MODEL";
	public static final String EXTRA_IMAGE_CHOOSED_NUM = "choosed image number";

	public static final int ADD_IMAGE_MAX_ITEM = 8;
	// 缩略图的宽高值
	public static final int THUMBNAIL_WIDTH = 100;

	public static final String EXTRA_IMAGE_LIST = "imagelist";
	public static final String EXTRA_BUCKET_NAME = "bucket name";
	public static final String EXTRA_IMAGE_CHOOSE_LIST = "choosed image list";
	public static final String EXTRA_IMAGE_DRAFT_ITEM = "EXTRA_IMAGE_DRAFT_ITEM";
	public static final String EXTRA_NEWS_ID = "NEWS_ID";
	public static final String EXTRA_NEWS_TYPE = "NEWS_TYPE";
	public static final String EXTRA_NEWS_CAN_EDIT = "NEWS_CAN_EDIT";
	public static final String EXTRA_CLASS_NEWS_MODEL = "EXTRA_CLASS_NEWS_MODEL";
	public static final String EXTRA_CLASS_REVIEW_MODEL = "EXTRA_CLASS_REVIEW_MODEL";
	public static final String EXTRA_HISTORY_MSG_MODEL = "EXTRA_HISTORY_MSG_MODEL";
	public static final String EXTRA_EXCHANGE_MODEL = "EXTRA_EXCHANGE_MODEL";
	public static final String ACTION_FINISH_CHECK_NETWORK_ACTIVITY = "FINISH_CHECK_NETWORK_ACTIVITY";

	public static final String FILE_XYT_CONFIG = "FILE_XYT_CONFIG";
	public static final String KEY_IS_REMEMBER_ME = "KEY_IS_REMEMBER_ME";
	// 新手引导
	public static final String KEY_NEWER_GUIDING_FILE = "KEY_NEWER_GUIDING_FILE";
	public static final String KEY_NEWER_GUIDING_FINISH = "KEY_NEWER_GUIDING_FINISH";

	public static final String FILE_QIANJIANG_CONFIG = "com.qianjiang.system.config";
	public static final String SYSTEM_CONFIG_DEBUG_LOG_ENABLE = "system_config_debug_log_enable";
	public static final String SYSTEM_CONFIG_API_ROOT_URL = "SYSTEM_CONFIG_API_ROOT_URL";
	public static final String KEY_REVIEW_LEFT_COUNT = "KEY_REVIEW_LEFT_COUNT";
}
