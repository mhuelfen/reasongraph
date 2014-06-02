package edu.tuberlin.dima.textmining.copa;

import java.util.List;
import java.util.Set;


public class CopaEntitySent {
    
    /* original text from sentence */
    private String text;
    
    /* noun entities from reason graph */
    private Set<String> nouns;
    /* statement entities from reason graph */
    private Set<String> statements;
    
    public CopaEntitySent(String text, Set<String> nouns, Set<String> statements) {
        super();
        this.text = text;
        this.nouns = nouns;
        this.statements = statements;
    }
    
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Set<String> getNouns() {
        return nouns;
    }

    public void setNouns(Set<String> nouns) {
        this.nouns = nouns;
    }

    public Set<String> getStatements() {
        return statements;
    }

    public void setStatements(Set<String> statements) {
        this.statements = statements;
    }
}
