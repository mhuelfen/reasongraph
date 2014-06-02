package edu.tuberlin.dima.textmining.copa;

import static org.junit.Assert.assertNotNull;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

public class CopaTest {

    private static CopaEntityQuestion copaQuest1;
    private static CopaEntityQuestion copaQuest2;

    
    @BeforeClass
    public static void initExtractor() throws IOException {
        CopaEntitySent premise = new CopaEntitySent("The politician lost the election.",
                new HashSet<String>(Arrays.asList("politician", "election")),
                new HashSet<String>(Arrays.asList("{someone} may lose {___}", "{person} may lose {___}")));
        
        CopaEntitySent alternative1 = new CopaEntitySent("He ran negative campaign ads.",
                new HashSet<String>(Arrays.asList("campaign", "ad")),
                new HashSet<String>(Arrays.asList("{someone} may run {___}", "{people} may run {___}")));
        
        CopaEntitySent alternative2 = new CopaEntitySent("No one voted for him.",
                new HashSet<String>(Arrays.asList("election")),
                new HashSet<String>(Arrays.asList("{___} may vote for {someone}", "{someone} may vote for {___}")));

        
        copaQuest1 = new CopaEntityQuestion(6, 2,premise,alternative1,alternative2);
        copaQuest2 = new CopaEntityQuestion(10, 2,premise,alternative1,alternative2);
        
    }

    /*
     * /*
     *  "6": {
     * "right_alt" = "2",
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
*      "alt1_text": "He ran negative campaign ads.",
*      "alt1_nouns": [
*          "campaign",
*          "ad"
*      ],
*      "alt1_stats": [
*          "{someone} may run {___}",
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
    @Test
    public void copaQuestToJsonTest() throws Exception {
        System.out.println(copaQuest1.toJson().toString(2));
    }
    
    @Test
    public void copaFileToJsonTest() throws Exception {
        CopaProcessor copaProc = new CopaProcessor(); 
        
        // load copa question file
//      assertNotNull("Test url file missing : src/test/resources/copa_questions_dev.tsv", 
//              getClass().getResource("/copa_questions_dev.tsv"));
        List<CopaEntityQuestion> copaQuests = copaProc.readQuestions(getClass().getResource("/copa_questions_dev.tsv").toURI().getPath());
         
        String questsJson = copaProc.copaQuestsToJson(copaQuests);
        
        System.out.println(questsJson);
    }
}
