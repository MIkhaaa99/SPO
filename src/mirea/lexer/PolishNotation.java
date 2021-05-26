package mirea.lexer;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class PolishNotation {

    private List<Lexeme> currentList;
    private List<String> polishNotation;
    private Stack<Lexeme> stringStack;

    private boolean isElse = false;

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
        return lexemeList;
    }

    private List<Lexeme> parseOnWhileExpression() {
        List<Lexeme> lexemeList = new ArrayList<>();
        while(!currentList.get(0).getValue().equals("}")) {
            lexemeList.add(currentList.get(0));
            currentList.remove(0);
        }
        lexemeList.add(currentList.get(0));
        currentList.remove(0);
        return lexemeList;
    }

    private List<Lexeme> parseOnIfExpression() {
        List<Lexeme> lexemeList = new ArrayList<>();
        while(!currentList.get(0).getValue().equals("}")) {
            lexemeList.add(currentList.get(0));
            currentList.remove(0);
        }
        lexemeList.add(currentList.get(0));
        currentList.remove(0);
        if(currentList.size()!=0 && currentList.get(0).getValue().equals("else")) {
            isElse = true;
            while(!currentList.get(0).getValue().equals("}")) {
                lexemeList.add(currentList.get(0));
                currentList.remove(0);
            }
            lexemeList.add(currentList.get(0));
            currentList.remove(0);
        }
        return lexemeList;
    }

    public List<String> translateToPolishNotation() {

        List<Lexeme> lexemeList = new ArrayList<>();

        while(currentList.size()!=0) {
            if(currentList.get(0).getTerminal().getIdentifier()=="WHILE_KEYWORD") {
                List<String> polishNotationForWhile;
                List<Lexeme> whileExpr = parseOnWhileExpression();

                polishNotationForWhile = convertExpression(getHead(whileExpr));
                polishNotationForWhile.add(" ");
                polishNotationForWhile.add("!F");
                polishNotationForWhile.addAll(getExpressionFromBody(getBody(whileExpr)));
                polishNotationForWhile.add("0");
                polishNotationForWhile.add("!T");

                polishNotationForWhile.set(polishNotationForWhile.indexOf(" "), String.valueOf(polishNotationForWhile.size()-1));

                polishNotation.addAll(polishNotationForWhile);

                System.out.println();
            }
            else if(currentList.get(0).getTerminal().getIdentifier()=="IF_KEYWORD") {
                List<String> polishNotationForIf;
                List<Lexeme> ifExpr = parseOnIfExpression();

                polishNotationForIf = convertExpression(getHead(ifExpr));
                polishNotationForIf.add(" ");
                polishNotationForIf.add("!F");
                polishNotationForIf.addAll(getExpressionFromBody(getBody(ifExpr)));
                if(isElse==true) {
                    polishNotationForIf.add("  ");
                    polishNotationForIf.add("!");
                    polishNotationForIf.addAll(getExpressionFromBody(getBody(ifExpr)));
                }
                polishNotationForIf.add("!T");

                if(isElse==true) {
                    polishNotationForIf.set(polishNotationForIf.indexOf("  "), String.valueOf(polishNotationForIf.size()-1));
                    polishNotationForIf.set(polishNotationForIf.indexOf(" "), String.valueOf(polishNotationForIf.indexOf("!")));
                }
                else {
                    polishNotationForIf.set(polishNotationForIf.indexOf(" "), String.valueOf(polishNotationForIf.size()-1));
                }
                polishNotation.addAll(polishNotationForIf);

                isElse = false;
                System.out.println();
            }
            else if(currentList.get(0).getTerminal().getIdentifier()=="VAR") {
                lexemeList = parseOnExpression();
                polishNotation.addAll(convertExpression(lexemeList));
            }
        }
        return polishNotation;
    }

    private List<Lexeme> getHead(List<Lexeme> listExpr) {
        List<Lexeme> headStringList = new ArrayList<>();
        while(!listExpr.get(0).getTerminal().getIdentifier().equals("VAR")) {
            listExpr.remove(0);
        }
        while(!listExpr.get(2).getTerminal().getIdentifier().equals("L_S_BR")) {
            headStringList.add(listExpr.get(0));
            listExpr.remove(0);
        }
        headStringList.add(listExpr.get(0));
        listExpr.remove(0);
        return headStringList;
    }

    private List<Lexeme> getBody(List<Lexeme> listExpr) {
        List<Lexeme> bodyStringList = new ArrayList<>();
        while(!listExpr.get(0).getTerminal().getIdentifier().equals("VAR")) {
            listExpr.remove(0);
        }
        while(!listExpr.get(2).getTerminal().getIdentifier().equals("R_S_BR")) {
            bodyStringList.add(listExpr.get(0));
            listExpr.remove(0);
        }
        bodyStringList.add(listExpr.get(0));
        listExpr.remove(0);
        return bodyStringList;
    }

    private List<String> getExpressionFromBody(List<Lexeme> listWhileExpr) {
        List<Lexeme> listExpressionsFromBody = new ArrayList<>();
        List<String> convertToPN = new ArrayList<>();
        while(listWhileExpr.size()!=0) {

            if(listWhileExpr.get(0).getTerminal().getIdentifier()=="SC") {
                listWhileExpr.remove(0);
                convertToPN.addAll(convertExpression(listExpressionsFromBody));
            }
            listExpressionsFromBody.add(listWhileExpr.get(0));
            listWhileExpr.remove(0);
        }
        convertToPN.addAll(convertExpression(listExpressionsFromBody));
        return convertToPN;
    }

    private List<String> convertExpression(List<Lexeme> lexemeList) {

        List<String> polishNotationForOneExpr = new ArrayList<>();

        while(lexemeList.size()!=0) {   //Последний символ ;
            //Если символ является числом
            if(lexemeList.get(0).getTerminal().getIdentifier()=="VAR" || lexemeList.get(0).getTerminal().getIdentifier()=="NUMBER") {
                polishNotationForOneExpr.add(lexemeList.get(0).getValue());
                lexemeList.remove(0);
                continue;
            }
            //Если символ является открывающей скобкой
            if(lexemeList.get(0).getTerminal().getIdentifier()=="LBR") {
                stringStack.push(lexemeList.get(0));
                lexemeList.remove(0);
                continue;
            }
            //Если символ является закрывающей скобкой
            if(lexemeList.get(0).getTerminal().getIdentifier()=="RBR") {
                lexemeList.remove(0);
                while(stringStack.get(stringStack.size()-1).getTerminal().getIdentifier()!="LBR") {
                    polishNotationForOneExpr.add(stringStack.pop().getValue());
                }
                stringStack.pop();
                continue;
            }
            //Если символ является бинарной операцией
            if(lexemeList.get(0).getTerminal().getIdentifier()=="OP" || lexemeList.get(0).getTerminal().getIdentifier()=="ASSIGN" || lexemeList.get(0).getTerminal().getIdentifier()=="LOGICAL_OP") {
                int priority = getPriority(lexemeList.get(0).getValue());
                if(stringStack.size()!=0) {
                    int priorityTopOfStack = getPriority(stringStack.get(stringStack.size()-1).getValue());
                    while(stringStack.size()!=0 && priorityTopOfStack >= priority) {
                        polishNotationForOneExpr.add(stringStack.pop().getValue());
                        if(stringStack.size()!=0) {
                            priorityTopOfStack = getPriority(stringStack.get(stringStack.size()-1).getValue());
                        }
                    }
                }
                stringStack.push(lexemeList.get(0));
                lexemeList.remove(0);
                continue;
            }
        }

        //Если больше не осталось токенов на входе
        if(lexemeList.size()==0) {
            while(stringStack.size()!=0) {
                polishNotationForOneExpr.add(stringStack.pop().getValue());
            }
        }

        return polishNotationForOneExpr;
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
