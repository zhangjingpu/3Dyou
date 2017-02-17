package knowcreater;

import java.util.*;

public class TrieNode {
	private ArrayList<Integer> version;
	private ArrayList<TrieNode> son = new ArrayList<TrieNode>();
	private String info;
	private TrieNode father;
	private String[] answer;

	public int getLength() {
		return version.size();
	}

	public ArrayList<Integer> getIntegers() {
		return version;
	}

	public String getInfo() {
		return info;
	}

	public ArrayList<TrieNode> getSon() {
		return son;
	}

	public TrieNode getFather() {
		return father;
	}

	public String[] getAnswer() {
		return answer;
	}

	public TrieNode(String data, TrieNode father) {
		version = new ArrayList<Integer>();
		son = new ArrayList<TrieNode>();
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(data);
		while (scanner.hasNextInt()) {
			version.add(new Integer(scanner.nextInt()));
		}
		this.father = father;
		// Tool.getPrintWriter().println(data);
		scanner.next();
		info = scanner.next();
		initAnswer(data);
	}

	public TrieNode(String data, TrieNode father, ArrayList<Integer> arrayList, String[] answer) {
		this.version = arrayList;
		this.father = father;
		this.info = data;
		this.answer = answer;
	}

	private void initAnswer(String data) {
		String temp = data.substring(data.indexOf('#') + 1, data.length());
		answer = new String[Tool.getCount(temp)];
		Tool.putIn(answer, temp);
		// Tool.print(Answer);
	}
}