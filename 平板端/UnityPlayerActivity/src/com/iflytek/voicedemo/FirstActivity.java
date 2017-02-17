package com.iflytek.voicedemo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.NumberPicker.OnValueChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.SimpleAdapter;
import android.widget.TextView;

@SuppressLint("NewApi")
public class FirstActivity extends Activity implements OnClickListener {
	
	public static boolean studyTochat = false;
	
	private ViewPager mviewPager;
	private PagerAdapter mAdapter;
	private List<View> mViews = new ArrayList<View>();

	private LinearLayout mTabWeixin;
	private LinearLayout mTabFrd;
	private LinearLayout mTabAddress;
	private LinearLayout mTabSetting;

	private ImageButton mWeixinImg;
	private ImageButton mFrdImg;
	private ImageButton mAddressImg;
	private ImageButton mSettingImg;

	private ListView listView01;
	private ListView listView03;

	private SimpleAdapter adapter01;
	private SimpleAdapter adapter03;

	private Button btn1;
	private Button btn2;
	private Fragment1 f1;
	private Fragment2 f2;

	private EditText height;
	private EditText weight;
	private EditText sex;
	private EditText age;
	private EditText strong;
	private EditText goal;
	private List<Map<String, Object>> listPlanList;
	private ImageButton imageButton;
	private Button gotoUnity;
	ConnectRunnable2 connectRunnable2 = new ConnectRunnable2();
	Bridge bridge = Bridge.getBridge();
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == 0x111) {
				FragmentManager fm = getFragmentManager();
				FragmentTransaction transaction = fm.beginTransaction();
				if (f1 == null) {
					f1 = new Fragment1();
					transaction.add(R.id.container, f1);
					
				}
				transaction.commit();
			}
			if(msg.what==0x234){
				Log.e("111111111", "runnable");
				
				new Thread(connectRunnable2).start();
			}
			if(msg.what==0x222){
				connectRunnable2.makeSocketClose();
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		setContentView(R.layout.activity_main);
		initView();
		initEvents();
		Log.e("onCreate", "onCreate");
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (listPlanList != null && listPlanList.size() != 0) {
			listPlanList.remove(listPlanList.size()-1);
		}
	}
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	private void initEvents() {
		Message message = new Message();
		message.what = 0x234;
		handler.sendEmptyMessage(message.what);
		mTabWeixin.setOnClickListener(this);
		mTabFrd.setOnClickListener(this);
		mTabAddress.setOnClickListener(this);
		mTabSetting.setOnClickListener(this);
		mviewPager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int arg0) {
				int currentItem = mviewPager.getCurrentItem();
				resetImg();
				switch (currentItem) {
				case 0:
					getListView01();
					mWeixinImg.setImageResource(R.drawable.tab_weixin_pressed);
					break;
				case 1:
					mFrdImg.setImageResource(R.drawable.tab_find_frd_pressed);
					break;
				case 2:
					getListView03();
					mAddressImg
							.setImageResource(R.drawable.tab_address_pressed);
					break;
				case 3:
					mSettingImg
							.setImageResource(R.drawable.tab_settings_pressed);
					break;
				default:
					break;
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});
		if (mviewPager.getCurrentItem() == 0) {
			getListView01();

		}
		Message message1 = new Message();
		message1.what = 0x111;
		handler.sendMessage(message1);
		btn1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				FragmentManager fm = getFragmentManager();
				FragmentTransaction transaction = fm.beginTransaction();
				Log.e("chat", ""+ConnectRunnable2.chat);
				if (!ConnectRunnable2.chat) {
					studyTochat = true;
				}else {
					studyTochat = false;
				}
				Log.e("studyTochat", ""+studyTochat);
				ConnectRunnable2.chat = true;
				if (f2 != null) {
					transaction.hide(f2);
				}
				if (f1 == null) {
					f1 = new Fragment1();
					transaction.add(R.id.container, f1);
				} else {
					transaction.show(f1);
				}
				transaction.commit();
				
				bridge.setQuestion("/quit");
				
			}
		});
		btn2.setOnClickListener(new OnClickListener() {

			@TargetApi(Build.VERSION_CODES.HONEYCOMB)
			@SuppressLint("NewApi")
			@Override
			public void onClick(View v) {
				android.app.FragmentManager fm = getFragmentManager();
				android.app.FragmentTransaction transaction = fm
						.beginTransaction();
				if (f1 != null) {
					transaction.hide(f1);
				}
				if (f2 == null) {
					f2 = new Fragment2();
					transaction.add(R.id.container, f2);
				} else {
					transaction.show(f2);
				}
				transaction.commit();
				ConnectRunnable2.chat = false;
				bridge.setQuestion("/study");
			}
		});
		imageButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getListView03();
			}
		});
		height.setOnClickListener(new OnClickListener() {
			String string = "";
			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						FirstActivity.this);
				builder.setTitle("请选择身高");
				View view = LayoutInflater.from(FirstActivity.this).inflate(
						R.layout.dialogheight, null);
				builder.setView(view);
				NumberPicker numberPicker = (NumberPicker) view
						.findViewById(R.id.heightpicker);
				numberPicker.setMinValue(100);
				numberPicker.setMaxValue(220);
				numberPicker.setValue(170);
				numberPicker
						.setOnValueChangedListener(new OnValueChangeListener() {
							public void onValueChange(NumberPicker picker,
									int oldVal, int newVal) {
								string = "" + newVal;
							}
						});
				builder.setPositiveButton("确定",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								height.setText(string);
								dialog.dismiss();
							}
						});
				builder.setNegativeButton("取消",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
							}
						});
				builder.create().show();
			}
		});
		weight.setOnClickListener(new OnClickListener() {
			String string = "";
			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						FirstActivity.this);
				builder.setTitle("请选择体重");
				View view = LayoutInflater.from(FirstActivity.this).inflate(
						R.layout.dialogheight, null);
				builder.setView(view);
				NumberPicker numberPicker = (NumberPicker) view
						.findViewById(R.id.heightpicker);
				numberPicker.setMinValue(60);
				numberPicker.setMaxValue(200);
				numberPicker.setValue(100);
				numberPicker
						.setOnValueChangedListener(new OnValueChangeListener() {
							public void onValueChange(NumberPicker picker,
									int oldVal, int newVal) {
								string = "" + newVal;
							}
						});
				builder.setPositiveButton("确定",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								weight.setText(string);
								dialog.dismiss();
							}
						});
				builder.setNegativeButton("取消",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
							}
						});
				builder.create().show();

			}
		});
		sex.setOnClickListener(new OnClickListener() {
			String string = "";
			@Override
			public void onClick(View v) {

				AlertDialog.Builder builder = new AlertDialog.Builder(
						FirstActivity.this);
				builder.setTitle("请选择性别");
				 final String[] sex1 = {"男", "女", "未知性别"};
				builder.setSingleChoiceItems(sex1, 1,new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,int which) {
								string = sex1[which];
							}
						});
				builder.setPositiveButton("确定",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								sex.setText(string);
								dialog.dismiss();
							}
						});
				builder.setNegativeButton("取消",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
							}
						});
				builder.create().show();

			}
		});
		age.setOnClickListener(new OnClickListener() {
			String string = "";
			@Override
			public void onClick(View v) {

				AlertDialog.Builder builder = new AlertDialog.Builder(
						FirstActivity.this);
				builder.setTitle("请选择年龄");
				View view = LayoutInflater.from(FirstActivity.this).inflate(
						R.layout.dialogheight, null);
				builder.setView(view);
				NumberPicker numberPicker = (NumberPicker) view
						.findViewById(R.id.heightpicker);
				numberPicker.setMinValue(3);
				numberPicker.setMaxValue(70);
				numberPicker.setValue(20);
				numberPicker
						.setOnValueChangedListener(new OnValueChangeListener() {
							public void onValueChange(NumberPicker picker,
									int oldVal, int newVal) {
								string = "" + newVal;
							}
						});
				builder.setPositiveButton("确定",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								age.setText(string);
								dialog.dismiss();
							}
						});
				builder.setNegativeButton("取消",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
							}
						});
				builder.create().show();
			}
		});
		strong.setOnClickListener(new OnClickListener() {
			String string = "";
			@Override
			public void onClick(View v) {


				AlertDialog.Builder builder = new AlertDialog.Builder(
						FirstActivity.this);
				builder.setTitle("请选择健身强度");
				 final String[] sex1 = {"初级", "中级", "高级"};
				builder.setSingleChoiceItems(sex1, 1,new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,int which) {
								string = sex1[which];
							}
						});
				builder.setPositiveButton("确定",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								strong.setText(string);
								dialog.dismiss();
							}
						});
				builder.setNegativeButton("取消",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
							}
						});
				builder.create().show();

			
			}
		});
		goal.setOnClickListener(new OnClickListener() {
			String string = "";
			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						FirstActivity.this);
				builder.setTitle("请选择你的健身目标");
				 final String[] sex1 = {"增肌","健身", "养生", "塑形"};
				builder.setSingleChoiceItems(sex1, 1,new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,int which) {
								string = sex1[which];
							}
						});
				builder.setPositiveButton("确定",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								goal.setText(string);
								dialog.dismiss();
							}
						});
				builder.setNegativeButton("取消",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
							}
						});
				builder.create().show();
			}
		});
		gotoUnity.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Message message = new Message();
				message.what = 0x222;
				handler.sendEmptyMessage(message.what);
				Intent intent = new Intent(FirstActivity.this,MainActivity.class);
				startActivity(intent);
			}
		});
	}

	private void initView() {
		mviewPager = (ViewPager) findViewById(R.id.id_viewpager);
		mTabWeixin = (LinearLayout) findViewById(R.id.id_tab_weixin);
		mTabFrd = (LinearLayout) findViewById(R.id.id_tab_frd);
		mTabAddress = (LinearLayout) findViewById(R.id.id_tab_address);
		mTabSetting = (LinearLayout) findViewById(R.id.id_tab_setting);
		mWeixinImg = (ImageButton) findViewById(R.id.id_tab_weixin_img);
		mFrdImg = (ImageButton) findViewById(R.id.id_tab_frd_img);
		mAddressImg = (ImageButton) findViewById(R.id.id_tab_address_img);
		mSettingImg = (ImageButton) findViewById(R.id.id_tab_setting_img);

		// 将布局文件转换为View
		LayoutInflater mInflater = LayoutInflater.from(this);
		View tab01 = mInflater.inflate(R.layout.tab01, null);
		View tab02 = mInflater.inflate(R.layout.tab02, null);
		View tab03 = mInflater.inflate(R.layout.tab03, null);
		View tab04 = mInflater.inflate(R.layout.tab04, null);

		listView01 = (ListView) tab01.findViewById(R.id.id_listView_info);
		listView03 = (ListView) tab03.findViewById(R.id.id_listview_plan);

		listPlanList = new ArrayList<Map<String, Object>>();
		btn1 = (Button) tab02.findViewById(R.id.chat);
		btn2 = (Button) tab02.findViewById(R.id.study);
		imageButton = (ImageButton) tab03.findViewById(R.id.add);
		gotoUnity = (Button) tab03.findViewById(R.id.gotoUnity);
		height = (EditText) tab04.findViewById(R.id.height);
		weight = (EditText)tab04. findViewById(R.id.weight);
		sex = (EditText)tab04. findViewById(R.id.sex);
		age = (EditText) tab04.findViewById(R.id.age);
		strong = (EditText)tab04. findViewById(R.id.strong);
		goal = (EditText) tab04.findViewById(R.id.goal);
		mViews.add(tab01);
		mViews.add(tab02);
		mViews.add(tab03);
		mViews.add(tab04);
		mAdapter = new PagerAdapter() {
			@Override
			public void destroyItem(ViewGroup container, int position,
					Object object) {
				container.removeView(mViews.get(position));
			}

			@Override
			public Object instantiateItem(ViewGroup container, int position) {
				View view = mViews.get(position);
				container.addView(view);
				return view;
			}

			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {
				return arg0 == arg1;
			}

			@Override
			public int getCount() {
				return mViews.size();
			}
		};
		mviewPager.setAdapter(mAdapter);
	}

	@Override
	public void onClick(View view) {
		resetImg();
		switch (view.getId()) {
		case R.id.id_tab_weixin:
			mviewPager.setCurrentItem(0);
			mWeixinImg.setImageResource(R.drawable.tab_weixin_pressed);
			getListView01();
			break;
		case R.id.id_tab_frd:
			mviewPager.setCurrentItem(1);
			mFrdImg.setImageResource(R.drawable.tab_find_frd_pressed);
			break;
		case R.id.id_tab_address:
			mviewPager.setCurrentItem(2);
			mAddressImg.setImageResource(R.drawable.tab_address_pressed);
			getListView03();
			break;
		case R.id.id_tab_setting:
			mviewPager.setCurrentItem(3);
			mSettingImg.setImageResource(R.drawable.tab_settings_pressed);
			break;
		}
	}

	// 将所有的图片切换为暗色
	private void resetImg() {
		mWeixinImg.setImageResource(R.drawable.tab_weixin_normal);
		mFrdImg.setImageResource(R.drawable.tab_find_frd_normal);
		mAddressImg.setImageResource(R.drawable.tab_address_normal);
		mSettingImg.setImageResource(R.drawable.tab_settings_normal);
	}

	/*
	 * **********************************************************************************************************************
	 */
	public void getListView01() {
		Log.e("adapter01", "11111111111111");
		adapter01 = new SimpleAdapter(this, getData01(),
				R.layout.for_listview01,
				new String[] { "img", "title", "info" }, new int[] { R.id.img,
						R.id.title, R.id.info });
		listView01.setAdapter(adapter01);
		getListView01Listener();
	}

	public void getListView01Listener() {
		listView01.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(FirstActivity.this,
						Introduce1Acticity.class);
				startActivity(intent);
			}
		});
	}

	public void getListView03Listener() {
		listView03.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(FirstActivity.this,
						PlanActivity.class);
				startActivityForResult(intent, 1);
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != RESULT_OK) {
			return;
		}
		View view = LayoutInflater.from(FirstActivity.this).inflate(
				R.layout.for_listview03, null);
