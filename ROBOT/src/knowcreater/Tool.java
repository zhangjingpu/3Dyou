package knowcreater;

import java.util.ArrayList;
import java.util.Scanner;
import ANN.BP;
import net.paoding.analysis.FenciTest;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;

public class Tool {

	/**
	 * The File is to store the BP net .
	 */
	private static File BPPositiveFile = new File("BPPositive.sricfg");

	/**
	 * The File is to store the BP net .
	 */
	private static File BPNegativeFile = new File("BPNegative.sricfg");

	/**
	 * The two files are the dic we need to feed the BP
	 */
	private static File posiviveFile = new File("ntusd-positive.txt"), negativeFile = new File("ntusd-negative.txt");
	/**
	 * The class contains the tools we need .
	 */

	private static PrintWriter pw = new PrintWriter(System.out, true);

	/**
	 * BP神经网络元
	 */
	private static BP bpPositive, bpNegative;

	/**
	 * The method is to count the .hasNext in data
	 *
	 * @author 齐软赛亲爱滴小伙伴们
	 * @param data
	 * @return The number in data of .hasNext
	 */
	public static int getCount(String data) {
		int i = 0;
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(data);
		while (scanner.hasNext()) {
			scanner.next();
			i++;
		}
		return i;
	}

	/**
	 * The method is to get the number of .hasNextInt in the data.
	 *
	 * @author 齐软赛亲爱滴小伙伴们
	 * @param data
	 * @return The number of .hasNextInt in the data
	 */
	public static int getCountNumber(String data) {
		int i = 0;
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(data);
		while (scanner.hasNextInt()) {
			scanner.nextInt();
			i++;
		}
		return i;
	}

	/**
	 * The method is to get the line number in data using .hasNextLine .
	 *
	 * @author 齐软赛亲爱滴小伙伴们
	 * @param data
	 * @return the line numbert of data .
	 */
	public static int getLineCount(String data) {
		int i = 0;
		@SuppressWarnings("resource")
		Scanner scan = new Scanner(data);
		while (scan.hasNextLine()) {
			scan.nextLine();
			i++;
		}
		scan = null;
		return i;
	}

	/**
	 * The method is to put all String in data into the answer expediently
	 *
	 * @author 齐软赛亲爱滴小伙伴们
	 * @param answer
	 * @param data
	 * @return void
	 */
	public static void putIn(String[] answer, String data) {
		// TODO Auto-generated method stub
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(data);
		int count = 0;
		while (scanner.hasNext()) {
			answer[count++] = scanner.next();
		}
	}

	/**
	 * The method is to print the word[i].getWord().Just fot testing
	 *
	 * @author 齐软赛亲爱滴小伙伴们
	 * @param word
	 */
	public static void print(Word[] word) {
		for (int i = 0; i < word.length; i++) {
			Tool.getPrintWriter().println(word[i].getWord());
		}
		Tool.getPrintWriter().println("TOOL.PRINT Word[] DONE ! ");
	}

	/**
	 * The method is to print all String[]
	 *
	 * @author 齐软赛亲爱滴小伙伴们
	 * @param answer
	 */
	public static void print(String[] answer) {
		for (int i = 0; i < answer.length; i++) {
			Tool.getPrintWriter().println(answer[i]);
		}
		Tool.getPrintWriter().println("Tool.PRINT String[] DONE ! ");
	}

	/**
	 * The method is to print the single trieNode's info , answer , father and
	 * son.
	 *
	 * @param trieNode
	 */
	public static void printAll(TrieNode trieNode) {
		Tool.getPrintWriter().println("Info :" + trieNode.getInfo());
		Tool.getPrintWriter().println("Answer :" + trieNode.getAnswer().length);
		Tool.getPrintWriter()
				.println("Father :" + (trieNode.getFather() != null ? trieNode.getFather().getInfo() : "null"));
		Tool.getPrintWriter().println("Son : " + trieNode.getSon().size());
	}

