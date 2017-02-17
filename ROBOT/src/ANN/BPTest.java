package ANN;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.util.Random;

import knowcreater.Tool;

public class BPTest {

	public static void main(String[] args) throws UnsupportedEncodingException, IOException {
		// randomAdd();
		// addNegative();
		// getErr();
		addNegative();
		addPositive();
		// Test();
		// BP bp = new BP(75, 20, 2);
		// double[] output = new double[] { 1, 0 };
		// for (int i = 0; i < 2; i++) {
		// bp.train(Tool.getDoubleMatrix("高兴不"), output);
		// }
		// Tool.getPrintWriter().println(Tool.getBP().test(Tool.getDoubleMatrix("高兴"))[0]);
		// Tool.save(Tool.getBPFile(), bp);
	}

	// private static void Test() throws UnsupportedEncodingException {
	// double[] result =
	// Tool.getNegativeBP().test(Tool.getDoubleMatrix(Tool.changeCode("我爱你")));
	// for (int i = 0; i < result.length; i++) {
	// Tool.getPrintWriter().println(result[i]);
	// }
	// }

	private static void Test() {
		double[] result = Tool.getNegativeBP().test(Tool.getDoubleMatrix("你好世界."));
		for (int i = 0; i < result.length; i++) {
			Tool.getPrintWriter().println(result[i]);
		}
	}

	private static void addNegative() throws IOException {
		BP bp = new BP(75, 30, 6);
		// String tempP = "", tempN = "";
		String tempN = "";
		double[] matrix = new double[] { 0, 0, 0, 1, 1, 1 };
		RandomAccessFile rafN = new RandomAccessFile(Tool.getNegative(), "rw");
		int temp = 0;
		// boolean flag = true;
		while ((tempN = rafN.readLine()) != null) {
			int times = 0;
			bp.train(Tool.getDoubleMatrix(Tool.changeCode(tempN)), matrix);
			while (Tool.isDone(bp.test(Tool.getDoubleMatrix(Tool.changeCode(tempN))), false)) {
				bp.train(Tool.getDoubleMatrix(Tool.changeCode(tempN)), matrix);
				Tool.getPrintWriter().println(++times);
			}
			// if (flag) {
			//
			// }
			Tool.getPrintWriter().println(++temp + " Found. ");
			// else
			// flag = true;
		}
		rafN.close();
		// Tool.saveNegative(Tool.getBPNegativeFile(), bp);
		// double[] result =
		// Tool.getNegativeBP().test(Tool.getDoubleMatrix(Tool.changeCode("爱")));
		// for (int i = 0; i < result.length; i++) {
		// Tool.getPrintWriter().println(result[i]);
		// }
		Tool.saveNegative(Tool.getBPNegativeFile(), bp);
		Tool.getPrintWriter().println("Done !");
		// getErr();
	}

	private static void addPositive() throws IOException {
		BP bp = new BP(75, 30, 6);
		// String tempP = "", tempN = "";
		String tempN = "";
		double[] matrix = new double[] { 1, 1, 1, 0, 0, 0 };
		RandomAccessFile rafP = new RandomAccessFile(Tool.getPositive(), "rw");
		int temp = 0;
		// boolean flag = true;
		while ((tempN = rafP.readLine()) != null) {
			int times = 0;
			bp.train(Tool.getDoubleMatrix(Tool.changeCode(tempN)), matrix);
			while (Tool.isDone(bp.test(Tool.getDoubleMatrix(Tool.changeCode(tempN))), true)) {
				bp.train(Tool.getDoubleMatrix(Tool.changeCode(tempN)), matrix);
				Tool.getPrintWriter().println(++times);
			}
			// if (flag) {
			//
			// }
			Tool.getPrintWriter().println(++temp + " Found. ");
			// else
			// flag = true;
		}
		rafP.close();
		// Tool.saveNegative(Tool.getBPNegativeFile(), bp);
		// double[] result =
		// Tool.getNegativeBP().test(Tool.getDoubleMatrix(Tool.changeCode("爱")));
		// for (int i = 0; i < result.length; i++) {
		// Tool.getPrintWriter().println(result[i]);
		// }
		Tool.savePositive(Tool.getBPPositiveFile(), bp);
		Tool.getPrintWriter().println("Done !");
		// getErr();
	}

