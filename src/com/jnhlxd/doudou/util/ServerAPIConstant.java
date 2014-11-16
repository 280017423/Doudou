package com.jnhlxd.doudou.util;

import com.qianjiang.framework.app.QJApplicationBase;
import com.qianjiang.framework.util.AppUtil;
import com.qianjiang.framework.util.StringUtil;

/**
 * 定义与服务器端的接口交互需要用到的常量
 * 
 * @author xu.xb
 * 
 */
public class ServerAPIConstant {
	// API地址
	public static final String API_ROOT_URL = "api_root_url";
	public static final String KEY_APP_SIGN = "appSign";
	public static final String LOGIN_API = "/user/login";
	public static final String GET_CLASS_NEWS_API = "/article/list4clsteacher";
	public static final String GET_SCHOOL_NEWS_API = "/article/list4sch";
	public static final String GET_MAX_ID = "/status/getMax";
	public static final String GET_DETAIL_API = "/article/detail";
	public static final String GET_MY_SMS = "/sms/teacherGetMySms";
	public static final String SEND_SMS = "/sms/sendMsg";
	public static final String SEND_CLEAR = "/sms/clear";
	public static final String DELETE_CHECK = "/sms/delCheck";
	public static final String GET_SMS_DETAIL = "/sms/teacherGetSmsDetail";
	public static final String CREATE_ARTICLE = "/article/create";
	public static final String DELETE_ARTICLE = "/article/del";
	public static final String GET_CONTACT = "/user/getContacts";
	public static final String CHECK_VERSION = "/status/checkVersion";
	public static final String CHECK_NETWORK = "/status/checkNetwork";
	public static final String GET_EXCHANGE_LIST = "/points/exchangelist";
	public static final String EXCHANGE = "/points/exchange";
	public static final String EXCHANGE_RECORD_LIST = "/points/exchangerecordlist";
	public static final String REVIEW_LIST = "/review/list";
	public static final String REVIEW_DETAIL = "/review/detail";
	public static final String REVIEW_MSG_DO = "/review/msgDo";
	public static final String REVIEW_NEWS_DO = "/review/newsDo";
	public static final String REVIEW_HISTORY = "/review/history";
	// API KEY
	public static final String KEY_USER_NAME = "user_name";
	public static final String KEY_SMS_ID = "sms_id";
	public static final String KEY_PWD = "pwd";
	public static final String KEY_APP = "app";
	public static final String KEY_FROM_ID = "from_id";
	public static final String KEY_ID = "id";
	public static final String KEY_MAX_ID = "max_id";
	public static final String KEY_MIN_ID = "min_id";
	public static final String KEY_TYPE = "type";
	public static final String KEY_TEACHERS = "teachers";
	public static final String KEY_CHILDS = "childs";
	public static final String KEY_CHILD_ID = "child_id";
	public static final String KEY_TEACHER_ID = "teacher_id";
	public static final String KEY_CONTENT = "content";
	public static final String KEY_IMGSRC = "imgsrc";
	public static final String KEY_ARTICLE_ID = "article_id";
	public static final String KEY_USER_INFO = "userInfo";
	public static final String KEY_CLASS_INFO = "classInfo";
	public static final String KEY_SCHOOL_INFO = "schoolInfo";
	public static final String KEY_SEND_SMS = "send_sms";
	public static final String KEY_CLIENT_MAX = "clientMax";
	public static final String KEY_TEACHER_LIST = "teacherList";
	public static final String KEY_PUSH_ALIAS = "pushAlias";
	public static final String KEY_PUSH_TAG = "pushTag";
	public static final String KEY_IMG_SERVER = "imgServer";
	public static final String KEY_SERVER_TIME = "ServerTime";
	public static final String KEY_TITLE = "title";
	public static final String KEY_CLASS_ID = "class_id";
	public static final String KEY_VERSION = "version";
	public static final String PROJECT_SIGN = "2";
	public static final String KEY_CLIENT_TYPE = "clientType";
	public static final String KEY_PROJECT_SIGN = "projectSign";
	public static final String KEY_POINT = "point";
	public static final String KEY_EXCHANGE_LIST = "exchangeList";
	public static final String KEY_EXCHANGEID = "exchangeId";
	public static final String KEY_POINTRULE = "pointRule";
	public static final String KEY_ACTIVERULE = "activeRule";
	public static final String KEY_CHECKSTATUS = "checkStatus";
	public static final String KEY_MOBILE = "mobile";
	public static final String KEY_MSG_LIST = "msgList";
	public static final String KEY_NEWS_LIST = "newsList";
	public static final String KEY_REVIEW_ID = "reviewId";
	public static final String KEY_MSG_ID = "msgId";
	public static final String KEY_REVIEW_CONTENT = "reviewContent";
	public static final String KEY_REVIEW_STATUS = "reviewStatus";
	public static final String KEY_PAGE = "page";
	public static final String KEY_PAGE_SIZE = "pageSize";
	public static final String KEY = "key";

	public static final String[] API_ROOT_URL_ARRAY = new String[] {
			AppUtil.getMetaDataByKey(QJApplicationBase.CONTEXT, API_ROOT_URL),
			"http://xytv2.api.weare-team.com/api",
			"http://54.251.110.27:8092/api" };

	public static final String[] VERSION_URL_ARRAY = new String[] {
			"http://version.api.ikisos.com/api/version/checkVersion",
			"http://version.api.weare-team.com/api/version/checkVersion",
			"http://54.251.110.27:8093/api/version/checkVersion" };

	public static String getApiRootUrl() {

		int index = SharedPreferenceUtil.getIntegerValueByKey(QJApplicationBase.CONTEXT, ConstantSet.KEY_API_URL_FILE,
				ConstantSet.KEY_API_URL_INDEX);
		if (SharedPreferenceUtil.INVALID_CODE == index) {
			index = 0;
			String localUrl = SharedPreferenceUtil.getStringValueByKey(QJApplicationBase.CONTEXT,
					ConstantSet.FILE_QIANJIANG_CONFIG, ConstantSet.SYSTEM_CONFIG_API_ROOT_URL);
			if (!StringUtil.isNullOrEmpty(localUrl)) {
				return localUrl;
			}
		}
		return API_ROOT_URL_ARRAY[index % API_ROOT_URL_ARRAY.length];
	}

	public static String getAppSign() {
		return AppUtil.getMetaDataByKey(QJApplicationBase.CONTEXT, KEY_APP_SIGN);
	}

	/**
	 * 获取接口地址
	 * 
	 * @param interfaceName
	 *            接口名字
	 * @return String
	 * @throws
	 */
	public static String getUrl(String interfaceName) {
		return getApiRootUrl() + interfaceName;
	}

	/**
	 * 获取版本更新接口地址
	 * 
	 * @return 接口地址
	 */
	public static String getVersionCheckUrl() {
		int index = SharedPreferenceUtil.getIntegerValueByKey(QJApplicationBase.CONTEXT, ConstantSet.KEY_API_URL_FILE,
				ConstantSet.KEY_API_URL_INDEX);
		if (SharedPreferenceUtil.INVALID_CODE == index) {
			index = 0;
		}
		return VERSION_URL_ARRAY[index % VERSION_URL_ARRAY.length];
	}
}
