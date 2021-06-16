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

    public AstNode lang() {
        AstNode astNode = new AstNode("lang");
        astNode.addChild(expr());
        while(currentToken()!=null && List.of("VAR", "IF_KEYWORD", "WHILE_KEYWORD").contains(currentToken().getTerminal().getIdentifier())) {
            astNode.addChild(expr());
        }
        if(currentToken()!=null && !List.of("VAR", "IF_KEYWORD", "WHILE_KEYWORD").contains(currentToken().getTerminal().getIdentifier())) {
            throw new RuntimeException("Invalid token");
        }
        return astNode;
    }

    private AstNode expr() throws RuntimeException {
        AstNode astNode = new AstNode("expr");
        if(currentToken().getTerminal().getIdentifier().equals("VAR")) {
            astNode.addChild(assignExpression());
        }
        else if(currentToken().getTerminal().getIdentifier().equals("IF_KEYWORD")) {
            astNode.addChild(ifExpression());
        }
        else if(currentToken().getTerminal().getIdentifier().equals("WHILE_KEYWORD")) {
            astNode.addChild(whileExpression());
        }
        return astNode;
    }

    private AstNode assignExpression() {
        AstNode astNode = new AstNode("assignExpression");
        match(List.of("VAR"), astNode);
        match(List.of("ASSIGN"), astNode);

        astNode.addChild(mathExpression());

        match(List.of("SC"), astNode);

        return astNode;
    }

    private AstNode mathExpression() {
        AstNode astNode = new AstNode("mathExpression");
        if(List.of("VAR", "NUMBER").contains(currentToken().getTerminal().getIdentifier())) {
            astNode.addChild(value());
        }
        else if(List.of("LBR").contains(currentToken().getTerminal().getIdentifier())) {
            astNode.addChild(mathExprWbr());
        }
        while(List.of("OP").contains(currentToken().getTerminal().getIdentifier())) {
            match(List.of("OP"), astNode);
            if(List.of("VAR", "NUMBER", "LBR").contains(currentToken().getTerminal().getIdentifier())) {
                astNode.addChild(mathExpression());
            }
            else {
                throw new RuntimeException("Invalid token");
            }
        }
        return astNode;
    }

    private AstNode mathExprWbr() {
        AstNode astNode = new AstNode("mathExprWbr");
        match(List.of("LBR"), astNode);
        astNode.addChild(mathExpression());
        match(List.of("RBR"), astNode);

        return astNode;
    }

    private AstNode value() {
        AstNode astNode = new AstNode("value");
        match(List.of("NUMBER", "VAR"), astNode);
        return astNode;
    }

    private AstNode ifExpression() {
        AstNode astNode = new AstNode("ifExpression");
        astNode.addChild(ifHead());
        astNode.addChild(body());
        if(lexemes.size()<point && List.of("ELSE_KEYWORD").contains(currentToken().getTerminal().getIdentifier())) {
            astNode.addChild(elseHead());
            astNode.addChild(body());
        }
        return astNode;
    }

    private AstNode ifHead() {
        AstNode astNode = new AstNode("ifHead");
        match(List.of("IF_KEYWORD"), astNode);
        astNode.addChild(condition());
        return astNode;
    }

    private AstNode condition() {
        AstNode astNode = new AstNode("condition");
        match(List.of("LBR"), astNode);
        astNode.addChild(logicalExpression());
        match(List.of("RBR"), astNode);
        return astNode;
    }

    private AstNode logicalExpression() {
        AstNode astNode = new AstNode("logicalExpression");
        astNode.addChild(value());
        while(List.of("LOGICAL_OP").contains(currentToken().getTerminal().getIdentifier())) {
            match(List.of("LOGICAL_OP"), astNode);
            astNode.addChild(value());
        }
        return astNode;
    }

    private AstNode body() {
        AstNode astNode = new AstNode("body");
        match(List.of("L_S_BR"), astNode);
        while(List.of("VAR", "IF_KEYWORD", "WHILE_KEYWORD").contains(currentToken().getTerminal().getIdentifier())) {
            astNode.addChild(expr());
        }
        match(List.of("R_S_BR"), astNode);
        return astNode;
    }

    private AstNode elseHead() {
        AstNode astNode = new AstNode("elseHead");
        match(List.of("ELSE_KEYWORD"), astNode);
        return astNode;
    }

    private AstNode whileExpression() {
        AstNode astNode = new AstNode("whileExpression");
        astNode.addChild(whileHead());
        astNode.addChild(body());
        return astNode;
    }

    private AstNode whileHead() {
        AstNode astNode = new AstNode("whileHead");
        match(List.of("WHILE_KEYWORD"), astNode);
        astNode.addChild(condition());
        return astNode;
    }

    private Lexeme currentToken() {
        if(point>=lexemes.size()) {
            return null;
        }
        return lexemes.get(point);
    }

    private void match(List<String> terminal, AstNode astNode) {
        if(terminal.contains(currentToken().getTerminal().getIdentifier())) {
            //astNode.addChild(new AstNode(currentToken().getTerminal().getIdentifier()));
            astNode.addChild(currentToken());
            point++;
        }
        else {
            throw new RuntimeException("Invalid token");
        }
    }

}
