package ANN;

import java.io.RandomAccessFile;

import knowcreater.Tool;

public class InitBP {
	public void init() {
		double[] testMatrix = new double[] { 2, 3, 4, 5, 6, 8, 2, 4, 5, 62, 2, 3, 4 };
		BP bp = new BP(100, 200, 1);
		double[] input = loadPositiveInput();
		double[] output = new double[] { 0 };
		bp.train(input, output);
		input = loadNegativeOutput();
		output = new double[] { 1 };
		bp.train(input, output);
		bp.train(input, output);
		Tool.getPrintWriter().println("Done");
		Tool.getPrintWriter().println(bp.test(testMatrix)[0]);
	}

	private double[] loadPositiveInput() {
		RandomAccessFile raf;
		try {
			raf = new RandomAccessFile(Tool.getPositive(), "rw");

			raf.close();
		} catch (Exception e)
		{
			e.printStackTrace();
		}

		raf = null;
		return null;
	}

	private double[] loadNegativeOutput() {
		return null;
	}

	public static int query() {
		return 0;
	}

	public static void main(String[] args) {
	}
}