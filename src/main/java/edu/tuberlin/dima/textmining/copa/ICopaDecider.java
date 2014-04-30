package edu.tuberlin.dima.textmining.copa;

/**
 * Interface for classes that decide for one alternative given a COPA question. 
 * 
 * @author Michael Huelfenhaus
 *
 */
public interface ICopaDecider {
	
	/**
	 * Decide for one alternative that fulfill the premise of a given COPA question. 
	 * 
	 * @param copaQuest COPA question to decide
	 * @return numer choosen alternative or 0 if no decision could be made
	 */
	public int decideCopaQuestion(CopaParseQuestion copaQuest);

}
