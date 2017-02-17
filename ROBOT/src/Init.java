import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;

import knowcreater.Tool;
import net.paoding.analysis.FenciTest;

public class Init {

	public static void main(String[] args) throws IOException {
		FenciTest fenciTest = new FenciTest();
		Scanner scan = new Scanner(System.in);
		String input = scan.nextLine();
		String answer = scan.nextLine();
		fenciTest.Learn(input, answer);
		TestFile();
	}

	private static void Learn() throws IOException {
		String[] file = new String[] { "content/Primary4.txt", "content/Primary5.txt", "content/Primary6.txt",
				"content/Primary7.txt", "content/Primary8.txt", "content/Primary9.txt", "content/Primary10.txt",
				"content/Primary11txt", "content/Primary12.txt", "content/Primary13.txt" };
		try {
			RandomAccessFile raf = new RandomAccessFile(file[1], "rw");
			FenciTest fenciTest = new FenciTest();
			String question = "", answer = "", content = "";
			int i = 1;
			while ((content = raf.readLine()) != null) {
				if (content.trim().length() == 0) {
					continue;
				}
				content = Tool.changeCode(content);
				if (++i == 2) {
					i = 0;
					question = content;
				} else {
					answer = content;
					fenciTest.Learn(question, answer);
					// Tool.getPrintWriter().println("Question : " +
					// question +
					// "\tAnswer : " + answer);
				}
			}
			// Tool.getPrintWriter().println(raf.getFilePointer());
			raf.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void TestFile() throws UnsupportedEncodingException, IOException {
		RandomAccessFile raf = new RandomAccessFile(new File("DATA"), "rw");
		String temp = "";
		int count = 0;
		while ((temp = raf.readLine()) != null) {
			temp = Tool.changeCode(temp);
			if (Tool.getCountNumber(temp) != 1) {
				continue;
			} else {
				if ((new Scanner(temp).nextInt()) - count++ != 1) {
					Tool.getPrintWriter().print(temp + "\n");
					Tool.getPrintWriter().println("Something is really wrong . ");
					break;
				}
			}
		}
		raf = null;
		Tool.getPrintWriter().println("DONE");
	}
}