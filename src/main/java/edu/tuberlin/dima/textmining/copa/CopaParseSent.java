package edu.tuberlin.dima.textmining.copa;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
/**
 * Class to represent a sentence with sets of lemmas by word type.
 * 
 * @author Michael Huelfenhaus
 *
 */
public class CopaParseSent {

	private String text;

	private Set<String> nouns;

	private Set<String> verbs;
	
	private Set<String> adjectives;
	
	private Set<String> adverbs;
	
	private List<String> lemmas;
	



	public CopaParseSent(String text) {
		super();
		this.text = text;
	}

	public String getText() {
		return text;
	}
	/**
	 * Get all lemmas from this sentence
	 * @return all lemmas
	 */
	public Set<String> getAllLemmas() {
		HashSet<String> lemmas = new HashSet<String>();
		lemmas.addAll(nouns);
		lemmas.addAll(verbs);
		lemmas.addAll(adjectives);
		lemmas.addAll(adverbs);
		return lemmas;
	}

	public void setText(String text) {
		this.text = text;
	}
	

	public Set<String> getNouns() {
		return  new HashSet<String>(nouns);
	}

	public void setNouns(Set<String> nouns) {
		this.nouns = nouns;
	}

	public Set<String> getVerbs() {
		return  new HashSet<String>(verbs);
	}

	public void setVerbs(Set<String> verbs) {
		this.verbs = verbs;
	}

	public Set<String> getAdjectives() {
		return adjectives;
	}

	public void setAdjectives(Set<String> adjectives) {
		this.adjectives = adjectives;
	}

	public Set<String> getAdverbs() {
		return adverbs;
	}

	public void setAdverbs(Set<String> adverbs) {
		this.adverbs = adverbs;
	}


	public List<String> getLemmas() {
		return lemmas;
	}

	public void setLemmas(List<String> lemmas) {
		this.lemmas = lemmas;
	}
	
	@Override
	public String toString() {
		return text;
	}

}
