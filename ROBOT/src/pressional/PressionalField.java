package pressional;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Random;

import com.sun.org.apache.regexp.internal.recompile;

import knowcreater.Tool;
import net.paoding.analysis.FenciTest;

public class PressionalField {
	private static ArrayList<String> keyword = new ArrayList<String>();
	private static ArrayList<String> info = new ArrayList<String>();
	private static ArrayList<String> preCheck = new ArrayList<String>();
	private static ArrayList<String> nextCheck = new ArrayList<String>();
	private static ArrayList<String> rule = new ArrayList<String>();
	private static ArrayList<String> attention = new ArrayList<String>();

	private static String keywordFilePath = "keyword.conf", infoFilePath = "info.conf", preCheckPath = "preCheck.conf",
			nextCheckPath = "nextCheck.conf", rulePath = "rule.conf", attentionPath = "attention.conf", output;

	public ArrayList<String> getAttention() {
		return PressionalField.attention;
	}

	public ArrayList<String> getRule() {
		return PressionalField.rule;
	}

	public ArrayList<String> getKeyword() {
		return PressionalField.keyword;
	}

	public ArrayList<String> getNextCheck() {
		return PressionalField.nextCheck;
	}

	public ArrayList<String> getPreCheck() {
		return PressionalField.preCheck;
	}

	public ArrayList<String> getInfo() {
		return info;
	}

	public String getAttentionPath() {
		return PressionalField.attentionPath;
	}

	public String getPreCheckPath() {
		return PressionalField.preCheckPath;
	}

	public String getNextCheckPath() {
		return PressionalField.nextCheckPath;
	}

	public String getKeywordPath() {
		return keywordFilePath;
	}

	public String getInfoPath() {
		return infoFilePath;
	}

	public String getRulePaht() {
		return PressionalField.rulePath;
	}

	public String getOutput() {
		return output;
	}

