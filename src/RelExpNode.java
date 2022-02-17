import java.util.ArrayList;

public class RelExpNode extends ASTExpNode {
    private RelExpNode relExp = null;
    private AddExpNode addExp;
    private int compute = 0;//0 无  1.< 2.> 3.<= 4.>=

    public RelExpNode(Node root, SymbolTable symbolTable) {
        super(symbolTable);
        ArrayList<Node> subnodes = root.getSubNodes();
        if (subnodes.size() == 1) {
            addExp = new AddExpNode(subnodes.get(0), symbolTable);
            dim = addExp.getDim();
        } else {
            relExp = new RelExpNode(subnodes.get(0), symbolTable);
            addExp = new AddExpNode(subnodes.get(2), symbolTable);
            compute = ((TermianlNode) subnodes.get(1)).getWord().getSymbol() == Symbol.LSS ? 1 :
                    ((TermianlNode) subnodes.get(1)).getWord().getSymbol() == Symbol.GRE ? 2 :
                            ((TermianlNode) subnodes.get(1)).getWord().getSymbol() == Symbol.LEQ ? 3 : 4;
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
            return addExp.getMidCode();
        }
        else if (compute==1){
            String v1 = addExp.getMidCode();
            String v2 = relExp.getMidCode();
            if (!(v2.startsWith("a")||v2.startsWith("c")||v2.startsWith("t"))&&!(v1.startsWith("a")||v1.startsWith("c")||v1.startsWith("t"))){
                return String.valueOf(Integer.parseInt(v1)>Integer.parseInt(v2)?1:0);
            } else if (!(v2.startsWith("a")||v2.startsWith("c")||v2.startsWith("t"))){
                String t = "t_"+IdCtrl.getTempId();
                MidCode.add(new FourUnit("sgt",v1,v2,t));
                return t;
            } else if (!(v1.startsWith("a")||v1.startsWith("c")||v1.startsWith("t"))){
                String t = "t_"+IdCtrl.getTempId();
                MidCode.add(new FourUnit("slt",v2,v1,t));
                return t;
            } else{
                String t = "t_"+IdCtrl.getTempId();
                MidCode.add(new FourUnit("sgt",v1,v2,t));
                return t;
            }

        }
        else if (compute==2){
            String v1 = addExp.getMidCode();
            String v2 = relExp.getMidCode();
            if (!(v2.startsWith("a")||v2.startsWith("c")||v2.startsWith("t"))&&!(v1.startsWith("a")||v1.startsWith("c")||v1.startsWith("t"))){
                return String.valueOf(Integer.parseInt(v1)<Integer.parseInt(v2)?1:0);
            } else if (!(v2.startsWith("a")||v2.startsWith("c")||v2.startsWith("t"))){
                String t = "t_"+IdCtrl.getTempId();
                MidCode.add(new FourUnit("slt",v1,v2,t));
                return t;
            } else if (!(v1.startsWith("a")||v1.startsWith("c")||v1.startsWith("t"))){
                String t = "t_"+IdCtrl.getTempId();
                MidCode.add(new FourUnit("sgt",v2,v1,t));
                return t;
            } else{
                String t = "t_"+IdCtrl.getTempId();
                MidCode.add(new FourUnit("slt",v1,v2,t));
                return t;
            }
//            String t = "t_"+IdCtrl.getTempId();
//            MidCode.add(new FourUnit("slt",addExp.getMidCode(),relExp.getMidCode(),t));
//            return t;
        }
        else if (compute==3){
            String v1 = addExp.getMidCode();
            String v2 = relExp.getMidCode();
            if (!(v2.startsWith("a")||v2.startsWith("c")||v2.startsWith("t"))&&!(v1.startsWith("a")||v1.startsWith("c")||v1.startsWith("t"))){
                return String.valueOf(Integer.parseInt(v1)<Integer.parseInt(v2)?1:0);
            } else if (!(v2.startsWith("a")||v2.startsWith("c")||v2.startsWith("t"))){
                String t = "t_"+IdCtrl.getTempId();
                MidCode.add(new FourUnit("sge",v1,v2,t));
                return t;
            } else if (!(v1.startsWith("a")||v1.startsWith("c")||v1.startsWith("t"))){
                String t = "t_"+IdCtrl.getTempId();
                MidCode.add(new FourUnit("sle",v2,v1,t));
                return t;
            } else{
                String t = "t_"+IdCtrl.getTempId();
                MidCode.add(new FourUnit("sge",v1,v2,t));
                return t;
            }
//            String t = "t_"+IdCtrl.getTempId();
//            MidCode.add(new FourUnit("sge",addExp.getMidCode(),relExp.getMidCode(),t));
//            return t;
        }
        else {
            String v1 = addExp.getMidCode();
            String v2 = relExp.getMidCode();
            if (!(v2.startsWith("a")||v2.startsWith("c")||v2.startsWith("t"))&&!(v1.startsWith("a")||v1.startsWith("c")||v1.startsWith("t"))){
                return String.valueOf(Integer.parseInt(v1)<Integer.parseInt(v2)?1:0);
            } else if (!(v2.startsWith("a")||v2.startsWith("c")||v2.startsWith("t"))){
                String t = "t_"+IdCtrl.getTempId();
                MidCode.add(new FourUnit("sle",v1,v2,t));
                return t;
            } else if (!(v1.startsWith("a")||v1.startsWith("c")||v1.startsWith("t"))){
                String t = "t_"+IdCtrl.getTempId();
                MidCode.add(new FourUnit("sge",v2,v1,t));
                return t;
            } else{
                String t = "t_"+IdCtrl.getTempId();
                MidCode.add(new FourUnit("sle",v1,v2,t));
                return t;
            }
//            String t = "t_"+IdCtrl.getTempId();
//            MidCode.add(new FourUnit("sle",addExp.getMidCode(),relExp.getMidCode(),t));
//            return t;
        }
    }

    @Override
    public void intoMid() {

    }
    public void cond2Mid(String ELSE){
        if (compute==0){
            MidCode.add(new FourUnit("jeq",addExp.getMidCode(),"0",ELSE));
        }
        else if (compute==1){
            MidCode.add(new FourUnit("jge",relExp.getMidCode(),addExp.getMidCode(),ELSE));
        }
        else if (compute==2){
            MidCode.add(new FourUnit("jle",relExp.getMidCode(),addExp.getMidCode(),ELSE));
        }
        else if (compute==3){
            MidCode.add(new FourUnit("jgt",relExp.getMidCode(),addExp.getMidCode(),ELSE));
        }
        else {
            MidCode.add(new FourUnit("jlt",relExp.getMidCode(),addExp.getMidCode(),ELSE));
        }
    }
}
