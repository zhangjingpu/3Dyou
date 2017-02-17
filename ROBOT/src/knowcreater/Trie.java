package knowcreater;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;
import net.paoding.analysis.FenciTest;

public class Trie {

	/**
	 * 
	 * 字典树.核心搜索类.学习类.
	 * 
	 * @author 齐软赛亲爱滴小伙伴
	 * 
	 */
	private final static String PATH = "DATA";
	private static ArrayList<TrieNode> root = new ArrayList<TrieNode>();
	private static int position;
	private static Scanner scanner;
	private static TrieNode record = null, now = null;
	public static String location = "";

	public Trie() {
		init();
	}

	public static String getPATH() {
		return Trie.PATH;
	}

	public static Scanner getScanner() {
		return scanner;
	}

	public static ArrayList<TrieNode> getRoot() {
		return root;
	}

	public static void clear() {
		root = null;
		position = -1;
		scanner = null;
	}

	public void init() {
		try {
			scanner = new Scanner(new File(Trie.PATH));
			String temp = "";
			int before_count = 0, now_count = 0;
			while (scanner.hasNextLine()) {
				temp = scanner.nextLine();
				now_count = Tool.getCountNumber(temp);
				if (now_count == 1) {
					now = new TrieNode(temp, null);
					getRoot().add(now);
				} else if (before_count == now_count) {
					now = new TrieNode(temp, record.getFather());
					record.getFather().getSon().add(now);
				} else if (now_count > before_count) {
					now = new TrieNode(temp, record);
					Trie.record.getSon().add(now);
				} else {
					now = backAdd(record, temp, now_count);
				}
				before_count = now_count;
				record = now;
			}
			// show(Trie.root);
			Trie.now = null;
			Trie.record = null;
			scanner = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private TrieNode backAdd(TrieNode record, String temp, int now_count) {
		if (record.getFather() == null) {
			getRoot().add(new TrieNode(temp, null));
			return getRoot().get(getRoot().size() - 1);
		} else if (record.getIntegers().size() == now_count) {
			record.getFather().getSon().add(new TrieNode(temp, record.getFather()));
			return record.getFather().getSon().get(record.getFather().getSon().size() - 1);
		} else {
			return backAdd(record.getFather(), temp, now_count);
		}
	}

	// private void show(ArrayList<TrieNode> rootArrayList) {
	// if (rootArrayList.size() == 0)
	// return;
	// for (int i = 0; i < rootArrayList.size(); i++) {
	// Tool.printAll(rootArrayList.get(i));
	// show(rootArrayList.get(i).getSon());
	// }
	// }

	private static int getPosition() {
		return position;
	}

	public String[] query(Word[] word) throws Exception {
		TrieNode tempNode = null;
		String tempString = "";
		for (int i = 0; i < word.length; i++) {
			// Tool.getPrintWriter().println(tempString);
			tempString = word[i].getWord();
			tempNode = matchTheWord(tempString, tempNode);
		}
		if (tempNode == null) {
			return new String[] { "我可以表示无言以对嘛。", "麻烦说的清楚一点，我听不懂你在说什么", "我们聊点别的什么，好不？", "亲爱的，不明白你是什么意思，麻烦换一种说法",
					"恩～" };
			// return matchFail(word);
		}
		return tempNode.getAnswer();
	}

	public String[] matchFail(Word[] word, FenciTest fenciTest) throws Exception {
		return learn(word, null, getRoot(), 0, fenciTest);
	}

	private String[] learn(Word[] word, TrieNode root, ArrayList<TrieNode> arrayList, int version, FenciTest fenciTest)
			throws Exception {
		try {
			if (root == null) {
				if (matchHeadString(word)) {
					return learn(word, arrayList.get(Trie.getPosition()), arrayList.get(Trie.getPosition()).getSon(),
							++version, fenciTest);
				} else {
					// Tool.getPrintWriter().println("learn() : Run the root ==
					// null and createTrieNode() ");
					// Tool.getPrintWriter().println("version : " + version
					// +"\nword : ");
					// Tool.print(word);
					return createTrieNode(word, null, getRoot(), version, true, fenciTest);
				}
			} else {
				if (matchBodyString(word, root, arrayList, version)) {
					return learn(word, arrayList.get(Trie.getPosition()), arrayList.get(Trie.getPosition()).getSon(),
							++version, fenciTest);
				} else {
					// Tool.getPrintWriter().println("learn() : Run the root !=
					// null and createTrieNode() ");
					return createTrieNode(word, root, arrayList, version, false, fenciTest);
				}
			}
		} catch (Exception e) {
			Trie.location = Trie.location.trim();
			// Tool.getPrintWriter().println(Trie.location);
			Tool.update(Trie.location, fenciTest.getAnswer(), fenciTest);
			return fenciTest.getAnswer();
		}

	}

	private boolean matchBodyString(Word[] word, TrieNode root, ArrayList<TrieNode> arrayList, int version)
			throws Exception {
		int hashCode = word[version].getWord().hashCode();
		for (int i = 0; i < root.getSon().size(); i++) {
			if (hashCode == root.getSon().get(i).getInfo().hashCode()) {
				Trie.position = i;
				// Tool.getPrintWriter().println("The position i (body) : " +
				// i);
				Trie.location += (i + 1) + " ";
				return true;
			}
		}
		return false;
	}

	private boolean matchHeadString(Word[] word) throws Exception {
		int hashCode = word[0].getWord().hashCode();
		for (int i = 0; i < getRoot().size(); i++) {
			if (hashCode == getRoot().get(i).getInfo().hashCode()) {
				Trie.position = i;
				// Tool.getPrintWriter().println("The position i(head) : " + i);
				Trie.location += (i + 1) + " ";
				return true;
			}
		}
		return false;
	}

	private String[] createTrieNode(Word[] word, TrieNode root, ArrayList<TrieNode> arrayList, int version,
			boolean flag, FenciTest fenciTest) throws Exception {
		if (root == null) {
			// Tool.getPrintWriter().println("CreateTrieNode () :Run the root ==
			// null");
			ArrayList<Integer> integers = new ArrayList<Integer>();
			integers.add(new Integer(arrayList.size() + 1));
			// for(int i = 0 ; i < word.length ; i++){
			// Tool.getPrintWriter().println("CreateTrieNode () : word[" + i
			// +"]" + " " + word[i].getWord());
			// }
			arrayList.add(new TrieNode(word[version].getWord(), null, integers, fenciTest.getAnswer()));
			// for (int i = 0; i < integers.size(); i++) {
			// Tool.getPrintWriter()
			// .println("CreateTrieNode () :The integer of " + i + " is " +
			// integers.get(i).intValue());
			// }
			// for (int i = 0; i < arrayList.size(); i++) {
			// Tool.getPrintWriter()
			// .println("CreateTrieNode () :The ArrayList'info of " + (i+1) + "
			// is " + arrayList.get(i).getInfo());
			// }
			Tool.writeInFile(word, null, arrayList, version, flag);
			// Tool.getPrintWriter().println(word[version]);
			return version == word.length - 1 ? fenciTest.getAnswer()
					: createTrieNode(word, arrayList.get(arrayList.size() - 1),
							arrayList.get(arrayList.size() - 1).getSon(), ++version, flag, fenciTest);
		} else if (arrayList.size() != 0) {
			// Tool.getPrintWriter().println("CreateTrieNode () :Run the size !=
			// 0");
			// Tool.getPrintWriter().println("ROOT : " + root.getInfo());
			// Tool.getPrintWriter().println("Root's size is : " +
			// root.getSon().size());
			ArrayList<Integer> integers = new ArrayList<Integer>();
			for (int i = 0; i < root.getIntegers().size(); i++) {
				integers.add(new Integer(root.getIntegers().get(i).intValue()));
			}
			integers.add(new Integer(root.getSon().size() + 1));
			arrayList.add(new TrieNode(word[version].getWord(), root, integers, fenciTest.getAnswer()));
			// for (int i = 0; i < integers.size(); i++) {
			// Tool.getPrintWriter()
			// .println("CreateTrieNode () :The integer of " + i + " is " +
			// integers.get(i).intValue());
			// }
			// for (int i = 0; i < arrayList.size(); i++) {
			// Tool.getPrintWriter()
			// .println("CreateTrieNode () :The ArrayList'info of " + i + " is "
			// + arrayList.get(i).getInfo());
			// }
			Tool.writeInFile(word, root, arrayList, version, flag);
			// for(int i = 0 ; i < word.length ; i++){
			// Tool.getPrintWriter().println("CreateTrieNode () : word[" + i
			// +"]" + " " + word[i].getWord());
			// }
			return version == word.length - 1 ? fenciTest.getAnswer()
					: createTrieNode(word, arrayList.get(arrayList.size() - 1),
							arrayList.get(arrayList.size() - 1).getSon(), ++version, flag, fenciTest);
		} else {
			// Tool.getPrintWriter().println("Run the size = 0 ");
			ArrayList<Integer> integers = new ArrayList<Integer>();
			int size = root.getIntegers().size();
			for (int i = 0; i < size; i++) {
				integers.add(new Integer(root.getIntegers().get(i).intValue()));
			}
			integers.add(new Integer(1));
			// for (int i = 0; i < integers.size(); i++) {
			// Tool.getPrintWriter()
			// .println("CreateTrieNode () :The integer of " + i + " is " +
			// integers.get(i).intValue());
			// }
			// for (int i = 0; i < arrayList.size(); i++) {
			// Tool.getPrintWriter()
			// .println("CreateTrieNode () :The ArrayList'info of " + i + " is "
			// + arrayList.get(i).getInfo());
			// }
			arrayList.add(new TrieNode(word[version].getWord(), root, integers, fenciTest.getAnswer()));
			// for(int i = 0 ; i < word.length ; i++){
			// Tool.getPrintWriter().println("CreateTrieNode () : word[" + i
			// +"]" + " " +word[i].getWord());
			// }
			Tool.writeInFile(word, root, arrayList, version, flag);
			return version == word.length - 1 ? fenciTest.getAnswer()
					: createTrieNode(word, arrayList.get(0), arrayList.get(0).getSon(), ++version, flag, fenciTest);
		}
	}

	private TrieNode matchTheWord(String tempString, TrieNode tempNode) {
		if (tempNode == null) {
			for (int i = 0; i < getRoot().size(); i++) {
				if (tempString.hashCode() == getRoot().get(i).getInfo().hashCode()) {
					return getRoot().get(i);
				}
			}
			return null;
		} else {
			for (int i = 0; i < tempNode.getSon().size(); i++) {
				if (tempString.hashCode() == tempNode.getSon().get(i).getInfo().hashCode()) {
					return tempNode.getSon().get(i);
				}
			}
		}
		return tempNode;
	}
}