package com.jnhlxd.doudou.req;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.jnhlxd.doudou.authentication.ActionResult;
import com.jnhlxd.doudou.db.PunchDao;
import com.jnhlxd.doudou.model.SignModel;
import com.jnhlxd.doudou.util.ServerAPIConstant;
import com.pdw.gson.Gson;
import com.qianjiang.framework.app.JsonResult;
import com.qianjiang.framework.util.EvtLog;
import com.qianjiang.framework.util.HttpClientUtil;
import com.qianjiang.framework.util.PackageUtil;

/**
 * 
 * Description the class 签到数据处理类
 * 
 * @version 1.0
 * @author zou.sq
 * 
 */
public class PunchReq {

	private static final String TAG = "PunchReq";

	private PunchReq() {
	}

	/**
	 * 
	 * @Description 提交考勤数据
	 * @param models
	 *            考勤数据
	 * @return ActionResult 考勤数据处理结果
	 */
	public static ActionResult submitSignInfo(List<SignModel> models) {
		EvtLog.d(TAG, "提交打卡数据");
		ActionResult result = new ActionResult();
		try {

			Gson gson = new Gson();
			String jsonString = gson.toJson(models);
			String url = ServerAPIConstant.getUrl(ServerAPIConstant.API_SUBMIT_SIGN_INFO);
			List<NameValuePair> postParams = new ArrayList<NameValuePair>();
			postParams.add(new BasicNameValuePair(ServerAPIConstant.KEY_SIGN_DATA, jsonString));
			postParams.add(new BasicNameValuePair(ServerAPIConstant.KEY_DEVICE_ID, PackageUtil.getDeviceId()));
			postParams.add(new BasicNameValuePair(ServerAPIConstant.KEY_APP, ServerAPIConstant.getAppSign()));
			JsonResult jsonResult = HttpClientUtil.post(url, null, postParams);
			if (jsonResult != null) {
				if (!jsonResult.isOK()) {
					result.ResultObject = jsonResult.Msg;
				} else {
					// PunchDao.deleteHistoryData(models);
					PunchDao.updateHistoryData(models);// 现在的逻辑是不删除了，直接更新状态
				}
				result.ResultCode = jsonResult.Code;
			} else {
				result.ResultCode = ActionResult.RESULT_CODE_NET_ERROR;
			}
		} catch (Exception e) {
			result.ResultCode = ActionResult.RESULT_CODE_NET_ERROR;
			EvtLog.w(TAG, e);
		}
		return result;
	}
}
