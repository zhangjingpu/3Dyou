package ANN;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;
import knowcreater.Tool;
import net.paoding.analysis.FenciTest;

public class Init {
	public static void main(String[] args) throws UnsupportedEncodingException, IOException
	{
		FenciTest fs = new FenciTest();
		Scanner scanner = new Scanner(System.in);
		while (true) {
			fs.Learn(scanner.nextLine(), scanner.nextLine());
			TestFile();
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
