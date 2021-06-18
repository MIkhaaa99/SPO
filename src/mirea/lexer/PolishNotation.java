package mirea.lexer;

import java.util.*;

public class PolishNotation {

    private Stack<Lexeme> stringStack = new Stack<>();
    private List<String> polishNotation = new ArrayList<>();

    public List<String> getPolishNotation(AstNode node) {
        convertToPN(node);
        return polishNotation;
    }

    private void transferFromStackToPolishNotation() {
        while (stringStack.size()!=0) {
            polishNotation.add(stringStack.pop().getValue());
        }
    }

    private void convertToPN(AstNode node) {
        Iterator<AstNode> childs = node.getChilds();
        boolean isWhile = false;
        boolean isElse = false;
        if(node.getName().equals("whileBody")) {
            isWhile = true;
        }
        if(node.getName().equals("elseBody")) {
            isElse = true;
        }
        Integer endPointForWhile = 0;
        while(childs.hasNext()) {
            AstNode n = childs.next();
            if(n.getLexeme()!=null) {
                if(List.of("VAR", "NUMBER").contains(n.getLexeme().getTerminal().getIdentifier())) {
                    polishNotation.add(n.getLexeme().getValue());
                }
                else if(List.of("OP", "LOGICAL_OP", "ASSIGN").contains(n.getLexeme().getTerminal().getIdentifier())) {
                    int priorityForCurrentToken = getPriority(n.getLexeme());
                    while(stringStack.size()!=0 && getPriority(stringStack.get(stringStack.size()-1))>=priorityForCurrentToken) {
                        polishNotation.add(stringStack.pop().getValue());
                    }
                    stringStack.add(n.getLexeme());
                }
                else if(List.of("LBR").contains(n.getLexeme().getTerminal().getIdentifier())) {
                    stringStack.add(n.getLexeme());
                }
                else if(List.of("RBR").contains(n.getLexeme().getTerminal().getIdentifier())) {
                    while(!stringStack.get(stringStack.size()-1).getTerminal().getIdentifier().equals("LBR")) {
                        polishNotation.add(stringStack.pop().getValue());
                    }
                    stringStack.pop();
                }
                else if(List.of("ELSE_KEYWORD").contains(n.getLexeme().getTerminal().getIdentifier())) {
                    polishNotation.remove(polishNotation.size()-1);
                }
                else if(List.of("L_S_BR").contains(n.getLexeme().getTerminal().getIdentifier())) {
                    endPointForWhile = polishNotation.size()-3;
                    if(isElse==false) {
                        polishNotation.add("_");
                        polishNotation.add("!F");
                    }
                    else {
                        polishNotation.add("unconditionalJump");
                        polishNotation.add("!");
                        polishNotation.set(polishNotation.lastIndexOf("!F")-1, String.valueOf(polishNotation.size()));
                    }
                }
                else if(List.of("R_S_BR").contains(n.getLexeme().getTerminal().getIdentifier())) {
                    if(isWhile==true) {
                        polishNotation.add(String.valueOf(endPointForWhile));
                    }
                    polishNotation.add("!T");
                    if(isElse==true) {
                        polishNotation.set(polishNotation.lastIndexOf("unconditionalJump"), String.valueOf(polishNotation.size()-1));
                        //polishNotation.set(polishNotation.lastIndexOf("!F")-1, String.valueOf(polishNotation.size()-1));
                    }
                    else {
                        polishNotation.set(polishNotation.lastIndexOf("_"), String.valueOf(polishNotation.size()-1));
                    }
                }
                else if(List.of("SC").contains(n.getLexeme().getTerminal().getIdentifier())) {
                    transferFromStackToPolishNotation();
                }
            }
            else {
                convertToPN(n);
            }
        }
    }

    private Integer getPriority(Lexeme lexeme) {

        if(List.of("*", "/").contains(lexeme.getValue())) {
            return 15;
        }
        else if(List.of("+", "-").contains(lexeme.getValue())) {
            return 10;
        }
        else if(List.of(">", "<", ">=", "<=").contains(lexeme.getValue())) {
            return 8;
        }
        else if(List.of("==", "!=").contains(lexeme.getValue())) {
            return 7;
        }
        else if(List.of("=").contains(lexeme.getValue())) {
            return 5;
        }
        else if(List.of("(", ")").contains(lexeme.getValue())) {
            return 0;
        }
        throw new RuntimeException("Bad operation");
    }
}