	/**
	 * The method is to writeInFile with RandomAccessFile
	 *
	 * @author 齐软赛亲爱滴小伙伴们
	 * @param word
	 * @param trie
	 * @param arrayList
	 * @param version
	 * @param flag
	 * @throws Exception
	 */
	public static void writeInFile(Word[] word, TrieNode trie, ArrayList<TrieNode> arrayList, int version, boolean flag)
			throws Exception {
		try {
			RandomAccessFile rf = new RandomAccessFile(Trie.getPATH(), "rw");
			if (trie == null) {
				// Tool.getPrintWriter().println("writeInFile():Run the trie
				// ==null");
				// Tool.getPrintWriter().println(arrayList.get(arrayList.size()
				// - 1).getInfo());
				rf.seek(rf.length()); // 将指针移动到文件末尾
				rf.write(("\n" + dealInput(trie, arrayList)).getBytes());
			} else if (arrayList.size() != 1) {
				String temp = "";
				// Tool.getPrintWriter().println("writeInFile():Run
				// inarrayList.size() != 1");
				// Tool.getPrintWriter().println("size() -1 Info :" +
				// arrayList.get(arrayList.size() - 1).getInfo());
				// Tool.getPrintWriter().println("size() -2 Info :" +
				// arrayList.get(arrayList.size() - 2).getInfo());
				String compare = "";
				for (int i = 0; i < arrayList.get(arrayList.size() - 2).getIntegers().size(); i++) {
					compare += arrayList.get(arrayList.size() - 2).getIntegers().get(i).intValue() + " ";
					// Tool.getPrintWriter().println("compare:" + compare);
				}
				compare = compare.trim();
				// Tool.getPrintWriter().println("The finally compare is :" +
				// compare);
				while ((temp = rf.readLine()) != null) {
					temp = changeCode(temp);
					// Tool.getPrintWriter().println(temp);
					if (compare(temp, compare)) {
						// Tool.getPrintWriter().println("Compare the
						// wordSuccessful");
						find(rf, compare, word, trie, arrayList, version, flag);
						break;
					}
				}
			} else {
				// Tool.getPrintWriter().println("writeInFile():Run in the size
				// == 1");
				// Tool.getPrintWriter().println(arrayList.get(0).getInfo());
				int size = trie.getIntegers().size();
				String check = "";
				for (int i = 0; i < size; i++) {
					check += trie.getIntegers().get(i).intValue() + " ";
				}
				check = check.trim();
				// Tool.getPrintWriter().println(check);
				String temp = "";
				while ((temp = rf.readLine()) != null) {
					temp = changeCode(temp);
					// Tool.getPrintWriter().println(temp);
					if (compare(temp, check)) {
						// Tool.getPrintWriter().println(temp + "\tcompare the :
						// " + check + " Successful ");
						if (rf.getFilePointer() == rf.length()) {
							// Tool.getPrintWriter().println("End of buffer .
							// ");
							Tool.rafWriteIn(rf.getFilePointer(), ("\n" + dealInput(trie, arrayList) + ""), rf);
						} else {
							Tool.rafWriteIn(rf.getFilePointer(),
									((flag ? "\n" : "") + dealInput(trie, arrayList) + ((flag) ? "" : "\n")), rf);
						}
						break;
					}
				}
			}
			rf.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * The method is to find the position of run in arrayList's size > 1
	 *
	 * @author 齐软赛亲爱滴小伙伴们
	 * @param rf
	 * @param compare
	 * @param word
	 * @param trie
	 * @param arrayList
	 * @param version
	 * @param flag
	 * @throws Exception
	 */
	private static void find(RandomAccessFile rf, String compare, Word[] word, TrieNode trie,
			ArrayList<TrieNode> arrayList, int version, boolean flag) throws Exception {
		int length = 0, temp_count = 0;
		String temp_string = "";
		@SuppressWarnings("resource")
		Scanner scanner_compare = new Scanner(compare);
		while (scanner_compare.hasNext()) {
			scanner_compare.nextInt();
			++length;
		}
		while ((temp_string = rf.readLine()) != null) {
			temp_string = changeCode(temp_string);
			// Tool.getPrintWriter().println(temp_string);
			temp_count = Tool.getCountNumber(temp_string);
			// Tool.getPrintWriter().println("temp_count : " + temp_count + "
			// length :" +
			// length);
			if (temp_count < length) {
				// Tool.getPrintWriter().println("我找到了这个节点 . temp_count < length
				// ");
				rafWriteIn(rf.getFilePointer() - temp_string.getBytes().length - 2, ("\n" + dealInput(trie, arrayList)),
						rf);
				return;
			}
		}
		// Tool.getPrintWriter().println("rf.seek(rf.length());// 将指针移动到文件末尾");
		rf.seek(rf.length()); // 将指针移动到文件末尾
		rf.write(("\n" + dealInput(trie, arrayList)).getBytes());
	}

	/**
	 * The method is called by another function . Please don't change it easily
	 *
	 * @author 齐软赛亲爱滴小伙伴们
	 * @param temp
	 * @param compare
	 * @return
	 */
	private static boolean compare(String temp, String compare) {
		@SuppressWarnings("resource")
		Scanner scanner_temp = new Scanner(temp);
		@SuppressWarnings("resource")
		Scanner scanner_compare = new Scanner(compare);
		// Tool.getPrintWriter().println("temp :" + temp);
		// Tool.getPrintWriter().println("compare:" + compare);
		while (scanner_compare.hasNextInt() && scanner_temp.hasNextInt()) {
			// Tool.getPrintWriter().println("a == b means:" + a + "\t==\t" +
			// b);
			if (scanner_compare.nextInt() != scanner_temp.nextInt())
				return false;
			// if (scanner_compare.nextInt() != scanner_temp.nextInt()) {
			// return false;
			// }
			if ((scanner_compare.hasNextInt() && !scanner_temp.hasNextInt())
					|| (!scanner_compare.hasNextInt() && scanner_temp.hasNextInt()))
				return false;
		}
		scanner_compare = null;
		scanner_temp = null;
		return true;
	}

	/**
	 * The method is to cooperate the RandomAccessFile to change the String with
	 * new String(temp.getBytes("8859_1") , "utf-8")
	 *
	 * @param temp
	 * @author 齐软赛亲爱滴小伙伴们
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String changeCode(String temp) throws UnsupportedEncodingException {
		// TODO Auto-generated method stub
		return new String(temp.getBytes("8859_1"), "utf-8");
	}

	/**
	 * Just dealInput to get the standard input .
	 *
	 * @author 齐软赛亲爱滴小伙伴们
	 * @param trieNode
	 * @param arrayList
	 * @return
	 */
	private static String dealInput(TrieNode trieNode, ArrayList<TrieNode> arrayList) {
		String input = "";
		// Tool.getPrintWriter().println(trieNode==null);
		TrieNode trie = arrayList.get(arrayList.size() - 1);
		int size = trie.getIntegers().size();
		for (int i = 0; i < size; i++) {
			input += trie.getIntegers().get(i).intValue() + " ";
		}
		return input += ("\t$ " + trie.getInfo() + "\t#\t" + Tool.getStringFromStringArray(trie.getAnswer()));
	}

	/**
	 * The method is to use the RandomAccessFile to write into file complete the
	 * learn()
	 *
	 * @author 齐软赛亲爱滴小伙伴们
	 * @param skip
	 * @param str
	 * @param raf
	 * @throws Exception
	 */
	public static void rafWriteIn(long skip, String str, RandomAccessFile raf) throws Exception {
		try {
			if (skip < 0 || skip > raf.length()) {
				throw new Exception("跳过字节数异常.");
			}
			byte[] b = str.getBytes();
			raf.setLength(raf.length() + b.length);
			for (long i = raf.length() - 1; i > b.length + skip - 1; i--) {
				raf.seek(i - b.length);
				byte temp = raf.readByte();
				raf.seek(i);
				raf.writeByte(temp);
			}
			raf.seek(skip);
			raf.write(b);
			raf.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static String getStringFromStringArray(String[] answer) {
		String temp = "";
		for (int i = 0; i < answer.length; i++) {
			temp += answer[i];
		}
		return temp;
	}

	/**
	 * The method is to travseral the root(arrayList) , you can use it in
	 * testing .
	 *
	 * @author 齐软赛亲爱滴小伙伴们
	 * @param root
	 */
	public static void travseral(ArrayList<TrieNode> root) {
		if (root.size() == 0)
			return;
		for (int i = 0; i < root.size(); i++) {
			// Tool.getPrintWriter().println("The number in arraylist is " + (i
			// + 1) + " of " + root.get(i).getInfo());
			travseral(root.get(i).getSon());
		}
	}

	// public static byte[] stringChangeByte(String string) {
	// return string.getBytes();
	// }

	/**
	 * 初始化一个全新的bp神经网络
	 * 
	 * @param inputSize
	 * @param hiddenSize
	 * @param outputSize
	 */
	public static void initializationPositive(int inputSize, int hiddenSize, int outputSize) {
		Tool.bpPositive = new BP(inputSize, hiddenSize, outputSize);
	}

	/**
	 * 从文件数据中读取bp神经网络
	 * 
	 * @param file
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static void initializationPositive(File file) {
		FileInputStream fi;
		ObjectInputStream si;
		try {
			fi = new FileInputStream(file);
			si = new ObjectInputStream(fi);
			Tool.bpPositive = (BP) si.readObject();
			// Tool.getPrintWriter().println(bp == null);
			fi.close();
			si.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e2) {
			e2.printStackTrace();
		} catch (ClassNotFoundException e3) {
			e3.printStackTrace();
		}
	}

	/**
	 * 从文件数据中读取bp神经网络
	 * 
	 * @param file
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static void initializationNegative(File file) {
		FileInputStream fi;
		ObjectInputStream si;
		try {
			fi = new FileInputStream(file);
			si = new ObjectInputStream(fi);
			Tool.bpNegative = (BP) si.readObject();
			// Tool.getPrintWriter().println(bp == null);
			fi.close();
			si.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e2) {
			e2.printStackTrace();
		} catch (ClassNotFoundException e3) {
			e3.printStackTrace();
		}
	}

	/**
	 * 将目前的神经网络储存在指定文件
	 * 
	 * @param file
	 * @throws IOException
	 */
	public static void savePositive(File file, BP bp) throws IOException {
		FileOutputStream fo = new FileOutputStream(file);
		ObjectOutputStream so = new ObjectOutputStream(fo);
		Tool.bpPositive = bp;
		so.writeObject(bp);
		so.close();
	}

	/**
	 * 将目前的神经网络储存在指定文件
	 * 
	 * @param file
	 * @throws IOException
	 */
	public static void saveNegative(File file, BP bp) throws IOException {
		FileOutputStream fo = new FileOutputStream(file);
		ObjectOutputStream so = new ObjectOutputStream(fo);
		Tool.bpNegative = bp;
		so.writeObject(bp);
		so.close();
	}

	/**
	 * 训练BP神经网络
	 * 
	 * @param trainData
	 * @param target
	 */
	public static void trainPositive(double[] trainData, double[] target) {
		Tool.bpPositive.train(trainData, target);
	}

	/**
	 * 训练BP神经网络
	 * 
	 * @param trainData
	 * @param target
	 */
	public static void trainNegative(double[] trainData, double[] target) {
		Tool.bpNegative.train(trainData, target);
	}

	/**
	 * We should use PrintWriter not the System.out.print
	 * 
	 * @return
	 */
	public static PrintWriter getPrintWriter() {
		return pw;
	}

	/**
	 * To ge the Positive dic
	 * 
	 * @return
	 */
	public static File getPositive() {
		return Tool.posiviveFile;
	}

	/**
	 * To ge the Negative dic
	 * 
	 * @return
	 */
	public static File getNegative() {
		return negativeFile;
	}

	/**
	 * To ge the BP serializable
	 * 
	 * @return
	 */
	public static File getBPPositiveFile() {
		return Tool.BPPositiveFile;
	}

	/**
	 * To ge the BP serializable
	 * 
	 * @return
	 */
	public static File getBPNegativeFile() {
		return Tool.BPNegativeFile;
	}

	/**
	 * To get the BP obj
	 * 
	 * @return
	 */
	public static BP getPositiveBP() {
		if (Tool.bpPositive == null) {
			initializationPositive(Tool.getBPPositiveFile());
		}
		return Tool.bpPositive;
	}

	/**
	 * To get the BP obj
	 * 
	 * @return
	 */
	public static BP getNegativeBP() {
		if (Tool.bpNegative == null) {
			initializationNegative(Tool.getBPNegativeFile());
		}
		return Tool.bpNegative;
	}

	/**
	 * The method is to convert the string with utf-8 to the double[] with the
	 * final length
	 * 
	 * @param input
	 * @author 齐软赛亲爱滴小伙伴
	 * @return
	 */
	public static double[] getDoubleMatrix(String input) {
		if (3 * input.length() > 75) {
			throw new IllegalArgumentException("不要乱来输入少一点O(∩_∩)O谢谢 ~ ");
		}
		double[] convert = new double[75];
		byte[] temp = input.getBytes();
		for (int i = 0; i < temp.length; i++) {
			convert[i] = temp[i];
		}
		return convert;
	}

	public static boolean isDone(double[] testResult, boolean flag) {
		return getResult(testResult, flag) > 0 ? false : true;
	}

	public static double getResult(double[] test, boolean flag) {
		double sum = 0;
		for (int i = 0, half = test.length / 2, length = test.length; i < length; i++) {
			if (i < half) {
				sum = sum + (flag ? test[i] : -test[i]);
			} else {
				sum = sum - (flag ? test[i] : -test[i]);
			}
		}
		return sum;
	}

	public static void update(String location, String[] answer, FenciTest fenciTest) {
		RandomAccessFile raf;
		try {
			raf = new RandomAccessFile(new File(Trie.getPATH()), "rw");
			String temp = "";
			while ((temp = raf.readLine()) != null) {
				temp = Tool.changeCode(temp);
				if (Tool.getNumberString(temp).hashCode() == Trie.location.hashCode()) {
					String answerString = "";
					for (int i = 0, len = fenciTest.getAnswer().length; i < len; i++) {
						answerString += fenciTest.getAnswer()[i] + "\t";
					}
					Tool.rafWriteIn(raf.getFilePointer() - "\n".getBytes().length, ('\t' + answerString.trim()), raf);
					// Tool.getPrintWriter().println("DONE > _ < ");
					return;
				}
			}
			raf.close();
			raf = null;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * The method is to get the number string from temp
	 * 
	 * @param temp
	 * @return
	 */
	private static String getNumberString(String temp) {
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(temp);
		String returnString = "";
		while (scanner.hasNextInt()) {
			returnString += (scanner.nextInt() + " ");
		}
		scanner = null;
		return returnString.trim();
	}

	/**
	 * The method use dnamic program to solve the problem of public string macth
	 * 
	 * @param a
	 * @param b
	 */
	public static String dynamicProgram(String a, String b) {
		int[][] save = new int[a.length() + 1][b.length() + 1];
		for (int i = save.length - 1; i >= 0; i--) {
			for (int j = save[0].length - 1; j >= 0; j--) {
				if (i == 0 || j == 0) {
					save[i][j] = 0;
				} else if (a.charAt(i - 1) == b.charAt(j - 1)) {
					save[i][j] = save[i - 1][j - 1] + 1;
				} else {
					save[i][j] = save[i - 1][j] >= save[i][j - 1] ? save[i - 1][j] : save[i][j - 1];
				}
			}
		}
		int max = 0, index = 0;
		for (int i = 1; i < a.length() + 1; i++) {
			for (int j = 1; j < b.length() + 1; j++) {
				if (save[i][j] != 0 && save[i - 1][j - 1] != 0) {
					save[i][j] = save[i - 1][j - 1] + 1;
					if (save[i][j] > max) {
						max = save[i][j];
						index = i;
					}
				}
			}
		}
		return (max == 0 && index == 0) ? "" : a.substring(index - max, index);
	}
}