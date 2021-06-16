package mirea.lexer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AstNode {
    private String name;
    private Lexeme lexeme;
    private List<AstNode> childs = new ArrayList<AstNode>();

    public AstNode(String name) {
        this.name = name;
    }

    public AstNode(Lexeme lexeme) {
        this.lexeme = lexeme;
    }

    public void addChild(AstNode node) {
        childs.add(node);
    }

    public void addChild(Lexeme lexeme) {
        childs.add(new AstNode(lexeme));
    }

    public Lexeme getLexeme() {
        return lexeme;
    }

    public String getName() {
        return name;
    }

    public Iterator<AstNode> getChilds() {
        return childs.iterator();
    }

}
