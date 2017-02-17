package com.iflytek.voicedemo;

import android.util.Log;

public class Bridge {
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		if(!answer.equals("")){
			answer = answer.substring(0, answer.length()-2);
		}
		this.answer = answer;
	}
	public static Bridge bridge = new Bridge();
	public static Bridge getBridge(){
		return bridge;
	}
	private String question;
	private String answer;
	
}
