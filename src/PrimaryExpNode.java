import java.util.ArrayList;

public class PrimaryExpNode extends ASTExpNode{
    //PrimaryExp → '(' Exp ')' | LVal | Number
    private ASTExpNode subExp;

    public PrimaryExpNode(Node root ,SymbolTable symbolTable) {

        super(symbolTable);
        ArrayList<Node> subnodes = root.getSubNodes();
        if (subnodes.size()>=2){
            subExp = new ExpNode(subnodes.get(1),symbolTable);
            dim = subExp.getDim();
        }
        else if (((NonTermianlNode)subnodes.get(0)).getNonTermianlName().equals("LVal")){
            subExp = new LValNode(subnodes.get(0),symbolTable);
            dim = subExp.getDim();
        } else {
            subExp = new NumberNode(subnodes.get(0),symbolTable);
            dim = subExp.getDim();
        }
    }

    @Override
    public int getConstValue() {
        return subExp.getConstValue();
    }

    @Override
    public String getMidCode() {
        return subExp.getMidCode();
    }

    @Override
    public void intoMid() {

    }
}
