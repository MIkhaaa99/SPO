package mirea.lexer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

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
                else if(List.of("SC").contains(n.getLexeme().getTerminal().getIdentifier())) {
                    transferFromStackToPolishNotation();
                }
            }
            else {
                convertToPN(n);
            }
        }
    }

/*    public List<String> convertToPN(AstNode node) {
        Iterator<AstNode> childs = node.getChilds();
        while(childs.hasNext()) {
            AstNode n = childs.next();
            if(n.getLexeme()==null) {
                childs = n.getChilds();
            }
            else if(List.of("VAR", "NUMBER").contains(n.getLexeme().getTerminal().getIdentifier())) {
                polishNotation.add(n.getLexeme().getValue());
            }
            else if(List.of("OP", "LOGICAL_OP", "ASSIGN").contains(n.getLexeme().getTerminal().getIdentifier())) {
                int priorityForCurrentToken = getPriority(n.getLexeme());
                if(stringStack.size()!=0 && getPriority(stringStack.get(stringStack.size()-1))>priorityForCurrentToken) {
                    polishNotation.add(stringStack.pop().getValue());
                }
                else {
                    stringStack.add(n.getLexeme());
                }
            }
            else if(List.of("(").contains(n.getLexeme().getTerminal().getIdentifier())) {
                stringStack.add(n.getLexeme());
            }
            else if(List.of(")").contains(n.getLexeme().getTerminal().getIdentifier())) {
                if(stringStack.get(stringStack.size()-1).getTerminal().getIdentifier().equals("(")) {
                    stringStack.pop();
                }
                else {
                    polishNotation.add(stringStack.pop().getValue());
                }
            }
        }
        while (stringStack.size()!=0) {
            polishNotation.add(stringStack.pop().getValue());
        }
        return polishNotation;
    }*/

    private Integer getPriority(Lexeme lexeme) {
        if(List.of("(", ")").contains(lexeme.getValue())) {
            return -5;
        }
        else if(List.of("*", "/").contains(lexeme.getValue())) {
            return 10;
        }
        else if(List.of("+", "-").contains(lexeme.getValue())) {
            return 5;
        }
        else if(List.of("=").contains(lexeme.getValue())) {
            return 0;
        }
        return null;
    }
}
