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
	static int returnDeal = 0;
	Boolean firstReadBoolean = true;

	public void run() {
		try {
			socket = new Socket("192.168.43.244", 8888);
			Log.e("已连接服务器", "111");
			osw = new OutputStreamWriter(socket.getOutputStream(), "utf-8");
			bfw = new BufferedWriter(osw);
			isr = new InputStreamReader(socket.getInputStream(), "utf-8");
			bfr = new BufferedReader(isr);
			pwPrintWriter = new PrintWriter(bfw);
			while (true) {
				/*
				 * Log.e("进入得到question的循环", "准备得到问题"); String question =
				 * bridge.getQuestion(); while (question == null ||
				 * question.trim().equals("")) { question =
				 * bridge.getQuestion(); } Log.e("quesiton", "getquestion" +
				 * question); pwPrintWriter.write(question);
				 * pwPrintWriter.flush(); dealAction(question);
				 * Log.e("已写入内容问题：", "getquestion" + question);
				 * bridge.setQuestion(""); question = "";
				 */
				try {
					string = bfr.readLine();
					bridge.setAnswer(string);
					dealAction(string);
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

	private void dealAction(String str) {
		if(str.indexOf("/10") != -1){
			returnDeal = 10;
			return;
		} else if(str.indexOf("/11") !=-1){
			returnDeal = 11;
			return;
		} else if(str.indexOf("/12") !=-1){
			returnDeal = 12;
			return;
		} else if(str.indexOf("/13") !=-1){
			returnDeal = 13;
			return;
		} else if(str.indexOf("/14") !=-1){
			returnDeal = 14;
			return;
		} else if(str.indexOf("/15")!=-1){
			returnDeal = 15;
			return;
		} else if(str.indexOf("/16")!=-1){
			returnDeal = 16;
			return;
		} else if(str.indexOf("/17")!=-1){
			returnDeal = 17;
			return;
		} else if(str.indexOf("/18")!=-1){
			returnDeal = 18;
			return;
		}else if(str.indexOf("/19")!=-1){
			returnDeal = 19;
			return;
		}
		else if (str.indexOf("/1") != -1) {
			returnDeal = 1;
			return;
		} else if (str.indexOf("/2") != -1) {
			returnDeal = 2;
			return;
		} else if (str.indexOf("/3") != -1) {
			returnDeal = 3;
			return;
		} else if (str.indexOf("/4") != -1) {
			returnDeal = 4;
			return;
		} else if (str.indexOf("/5") != -1) {
			returnDeal = 5;
			return;
		} else if (str.indexOf("/6") != -1) {
			returnDeal = 6;
			return;
		} else if (str.indexOf("/7") != -1) {
			returnDeal = 7;
			return;
		} else if (str.indexOf("/8") != -1) {
			returnDeal = 8;
			return;
		} else if (str.indexOf("/9") != -1) {
			returnDeal = 9;
			return;
		} 
	}

	public static int getDeal() {
		return returnDeal;
	}
}
