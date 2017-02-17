package com.iflytek.voicedemo;

import android.R.string;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.SynthesizerListener;
import com.iflytek.speech.setting.TtsSettings;
import com.iflytek.speech.util.ApkInstaller;
import com.iflytek.sunflower.FlowerCollector;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.LexiconListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.iflytek.cloud.util.ContactManager;
import com.iflytek.cloud.util.ContactManager.ContactListener;
import com.iflytek.speech.setting.IatSettings;
import com.iflytek.speech.util.ApkInstaller;
import com.iflytek.speech.util.FucUtil;
import com.iflytek.speech.util.JsonParser;
import com.iflytek.sunflower.FlowerCollector;

import java.util.HashMap;
import java.util.LinkedHashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.LexiconListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.iflytek.cloud.util.ContactManager;
import com.iflytek.cloud.util.ContactManager.ContactListener;
import com.iflytek.speech.setting.IatSettings;
import com.iflytek.speech.util.ApkInstaller;
import com.iflytek.speech.util.FucUtil;
import com.iflytek.speech.util.JsonParser;
import com.iflytek.sunflower.FlowerCollector;
import com.iflytek.voicedemo.ChatMessage.Type;

public class MainActivity extends Activity implements OnClickListener {
	private Bridge bridge = Bridge.getBridge();
	private ListView mMsgs;
	private ChatMessageAdapter mAdapter;
	private List<ChatMessage> mDatas;// 数据源
	private EditText mInputMsg;
	private Button mSendMsg;
	private Boolean isFirst = true;
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			// 等待接收子线程数据返回
			if(msg.what==0x111){
				ChatMessage froMessage = (ChatMessage) msg.obj;
				mDatas.add(froMessage);
				mAdapter.notifyDataSetChanged();
			}
			if(msg.what== 0x234){
				String listenerString = msg.obj.toString();
				if(!listenerString.trim().equals("")){
					UpdateListView(listenerString);
				}
			}
			if(msg.what ==0x333){
				new Thread(new ConnectRunnable()).start();
			}
			if(msg.what==0x222){
				ChatMessage froMessage = (ChatMessage) msg.obj;
				mDatas.add(froMessage);
				mAdapter.notifyDataSetChanged();
			}
		};
	};

	private static String TAG = MainActivity.class.getSimpleName();
	// 语音听写对象
	private SpeechRecognizer mIat;
	// 语音听写UI
	private RecognizerDialog mIatDialog;
	// 用HashMap存储听写结果
	private HashMap<String, String> mIatResults = new LinkedHashMap<String, String>();
	private SharedPreferences mSharedPreferences2;
	// 引擎类型
	private String mEngineType = SpeechConstant.TYPE_CLOUD;
	// 语记安装助手类
	ApkInstaller mInstaller;
	// 语音合成对象
	private Toast mToast;

	private SpeechSynthesizer mTts;
	// 默认发音人
	private String voicer = "xiaoyan";
	// 缓冲进度
	private int mPercentForBuffering = 0;
	// 播放进度
	private int mPercentForPlaying = 0;
	// 云端/本地单选按钮
	private SharedPreferences mSharedPreferences1;
	int ret = 0; // 函数调用返回值

	@SuppressLint("ShowToast")
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);

		// 初始化识别无UI识别对象
		// 使用SpeechRecognizer对象，可根据回调消息自定义界面；
		mIat = SpeechRecognizer.createRecognizer(MainActivity.this,
				mInitListener);
		mTts = SpeechSynthesizer.createSynthesizer(MainActivity.this,
				mTtsInitListener);
		// 初始化听写Dialog，如果只使用有UI听写功能，无需创建SpeechRecognizer
		// 使用UI听写功能，请根据sdk文件目录下的notice.txt,放置布局文件和图片资源
		mIatDialog = new RecognizerDialog(MainActivity.this, mInitListener);
		mSharedPreferences1 = getSharedPreferences(TtsSettings.PREFER_NAME,
				MODE_PRIVATE);
		mSharedPreferences2 = getSharedPreferences(IatSettings.PREFER_NAME,
				Activity.MODE_PRIVATE);
		mToast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
		mInstaller = new ApkInstaller(MainActivity.this);
		initView();
		initDatas();
		initListener();
	}

	private void initListener() {
		mSendMsg.setOnClickListener(MainActivity.this);
	}

	private void initView() {

		mMsgs = (ListView) findViewById(R.id.id_listView_msgs);
		mInputMsg = (EditText) findViewById(R.id.id_input_msg);
		mSendMsg = (Button) findViewById(R.id.id_send_msg);
	}

	private void initDatas() {
		// 初始化Adapter
		mDatas = new ArrayList<ChatMessage>();
		mDatas.add(new ChatMessage("你好，我是好人", Type.INCOMIGN, new Date()));
		mAdapter = new ChatMessageAdapter(this, mDatas);
		mMsgs.setAdapter(mAdapter);
	}

	/**
	 * 初始化Layout。
	 */
	/*
	 * **************************************************语音转文本*******************
	 * ***************************************
	 */
	public void UpdateListView(String str){
			mInputMsg.setText(str);
			bridge.setQuestion(str);
			ChatMessage toMessage = new ChatMessage();
			toMessage.setDate(new Date());
			toMessage.setMsg(str);
			toMessage.setType(Type.OUTCOMING);
			mDatas.add(toMessage);
			mAdapter.notifyDataSetChanged();
		//	mInputMsg.setText("");
			new Thread() {
				String answer = "";
				public void run() {
					answer = bridge.getAnswer();
					while(answer==null||answer.trim().equals("")){
						answer = bridge.getAnswer();
					}
					bridge.setAnswer("");
					ChatMessage fromMessage = new ChatMessage(answer,
							Type.INCOMIGN, new Date());
					Message m = Message.obtain();
					m.obj = fromMessage;
					m.what = 0x222;
					mHandler.sendMessage(m);
					answer = "";
				};
			}.start();
	}
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		// 开始听写
		// 如何判断一次听写结束：OnResult isLast=true 或者 onError
		case R.id.id_send_msg:
			Log.e("点击录音", "111111111");
			if(isFirst){
				Message message = new Message();
				message.what = 0x333;
				mHandler.sendEmptyMessage(message.what);
				isFirst = false;
			}
			Log.e("点击录音", "2222222222");
			mInputMsg.setText("");
			mIatResults.clear();
			// 设置参数
			setRecordParam();
			boolean isShowDialog = mSharedPreferences2.getBoolean(
					getString(R.string.pref_key_iat_show), true);
			if (isShowDialog) {
				Log.e("点击录音", "3333333333333");
				// 显示听写对话框
				mIatDialog.setListener(mRecognizerDialogListener);
				mIatDialog.show();
				showTip(getString(R.string.text_begin));
			} else {
				// 不显示听写对话框
				ret = mIat.startListening(mRecognizerListener);
				if (ret != ErrorCode.SUCCESS) {
					showTip("听写失败,错误码：" + ret);
				} else {
					showTip(getString(R.string.text_begin));
				}
			}
			break;
		default:
			break;
		}
	}

	/**
	 * 初始化监听器。
	 */
	private InitListener mInitListener = new InitListener() {

		@Override
		public void onInit(int code) {
			Log.d(TAG, "SpeechRecognizer init() code = " + code);
			if (code != ErrorCode.SUCCESS) {
				showTip("初始化失败，错误码：" + code);
			}
		}
	};
	/**
	 * 听写监听器。
	 */
	private RecognizerListener mRecognizerListener = new RecognizerListener() {

		@Override
		public void onBeginOfSpeech() {
			// 此回调表示：sdk内部录音机已经准备好了，用户可以开始语音输入
			showTip("开始说话");
		}

		@Override
		public void onError(SpeechError error) {
			// Tips：
			// 错误码：10118(您没有说话)，可能是录音机权限被禁，需要提示用户打开应用的录音权限。
			// 如果使用本地功能（语记）需要提示用户开启语记的录音权限。
			showTip(error.getPlainDescription(true));
		}

		@Override
		public void onEndOfSpeech() {
			// 此回调表示：检测到了语音的尾端点，已经进入识别过程，不再接受语音输入
			showTip("结束说话");
		}

		@Override
		public void onResult(RecognizerResult results, boolean isLast) {
			Log.d(TAG, results.getResultString());
			Log.e("print", "2222");
			printResult(results);

			if (isLast) {
				// TODO 最后的结果
			}
		}

		@Override
		public void onVolumeChanged(int volume, byte[] data) {
			showTip("当前正在说话，音量大小：" + volume);
			Log.d(TAG, "返回音频数据：" + data.length);
		}

		@Override
		public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
			// 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
			// 若使用本地能力，会话id为null
			// if (SpeechEvent.EVENT_SESSION_ID == eventType) {
			// String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
			// Log.d(TAG, "session id =" + sid);
			// }
		}
	};

	String finalResult = "";

	private void printResult(RecognizerResult results) {
		String text = JsonParser.parseIatResult(results.getResultString());
		String sn = null;
		// 读取json结果中的sn字段
		try {
			JSONObject resultJson = new JSONObject(results.getResultString());
			sn = resultJson.optString("sn");
		} catch (JSONException e) {
			e.printStackTrace();
		}

		mIatResults.put(sn, text);

		StringBuffer resultBuffer = new StringBuffer();
		for (String key : mIatResults.keySet()) {
			resultBuffer.append(mIatResults.get(key));
		}
		// Log.e("录音后的文本：", resultBuffer.toString());
		finalResult = resultBuffer.toString();
		Log.e("最后一次文本", finalResult);
		
	}

	/**
	 * 听写UI监听器
	 */

	private RecognizerDialogListener mRecognizerDialogListener = new RecognizerDialogListener() {
		public void onResult(RecognizerResult results, boolean isLast) {
			printResult(results);
			if (isLast) {
				// 得到的最终结果
				Log.e("final", finalResult);
				Message message = new Message();
				message.what = 0x234;
				message.obj = finalResult;
				mHandler.sendMessage(message);
			}
		}

		/**
		 * 识别回调错误.
		 */
		public void onError(SpeechError error) {
			showTip(error.getPlainDescription(true));
		}

	};

	private void showTip(final String str) {
		mToast.setText(str);
		mToast.show();
	}

	/**
	 * 参数设置
	 * 
	 * @param param
	 * @return
	 */
	public void setRecordParam() {
		// 清空参数
		mIat.setParameter(SpeechConstant.PARAMS, null);

		// 设置听写引擎
		mIat.setParameter(SpeechConstant.ENGINE_TYPE, mEngineType);
		// 设置返回结果格式
		mIat.setParameter(SpeechConstant.RESULT_TYPE, "json");

		String lag = mSharedPreferences2.getString("iat_language_preference",
				"mandarin");
		if (lag.equals("en_us")) {
			// 设置语言
			mIat.setParameter(SpeechConstant.LANGUAGE, "en_us");
		} else {
			// 设置语言
			mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
			// 设置语言区域
			mIat.setParameter(SpeechConstant.ACCENT, lag);
		}

		// 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
		mIat.setParameter(SpeechConstant.VAD_BOS,
				mSharedPreferences2.getString("iat_vadbos_preference", "4000"));

		// 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
		mIat.setParameter(SpeechConstant.VAD_EOS,
				mSharedPreferences2.getString("iat_vadeos_preference", "1000"));

		// 设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
		mIat.setParameter(SpeechConstant.ASR_PTT,
				mSharedPreferences2.getString("iat_punc_preference", "1"));

		// 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
		// 注：AUDIO_FORMAT参数语记需要更新版本才能生效
		mIat.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
		mIat.setParameter(SpeechConstant.ASR_AUDIO_PATH,
				Environment.getExternalStorageDirectory() + "/msc/iat.wav");
	}

	/*
	 * (non-Javadoc)**************************************************语音转文本******
	 * ****************************************************
	 * 
	 * @see android.app.Activity#onDestroy()
	 */

	/**
	 * 初始化监听。***********************************文本转语音***************************
	 * *********************************
	 */

	public void startSpeak() {
		setSpeechParam();
		int code = mTts.startSpeaking("你好你好你好", mTtsListener);
		if (code != ErrorCode.SUCCESS) {
			if (code == ErrorCode.ERROR_COMPONENT_NOT_INSTALLED) {
				// 未安装则跳转到提示安装页面
				mInstaller.install();
			} else {
				showTip("语音合成失败,错误码: " + code);
			}
		}
	}

	private InitListener mTtsInitListener = new InitListener() {
		@Override
		public void onInit(int code) {
			Log.d(TAG, "InitListener init() code = " + code);
			if (code != ErrorCode.SUCCESS) {
				showTip("初始化失败,错误码：" + code);
			} else {
				// 初始化成功，之后可以调用startSpeaking方法
				// 注：有的开发者在onCreate方法中创建完合成对象之后马上就调用startSpeaking进行合成，
				// 正确的做法是将onCreate中的startSpeaking调用移至这里
			}
		}
	};
	/**
	 * 合成回调监听。
	 */
	private SynthesizerListener mTtsListener = new SynthesizerListener() {

		@Override
		public void onSpeakBegin() {
			showTip("开始播放");
		}

		@Override
		public void onSpeakPaused() {
			showTip("暂停播放");
		}

		@Override
		public void onSpeakResumed() {
			showTip("继续播放");
		}

		@Override
		public void onBufferProgress(int percent, int beginPos, int endPos,
				String info) {
			// 合成进度
			mPercentForBuffering = percent;
			showTip(String.format(getString(R.string.tts_toast_format),
					mPercentForBuffering, mPercentForPlaying));
		}

		@Override
		public void onSpeakProgress(int percent, int beginPos, int endPos) {
			// 播放进度
			mPercentForPlaying = percent;
			showTip(String.format(getString(R.string.tts_toast_format),
					mPercentForBuffering, mPercentForPlaying));
		}

		@Override
		public void onCompleted(SpeechError error) {
			if (error == null) {
				showTip("播放完成");
			} else if (error != null) {
				showTip(error.getPlainDescription(true));
			}
		}

		@Override
		public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {

		}
	};

	/**
	 * 参数设置
	 * 
	 * @param param
	 *            **************************************文本转语音*******************
	 *            *****************************************
	 * @return
	 */
	private void setSpeechParam() {
		// 清空参数
		mTts.setParameter(SpeechConstant.PARAMS, null);
		// 根据合成引擎设置相应参数
		if (mEngineType.equals(SpeechConstant.TYPE_CLOUD)) {
			mTts.setParameter(SpeechConstant.ENGINE_TYPE,
					SpeechConstant.TYPE_CLOUD);
			// 设置在线合成发音人
			mTts.setParameter(SpeechConstant.VOICE_NAME, voicer);
		} else {
			mTts.setParameter(SpeechConstant.ENGINE_TYPE,
					SpeechConstant.TYPE_LOCAL);
			// 设置本地合成发音人 voicer为空，默认通过语记界面指定发音人。
			mTts.setParameter(SpeechConstant.VOICE_NAME, "");
		}
		if (mSharedPreferences1.getString("speed_preference", "50") == null) {
			Log.e("null", "null");
			mTts.setParameter(SpeechConstant.SPEED,
					mSharedPreferences1.getString("speed_preference", "50"));
		}

		// 设置合成音调
		mTts.setParameter(SpeechConstant.PITCH,
				mSharedPreferences1.getString("pitch_preference", "50"));
		// 设置合成音量
		mTts.setParameter(SpeechConstant.VOLUME,
				mSharedPreferences1.getString("volume_preference", "50"));
		// 设置播放器音频流类型
		mTts.setParameter(SpeechConstant.STREAM_TYPE,
				mSharedPreferences1.getString("stream_preference", "3"));

		// 设置播放合成音频打断音乐播放，默认为true
		mTts.setParameter(SpeechConstant.KEY_REQUEST_FOCUS, "true");

		// 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
		// 注：AUDIO_FORMAT参数语记需要更新版本才能生效
		mTts.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
		mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH,
				Environment.getExternalStorageDirectory() + "/msc/tts.wav");
	}

	/*
	 * ***************************************************文本转语音******************
	 * *****************************************
	 */

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// 退出时释放连接
		mIat.cancel();
		mIat.destroy();
		mTts.destroy();
	}

	@Override
	protected void onResume() {
		// 移动数据统计分析
		FlowerCollector.onResume(MainActivity.this);
		FlowerCollector.onPageStart(TAG);
		super.onResume();
	}

	@Override
	protected void onPause() {
		// 移动数据统计分析
		FlowerCollector.onPageEnd(TAG);
		FlowerCollector.onPause(MainActivity.this);
		super.onPause();
	}

}
