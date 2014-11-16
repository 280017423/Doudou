package com.jnhlxd.doudou.adapter;

import java.util.List;

import android.app.Activity;
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
		StudentModel item = mDataList.get(position);
		if (null != item) {
			holder.mTvName.setText(item.getName());
		}
		return convertView;
	}

	class Holder {
		private TextView mTvName;
	}
}
