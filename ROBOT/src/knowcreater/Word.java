package knowcreater;

public class Word {
	/**
	 * This class is to store the element
	 * 
	 * @author 齐软赛亲爱滴小伙伴们
	 */
	private String word = "";
	private int start, end;

	public Word(String word, int start, int end) {
		this.word = word;
		this.start = start;
		this.end = end;
	}

	public String getWord() {
		return word;
	}

	public int getStart() {
		return start;
	}

	public int getEnd() {
		return end;
	}
}
