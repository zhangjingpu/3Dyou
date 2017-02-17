package com.iflytek.voicedemo;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Intent;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.Toast;

public  class PlanActivity extends Activity {
	private static final String TAG = "AlarmActivity";
	AlarmManager alarmManager;
	Calendar calendar1 = Calendar.getInstance(Locale.CHINESE);
	Calendar calendar2 = Calendar.getInstance(Locale.CHINESE);
	EditText setTime;
	EditText setRing;
	EditText setFinishTime;
	EditText primaryContent;
	static String string = "";
	ImageButton back;
	ImageButton setOver;
	Intent intentReceiveIntent;
	Uri ringUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.plan);
		setTime = (EditText) findViewById(R.id.settime);
		setRing = (EditText) findViewById(R.id.setRing);
		setFinishTime = (EditText) findViewById(R.id.setFinishTime);
		primaryContent = (EditText) findViewById(R.id.primarycontent);
		setOver = (ImageButton) findViewById(R.id.setOver);
		back = (ImageButton) findViewById(R.id.back);
		setTimeAndRing();
	}

	private void setTimeAndRing() {
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				PlanActivity.this.finish();
				
			}
		});
		setTime.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				setTime();
			}

		});
		setRing.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setRingtone();
			}
		});
		setOver.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.putExtra("result",primaryContent.getText().toString());
				PlanActivity.this.setResult(RESULT_OK, intent);
				setAlarm();
				Toast.makeText(PlanActivity.this, "闹钟设置成功", Toast.LENGTH_SHORT).show();
				PlanActivity.this.finish();
			}
		});
//		setFinishTime.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				 
//				setFinishTime();
//			}
//		});
	}
	private void setFinishTime(){
		Date date = new Date();
		calendar2.setTime(date);
		int hour = calendar2.get(Calendar.HOUR);
		int minute = calendar2.get(Calendar.MINUTE);
		new TimePickerDialog(this, new OnTimeSetListener() {
			public void onTimeSet(TimePicker view, int hour, int minute) {
				calendar2.set(Calendar.HOUR, hour);
				calendar2.set(Calendar.MINUTE, minute);
				setFinishTime.setText(hour + ":" + minute);
			}
		}, hour, minute, true).show();
	}
	private void setTime() {
		Date date = new Date();
		calendar1.setTime(date);
		int hour = calendar1.get(Calendar.HOUR);
		int minute = calendar1.get(Calendar.MINUTE);
		new TimePickerDialog(this, new OnTimeSetListener() {
			public void onTimeSet(TimePicker view, int hour, int minute) {
				calendar1.set(Calendar.HOUR, hour);
				calendar1.set(Calendar.MINUTE, minute);
				setTime.setText(hour + ":" + minute);
			}
		}, hour, minute, true).show();
	}

	private void setRingtone() {
		Intent intent = new Intent();
		//跳转到选取声音的界面
		intent.setAction(RingtoneManager.ACTION_RINGTONE_PICKER);
		intent.putExtra(RingtoneManager.EXTRA_RINGTONE_DEFAULT_URI, false);
		intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "设置闹钟铃声");
		intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE,
				RingtoneManager.TYPE_ALL);
		Uri pickedUri = RingtoneManager.getActualDefaultRingtoneUri(this,
				RingtoneManager.TYPE_ALARM);
		if (pickedUri != null) {
			intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI,
					pickedUri);
			ringUri = pickedUri;
		}
		startActivityForResult(intent, 1);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode != RESULT_OK) {
			return;
		}
		switch (requestCode) {
		case 1:
			Uri pickedURI = data
					.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
			Log.e(TAG+"pick", pickedURI.toString());
			Log.e("1111111", "111111111");
			RingtoneManager.setActualDefaultRingtoneUri(this,
					RingtoneManager.TYPE_ALARM, pickedURI);
			Log.e("getName", "22222222222222");
			break;
		default:
			break;
		}
	}

	private String getName(int type) {
		String ring_name ="";
		Log.e("getname", "0000000000000");
		Uri pickedUri = RingtoneManager.getActualDefaultRingtoneUri(this, type);
		Log.e(TAG+"11111111111111", pickedUri.toString());
		Cursor cursor = this.getContentResolver()
				.query(pickedUri,
						new String[] { MediaStore.Audio.Media.TITLE }, null,
						null, null);
		if (cursor != null) {
			if (cursor.moveToFirst()) {
				 ring_name = cursor.getString(0);
				Log.e(TAG+"22222222222222", ring_name);
				String[] c = cursor.getColumnNames();
				for (String string : c) {
					Log.e(TAG+"333333333333333", string);
				}
			}
			cursor.close();
		}
		return ring_name;
	}

	private void setAlarm() {
		alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
		
		intentReceiveIntent = new Intent(this, AlarmBroadcastReceiver.class);
//		intentReceiveIntent.putExtra("msg", "begin");
//		intentReceiveIntent.putExtra("ringURI", ringUri.toString());
		setRing.setText(getName(RingtoneManager.TYPE_ALARM));
		PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0,
				intentReceiveIntent, 0);
		alarmManager.set(AlarmManager.RTC_WAKEUP, calendar1.getTimeInMillis(),
				pendingIntent);
		Log.e("PlanActivity", "发送完毕");
//		alarmManager.set(AlarmManager.RTC_WAKEUP, calendar2.getTimeInMillis(),pendingIntent);
//		AlertDialog.Builder builder = new Builder(PlanActivity.this);
//		builder.setIcon(R.drawable.ic_launcher);
//		builder.setMessage("快去减肥");
//		builder.setTitle("3D的你");
//		builder.setPositiveButton("关闭", new DialogInterface.OnClickListener() {
//			public void onClick(DialogInterface dialog, int which) {
//				Intent intent = new Intent();
//				intent.setClass(PlanActivity.this, AlarmBroadcastReceiver.class);
//				intent.putExtra("msg", "close");
//				PendingIntent pendingIntent = PendingIntent.getBroadcast(PlanActivity.this, 0, intent, 0);
//				alarmManager.set(AlarmManager.RTC, calendar1.getTimeInMillis(), pendingIntent);
//				dialog.dismiss();
//			}
//		});
//		builder.setNegativeButton("ok", new DialogInterface.OnClickListener() {
//			
//			public void onClick(DialogInterface dialog, int which) {
//				dialog.dismiss();
//			}
//		});
//		builder.create().show();
		
		//可以点击按钮给Alarm发送，当接收，就
	}
	
}
