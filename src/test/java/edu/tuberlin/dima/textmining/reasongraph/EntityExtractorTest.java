package edu.tuberlin.dima.textmining.reasongraph;

import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Test;


import edu.tuberlin.dima.nlp.domain.DependencyParse;
import edu.tuberlin.dima.textmining.parser.ClearNLPParser;


/**
 * Unit test for simple App.
 */
public class EntityExtractorTest {

	private static EntityExtractor extractor;
	private static ClearNLPParser parserClear;

	@BeforeClass
	public static void initExtractor() throws IOException{
		extractor = new EntityExtractor();
		parserClear = new ClearNLPParser();
	}
	
	@Test
	public void parsingExample() throws Exception {

		String sentence = "The tree was green and the house was tall.";
		sentence = "When the sun is obscured by clouds.";
		sentence = "The politician lost the election.";
		

		DependencyParse clearParse = parserClear.parse(sentence);

		// System.out.println("\n" +
		// "\n" +
		// "\nCLEARNLP parse (json): " + clearParse.toJson());
		
		String ngramLine = extractor.convertParseToSynNgram(clearParse);

		extractor.sentenceToSynNgrams(ngramLine);
	}

}