	private static void getErr() throws IOException {
		RandomAccessFile raf = new RandomAccessFile(Tool.getNegative(), "rw");
		RandomAccessFile raf2 = new RandomAccessFile(Tool.getPositive(), "rw");
		String temp = "";
		int err = 0, line = 0;
		while ((temp = raf.readLine()) != null) {
			if (Tool.isDone(Tool.getNegativeBP().test(Tool.getDoubleMatrix(Tool.changeCode(temp))), false)) {
				err++;
			}
			line++;
		}
		Tool.getPrintWriter().println("The Rate is : " + ((double) err) / line);
		Tool.getPrintWriter().println("ERR : " + err + "\t line :" + line);
		err = 0;
		line = 0;
		while ((temp = raf2.readLine()) != null) {
			if (Tool.isDone(Tool.getPositiveBP().test(Tool.getDoubleMatrix(Tool.changeCode(temp))), true)) {
				err++;
			}
			line++;
		}
		Tool.getPrintWriter().println("The Rate is : " + ((double) err) / line);
		Tool.getPrintWriter().println("ERR : " + err + "\t line :" + line);
		raf.close();
		raf2.close();
		raf = null;
		raf2 = null;
	}

	// private static void testBP() {
	// Tool.initialization(Tool.getBPFile());
	// Tool.getPrintWriter().println(Tool.getBP().test(Tool.getDoubleMatrix("高兴"))[0]);
	// }

	// private static void init() throws UnsupportedEncodingException,
	// IOException {
	// BP bp = new BP(75, 20, 2);
	// double[] output = new double[] { 10, 0 };
	// RandomAccessFile raf = new RandomAccessFile(Tool.getPositive(), "rw");
	// int times = 30000;
	// randomAdd();
	// String temp = "";
	// while ((temp = raf.readLine()) != null) {
	// bp.train(Tool.getDoubleMatrix(Tool.changeCode(temp)), output);
	// }
	// output[0] = 10;
	// output[1] = 0;
	// raf.close();
	// raf = null;
	// raf = new RandomAccessFile(Tool.getNegative(), "rw");
	// while ((temp = raf.readLine()) != null) {
	// bp.train(Tool.getDoubleMatrix(Tool.changeCode(temp)), output);
	// }
	// raf.close();
	// raf = null;
	// Tool.getPrintWriter().println(bp.test(Tool.getDoubleMatrix("高兴"))[0]);
	// Tool.getPrintWriter().println(bp.test(Tool.getDoubleMatrix("悲伤"))[0]);
	// Tool.getPrintWriter().println(bp.test(Tool.getDoubleMatrix("一文不值"))[0]);
	// Tool.getPrintWriter().println(bp.test(Tool.getDoubleMatrix("痛苦"))[0]);
	// Tool.getPrintWriter().println(bp.test(Tool.getDoubleMatrix("邋遢"))[0]);
	// Tool.getPrintWriter().println(bp.test(Tool.getDoubleMatrix("一夜春风"))[1]);
	// Tool.save(Tool.getBPFile(), bp);
	// Tool.getPrintWriter().println("Save done . ");
	// }

	private static void randomAdd() throws IOException {
		BP bp = new BP(75, 100, 6);
		// Random random = new Random();
		// double flag = 25 / (25 + 29);
		// String tempP = "", tempN = "";
		String tempP = "";
		double[] matrix = new double[] { 1, 1, 1, 0, 0, 0 };
		RandomAccessFile rafP = new RandomAccessFile(Tool.getPositive(), "rw");
		// RandomAccessFile rafN = new RandomAccessFile(Tool.getNegative(),
		// "rw");
		while ((tempP = rafP.readLine()) != null) {
			for (int i = 0; i < 2; i++) {
				bp.train(Tool.getDoubleMatrix(Tool.changeCode(tempP)), matrix);
			}
		}
		// while (((tempP = rafP.readLine()) != null) || ((tempN =
		// rafN.readLine()) != null)) {
		// if (tempN == null) {
		// matrix[0] = 1;
		// matrix[1] = 0;
		// bp.train(Tool.getDoubleMatrix(Tool.changeCode(tempP)), matrix);
		// } else if (tempP == null) {
		// matrix[0] = 0;
		// matrix[1] = 1;
		// bp.train(Tool.getDoubleMatrix(Tool.changeCode(tempN)), matrix);
		// } else {
		// if (random.nextDouble() > flag) {
		// matrix[0] = 1;
		// matrix[1] = 0;
		// bp.train(Tool.getDoubleMatrix(Tool.changeCode(tempP)), matrix);
		// } else {
		// matrix[0] = 0;
		// matrix[1] = 1;
		// bp.train(Tool.getDoubleMatrix(Tool.changeCode(tempN)), matrix);
		// }
		// }
		// }
		Tool.savePositive(Tool.getBPPositiveFile(), bp);
		// Tool.getBP().printIptHidWeight();
		// Tool.getBP().printHidOptWeight();
		// rafN.close();
		rafP.close();
		// rafN = null;
		rafP = null;
		double[] result = Tool.getPositiveBP().test(Tool.getDoubleMatrix(Tool.changeCode("开心")));
		for (int i = 0; i < result.length; i++) {
			Tool.getPrintWriter().println(result[i]);
		}
	}
}