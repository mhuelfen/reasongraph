package edu.tuberlin.dima.textmining.copa;
/**
 * Class to represent a Copa question.
 * 
 * @author Michael Huelfenhaus
 *
 */
public class CopaParseQuestion {

	private int questionNr;

	private int rightAlternative;

	private CopaParseSent premise;

	private CopaParseSent alternative1;

	private CopaParseSent alternative2;

	public CopaParseQuestion(int questionNr, int rightAlternative, CopaParseSent premise,
			CopaParseSent alternative1, CopaParseSent alternative2) {
		super();
		this.questionNr = questionNr;
		this.rightAlternative = rightAlternative;
		this.premise = premise;
		this.alternative1 = alternative1;
		this.alternative2 = alternative2;
	}

	public int getQuestionNr() {
		return questionNr;
	}

	public void setQuestionNr(int questionNr) {
		this.questionNr = questionNr;
	}

	public int getRightAlternative() {
		return rightAlternative;
	}

	public void setRightAlternative(int rightAlternative) {
		this.rightAlternative = rightAlternative;
	}

	public CopaParseSent getPremise() {
		return premise;
	}

	public void setPremise(CopaParseSent premise) {
		this.premise = premise;
	}

	public CopaParseSent getAlternative1() {
		return alternative1;
	}

	public void setAlternative1(CopaParseSent alternative1) {
		this.alternative1 = alternative1;
	}

	public CopaParseSent getAlternative2() {
		return alternative2;
	}

	public void setAlternative2(CopaParseSent alternative2) {
		this.alternative2 = alternative2;
	}

	@Override
	public String toString() {
		return "CopaQuestion " + questionNr + ", \trightAlternative="
				+ rightAlternative + ", \tpremise: " + premise + ", \talternative1: "
				+ alternative1 + ", \talternative2: " + alternative2;
	}
	
	

}
