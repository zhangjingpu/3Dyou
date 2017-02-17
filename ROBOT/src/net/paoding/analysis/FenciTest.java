package net.paoding.analysis;

import java.io.*;
import java.util.Scanner;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenStream;
import ANN.AttitudeAnalysis;
import net.paoding.analysis.analyzer.PaodingAnalyzer;
import pressional.PressionalField;
import knowcreater.RepeatAnalysis;
import knowcreater.Rule;
import knowcreater.Tool;
import knowcreater.Trie;

public class FenciTest {
	private String[] answer;
	public static Trie trie = null;
	private static Analyzer analyzer = new PaodingAnalyzer();
	private static AttitudeAnalysis aas = new AttitudeAnalysis();
	public static double state = 0;
	private static PressionalField pf = new PressionalField();
	public String output = "";
	private RepeatAnalysis ra = new RepeatAnalysis();
	private ActionAnalysis aa = new ActionAnalysis();
	public RepeatAnalysis getRepeatAnalysis() {
		return ra;
	}
	
	public ActionAnalysis getActionAnalysis(){
		return aa;
	}

	public String getOutput() {
		return output;
	}

	public static PressionalField getPressionalField() {
		return pf;
	}

	public static AttitudeAnalysis getAttitudeAnalysis() {
		return aas;
	}

	public String[] getAnswer() {
		return answer;
	}

	public static Analyzer getAnalyzer() {
		return analyzer;
	}

	public static Trie getTrie() {
		return trie;
	}

	private static void close() {
		Trie.clear();
		FenciTest.analyzer = null;
		FenciTest.analyzer = null;
		FenciTest.trie = null;

	}

	@SuppressWarnings("resource")
	private void dealAnswer(String unityAnswer) {
		// Tool.getPrintWriter().println("请输入答案:");
		Scanner scanner = new Scanner(unityAnswer);
		int count = 0;
		while (scanner.hasNext()) {
			scanner.next();
			++count;
		}
		answer = new String[count];
		count = 0;
		scanner = new Scanner(unityAnswer);
		while (scanner.hasNext()) {
			answer[count++] = scanner.next();
		}
	}

	public void Learn(String unityInput, String unityAnswer) {
		int count = 0;
		// String result = "", input = "";
		String result = "";
		// Scanner scanner = new Scanner(System.in);
		// while (true) {
		// Tool.getPrintWriter().println("请输入问题:(Input : /quit to return the
		// chat mode or exit).");
		// unityInput = scanner.nextLine();
		// close();
		// result = null;
		// input = null;
		// Tool.getPrintWriter().println("训练结束.");
		// System.exit(0);
		// break;
		TokenStream tokenStream = getAnalyzer().tokenStream(unityInput, new StringReader(unityInput)); // 得到token序列的输出流
		try {
			Token t;
			while ((t = tokenStream.next()) != null) {
				result += t.toString() + '\n';
				 Tool.getPrintWriter().println(t.toString());
				++count;
			}
			// Tool.getPrintWrister().println("分词结束 . DONE");
			dealAnswer(unityAnswer);
			try {
				Rule.deal(result, count, false, this);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			answer = null;
			Trie.location = "";
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	// }

	public void run(String unityInput) {
		int count = 0;
		// String result = "", input =
		String result = "";
		// Tool.getPrintWriter().println("输入问题:");
		// Scanner scan = new Scanner(System.in);
		// unityInput = scan.nextLine();
		// if (unityInput.hashCode() == "/teach".hashCode()) {
		// Learn(unityInput);
		// } else
		if (unityInput.hashCode() == "/quit".hashCode()) {
			close();
			// Tool.getPrintWriter().println("Thank you , bye ~♪(^∇^*)");
			// System.exit(0);
			return;
		} else {
			if (getRepeatAnalysis().isReapeat(unityInput)) {
				if (getRepeatAnalysis().getCount() == 1) {
					output = new String("之前好像有过类似的啊...");
				} else {
					output = new String[] { "小乖乖，咱不做复读机成不成？", "呐呐，这个类似的问题好像重复了诶～" }[(int) (Math.random() * 2)];
				}
			} else if (getPressionalField().isPressionalField(unityInput)) {
				output = getPressionalField().getOutput();
			} else {
				TokenStream tokenStream = getAnalyzer().tokenStream(unityInput, new StringReader(unityInput)); // 得到token序列的输出流
				try {
					Token t;
					while ((t = tokenStream.next()) != null) {
						result += t.toString() + '\n';
						++count;
					}
					Tool.getPrintWriter().println(result);
					 Tool.getPrintWriter().println("分词结束 . DONE");
					try {
						Rule.deal(result, count, true, this);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			output += getActionAnalysis().getResult(unityInput);
		}
		// Tool.getPrintWriter().println(getOutput());
		answer = null;
		result = "";
		count = 0;
		Trie.location = "";
	}
}