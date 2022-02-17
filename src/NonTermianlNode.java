import java.util.ArrayList;

public class NonTermianlNode extends Node{
    public String getNonTermianlName() {
        return nonTermianlName;
    }

    private String nonTermianlName;

    public NonTermianlNode(String name) {
        super();
        this.nonTermianlName = name;

    }

    @Override
    public void print() {
        for (Node node: this.getSubNodes()) {
            node.print();
        }
        if (!this.nonTermianlName.equals("BlockItem")) System.out.println("<"+this.nonTermianlName+">");
    }
}
