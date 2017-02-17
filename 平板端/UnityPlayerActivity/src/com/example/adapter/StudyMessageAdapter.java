package com.example.adapter;
import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.List;

import com.example.message.StudyMessage;
import com.example.message.StudyMessage.Type;
import com.iflytek.voicedemo.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class StudyMessageAdapter extends BaseAdapter{
	private LayoutInflater mInflater;
	private List<StudyMessage>mDatas;
	public StudyMessageAdapter(Context context,List<StudyMessage>mDatas){
		mInflater = LayoutInflater.from(context);
		this.mDatas = mDatas;
	}
	@Override
	public int getCount() {
		//消息的个数
		return mDatas.size();
	}

	@Override
	public Object getItem(int position) {
		return mDatas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	@Override
	public int getItemViewType(int position) {
		StudyMessage studyMessage = mDatas.get(position);
			if (studyMessage.getType()==Type.INSCOMIGN) {
				return 0;
			}
			return 1;
	}
	@Override
	public int getViewTypeCount() {
		return 2;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		StudyMessage studyMessage = mDatas.get(position);
		ViewHolder viewHolder = null;
		//通过itemType设置不同的布局
		if (convertView==null) {
			if (getItemViewType(position)==0) {
				//如果接收到消息的类型为0，为左边的布局
				convertView = mInflater.inflate(R.layout.item_from_msg, parent,false);
				viewHolder = new ViewHolder();
				viewHolder.mDaTe = (TextView) convertView.findViewById(R.id.id_from_msg_date);
				viewHolder.mMsg = (TextView) convertView.findViewById(R.id.id_from_msg_info);
			}else {
				//如果接收到消息的类型为1，为右边的布局
				convertView = mInflater.inflate(R.layout.item_to_msg, parent,false);
				viewHolder = new ViewHolder();
				viewHolder.mDaTe = (TextView) convertView.findViewById(R.id.id_to_msg_date);
				viewHolder.mMsg= (TextView) convertView.findViewById(R.id.id_to_msg_info);
			}
			//存储一下viewHolder
			convertView.setTag(viewHolder);
		}else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		//设置数据
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		viewHolder.mDaTe.setText(df.format(studyMessage.getDate()));
		viewHolder.mMsg.setText(studyMessage.getMsg());
		return convertView;
	}
	//通过这个模式来提升效率
	private final class ViewHolder{
		TextView mDaTe;
		TextView mMsg;
	}
	
}
