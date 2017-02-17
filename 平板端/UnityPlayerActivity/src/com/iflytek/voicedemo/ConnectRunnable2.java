package com.iflytek.voicedemo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import android.R.integer;
import android.R.string;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

public class ConnectRunnable2 implements Runnable {
	/*
	 * Android界面的聊天
	 */
	Bridge bridge = Bridge.getBridge();
	Socket socket;
	OutputStreamWriter osw = null;
	InputStreamReader isr = null;
	BufferedReader bfr = null;
	BufferedWriter bfw = null;
	PrintWriter pwPrintWriter = null;
	String stringanswer = null;
	public static boolean chat = true;
	public void makeSocketClose(){
		try {
			if(socket==null){
				return;
			}
			else {
				socket.close();
				socket = null;
				chat = false;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void run() {
		try {
			Log.e("准备连接", "11111111111");
			socket = new Socket("192.168.43.244", 8888);
			Log.e("已连接服务器", "111");
			osw = new OutputStreamWriter(socket.getOutputStream(), "utf-8");
			bfw = new BufferedWriter(osw);
			pwPrintWriter = new PrintWriter(bfw);
			while (true) {
				Log.e("进入得到question的循环", "准备得到问题");
				String question = bridge.getQuestion();
				while (question == null||question.trim().equals("")) {
					question = bridge.getQuestion();
				}
				pwPrintWriter.write(question);
				pwPrintWriter.flush();
				Log.e("已写入内容问题：", question);
				bridge.setQuestion("");
				question = "";
				Log.e("chat追踪", ""+chat);
				if(chat){
					try {
						isr = new InputStreamReader(socket.getInputStream(),
								"utf-8");
						bfr = new BufferedReader(isr);
						stringanswer = bfr.readLine();
						Log.e("readline", stringanswer);
						Log.e("FirstActivity.studyTochat", ""+FirstActivity.studyTochat);
						if (!FirstActivity.studyTochat) {
							bridge.setAnswer(stringanswer);
							Log.e("setBridge", stringanswer);
						}else {
							FirstActivity.studyTochat = false;
						}
						Log.e("string", stringanswer);
						Log.e("getanswer", "get"+bridge.getAnswer());
						stringanswer = "";
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
