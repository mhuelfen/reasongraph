package edu.tuberlin.dima.textmining.reasongraph;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.tuberlin.dima.ngram.SyntacticVerbGram;
import de.tuberlin.dima.ngram.SyntacticVerbGram.ConceptStatement;

import edu.tuberlin.dima.nlp.domain.DepLink;
import edu.tuberlin.dima.nlp.domain.DepWord;
import edu.tuberlin.dima.nlp.domain.DependencyParse;
import edu.tuberlin.dima.textmining.copa.CopaEntitySent;
import edu.tuberlin.dima.textmining.parser.ClearNLPParser;

public class EntityExtractor {

	private static ClearNLPParser parserClear;

	/*
	 * Init Extractor and load Clear Parser
	 */
	public EntityExtractor() throws IOException {
		super();
		parserClear = new ClearNLPParser();

	}

	public String convertParseToSynNgram(DependencyParse parse) {

		System.out.println("sentence = " + parse.getSentence());

		String synNgram = "";
		String mainVerb = "";
		for (DepWord word : parse.getWords()) {
			// ignore punctuation
			if (!word.getPosTag().equals(".")) {

				System.out.println(word + " || " + word.getUpLinkLabels()
						+ "|| " + word.getDownLinkLabels());
				// add word, POS to ngramline
				synNgram += word.getWordName() + "/" + word.getPosTag() + "/";
				List<DepLink> inlinks = word.getIncomingLinks();

				// check for root
				if (!inlinks.isEmpty()) {

					// add link label and parent id to ngramline
					synNgram += word.getUpLinkLabels().replaceAll("\\s+", "")
							+ "/"
							+ word.getIncomingLinks().get(0).getOriginWord()
									.getWordPosId() + " ";
				} else {
					synNgram += "root/0 ";

				}
			}
			// System.out.println(synNgram);
			// find main verb
			if (word.getPosTag().startsWith("VB")) {
				mainVerb = word.getWordName();
			}
			// "obscured\twhen/WRB/compl/5 the/DT/det/3 sun/NN/nsubjpass/5 is/VBZ/aux/5 obscured/VBN/ccomp/0 by/IN/prep/5 cloud/NN/pobj/6"
			System.out.println();
		}

		// add main verb to syn ngram
		synNgram = mainVerb + "\t" + synNgram;
		System.out.println();
		System.out.println("SN :" + synNgram);		
		return synNgram;
	}

	public SyntacticVerbGram sentenceToSynNgrams(String ngramLine) throws Exception {

		// ngramLine =
		// "obscured\twhen/WRB/advmod/5 the/DT/det/3 sun/NN/nsubjpass/5 is/VBZ/auxpass/5 obscured/VBN/root/0 by/IN/agent/5 cloud/NNS/pobj/6";

		// from alans code
		// ngramLine =
		// "obscured\twhen/WRB/compl/5 the/DT/det/3 sun/NN/nsubjpass/5 is/VBZ/aux/5 obscured/VBN/ccomp/0 by/IN/prep/5 cloud/NN/pobj/6";
		SyntacticVerbGram syntacticVerbGram;
		syntacticVerbGram = new SyntacticVerbGram(ngramLine);
		printPatternInfo(ngramLine,syntacticVerbGram);
		return syntacticVerbGram;
	}
	
	

	/*
	 * 
	 */
	private void printPatternInfo(String ngramLine,SyntacticVerbGram syntacticVerbGram) throws Exception {
//		SyntacticVerbGram syntacticVerbGram;
//		syntacticVerbGram = new SyntacticVerbGram(ngramLine);
		System.out.println("\n"
				+ ngramLine.replaceAll("\\b"
						+ "([^/ ]+?)/[^/ ]+?/[^/ ]+?/[^/ ]+", "$1"));
		if (syntacticVerbGram.isRisky()) {
			System.out.println("is risky");
		} else {
			System.out.println("r: " + syntacticVerbGram.rebuildNgram());
			System.out.println("b: " + syntacticVerbGram.toStatement());
			List<SyntacticVerbGram.ConceptStatement> conceptStatements = syntacticVerbGram
					.generatePatterns2(false);
			int i = 1;
			for (SyntacticVerbGram.ConceptStatement pattern : conceptStatements) {
				System.out.println("p" + i++ + ": " + pattern);
			}
		}
	}
	
	/* 
	 * r: politician/NN/nsubj/3 may lose election/NN/dobj/3
     * b: politician/NN/nsubj/3 may lose election/NN/dobj/3
	 * p1: ConceptStatement{verb='lose', statement='{politician} may lose {___}', signature='{___} may lose {___}', noun='election', complement='null', concepts=[politician, election]}
	 * p2: ConceptStatement{verb='lose', statement='{___} may lose {election}', signature='{___} may lose {___}', noun='politician', complement='null', concepts=[politician, election]}
	 */
	public CopaEntitySent synVerbGramToCopaSent(SyntacticVerbGram syntacticVerbGram) {
		
		String text = "";
		Set<String> concepts = new HashSet<String>();
		Set<String> statements = new HashSet<String>();
		Boolean verbal = false;
		for (ConceptStatement pattern: syntacticVerbGram.generatePatterns2(verbal)){
			System.out.println(pattern);
			concepts.addAll(pattern.getConcepts());
			statements.add(pattern.getStatement());			
		}
		CopaEntitySent copaSent = new CopaEntitySent(text, concepts,statements);

		return copaSent;
		
	}
	
	public CopaEntitySent extractEntities(String sentence) throws Exception {
		DependencyParse clearParse = parserClear.parse(sentence);
		
		String ngramLine = this.convertParseToSynNgram(clearParse);

		SyntacticVerbGram synNgram = sentenceToSynNgrams(ngramLine);
		
		CopaEntitySent copaSent = synVerbGramToCopaSent(synNgram);
		
		copaSent.setText(sentence);
		return copaSent;
	}
	
}
