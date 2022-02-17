public class NumberNode extends ASTExpNode{

    private int value;
    public NumberNode(Node root,SymbolTable symbolTable) {
        super(symbolTable);
        TermianlNode node = (TermianlNode) root.getSubNodes().get(0);
        value = Integer.parseInt(node.getWord().getWord());
        dim = 0;
    }

    @Override
    public int getConstValue() {
        return value;
    }

    @Override
    public String getMidCode() {
        return String.valueOf(value);
    }

    @Override
    public void intoMid() {

    }
}
