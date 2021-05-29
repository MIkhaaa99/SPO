package mirea.lexer;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class StackMachine {

    private Integer point = 0;
    private Stack<String> stringStack = new Stack<>();
    private List<String> variable = new ArrayList<>();

    public Stack<String> execute(List<String> polishNotation) {
        while(polishNotation.size()>point){
            if(polishNotation.get(point).matches("[0-9]+")) {
                stringStack.push(polishNotation.get(point));
                point++;
            }
            if(polishNotation.get(point).matches("[a-zA-Z][a-zA-Z0-9]*")) {
                stringStack.push(polishNotation.get(point));
                point++;
            }
            if(polishNotation.get(point).matches("[+-/*]")) {
                String operation = polishNotation.get(point);
                point++;
                Integer operand2 = Integer.valueOf(stringStack.pop());
                Integer operand1 = Integer.valueOf(stringStack.pop());
                stringStack.push(String.valueOf(executeBinOperation(operand1, operand2, operation)));
            }
            if(polishNotation.get(point).matches("<|>|==|<=|>=")) {
                String operation = polishNotation.get(point);
                point++;
                Integer operand2 = Integer.valueOf(stringStack.pop());
                Integer operand1 = Integer.valueOf(stringStack.pop());
                /*
                    Дописать
                */
            }
            if(polishNotation.get(point).matches("=")) {
                String operation = polishNotation.get(point);
                point++;
                Integer operand2 = Integer.valueOf(stringStack.pop());
                String operand1 = stringStack.pop();
                assignOperation(operand1, operand2);
            }
        }
        return stringStack;
    }

    private int executeBinOperation(Integer a, Integer b, String operation) {
        if(operation.equals("*")) {
            return a*b;
        }
        if(operation.equals("/")) {
            return a/b;
        }
        if(operation.equals("+")) {
            return a+b;
        }
        if(operation.equals("-")) {
            return a-b;
        }
        throw new RuntimeException();
    }

    private boolean executeLogOperation(Integer a, Integer b, String operation) {
        if(operation.equals("<")) {
            return a<b;
        }
        if(operation.equals(">")) {
            return a>b;
        }
        if(operation.equals("<=")) {
            return a<=b;
        }
        if(operation.equals(">=")) {
            return a>=b;
        }
        if(operation.equals("==")) {
            return a==b;
        }
        throw new RuntimeException();
    }

    private void assignOperation(String left, Integer right) {
        variable.add(left);
    }

    private void isDefine() {

    }
}
