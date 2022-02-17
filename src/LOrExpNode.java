import java.util.ArrayList;

public class LOrExpNode extends ASTExpNode{
    private LOrExpNode lOrExp = null;
    private LAndExpNode lAndExp;
    private int compute = 0;//0 无  1 ||

    public LOrExpNode(Node root,SymbolTable symbolTable){
        super(symbolTable);
        ArrayList<Node> subnodes = root.getSubNodes();
        if (subnodes.size()==1){
            lAndExp = new LAndExpNode(subnodes.get(0),symbolTable);
            dim=lAndExp.getDim();
        }
        else {
            lOrExp = new LOrExpNode(subnodes.get(0),symbolTable);
            lAndExp = new LAndExpNode(subnodes.get(2),symbolTable);
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
    public void cond2Mid(String THEN,String ELSE){
        if (compute==0){
            lAndExp.cond2Mid(ELSE);
            MidCode.add(new FourUnit("j",null,null,THEN));
        }
        else {
            String label = "label"+IdCtrl.getLabelId();
            lOrExp.cond2Mid(THEN,label);
            MidCode.add(new FourUnit("label",null,null,label));
            lAndExp.cond2Mid(ELSE);
            MidCode.add(new FourUnit("j",null,null,THEN));

        }
    }
}
