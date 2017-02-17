package knowcreater;

import java.util.Scanner;
import net.paoding.analysis.FenciTest;

/**
 * @author 齐软赛亲爱滴小伙伴们
 * @param result
 * @param number
 */
public class Rule {
	public static Word[] word;

	synchronized public static void deal(String result, int number, boolean flag, FenciTest fenciTest)
			throws Exception {
		word = new Word[number];
		// Tool.getPrintWriter().println(number);
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(result);
		int count = 0;
		String temp = "";
		while (scanner.hasNextLine()) {
			temp = scanner.nextLine();
			word[count++] = new Word(temp.substring(1, temp.indexOf(',')),
					new Integer(temp.substring(temp.indexOf(',') + 1, temp.lastIndexOf(','))).intValue(),
					new Integer(temp.substring(temp.lastIndexOf(',') + 1, temp.length() - 1)).intValue());
		}
		FenciTest.state = FenciTest.getAttitudeAnalysis().query();
		// Tool.print(word);
		Tool.getPrintWriter().println("State : " + FenciTest.state);
		TreeSearch treeSearch = new TreeSearch(word);
		// if (flag) {
		// if (FenciTest.trie == null) {
		// FenciTest.trie = new Trie();
		// }
		// treeSearch.match();
		// Tool.print(treeSearch.match());
		// } else {
		// if (FenciTest.trie == null)
		// FenciTest.trie = new Trie();
		// FenciTest.getTrie().matchFail(word);
		if (FenciTest.trie == null) {
			FenciTest.trie = new Trie();
		}
		if (flag) {
			fenciTest.output = dealOutput(treeSearch.match(), FenciTest.state, fenciTest);
		} else {
			// Tool.getPrintWriter().println("run the false . ");
			fenciTest.output = dealOutput(FenciTest.getTrie().matchFail(word, fenciTest), FenciTest.state, fenciTest);
		}
	}

	synchronized public static Word[] getWord() {
		return word;
	}

	/**
	 * The method should be fill before the 2015/09/05
	 * 
	 * @param match
	 * @param d
	 * @param fenciTest
	 */
	synchronized public static String dealOutput(String[] match, double state, FenciTest fenciTest) {
		if (state > 0.5) {
			return match[match.length - 1];
		}
		try {
//			Tool.getPrintWriter().println("State : " + state );
			return state > -0.5 ? match[((int) (Math.random() * (match.length - 2) + 1))] : match[0];
		} catch (ArrayIndexOutOfBoundsException exception) {
//			Tool.getPrintWriter().println("As you wish . ");
			return match[0];
		}

	}
}
