package mirea.lexer;

import java.util.List;

public class Parser {

    private List<Lexeme> lexemes;

    private Integer point = 0;

    public List<Lexeme> getLexemes() {
        return lexemes;
    }

    public void setLexemes(List<Lexeme> lexemes) {
        this.lexemes = lexemes;
    }

    public void lang() {
        expr();
        while(List.of("VAR", "IF_KEYWORD", "WHILE_KEYWORD").contains(currentToken().getTerminal().getIdentifier())) {
            expr();
        }
    }

    private void expr() throws RuntimeException {
        if(currentToken().getTerminal().getIdentifier().equals("VAR")) {
            assignExpression();
        }
        else if(currentToken().getTerminal().getIdentifier().equals("IF_KEYWORD")) {
            ifExpression();
        }
        else if(currentToken().getTerminal().getIdentifier().equals("WHILE_KEYWORD")) {
            whileExpression();
        }
    }

    private void assignExpression() {

        match(List.of("VAR"));
        match(List.of("ASSIGN"));

        mathExpression();

        match(List.of("SC"));

    }

    private void mathExpression() {

        if(List.of("VAR", "NUMBER").contains(currentToken().getTerminal().getIdentifier())) {
            value();
        }
        else if(List.of("LBR").contains(currentToken().getTerminal().getIdentifier())) {
            mathExprWbr();
        }
        while(List.of("OP").contains(currentToken().getTerminal().getIdentifier())) {
            match(List.of("OP"));
            if(List.of("VAR", "NUMBER", "LBR").contains(currentToken().getTerminal().getIdentifier())) {
                mathExpression();
            }
            else {
                throw new RuntimeException("Invalid token");
            }
        }

    }

    private void mathExprWbr() {

        match(List.of("LBR"));
        mathExpression();
        match(List.of("RBR"));
    }

    private void value() {
        match(List.of("NUMBER", "VAR"));
    }

    private void ifExpression() {
        ifHead();
        body();
        if(lexemes.size()<point && List.of("ELSE_KEYWORD").contains(currentToken().getTerminal().getIdentifier())) {
            elseHead();
            body();
        }
    }

    private void ifHead() {
        match(List.of("IF_KEYWORD"));
        condition();
    }

    private void condition() {
        match(List.of("LBR"));
        logicalExpression();
        match(List.of("RBR"));
    }

    private void logicalExpression() {
        value();
        while(List.of("LOGICAL_OP").contains(currentToken().getTerminal().getIdentifier())) {
            match(List.of("LOGICAL_OP"));
            value();
        }
    }

    private void body() {
        match(List.of("L_S_BR"));
        while(List.of("VAR", "IF_KEYWORD", "WHILE_KEYWORD").contains(currentToken().getTerminal().getIdentifier())) {
            expr();
        }
        match(List.of("R_S_BR"));
    }

    private void elseHead() {
        match(List.of("ELSE_KEYWORD"));
    }

    private void whileExpression() {
        whileHead();
        body();
    }

    private void whileHead() {
        match(List.of("WHILE_KEYWORD"));
        condition();
    }

    private Lexeme currentToken() {
        if(point>=lexemes.size()) {
            point = lexemes.size()-1;
        }
        return lexemes.get(point);
    }

    private void match(List<String> terminal) {
        if(terminal.contains(currentToken().getTerminal().getIdentifier())) {
            point++;
        }
        else {
            throw new RuntimeException("Invalid token");
        }
    }

}
