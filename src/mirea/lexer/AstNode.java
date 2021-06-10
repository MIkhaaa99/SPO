package mirea.lexer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AstNode {
    private String name;
    private List<AstNode> childs = new ArrayList<AstNode>();

    public AstNode(String name) {
        this.name = name;
    }

    public void addChild(AstNode node) {
        childs.add(node);
    }

    public String getName() {
        return name;
    }

    public Iterator<AstNode> getChilds() {
        return childs.iterator();
    }

}
