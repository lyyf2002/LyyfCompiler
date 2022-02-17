import java.util.ArrayList;

public class LAndExpNode extends ASTExpNode{
    private LAndExpNode lAndExp = null;
    private EqExpNode eqExp;
    private int compute = 0;//0 无  1 &&

    public LAndExpNode(Node root,SymbolTable symbolTable){
        super(symbolTable);
        ArrayList<Node> subnodes = root.getSubNodes();
        if (subnodes.size()==1){
            eqExp = new EqExpNode(subnodes.get(0),symbolTable);
            dim=eqExp.getDim();
        }
        else {
            lAndExp = new LAndExpNode(subnodes.get(0),symbolTable);
            eqExp = new EqExpNode(subnodes.get(2),symbolTable);
            compute = 1;
            dim = 0;
        }
    }

    @Override
    public int getConstValue() {
        return 0;
    }

    @Override
    public String getMidCode() {
        return null;
    }

    @Override
    public void intoMid() {

    }
    public void cond2Mid(String ELSE){
        if (compute==0){
            eqExp.cond2Mid(ELSE);
        }
        else {
            lAndExp.cond2Mid(ELSE);
            eqExp.cond2Mid(ELSE);
        }
    }
}