//		TextView title = (TextView) view.findViewById(R.id.title);
//		title.setText(data.getExtras().getString("result"));
		String title = data.getExtras().getString("result");
		listPlanList.remove(listPlanList.size()-1);
		adapter03.notifyDataSetChanged();
		addData03(title);
		getData03();
		adapter03.notifyDataSetChanged();
		Log.e("111111111111111111111", data.getExtras().getString("result"));
		super.onActivityResult(requestCode, resultCode, data);
	}

	public void getListView03() {
		adapter03 = new SimpleAdapter(this, getData03(),
				R.layout.for_listview03, new String[] { "title", "img" },
				new int[] { R.id.title, R.id.img });
		listView03.setAdapter(adapter03);
		getListView03Listener();
	}

	public void addData03(String title) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("title", title);
		map.put("img", R.drawable.btn_radio_on_disabled_focused_holo_dark);
		listPlanList.add(map);
	}

	public List<Map<String, Object>> getData03() {
		addData03( "新建一个计划");
		return listPlanList;
	}

	public List<Map<String, Object>> getData01() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("img", R.drawable.img1);
		map.put("title", "锻炼腹肌体操法");
		map.put("info", "增肌");
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("img", R.drawable.img6);
		map.put("title", "跑步的好处");
		map.put("info", "健身");
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("img", R.drawable.img4);
		map.put("title", "饭后运动");
		map.put("info", "养生");
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("img", R.drawable.img3);
		map.put("title", "锻炼更强的力量");
		map.put("info", "健身");
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("img", R.drawable.img5);
		map.put("title", "绿色减肥");
		map.put("info", "塑形");
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("img", R.drawable.img7);
		map.put("title", "轮滑   Roller skating");
		map.put("info", "健身");
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("img", R.drawable.img8);
		map.put("title", "日常生活健康小常识");
		map.put("info", "养生");
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("img", R.drawable.img10);
		map.put("title", "运动8大注意事项");
		map.put("info", "健身");
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("img", R.drawable.img9);
		map.put("title", "生命在于运动");
		map.put("info", "健身");
		list.add(map);

		return list;
	}

}
