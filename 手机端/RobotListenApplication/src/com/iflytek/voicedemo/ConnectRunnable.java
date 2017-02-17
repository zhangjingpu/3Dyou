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

public class ConnectRunnable implements Runnable {
	/*
	 * 提供给3D动画界面并需要语音的通信连接
	 */
	Bridge bridge = Bridge.getBridge();
	Socket socket;
	OutputStreamWriter osw = null;
	InputStreamReader isr = null;
	BufferedReader bfr = null;
	BufferedWriter bfw = null;
	PrintWriter pwPrintWriter = null;
	String string = "";
	public void run() {
		try {
			socket = new Socket("192.168.43.244", 8888);
			Log.e("已连接服务器", "111");
			osw = new OutputStreamWriter(socket.getOutputStream(), "utf-8");
			bfw = new BufferedWriter(osw);
			pwPrintWriter = new PrintWriter(bfw);
			while (true) {
				Log.e("进入得到question的循环", "准备得到问题");
				String question = bridge.getQuestion();
				while (question == null || question.trim().equals("")) {
					question = bridge.getQuestion();
				}
				Log.e("quesiton", "getquestion"+question);
				pwPrintWriter.write(question);
				pwPrintWriter.flush();
				Log.e("已写入内容问题：", "getquestion"+question);
				bridge.setQuestion("");
				question = "";
				try {
					isr = new InputStreamReader(socket.getInputStream(),
							"utf-8");
					bfr = new BufferedReader(isr);
					string = bfr.readLine();
					
					bridge.setAnswer(string);
					Log.e("string", string);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