	public PressionalField() {
		try {
			loadKeyword();
			loadInfo();
			loadPreCheck();
			loadNextCheck();
			loadRule();
			loadAttention();
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	private void loadAttention() throws IOException {
		File file = new File(getAttentionPath());
		if (!file.exists()) {
			file.createNewFile();
		}
		try {
			RandomAccessFile raf = new RandomAccessFile(file, "rw");
			String input = "";
			while ((input = raf.readLine()) != null) {
				getAttention().add(Tool.changeCode(input));
				// Tool.getPrintWriter().println(Tool.changeCode(input));
			}
			raf.close();
			raf = null;
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void loadNextCheck() throws IOException {
		File file = new File(getNextCheckPath());
		if (!file.exists()) {
			file.createNewFile();
		}
		try {
			RandomAccessFile raf = new RandomAccessFile(file, "rw");
			String input = "";
			while ((input = raf.readLine()) != null) {
				getNextCheck().add(Tool.changeCode(input));
			}
			raf.close();
			raf = null;
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void loadPreCheck() {
		File file = new File(getPreCheckPath());
		try {
			RandomAccessFile raf = new RandomAccessFile(file, "rw");
			String input = "";
			while ((input = raf.readLine()) != null) {
				getPreCheck().add(Tool.changeCode(input));
			}
			raf.close();
			raf = null;
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void loadInfo() {
		File file = new File(getInfoPath());
		try {
			RandomAccessFile raf = new RandomAccessFile(file, "rw");
			String input = "";
			while ((input = raf.readLine()) != null) {
				getInfo().add(Tool.changeCode(input));
				// Tool.getPrintWriter().println(Tool.changeCode(input));
			}
			raf.close();
			raf = null;
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void loadKeyword() {
		File file = new File(getKeywordPath());
		try {
			RandomAccessFile raf = new RandomAccessFile(file, "rw");
			String input = "";
			while ((input = raf.readLine()) != null) {
				getKeyword().add(Tool.changeCode(input));
			}
			raf.close();
			raf = null;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void loadRule() throws IOException {
		File file = new File(getRulePaht());
		if (!file.exists()) {
			file.createNewFile();
		}
		try {
			// Tool.getPrintWriter().println("This is load the Rule .");
			RandomAccessFile raf = new RandomAccessFile(file, "rw");
			String input = "";
			while ((input = raf.readLine()) != null) {
				getRule().add(Tool.changeCode(input));
				// Tool.getPrintWriter().println(Tool.changeCode(input));
			}
			raf.close();
			raf = null;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean isPressionalField(String input) {
		return index(input);
	}

	private boolean index(String input) {
		int index = -1;
		for (int i = 0; i < getKeyword().size(); i++) {
//			Tool.getPrintWriter().println(input);
//			Tool.getPrintWriter().println(getKeyword().get(i));
//			Tool.getPrintWriter().println(input.indexOf(getKeyword().get(i).trim()));
			if ((index = input.trim().indexOf(getKeyword().get(i).trim())) != -1) {
//				Tool.getPrintWriter().println("HREE.");
				if (preCheck(input, i, index)) {
//					Tool.getPrintWriter().println("RETURN THE TRUE . ");
					return true;
				} else {
					if (index == 0) {
//						Tool.getPrintWriter().println(0+" Here . ");
						for (int k = 0, length = getNextCheck().size(); k < length; k++) {
							if ((input.indexOf(getNextCheck().get(k))) != -1) {
//								Tool.getPrintWriter().println("Find " + getNextCheck().get(k));
								PressionalField.output = getArrayListOutput(getNextCheck().get(k), i);
								return true;
							}
						}
						PressionalField.output = getInfo().get(i);
						return true;
					}
					else {
						PressionalField.output = getInfo().get(i);
						return true;
					}
				}
			}
		}
		return false;
	}

	private boolean preCheck(String input, int keyword_position, int index) {
		int tempInt = 0;
		String tempString;
		for (int i = 0, len = getPreCheck().size(); i < len; i++) {
			tempString = getPreCheck().get(i);
			// Tool.getPrintWriter().println(tempString);
			if (tempString.indexOf('#') != -1) {
				tempString = tempString.substring(tempString.indexOf('#') + 2, tempString.length());
				// Tool.getPrintWriter().println(tempString);
				if ((tempInt = input.indexOf(tempString)) != -1) {
					if (index - tempInt <= 4) {
						return false;
					} else {
						PressionalField.output = getInfo().get(keyword_position);
						return true;
					}
				}
			} else {
				if ((tempInt = input.indexOf(tempString)) != -1) {
					// Tool.getPrintWriter().println(tempInt + "\t" + index +
					// "\t" + input);
					if (Math.abs(tempInt - index) <= 4) {
						for (int j = 0, len2 = getNextCheck().size(); j < len2; j++) {
							// Tool.getPrintWriter().println(getNextCheck().get(j).length());
							// Tool.getPrintWriter().println((input.indexOf(getNextCheck().get(j).toString()))
							// != -1);
							if ((input.indexOf(getNextCheck().get(j))) != -1) {
//								Tool.getPrintWriter().println("Find " + getNextCheck().get(j));
								PressionalField.output = getArrayListOutput(getNextCheck().get(j), keyword_position);
								if (PressionalField.output == null) {
									return false;
								}
								return true;
							}
						}
						PressionalField.output = getInfo().get(keyword_position);
					} else {
						PressionalField.output = getInfo().get(keyword_position);
					}
					return true;
				}
			}
		}
		return false;
	}

	private String getArrayListOutput(String string, int keyword_position) {
//		Tool.getPrintWriter().println("Run in getArrayList () : " + string + "\t" + string.hashCode());
		switch (string.hashCode()) {
		case (812512):// This is the "指标"
		case (846495):// This is the "标准"
		case (1114325):// This is the "规则"
			Tool.getPrintWriter().println("PressionField : Rule ");
			return (getRule().get(keyword_position).hashCode() != 3392903 ? getRule().get(keyword_position)
					: getInfo().get(keyword_position));
		case (889127):// This is the "注意"
		case (662350):// This is the "事项"
			Tool.getPrintWriter().println("PressionField : getArrayListOutput . ");
			return getAttention().get(keyword_position);
//			return (getAttention().get(keyword_position).hashCode() != 3392903 ? getAttention().get(keyword_position)
//					: (Math.random() < 0.4 ? getInfo().get(keyword_position) : getRule().get(keyword_position)));
		case (671745):// This is the "准备"
			return null;
		case (937312):// This is the "特点"
			return null;
		case (1150969):// This is the "起源"
			return (getInfo().get(keyword_position));
		case (685613):// This is the "受伤"
		case (793063):// This is the "意外"
			return null;
		default:
			return(getInfo().get(keyword_position));
		}
	}
}