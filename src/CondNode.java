public class CondNode extends ASTNode{
    private LOrExpNode lOrExpNode;

    public CondNode(Node root,SymbolTable symbolTable){
        super(symbolTable);
        lOrExpNode = new LOrExpNode(root.getSubNodes().get(0),symbolTable);
    }

    @Override
    public void intoMid() {

    }
    public void cond2Mid(String THEN,String ELSE){
        lOrExpNode.cond2Mid(THEN,ELSE);

    }
}
