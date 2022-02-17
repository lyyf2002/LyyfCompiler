import java.util.ArrayList;

public class EqExpNode extends ASTExpNode{
    private EqExpNode eqExp = null;
    private RelExpNode relExp;
    private int compute = 0;//0 无  1 == 2 !=

    public EqExpNode(Node root,SymbolTable symbolTable){
        super(symbolTable);
        ArrayList<Node> subnodes = root.getSubNodes();
        if (subnodes.size()==1){
            relExp = new RelExpNode(subnodes.get(0),symbolTable);
            dim=relExp.getDim();
        }
        else {
            eqExp = new EqExpNode(subnodes.get(0),symbolTable);
            relExp = new RelExpNode(subnodes.get(2),symbolTable);
            compute = ((TermianlNode)subnodes.get(1)).getWord().getSymbol() == Symbol.EQL ? 1 : 2;
            dim = 0;
        }
    }

    @Override
    public int getConstValue() {
        return 0;
    }

    @Override
    public String getMidCode() {
        if (compute==0){
            return relExp.getMidCode();
        }
        else if (compute==1){

            String v1 = eqExp.getMidCode();
            String v2 = relExp.getMidCode();
            if (!(v2.startsWith("a")||v2.startsWith("c")||v2.startsWith("t"))&&!(v1.startsWith("a")||v1.startsWith("c")||v1.startsWith("t"))){
                return String.valueOf(Integer.parseInt(v1)==Integer.parseInt(v2)?1:0);
            }
            else if (!(v2.startsWith("a")||v2.startsWith("c")||v2.startsWith("t"))){
                String t = "t_"+IdCtrl.getTempId();
                MidCode.add(new FourUnit("seq",v1,v2,t));
                return t;
            }
            else if (!(v1.startsWith("a")||v1.startsWith("c")||v1.startsWith("t"))){
                String t = "t_"+IdCtrl.getTempId();
                MidCode.add(new FourUnit("seq",v2,v1,t));
                return t;
            }
            else {
                String t = "t_"+IdCtrl.getTempId();
                MidCode.add(new FourUnit("seq",v1,v2,t));
                return t;
            }

        }
        else {
            String v1 = eqExp.getMidCode();
            String v2 = relExp.getMidCode();
            if (!(v2.startsWith("a")||v2.startsWith("c")||v2.startsWith("t"))&&!(v1.startsWith("a")||v1.startsWith("c")||v1.startsWith("t"))){
                return String.valueOf(Integer.parseInt(v1)!=Integer.parseInt(v2)?1:0);
            }
            else if (!(v2.startsWith("a")||v2.startsWith("c")||v2.startsWith("t"))){
                String t = "t_"+IdCtrl.getTempId();
                MidCode.add(new FourUnit("sne",v1,v2,t));
                return t;
            }
            else if (!(v1.startsWith("a")||v1.startsWith("c")||v1.startsWith("t"))){
                String t = "t_"+IdCtrl.getTempId();
                MidCode.add(new FourUnit("sne",v2,v1,t));
                return t;
            }
            else {
                String t = "t_"+IdCtrl.getTempId();
                MidCode.add(new FourUnit("sne",v1,v2,t));
                return t;
            }
//            String t = "t_"+IdCtrl.getTempId();
//            MidCode.add(new FourUnit("sne",eqExp.getMidCode(),relExp.getMidCode(),t));
//            return t;
        }
    }

    @Override
    public void intoMid() {

    }
    public void cond2Mid(String ELSE){
        if (compute==0){
            relExp.cond2Mid(ELSE);
        }
        else {
            MidCode.add(new FourUnit(compute==2?"jeq":"jne",eqExp.getMidCode(),relExp.getMidCode(),ELSE));
        }
    }
}
