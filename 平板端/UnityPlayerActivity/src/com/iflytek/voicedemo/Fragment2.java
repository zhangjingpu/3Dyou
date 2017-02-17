package com.iflytek.voicedemo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.example.adapter.StudyMessageAdapter;
import com.example.message.StudyMessage;
import com.example.message.StudyMessage.Type;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

 @SuppressLint("NewApi") @TargetApi(Build.VERSION_CODES.HONEYCOMB) public class Fragment2 extends android.app.Fragment{
	
	private Context context ;
	private ListView mMsgs2;
	private StudyMessageAdapter mStudyAdapter;
	private List<StudyMessage> mStudyDatas;
	private EditText mInputMsg;
	private Button mSendMsg;
	Bridge bridge = Bridge.getBridge();
	String string = "";
	private Boolean two = false;
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			// 等待接收子线程数据返回
			if (msg.what == 0x222) {
				StudyMessage froMessage = (StudyMessage) msg.obj;
				mStudyDatas.add(froMessage);
				mStudyAdapter.notifyDataSetChanged();
			} 
		};
	};
	@Override
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment2, container, false);
		mMsgs2 = (ListView) view.findViewById(R.id.id_listView_msgs2);
//		screen = (LinearLayout)view.findViewById(R.id.screen);
		mInputMsg = (EditText)view. findViewById(R.id.id_input_msg);
		mSendMsg = (Button)view. findViewById(R.id.id_send_msg);
		context = this.getActivity().getApplicationContext();
		initStudyDatas();
		initStudyListener();
		return view;
	}
	private void initStudyDatas() {
		// 初始化Adapter
		mStudyDatas = new ArrayList<StudyMessage>();
		mStudyDatas.add(new StudyMessage("主人，你要教我什么？", Type.INSCOMIGN, new Date()));
		mStudyAdapter = new StudyMessageAdapter(context, mStudyDatas);
		mMsgs2.setAdapter(mStudyAdapter);
	}
	private void initStudyListener() {
		mSendMsg.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				two = !two;
				final String toMsg = mInputMsg.getText().toString();
				
				if (TextUtils.isEmpty(toMsg)) {
					Toast.makeText(context, "发送消息不能为空",
							Toast.LENGTH_SHORT).show();
					return;
				}
				bridge.setQuestion(toMsg);
					StudyMessage toMessage = new StudyMessage();
					toMessage.setDate(new Date());
					toMessage.setMsg(toMsg);
					toMessage.setType(Type.OUTSCOMING);
					mStudyDatas.add(toMessage);
					mStudyAdapter.notifyDataSetChanged();
					mInputMsg.setText("");
					new Thread() {
						public void run() {
							if(two){
								string = "主人，我想知道答案，我会努力学会的";
							}
							else{
								string = "主人，我懂了，你还要教我什么？";
							}
							StudyMessage fromMessage = new StudyMessage(string,
									Type.INSCOMIGN, new Date());
							Message m = Message.obtain();
							m.obj = fromMessage;
							m.what = 0x222;
							mHandler.sendMessage(m);
							string = "";
						};
					}.start();
			}
		});
	}

}
