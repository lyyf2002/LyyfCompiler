import java.util.ArrayList;

public class ReturnNode extends ASTNode{
    public boolean isReturnInt() {
        return isReturnInt;
    }

    private boolean isReturnInt=false;
    private ExpNode returnExp;
    public ReturnNode(ArrayList<Node> nodes,SymbolTable symbolTable) {
        super(symbolTable);
        if (nodes.size()>1&&nodes.get(1) instanceof NonTermianlNode){
            returnExp = new ExpNode(nodes.get(1),symbolTable);
            isReturnInt=true;

        }
    }

    @Override
    public void intoMid() {
        MidCode.add(new FourUnit("ret",null,null,isReturnInt?returnExp.getMidCode():null));
    }
}
