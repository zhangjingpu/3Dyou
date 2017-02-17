package com.iflytek.voicedemo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.example.adapter.ChatMessageAdapter;
import com.example.adapter.StudyMessageAdapter;
import com.example.message.ChatMessage;
import com.example.message.StudyMessage;
import com.example.message.ChatMessage.Type;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

@SuppressLint("NewApi") public class Fragment1 extends Fragment{
	private Context context ;
	private ListView mMsgs1;
	private ChatMessageAdapter mChatAdapter;
	private List<ChatMessage> mChatDatas;
	private EditText mInputMsg;
	private Button mSendMsg;
	Bridge bridge = Bridge.getBridge();
	String string = "";
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			// 等待接收子线程数据返回
			if (msg.what == 0x222) {
				ChatMessage froMessage = (ChatMessage) msg.obj;
				mChatDatas.add(froMessage);
				mChatAdapter.notifyDataSetChanged();
			} 
		};
	};
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		context = this.getActivity().getApplicationContext();
		LayoutInflater mInflater = LayoutInflater.from(context);
		View tab02 = mInflater.inflate(R.layout.tab02, null);
		View view = inflater.inflate(R.layout.fragment1, container, false);
		
		mMsgs1 = (ListView) view.findViewById(R.id.id_listView_msgs1);
		mInputMsg = (EditText)view. findViewById(R.id.id_input_msg);
		mSendMsg = (Button)view. findViewById(R.id.id_send_msg);
		
		
		initChatDatas();
		initChatListener();
		return view;
	}
	private void initChatDatas() {
		// 初始化Adapter
		mChatDatas = new ArrayList<ChatMessage>();
		mChatDatas.add(new ChatMessage("你好，我是好人", Type.INCOMIGN, new Date()));
		mChatAdapter = new ChatMessageAdapter(context, mChatDatas);
		mMsgs1.setAdapter(mChatAdapter);
	}
	private void initChatListener() {
		
		mSendMsg.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				final String toMsg = mInputMsg.getText().toString();
				
				if (TextUtils.isEmpty(toMsg)) {
					Toast.makeText(context, "发送消息不能为空",
							Toast.LENGTH_SHORT).show();
					return;
				}
				bridge.setQuestion(toMsg);
					ChatMessage toMessage = new ChatMessage();
					toMessage.setDate(new Date());
					toMessage.setMsg(toMsg);
					toMessage.setType(Type.OUTCOMING);
					mChatDatas.add(toMessage);
					mChatAdapter.notifyDataSetChanged();
					mInputMsg.setText("");
					new Thread() {
						public void run() {
							string= bridge.getAnswer();
							while(string==null||string.equals("")){
								string = bridge.getAnswer();
							}
							bridge.setAnswer("");
							ChatMessage fromMessage = new ChatMessage(string,
									Type.INCOMIGN, new Date());
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
