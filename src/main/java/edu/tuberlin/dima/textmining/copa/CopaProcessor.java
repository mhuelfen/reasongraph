package edu.tuberlin.dima.textmining.copa;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;

import org.json.JSONObject;

import com.google.common.collect.*;

import edu.tuberlin.dima.textmining.reasongraph.EntityExtractor;

//import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
//import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
//import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
//import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
//import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
//import edu.stanford.nlp.ling.CoreLabel;
//import edu.stanford.nlp.pipeline.Annotation;
//import edu.stanford.nlp.pipeline.StanfordCoreNLP;
//import edu.stanford.nlp.util.CoreMap;
//import edu.tuberlin.dima.textmining.conceptnet.ConceptNetApi;
//import edu.tuberlin.dima.textmining.conceptnet.ConceptNetData;
//import edu.tuberlin.dima.textmining.conceptnet.ConceptNetStore;
//import edu.tuberlin.dima.textmining.conceptnet.IConceptNetStore;
//import edu.tuberlin.dima.textmining.verbarg.IVerbargStore;
//import edu.tuberlin.dima.textmining.verbarg.TSVReader;
//import edu.tuberlin.dima.textmining.verbarg.VerbargData;
//import edu.tuberlin.dima.textmining.verbarg.VerbargStore;
//import edu.tuberlin.dima.textmining.verbarg.helper.MapSorter;
//import edu.tuberlin.dima.textmining.verbarg.helper.PosStart;
//import edu.tuberlin.dima.textmining.verbarg.helper.SimilarityDatabase;

/**
 * Class to process SemEval 2013 Task #7 COPA: Choice Of Plausible Alternatives
 * 
 * Task: Decide for the best alternative given a premise.
 * 
 * Example: Premise: The man broke his toe. What was the CAUSE of this?
 * Alternative 1: He got a hole in his sock. Alternative 2: He dropped a hammer
 * on his foot.
 * 
 * 
 * @author Michael Huelfenhaus
 * 
 */
public class CopaProcessor {

    /** Source to answer questions */
    public enum KNOWLEDGE_SOURCE {
        CONCEPTNET_API, CONCEPTNET_DB, VERBARGS, COMBI
    }

    private static EntityExtractor extractor;

    /** source of knowledge to decide question */
    private KNOWLEDGE_SOURCE knowledgeSource;

    /** decider class that choose one alternative */
    private ICopaDecider copaDecide;

    /**
     * Default Constructor
     * 
     * @throws IOException
     * 
     */
    public CopaProcessor() throws IOException {
        super();
        extractor = new EntityExtractor();

    }

    /**
     * Initialize Processor by initializing the annotation pipeline and loading
     * the a knowledge source..
     * 
     * @param nounNounCosFile
     *            path to file with noun - noun cosine similarities
     * @param statmentNounPmiFile
     *            path to file with verb - noun PMIs
     * @param knowledgeSource
     *            source of knowledge to decide question
     * @throws IOException
     */
    public CopaProcessor(String nounNounCosFile, String statmentNounPmiFile,
            KNOWLEDGE_SOURCE knowledgeSource) throws IOException {

        extractor = new EntityExtractor();

        // define steps of annotation
        Properties props = new Properties();
        props.put("annotators", "tokenize, ssplit, pos, lemma");

        this.knowledgeSource = knowledgeSource;

        // // get decider for choosen resource
        // if (knowledgeSource == KNOWLEDGE_SOURCE.CONCEPTNET_API) {
        // copaDecide = new CnApiDecider();
        // } else if (knowledgeSource == KNOWLEDGE_SOURCE.CONCEPTNET_DB) {
        // copaDecide = new CnDbDedicer();
        // } else if (knowledgeSource == KNOWLEDGE_SOURCE.VERBARGS) {
        // copaDecide = new WeltwissenDecider();
        // }else if (knowledgeSource == KNOWLEDGE_SOURCE.COMBI) {
        // copaDecide = new CombiDecider();
        // }

    }

