package mirea.lexer;

import java.util.List;

public class Parser {
    private List<Lexeme> lexemes;

    public List<Lexeme> getLexemes() {
        return lexemes;
    }

    public void setLexemes(List<Lexeme> lexemes) {
        this.lexemes = lexemes;
    }

    public void lang() {
        while(!lexemes.isEmpty()) {
            expr();
        }
    }

    private void expr() throws RuntimeException {
        if(lexemes.get(0).getTerminal().getIdentifier() == "VAR") {
            assignExpression();
        }
        else if(lexemes.get(0).getTerminal().getIdentifier() == "IF_KEYWORD") {
            ifExpression();
        }
        else if(lexemes.get(0).getTerminal().getIdentifier() == "WHILE_KEYWORD") {
            whileExpression();
        }
        else if(lexemes.get(0).getTerminal().getIdentifier() == "DO_KEYWORD") {
            doWhileExpression();
        }
        else if(lexemes.isEmpty())
            throw new RuntimeException("Token not found");
    }

    private void assignExpression() {

        //Терминал VAR
        lexemes.remove(0);

        //Терминал ASSIGN
        if(lexemes.get(0).getTerminal().getIdentifier() == "ASSIGN") {
            lexemes.remove(0);
        }
        else {
            throw new RuntimeException("Error assignOperation");
        }

        if(lexemes.get(0).getTerminal().getIdentifier() == "OP") {
            lexemes.remove(0);
        }

        //Нетерминал mathOperation
        mathExpression();

        //Терминал SC
        if(lexemes.get(0).getTerminal().getIdentifier() == "SC") {
            lexemes.remove(0);
        }
        else {
            throw new RuntimeException("Error assignOperation");
        }
    }

    private void mathExpression() {
        if(lexemes.get(0).getTerminal().getIdentifier() == "LBR") {
            mathExprWbr();
        }
        else if(lexemes.get(0).getTerminal().getIdentifier() == "NUMBER" || lexemes.get(0).getTerminal().getIdentifier() == "VAR") {
            value();
        }
        else {
            throw new RuntimeException("Error mathExpression");
        }

        while(lexemes.get(0).getTerminal().getIdentifier() == "OP") {
            //Терминал OP
            lexemes.remove(0);
            if(lexemes.get(0).getTerminal().getIdentifier() == "NUMBER" || lexemes.get(0).getTerminal().getIdentifier() == "VAR" || lexemes.get(0).getTerminal().getIdentifier() == "LBR") {
                mathExpression();
            }
            else {
                throw new RuntimeException("Error mathExpression");
            }
        }

    }

    private void mathExprWbr() {
        //Терминал LBR
        lexemes.remove(0);
        mathExpression();
        if(lexemes.get(0).getTerminal().getIdentifier() == "RBR") {
            lexemes.remove(0);
        }
    }

    private void value() {
        if(lexemes.get(0).getTerminal().getIdentifier() == "NUMBER" || lexemes.get(0).getTerminal().getIdentifier() == "VAR") {
            lexemes.remove(0);
        }
        else {
            throw new RuntimeException("Error value");
        }
    }

    private void ifExpression() {
        ifHead();
        body();
        if(lexemes.size()!=0 && lexemes.get(0).getTerminal().getIdentifier() == "ELSE_KEYWORD") {
            elseHead();
            elseBody();
        }
    }

    private void ifHead() {
        //Терминал IF_KEYWORD
        lexemes.remove(0);
        condition();
    }

    private void condition() {
        if(lexemes.get(0).getTerminal().getIdentifier()=="LBR") {
            lexemes.remove(0);
        }
        else {
            throw new RuntimeException("Operator IF error");
        }
        logicalExpression();
        if(lexemes.get(0).getTerminal().getIdentifier()=="RBR") {
            lexemes.remove(0);
        }
        else {
            throw new RuntimeException("Operator IF error");
        }
    }

    private void logicalExpression() {
        mathExpression();
        while(lexemes.get(0).getTerminal().getIdentifier() == "LOGICAL_OP") {
            lexemes.remove(0);
            value();
        }
    }

    private void body() {
        if(lexemes.get(0).getTerminal().getIdentifier()=="L_S_BR") {
            lexemes.remove(0);
        }
        else {
            throw new RuntimeException("Operator IF error");
        }
        while(!lexemes.isEmpty()) {
            if(lexemes.get(0).getTerminal().getIdentifier()=="R_S_BR") {
                break;
            }
            expr();
        }
        if(lexemes.get(0).getTerminal().getIdentifier()=="R_S_BR") {
            lexemes.remove(0);
        }
        else {
            throw new RuntimeException("Operator IF error");
        }
    }

    private void elseHead() {
        //Терминал ELSE_KEYWORD (проверка терминала есть в вызывающем методе)
        lexemes.remove(0);
    }

    private void elseBody() {
        if(lexemes.get(0).getTerminal().getIdentifier()=="L_S_BR") {
            lexemes.remove(0);
        }
        else {
            throw new RuntimeException("Operator ELSE error");
        }
        while(!lexemes.isEmpty()) {
            expr();
        }
        if(lexemes.get(0).getTerminal().getIdentifier()=="R_S_BR") {
            lexemes.remove(0);
        }
        else {
            throw new RuntimeException("Operator ELSE error");
        }
    }

    private void whileExpression() {
        whileHead();
        body();
    }

    private void whileHead() {
        if(lexemes.get(0).getTerminal().getIdentifier() == "WHILE_KEYWORD") {
            lexemes.remove(0);
        }
        condition();
    }

    private void doWhileExpression() {
        //Терминал DO_KEYWORD
        lexemes.remove(0);
        if(lexemes.get(0).getTerminal().getIdentifier() == "L_S_BR") {
            lexemes.remove(0);
        }
        else {
            throw new RuntimeException("Operator doWhileExpression error");
        }
        while(!lexemes.isEmpty()) {
            expr();
        }
        if(lexemes.get(0).getTerminal().getIdentifier()=="R_S_BR") {
            lexemes.remove(0);
        }
        else {
            throw new RuntimeException("Operator doWhileExpression error");
        }
        whileHead();
        //Терминал SC
        if(lexemes.get(0).getTerminal().getIdentifier() == "SC") {
            lexemes.remove(0);
        }
        else {
            throw new RuntimeException("Operator doWhileExpression error");
        }
    }

}
