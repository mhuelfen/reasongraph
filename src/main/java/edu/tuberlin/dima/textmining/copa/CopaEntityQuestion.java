package edu.tuberlin.dima.textmining.copa;

import org.json.JSONObject;

public class CopaEntityQuestion {

	private int questionNr;

	private int rightAlternative;

	private CopaEntitySent premise;

	private CopaEntitySent alternative1;

	private CopaEntitySent alternative2;

	public int getQuestionNr() {
		return questionNr;
	}

	public CopaEntityQuestion(int questionNr, int rightAlternative,
			CopaEntitySent premise, CopaEntitySent alternative1,
			CopaEntitySent alternative2) {
		super();
		this.questionNr = questionNr;
		this.rightAlternative = rightAlternative;
		this.premise = premise;
		this.alternative1 = alternative1;
		this.alternative2 = alternative2;
	}
	/*
	 *  "6": {
*      "type": "cause",
*      "premise_text": "The politician lost the election.",
*      "premise_nouns": [
*          "politician",
*          "election"
*      ],
*      "premise_stats": [
*          "{someone} may lose {___}",
*          "{person} may lose {___}",
*          "{people} may lose {___}"
*      ],
*      "alt1_text": " He ran negative campaign ads.",
*      "alt1_text": [
*          "campaign",
*          "ad"
*      ],
*      "alt1_stats": [
*          "{{someone} may run {___}",
*          "{person} may run {___}",
*          "{people} may run {___}"
*      ],
*      "alt2_text": "No one voted for him.",
*      "alt2_nouns": [
*          "election"
*      ],
*      "alt2_stats": [
*          "{___} may vote for {someone}",
*          "{someone} may vote for {___}"
*      ]
	*  }
	 */
	public JSONObject toJson(){
		JSONObject questJson = new JSONObject();  
		questJson.put("type", "");
		questJson.put("premise_text", this.premise.getText());
		questJson.put("premise_nouns", this.premise.getNouns());
		questJson.put("premise_stats", this.premise.getStatements());
		
		questJson.put("alt1_text", this.alternative1.getText());
		questJson.put("alt1_nouns", this.alternative1.getNouns());
		questJson.put("alt1_stats", this.alternative1.getStatements());
		
		questJson.put("alt2_text", this.alternative2.getText());
		questJson.put("alt2_nouns", this.alternative2.getNouns());
		questJson.put("alt2_stats", this.alternative2.getStatements());
		
//		JSONObject copaQuestJson = new JSONObject();
//		copaQuestJson.put(Integer.toString(this.questionNr),questJson);
		
		return questJson;
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

	public CopaEntitySent getPremise() {
		return premise;
	}

	public void setPremise(CopaEntitySent premise) {
		this.premise = premise;
	}

	public CopaEntitySent getAlternative1() {
		return alternative1;
	}

	public void setAlternative1(CopaEntitySent alternative1) {
		this.alternative1 = alternative1;
	}

	public CopaEntitySent getAlternative2() {
		return alternative2;
	}

	public void setAlternative2(CopaEntitySent alternative2) {
		this.alternative2 = alternative2;
	}
}
