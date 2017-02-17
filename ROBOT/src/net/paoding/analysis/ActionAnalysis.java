package net.paoding.analysis;

public class ActionAnalysis {
	String content = "";

	public ActionAnalysis() {

	}

	public String getResult(String input) {
		if (input.indexOf("篮球") != -1) {
			return "/1";
		} else if (input.indexOf("足球") != -1) {
			return "/2";
		} else if (input.indexOf("转球") != -1) {
			return "/3";
		} else if (input.indexOf("抬腿") != -1) {
			return "/4";
		} else if (input.indexOf("拍手") != -1) {
			return "/5";
		} else if (input.indexOf("不") != -1) {
			return "/6";
		} else if (input.indexOf("体操") != -1) {
			return "/7";
		} else if (input.indexOf("正步") != -1) {
			return "/8";
		} else if (input.indexOf("踏步") != -1) {
			return "/9";
		} else if (input.indexOf("左转") != -1 || input.indexOf("左传") != -1){
			return "/10";
		} else if (input.indexOf("右转") != -1){
			return "/11";
		} else if (input.indexOf("后转") != -1){
			return "/12";
		} else if (input.indexOf("前进") != -1){
			return "/13";
		} else if (input.indexOf("后退") != -1){
			return "/14";
		}else if (input.indexOf("足球背景") != -1){
			return "/17";
		} else if (input.indexOf("换背景") != -1){
			return "/18";
		} else if (input.indexOf("停止") != -1){
			return "/19";
		}
		return "/0";
	}
}
