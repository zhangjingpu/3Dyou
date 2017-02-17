package knowcreater;

public class RepeatAnalysis {
	private String before = "";
	private int count = 0;

	public boolean isReapeat(String now) {
		String matchString = Tool.dynamicProgram(now, before);
		float nowLength = now.length(), beforeLength = before.length(), matchStringLength;
		matchString = matchString.trim();
		Tool.getPrintWriter().println("matchString : " + matchString);
		if (before.trim().length() == 0 || matchString.trim() == null || matchString.length() == 0) {
			count = 0;
			before = now;
			return false;
		}
		matchStringLength = matchString.length();
		Tool.getPrintWriter().println("Beforelength : " + beforeLength + "\nnowLength : " + nowLength);
		beforeLength = beforeLength > nowLength ? beforeLength : nowLength;
		Tool.getPrintWriter().println("Match defredd : " + matchString.length() / beforeLength);
		if (matchStringLength / beforeLength > 0.8) {
			count++;
			before = now;
			return true;
		}
		count = 0;
		before = now;
		return false;
	}

	public int getCount() {
		return count;
	}
	public static void main(String[] args){
		RepeatAnalysis ra = new RepeatAnalysis();
		String str = Tool.dynamicProgram("fjdsl", " ");
		Tool.getPrintWriter().println(str);
	}
}


