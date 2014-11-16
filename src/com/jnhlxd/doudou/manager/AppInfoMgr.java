package com.jnhlxd.doudou.manager;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;

import com.jnhlxd.doudou.listener.NewVersionListener;
import com.jnhlxd.doudou.util.ServerAPIConstant;
import com.qianjiang.framework.app.JsonResult;
import com.qianjiang.framework.model.VersionInfo;
import com.qianjiang.framework.util.HttpClientUtil;
import com.qianjiang.framework.util.MessageException;
import com.qianjiang.framework.util.NetworkException;
import com.qianjiang.framework.util.PackageUtil;

/**
 * 应用信息类
 * 
 * @author zou.sq
 * @version 1.1.0 2013-04-02 xu.xb 修复BUG：点击版本更新提示：网络链接失败，请检查的你的网络设置 #49773<br>
 */
public class AppInfoMgr {

	/**
	 * 检测新版本
	 * 
	 * @param listener
	 *            新版本检查接口
	 */
	public static void checkVersion(NewVersionListener listener) {
		if (null == listener) {
			return;
		}
		try {
			String apiUrl = ServerAPIConstant.getVersionCheckUrl();
			List<NameValuePair> postParams = new ArrayList<NameValuePair>();
			postParams.add(new BasicNameValuePair(ServerAPIConstant.KEY_VERSION, PackageUtil.getVersionName() + ""));
			postParams.add(new BasicNameValuePair(ServerAPIConstant.KEY_APP, ServerAPIConstant.getAppSign()));
			postParams.add(new BasicNameValuePair(ServerAPIConstant.KEY_PROJECT_SIGN, ServerAPIConstant.PROJECT_SIGN)); // 2表示打卡教师端
			// 1 表示android
			postParams.add(new BasicNameValuePair(ServerAPIConstant.KEY_CLIENT_TYPE, "1"));
			JsonResult jsonResult;

			jsonResult = HttpClientUtil.get(apiUrl, postParams);
			if (jsonResult != null && jsonResult.isOK()) {
				VersionInfo version = jsonResult.getData(VersionInfo.class);
				listener.onUpdateReturned(version);
				return;
			}
		} catch (NetworkException e) {
			e.printStackTrace();
		} catch (MessageException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return;
	}
}
