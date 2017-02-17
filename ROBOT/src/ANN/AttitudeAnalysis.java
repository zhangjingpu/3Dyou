package ANN;

import java.io.UnsupportedEncodingException;

import knowcreater.Rule;
import knowcreater.Tool;

public class AttitudeAnalysis {
	private BP bpPositive, bpNegative;
	private static double sum = 0;

	public AttitudeAnalysis() {
		bpPositive = Tool.getPositiveBP();
		bpNegative = Tool.getNegativeBP();
	}

	private BP getPositive() {
		return bpPositive;
	}

	private BP getNegative() {
		return bpNegative;
	}

	public double query() throws UnsupportedEncodingException {
		String input = "";
		for (int i = 0, length = Rule.getWord().length; i < length; i++) {
			input = Rule.getWord()[i].getWord();
			Tool.getPrintWriter().println(Tool.getResult(getPositive().test(Tool.getDoubleMatrix(Tool.changeCode(input))), true) + "\t is the positive .");
			Tool.getPrintWriter().println(Tool.getResult(getNegative().test(Tool.getDoubleMatrix(Tool.changeCode(input))), false) + "\t is the negative .");
			sum += (Tool.getResult(getPositive().test(Tool.getDoubleMatrix(Tool.changeCode(input))), true)
					- Tool.getResult(getNegative().test(Tool.getDoubleMatrix(Tool.changeCode(input))), false));
		}

		return sum;
	}
}
