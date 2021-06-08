package mirea.lexer;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class PolishNotation {

    private List<String> polishNotation = new ArrayList<>();
    private Stack<Lexeme> stringStack = new Stack<>();

    private boolean isElse = false;

    private List<Lexeme> parseOnExpression(List<Lexeme> currentList) {
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

    private List<Lexeme> parseOnWhileExpression(List<Lexeme> currentList) {
        int numBR = 0;
        List<Lexeme> lexemeList = new ArrayList<>();
        while(!currentList.get(0).getTerminal().getIdentifier().equals("L_S_BR")) {
            lexemeList.add(currentList.get(0));
            currentList.remove(0);
        }
        lexemeList.add(currentList.get(0));
        currentList.remove(0);
        numBR++;
        while(numBR!=0) {
            if(currentList.get(0).getTerminal().getIdentifier().equals("L_S_BR")) {
                numBR++;
            }
            if(currentList.get(0).getTerminal().getIdentifier().equals("R_S_BR")) {
                numBR--;
            }
            lexemeList.add(currentList.get(0));
            currentList.remove(0);
        }
        return lexemeList;
    }

/*    private List<Lexeme> parseOnIfExpression(List<Lexeme> currentList) {
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
    }*/

    public List<String> translateToPolishNotation(List<Lexeme> currentList) {
        while(currentList.size()!=0) {
            polishNotation.addAll(executeTransformation(currentList));
            for(int i=0; i<polishNotation.size(); i++) {
                if(polishNotation.get(i).equals("START")) {
                    for(int j=polishNotation.size()-1; j>0; j--) {
                        if(polishNotation.get(j).equals("END")) {
                            polishNotation.set(j, "ENDCHECK");
                            polishNotation.set(i, String.valueOf(j+1));
                            break;
                        }
                    }
                }
            }
            for(int i=0; i<polishNotation.size(); i++) {
                if(polishNotation.get(i).equals("!F")) {
                    for(int j=polishNotation.size()-1; j>0; j--) {
                        if(polishNotation.get(j).equals("ENDCHECK")) {
                            polishNotation.set(j, String.valueOf(i+1));
                            break;
                        }
                    }
                }
            }
        }
        return polishNotation;
    }

    public List<String> executeTransformation(List<Lexeme> currentList) {

        List<Lexeme> lexemeList = new ArrayList<>();
        List<String> completeExpression = new ArrayList<>();

            if (currentList.get(0).getTerminal().getIdentifier() == "WHILE_KEYWORD") {
                List<Lexeme> whileExpr = parseOnWhileExpression(currentList);
                completeExpression=getExpressionFromBody(whileExpr);

                System.out.println();
            }
/*            else if(currentList.get(0).getTerminal().getIdentifier()=="IF_KEYWORD") {
                List<String> polishNotationForIf;
                List<Lexeme> ifExpr = parseOnIfExpression(currentList);

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
                    polishNotationForIf.set(polishNotationForIf.indexOf(" "), String.valueOf(polishNotationForIf.indexOf("!")+1));
                }
                else {
                    polishNotationForIf.set(polishNotationForIf.indexOf(" "), String.valueOf(polishNotationForIf.size()-1));
                }
                polishNotation.addAll(polishNotationForIf);

                isElse = false;
                System.out.println();
            }*/
            else if (currentList.get(0).getTerminal().getIdentifier() == "VAR") {
                lexemeList = parseOnExpression(currentList);
                List<String> stringList = new ArrayList<>();
                for (int i = 0; i < lexemeList.size(); i++) {
                    stringList.add(lexemeList.get(i).getValue());
                }
                completeExpression=convertExpression(lexemeList);
            }
        return completeExpression;
    }

    private List<String> getExpressionFromBody(List<Lexeme> listWhileExpr) {
        List<String> result = new ArrayList<>();
        List<Lexeme> headStringList = new ArrayList<>();
        List<String> headResult = new ArrayList<>();
        int point = 0;
        while(!List.of("VAR", "NUMBER").contains(listWhileExpr.get(0).getTerminal().getIdentifier())) {
            listWhileExpr.remove(0);
        }
        while(!listWhileExpr.get(2).getTerminal().getIdentifier().equals("L_S_BR")) {
            headStringList.add(listWhileExpr.get(0));
            listWhileExpr.remove(0);
        }
        headStringList.add(listWhileExpr.get(0));
        listWhileExpr.remove(0);
        headResult.addAll(convertExpression(headStringList));
        headResult.add("START");
        headResult.add("!F");
        List<Lexeme> bodyStringList = new ArrayList<>();
        List<String> bodyResult = new ArrayList<>();
        while(!List.of("VAR", "WHILE_KEYWORD").contains(listWhileExpr.get(0).getTerminal().getIdentifier())) {
            listWhileExpr.remove(0);
            if(listWhileExpr.size()==0) {
                headResult.add(String.valueOf(polishNotation.size()));
                headResult.add("!T");
                headResult.set(headResult.indexOf("START"), String.valueOf(headResult.size()+polishNotation.size()-1));
                return headResult;
            }
        }
        listWhileExpr.remove(listWhileExpr.size()-1);

        while(listWhileExpr.size()!=0) {
            if(List.of("WHILE_KEYWORD").contains(listWhileExpr.get(0).getTerminal().getIdentifier())) {
                while(!List.of("R_S_BR").contains(listWhileExpr.get(0).getTerminal().getIdentifier())) {
                    if(List.of("L_S_BR").contains(listWhileExpr.get(0).getTerminal().getIdentifier())) {
                        point++;
                    }
                    bodyStringList.add(listWhileExpr.get(0));
                    listWhileExpr.remove(0);
                }
                do {
                    bodyStringList.add(listWhileExpr.get(0));
                    listWhileExpr.remove(0);
                    point--;
                }
                while(point!=0);
                bodyResult.addAll(getExpressionFromBody(bodyStringList));
            }

            else if(List.of("VAR").contains(listWhileExpr.get(0).getTerminal().getIdentifier())) {
                while(!List.of("SC").contains(listWhileExpr.get(0).getTerminal().getIdentifier())) {
                    bodyStringList.add(listWhileExpr.get(0));
                    listWhileExpr.remove(0);
                }
                listWhileExpr.remove(0);
                if(listWhileExpr.size()!=0 && List.of("R_S_BR").contains(listWhileExpr.get(0).getTerminal().getIdentifier())) {
                    listWhileExpr.remove(0);
                }
                bodyResult.addAll(convertExpression(bodyStringList));
            }
        }
        result.addAll(headResult);
        result.addAll(bodyResult);

        result.add("END");
        result.add("!T");

        return result;
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
