package mirea.lexer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class StackMachine {

    private Integer point = 0;
    private Stack<String> stringStack = new Stack<>();
    private Map<String, Integer> variable = new HashMap<>();

    public StackMachine() {
        this.variable.put("a", null);
        this.variable.put("b", null);
        this.variable.put("c", null);
        this.variable.put("d", null);
        this.variable.put("e", null);
        this.variable.put("f", null);
        this.variable.put("g", null);
        this.variable.put("h", null);
    }

    public Map<String, Integer> execute(List<String> polishNotation) {
        while(polishNotation.size()>point){
            if(polishNotation.get(point).matches("[0-9]+")) {
                stringStack.push(polishNotation.get(point));
                point++;
            }
            else if(polishNotation.get(point).matches("[a-zA-Z][a-zA-Z0-9]*")) {
                stringStack.push(polishNotation.get(point));
                point++;
            }
            else if(polishNotation.get(point).matches("[+-/*%]")) {
                String operation = polishNotation.get(point);
                point++;
                Integer operand2 = 0;
                Integer operand1 = 0;
                if(stringStack.get(stringStack.size()-1).matches("[0-9]+")) {
                    operand2 = Integer.valueOf(stringStack.pop());
                }
                else if(stringStack.get(stringStack.size()-1).matches("[a-zA-Z][a-zA-Z0-9]*")) {
                    operand2 = variable.get(stringStack.pop());
                }

                if(stringStack.get(stringStack.size()-1).matches("[0-9]+")) {
                    operand1 = Integer.valueOf(stringStack.pop());
                }
                else if(stringStack.get(stringStack.size()-1).matches("[a-zA-Z][a-zA-Z0-9]*")) {
                    operand1 = variable.get(stringStack.pop());
                }
                stringStack.push(String.valueOf(executeBinOperation(operand1, operand2, operation)));
            }
            else if(polishNotation.get(point).matches("<|>|==|<=|>=|!=")) {
                String operation = polishNotation.get(point);
                point++;
                Integer operand2 = 0;
                Integer operand1 = 0;
                if(stringStack.get(stringStack.size()-1).matches("[0-9]+")) {
                    operand2 = Integer.valueOf(stringStack.pop());
                }
                else if(stringStack.get(stringStack.size()-1).matches("[a-zA-Z][a-zA-Z0-9]*")) {
                    operand2 = variable.get(stringStack.pop());
                }

                if(stringStack.get(stringStack.size()-1).matches("[0-9]+")) {
                    operand1 = Integer.valueOf(stringStack.pop());
                }
                else if(stringStack.get(stringStack.size()-1).matches("[a-zA-Z][a-zA-Z0-9]*")) {
                    operand1 = variable.get(stringStack.pop());
                }
                stringStack.push(String.valueOf(executeLogOperation(operand1, operand2, operation)));
            }
            else if(polishNotation.get(point).matches("=")) {
                String operation = polishNotation.get(point);
                point++;
                Integer operand2 = Integer.valueOf(stringStack.pop());
                String operand1 = stringStack.pop();
                assignOperation(operand1, operand2);
            }
            else if(polishNotation.get(point).matches("!F")) {
                Integer position = Integer.valueOf(stringStack.pop());
                String condition = stringStack.pop();
                if(condition.equals("true")) {
                    point++;
                }
                else {
                    point = position;
                }
            }
            else if(polishNotation.get(point).matches("!")) {
                Integer position = Integer.valueOf(stringStack.pop());
                point = position;
            }
            else if(polishNotation.get(point).matches("!T")) {
                if(stringStack.size()==0) {
                    point++;
                }
                else {
                    Integer position = Integer.valueOf(stringStack.pop());
                    point = position;
                }
            }
        }
        return variable;
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
        if(operation.equals("%")) {
            return a%b;
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
        if(operation.equals("!=")) {
            return a!=b;
        }
        throw new RuntimeException();
    }

    private void assignOperation(String left, Integer right) {
        isDefine(left);
        variable.put(left, right);
    }

    private boolean isDefine(String left) {
        if(variable.containsKey(left)) {
            return true;
        }
        throw new RuntimeException("Variable not found");
    }
}
