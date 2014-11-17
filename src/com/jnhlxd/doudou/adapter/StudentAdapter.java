package com.jnhlxd.doudou.adapter;

import java.util.List;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jnhlxd.doudou.R;
import com.jnhlxd.doudou.model.StudentModel;

/**
 * 
 * Description the class 学生列表适配器
 * 
 * @version 1.0
 * @author zou.sq
 * 
 */
public class StudentAdapter extends BaseAdapter {

	private Activity mActivity;
	private List<StudentModel> mDataList;

	/**
	 * 构造函数
	 * 
	 * @param act
	 *            上下文对象
	 * @param list
	 *            学生数据
	 * 
	 */
	public StudentAdapter(Activity act, List<StudentModel> list) {
		mActivity = act;
		mDataList = list;
	}

	@Override
	public int getCount() {
		if (mDataList != null) {
			return mDataList.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		return mDataList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder holder;
		if (convertView == null) {
			holder = new Holder();
			convertView = View.inflate(mActivity, R.layout.view_student_item, null);
			holder.mTvName = (TextView) convertView.findViewById(R.id.tv_student_name);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		StudentModel model = mDataList.get(position);
		if (null != model) {
			holder.mTvName.setText(model.getName());
			int status = model.getSignModel();
			switch (status) {
				case StudentModel.SIGN_TYPE_NOT_SIGN:
					holder.mTvName.setBackgroundResource(R.drawable.student_not_sign_selector);
					holder.mTvName.setTextColor(Color.BLACK);
					break;
				case StudentModel.SIGN_TYPE_SIGNING:
					holder.mTvName.setBackgroundResource(R.drawable.student_signing_selector);
					holder.mTvName.setTextColor(Color.WHITE);
					break;
				case StudentModel.SIGN_TYPE_SIGNED:
					holder.mTvName.setBackgroundResource(R.drawable.student_signed_selector);
					holder.mTvName.setTextColor(Color.BLACK);
					break;
				default:
					break;
			}
		}
		return convertView;
	}

	class Holder {
		private TextView mTvName;
	}
}
