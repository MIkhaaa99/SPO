package mirea.lexer;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class PolishNotation {

    private List<Lexeme> currentList;
    private List<Lexeme> polishNotation;
    private Stack<Lexeme> stringStack;

    private final String FALSE = "!F";
    private final String END = "!T";

    public PolishNotation(List<Lexeme> currentList) {
        this.currentList = currentList;
        this.polishNotation = new ArrayList<>();
        this.stringStack = new Stack<>();
    }

    private List<Lexeme> parseOnExpression() {
        List<Lexeme> lexemeList = new ArrayList<>();
        while(currentList.size()!=0) {
            if(currentList.get(0).getTerminal().getIdentifier()=="SC") {
                currentList.remove(0);
                return lexemeList;
            }
            lexemeList.add(currentList.get(0));
            currentList.remove(0);
        }
        return null;
    }

    public List<Lexeme> translateToPolishNotation() {

        List<Lexeme> lexemeList = new ArrayList<>();

        while(currentList.size()!=0) {

            lexemeList = parseOnExpression();

            while(lexemeList.size()!=1) {   //Последний символ ;

                //Если символ является числом
                if(lexemeList.get(0).getTerminal().getIdentifier()=="VAR" || lexemeList.get(0).getTerminal().getIdentifier()=="NUMBER") {
                    polishNotation.add(lexemeList.get(0));
                    lexemeList.remove(0);
                }
                //Если символ является открывающей скобкой
                if(lexemeList.get(0).getTerminal().getIdentifier()=="LBR") {
                    stringStack.push(lexemeList.get(0));
                    lexemeList.remove(0);
                }
                //Если символ является закрывающей скобкой
                if(lexemeList.get(0).getTerminal().getIdentifier()=="RBR") {
                    lexemeList.remove(0);
                    while(stringStack.get(stringStack.size()-1).getTerminal().getIdentifier()!="LBR") {
                        polishNotation.add(stringStack.pop());
                    }
                    stringStack.pop();
                }
                //Если символ является бинарной операцией
                if(lexemeList.get(0).getTerminal().getIdentifier()=="OP" || lexemeList.get(0).getTerminal().getIdentifier()=="ASSIGN" || lexemeList.get(0).getTerminal().getIdentifier()=="LOGICAL_OP") {
                    int priority = getPriority(lexemeList.get(0).getValue());
                    if(stringStack.size()!=0) {
                        int priorityTopOfStack = getPriority(stringStack.get(stringStack.size()-1).getValue());
                        while(stringStack.size()!=0 && priorityTopOfStack >= priority) {
                            polishNotation.add(stringStack.pop());
                            if(stringStack.size()!=0) {
                                priorityTopOfStack = getPriority(stringStack.get(stringStack.size()-1).getValue());
                            }
//                        stringStack.remove(stringStack.size()-1);
                        }
                    }
                    stringStack.push(lexemeList.get(0));
                    lexemeList.remove(0);
                }
                if(lexemeList.size()==1) {       //Последний символ ;
                    while(stringStack.size()!=0) {
                        polishNotation.add(stringStack.pop());
                    }
                }
            }
        }
        return polishNotation;
    }

    private int getPriority(String operation) {
        if (operation.equals("="))
            return 0;
        if (operation.equals("<") || operation.equals(">") || operation.equals("<=") || operation.equals(">="))
            return 1;
        if (operation.equals("+") || operation.equals("-"))
            return 2;
        if (operation.equals("*") || operation.equals("/"))
            return 3;
        return -1;
    }
}
