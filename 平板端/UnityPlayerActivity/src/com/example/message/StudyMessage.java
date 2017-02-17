package com.example.message;

import java.util.Date;

import com.example.message.ChatMessage.Type;

public class StudyMessage {
	private String name;
	private  String msg;
	private Type type;
	private Date date;
	//枚举作为类型
	public enum Type{
		INSCOMIGN,OUTSCOMING
	}	
	public StudyMessage(){
		
	}
	public StudyMessage(String msg,Type type,Date date){
		super();
		this.msg = msg;
		this.type = type;
		this.date = date;
	}
		
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public  String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public Type getType() {
		return type;
	}
	public void setType(Type type) {
		this.type = type;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
}
