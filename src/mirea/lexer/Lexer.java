package mirea.lexer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Lexer {

    private static final List<Terminal> TERMINALS = List.of(
            new Terminal("VAR", "[a-zA-Z][a-zA-Z0-9]*"),
            new Terminal("WHILE_KEYWORD", "while", 1),
            new Terminal("IF_KEYWORD", "if", 1),
            new Terminal("ELSE_KEYWORD", "else", 1),
            new Terminal("OP", "[+-/*]"),
            new Terminal("WS", "\\s+", true),
            new Terminal("TAB", "\\t+", true),
            new Terminal("ENTER", "\\n+", true),
            new Terminal("LBR", "\\("),
            new Terminal("RBR", "\\)"),
            new Terminal("LOGICAL_OP", "<|>|==|<=|>="),
            new Terminal("ASSIGN", "="),
            new Terminal("SC", ";"),
            new Terminal("NUMBER", "0|(-)?[1-9]+[0-9]*"),
            new Terminal("L_S_BR", "\\{"),
            new Terminal("R_S_BR", "\\}")
    );

    public static void main(String[] args) {
        String[] str = {"a=1+30*(7+10*(5+4))+89; b=100+50*((70+39)+10)*3;$"};
        StringBuilder input = new StringBuilder(lookupInput(str));
        List<Lexeme> lexemes = new ArrayList<>();

        while (input.charAt(0) != '$') {
            Lexeme lexeme = extractNextLexeme(input);
            if(!lexeme.getTerminal().isIgnore()) {
                lexemes.add(lexeme);
            }
            input.delete(0, lexeme.getValue().length());
        }

        print(lexemes);

        Parser parser = new Parser();
        parser.setLexemes(lexemes);
        System.out.println();
        AstNode astNode = parser.lang();
        printAst(astNode, 0);

        PolishNotation polishNotation = new PolishNotation();
        System.out.println(polishNotation.getPolishNotation(astNode));

        StackMachine stackMachine = new StackMachine();
        System.out.println("\n" + "Выполнение кода: " + stackMachine.execute(List.of("5", "6", ">", "10", "!F", "a" , "5", "=", "13", "!", "b", "6", "=", "!T")));
    }

    private static Lexeme extractNextLexeme(StringBuilder input) {
        StringBuilder buffer = new StringBuilder();
        buffer.append(input.charAt(0));

        if (anyTerminalMatches(buffer)) {
            while (anyTerminalMatches(buffer) && buffer.length() != input.length()) {
                buffer.append(input.charAt(buffer.length()));
            }

            buffer.deleteCharAt(buffer.length() - 1);

            List<Terminal> terminals = lookupTerminals(buffer);

            return new Lexeme(getPrioritizedTerminal(terminals), buffer.toString());
        } else {
            throw new RuntimeException("Unexpected symbol " + buffer);
        }
    }

    private static Terminal getPrioritizedTerminal(List<Terminal> terminals) {
        Terminal prioritizedTerminal = terminals.get(0);

        for (Terminal terminal : terminals) {
            if (terminal.getPriority() > prioritizedTerminal.getPriority()) {
                prioritizedTerminal = terminal;
            }
        }

        return prioritizedTerminal;
    }

    //Есть ли такой терминал (токен)
    private static boolean anyTerminalMatches(StringBuilder buffer) {
        return lookupTerminals(buffer).size() != 0;
    }

    //Возвращает найденный терминал (токен), если символ соответствует шаблону
    private static List<Terminal> lookupTerminals(StringBuilder buffer) {
        List<Terminal> terminals = new ArrayList<>();

        for (Terminal terminal : TERMINALS) {
            if (terminal.matches(buffer)) {
                terminals.add(terminal);
            }
        }

        return terminals;
    }

    private static String lookupInput(String[] arrays) {
        if (arrays.length == 0) {
            throw new IllegalArgumentException("Input string not found");
        }

        return arrays[0];
    }

    private static void print(List<Lexeme> lexemes) {
        for (Lexeme lexeme : lexemes) {
            System.out.printf("[%s, %s]%n",
                    lexeme.getTerminal().getIdentifier(),
                    lexeme.getValue());
        }
    }

    private static String getLevel(int level) {
        String result = "";
        for(int i = 0; i < level; i++)
            result += "\t";
        return result;
    }

    private static void printAst(AstNode node, int level) {
        System.out.println(getLevel(level) + node.getName());
        Iterator<AstNode> childs = node.getChilds();
        while(childs.hasNext()) {
            AstNode n = childs.next();
            if(n.getLexeme()!=null) {
                System.out.println(getLevel(level) + "\t" + n.getLexeme().toString());
            }
            else {
                printAst(n, level+1);
            }
        }
    }

}