    /**
     * Read Copa Question from tsv File.
     * 
     * Format: question number, number of right Alternative, premise,
     * alternative1, alternative2
     * 
     * @param questionFile
     *            path to copa question in tsv format
     * @return List of copa questions from file
     * @throws Exception 
     * @throws NumberFormatException 
     */
    public List<CopaEntityQuestion> readQuestions(String questionFile) throws NumberFormatException, Exception {

        List<CopaEntityQuestion> copaQuests = new ArrayList<CopaEntityQuestion>();
        Scanner scanner;
        try {
            scanner = new Scanner(new File(questionFile));
            scanner.useDelimiter("\t");

            String line = null;
            CopaEntityQuestion copaQuest;
            while (scanner.hasNextLine()) {
                line = scanner.nextLine();
                if (!line.isEmpty()) {

                    String[] fields = line.split("\t");

                    // create copa questions by parsing texts
                    copaQuest = new CopaEntityQuestion(
                            Integer.parseInt(fields[0]),
                            Integer.parseInt(fields[1]),
                            extractor.extractEntities(fields[2]),
                            extractor.extractEntities(fields[3]),
                            extractor.extractEntities(fields[4]));
                    copaQuests.add(copaQuest);
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return copaQuests;
    }
    
    public String copaQuestsToJson (List<CopaEntityQuestion> copaQuests) {
        JSONObject copaJson = new JSONObject();  
        for (CopaEntityQuestion copaQuest : copaQuests){
            copaJson.put(Integer.toString(copaQuest.getQuestionNr()), copaQuest.toJson());
        }
        return copaJson.toString(2);        
    }

//  /**
//   * Find reason graph entity with extractor
//   * 
//   * @param text
//   *            text to annotate
//   * @return copa sentence with extracted words by type
//   */
//  public CopaEntitySent analyseSentence(String text) {
//
//      List<CoreMap> parsedSent = annotateText(text);
//
//      CopaSent copaSent = new CopaSent(text);
//
//      copaSent.setNouns(findWordByPos(parsedSent, PosStart.NOUN));
//      // should only be one
//      copaSent.setVerbs(findWordByPos(parsedSent, PosStart.VERB));
//
//      copaSent.setAdjectives(findWordByPos(parsedSent, PosStart.ADJ));
//      copaSent.setAdverbs(findWordByPos(parsedSent, PosStart.ADV));
//
//      copaSent.setLemmas(sentenceLemmas(parsedSent));
//      // System.out.println("lemmas: " + copaSent.getLemmas());
//      return copaSent;
//  }

    // /**
    // * Read Copa Question from tsv File.
    // *
    // * Format: question number, number of right Alternative, premise,
    // * alternative1, alternative2
    // *
    // * @param questionFile
    // * path to copa question in tsv format
    // * @return List of copa questions from file
    // */
    // public List<CopaQuestion> readQuestions(String questionFile) {
    //
    // List<CopaQuestion> copaQuests = new ArrayList<CopaQuestion>();
    // Scanner scanner;
    // try {
    // scanner = new Scanner(new File(questionFile));
    // scanner.useDelimiter("\t");
    //
    // String line = null;
    // CopaQuestion copaQuest;
    // while (scanner.hasNextLine()) {
    // line = scanner.nextLine();
    // if (!line.isEmpty()) {
    //
    // String[] fields = line.split("\t");
    //
    // // create copa questions by parsing texts
    // copaQuest = new CopaQuestion(Integer.parseInt(fields[0]),
    // Integer.parseInt(fields[1]),
    // analyseSentence(fields[2]),
    // analyseSentence(fields[3]),
    // analyseSentence(fields[4]));
    // copaQuests.add(copaQuest);
    // }
    // }
    // scanner.close();
    // } catch (FileNotFoundException e) {
    // // TODO Auto-generated catch block
    // e.printStackTrace();
    // }
    // return copaQuests;
    // }

    // /**
    // * Annotate text with Stanford Pipline and extract different types of
    // words.
    // *
    // * @param text
    // * text to annotate
    // * @return copa sentence with extracted words by type
    // */
    // public CopaSent analyseSentence(String text) {
    //
    // List<CoreMap> parsedSent = annotateText(text);
    //
    // CopaSent copaSent = new CopaSent(text);
    //
    // copaSent.setNouns(findWordByPos(parsedSent, PosStart.NOUN));
    // // should only be one
    // copaSent.setVerbs(findWordByPos(parsedSent, PosStart.VERB));
    //
    // copaSent.setAdjectives(findWordByPos(parsedSent, PosStart.ADJ));
    // copaSent.setAdverbs(findWordByPos(parsedSent, PosStart.ADV));
    //
    // copaSent.setLemmas(sentenceLemmas(parsedSent));
    // //System.out.println("lemmas: " + copaSent.getLemmas());
    // return copaSent;
    // }
    //
    // /**
    // * Annotate a text with the Stanford Core NLP Pipeline. Pipeline consists
    // of
    // * Tokenizer, Sentence Splitter, Part-of-speech Tagger and Lemmatizer.
    // *
    // * @param text
    // * Text that should be annotated.
    // * @return List of annotated sentence. null if errors occur
    // */
    // public List<CoreMap> annotateText(String text) {
    //
    // // create an empty Annotation just with the given text
    // Annotation document = new Annotation(text);
    //
    // try {
    // // run all Annotators on this text
    // pipeline.annotate(document);
    //
    // // these are all the sentences in this document
    // // a CoreMap is essentially a Map that uses class objects as keys
    // // and has values with custom types
    // List<CoreMap> sentences = document.get(SentencesAnnotation.class);
    //
    // return sentences;
    //
    // } catch (Exception e) {
    // // TODO Auto-generated catch block
    // e.printStackTrace();
    // } catch (Error e) {
    // // TODO Auto-generated catch block
    // e.printStackTrace();
    // }
    // return null;
    // }
    //
    // /**
    // * Return all words with the given Part-of-speech class.
    // *
    // * @param sentences
    // * annotated sentence
    // * @param posStart
    // * Part-of-Speech class by first letter
    // * @return set of words for given class
    // */
    // public Set<String> findWordByPos(List<CoreMap> sentences, PosStart
    // posStart) {
    //
    // // String word = "";
    // String pos = "";
    // Set<String> nouns = new HashSet<String>();
    //
    // for (CoreMap sentence : sentences) {
    // // traversing the words in the current sentence
    // // a CoreLabel is a CoreMap with additional token-specific methods
    // for (CoreLabel token : sentence.get(TokensAnnotation.class)) {
    // // // this is the text of the token
    // // word = token.get(TextAnnotation.class);
    // // this is the POS tag of the token
    // pos = token.get(PartOfSpeechAnnotation.class);
    //
    // String lemma = token.get(LemmaAnnotation.class);
    // if (pos.startsWith(posStart.posStart())) {
    // nouns.add(lemma);
    // }
    // // System.out.println(word + " " + pos);
    // }
    // }
    // return nouns;
    // }
    //
    //
    // /**
    // * Get all lemmas for words in the sentence.
    // *
    // * @param sentences
    // * annotated sentence
    // * @return list of lemmas
    // */
    // public List<String> sentenceLemmas(List<CoreMap> sentences){
    //
    // List<String> lemmas = new ArrayList<String>();
    //
    // for (CoreMap sentence : sentences) {
    // // traversing the words in the current sentence
    // // a CoreLabel is a CoreMap with additional token-specific methods
    // for (CoreLabel token : sentence.get(TokensAnnotation.class)) {
    //
    // String lemma = token.get(LemmaAnnotation.class);
    // lemmas.add(lemma);
    // }
    // }
    // return lemmas;
    // }

//  /**
//   * Decide all copa question from tsv file and print results.
//   * 
//   * First result show right, wrong, decided questions and relation of
//   * right/wrong without undecided. Second results uses 2 as default answer
//   * for undecided questions.
//   * 
//   * @param path
//   *            path to tsv file with copa questions
//   */
//  public void evalCopaFile(String path) {
//      evalCopaFile(path, 1, -1);
//  }

    /**
     * Decide copa question from tsv file and print results.
     * 
     * First result show right, wrong, decided questions and relation of
     * right/wrong without undecided. Second results uses 2 as default answer
     * for undecided questions.
     * 
     * @param path
     *            path to tsv file with copa questions
     * @param questionsStartInd
     *            -1 tells use all
     * @param questionsEndInd
     * @throws Exception 
     * @throws NumberFormatException 
     */
    public void evalCopaFile(String path, int questionsStartInd,
            int questionsEndInd) throws NumberFormatException, Exception {
        int right = 0;
        int wrong = 0;
        int right2 = 0;
        int wrong2 = 0;

        int undecided = 0;
        List<CopaEntityQuestion> allCopaQuests = readQuestions(path);

        if (questionsEndInd == -1) {
            questionsEndInd = allCopaQuests.size();
        }
        List<CopaEntityQuestion> copaQuests = allCopaQuests.subList(
                questionsStartInd - 1, questionsEndInd);

        int choosenAlternative = 0;
//
//      for (CopaEntityQuestion copaQuest : copaQuests) {
//          System.out.println(copaQuest);
//
//          choosenAlternative = 0;
//
//          // get best alternative from decider
//          choosenAlternative = copaDecide.decideCopaQuestion(copaQuest);
//
//          // 0 show both altern. got same scores
//          if (choosenAlternative == 0) {
//              undecided++;
//              // use 2 as default choice and calc secondary results
//              if (copaQuest.getRightAlternative() == 2) {
//                  right2++;
//              } else {
//                  wrong2++;
//              }
//          } else if (choosenAlternative == copaQuest.getRightAlternative()) {
//              right++;
//          } else {
//              wrong++;
//          }
//          System.out.println("Decision: " + choosenAlternative + "\n");
//
//      }
//      double result = right / (0.0 + right + wrong);
//      double result2 = (right + right2)
//              / (0.0 + right + right2 + wrong + wrong2);
//      System.out.println("right: " + right + "  wrong: " + wrong
//              + " undecided: " + undecided);
//      System.out.println("result: " + result);
//      System.out.println("result with defalut decision 2 for undecided: "
//              + result2);

    }

}
