import java.util.ArrayList;

public class MulExpNode extends ASTExpNode{
    /*
AddExp → MulExp | AddExp ('+' | '−') MulExp
MulExp → UnaryExp | MulExp ('*' | '/' | '%') UnaryExp
UnaryExp → PrimaryExp | Ident '(' [FuncRParams] ')' | UnaryOp UnaryExp
PrimaryExp → '(' Exp ')' | LVal | Number
LVal → Ident {'[' Exp ']'}
 */
    private MulExpNode mulExpNode = null;
    private UnaryExpNode unaryExpNode;
    private int compute = 0;//0 无addExpNode  1 '*' 2 '/' 3 '%'

    public MulExpNode(Node root,SymbolTable symbolTable){
        super(symbolTable);
        ArrayList<Node> subnodes = root.getSubNodes();
        if (subnodes.size()==1){
            unaryExpNode = new UnaryExpNode(subnodes.get(0),symbolTable);
            dim = unaryExpNode.getDim();
        }
        else {
            mulExpNode = new MulExpNode(subnodes.get(0),symbolTable);
            unaryExpNode = new UnaryExpNode(subnodes.get(2),symbolTable);
            compute = ((TermianlNode)subnodes.get(1)).getWord().getSymbol() == Symbol.MULT ? 1 : (((TermianlNode)subnodes.get(1)).getWord().getSymbol() == Symbol.DIV ? 2 : 3);
            dim = 0;
        }
    }
    @Override
    public int getConstValue() {
        if (compute ==0){
            return unaryExpNode.getConstValue();
        }
        else if (compute == 1){
            return mulExpNode.getConstValue()*unaryExpNode.getConstValue();
        }
        else if (compute == 2){
            return mulExpNode.getConstValue()/unaryExpNode.getConstValue();
        }
        else {
            return mulExpNode.getConstValue()%unaryExpNode.getConstValue();
        }
    }

    @Override
    public String getMidCode() {
        if (compute ==0){
            return unaryExpNode.getMidCode();
        }
        else if (compute == 1){

            String v1 = mulExpNode.getMidCode();
            String v2 = unaryExpNode.getMidCode();
            if (!(v2.startsWith("a")||v2.startsWith("c")||v2.startsWith("t"))&&!(v1.startsWith("a")||v1.startsWith("c")||v1.startsWith("t"))){
//                String t = "t_"+IdCtrl.getTempId();
//                MidCode.add(new FourUnit("ass",String.valueOf(Integer.parseInt(v1)*Integer.parseInt(v2)) , null,t));
//                return t;
                return String.valueOf(Integer.parseInt(v1)*Integer.parseInt(v2));

            }else if (!(v2.startsWith("a")||v2.startsWith("c")||v2.startsWith("t"))){
                String t = "t_"+IdCtrl.getTempId();
                String t2 = "t_"+IdCtrl.getTempId();
                MidCode.add(new FourUnit("ass",v2 , null,t2));
                v2=t2;
                MidCode.add(new FourUnit("mul", v1,v2 ,t));
                return t;
            }else if (!(v1.startsWith("a")||v1.startsWith("c")||v1.startsWith("t"))){
                String t = "t_"+IdCtrl.getTempId();
                String t1 = "t_"+IdCtrl.getTempId();
                MidCode.add(new FourUnit("ass",v1 , null,t1));
                v1=t1;
                MidCode.add(new FourUnit("mul", v1,v2 ,t));
                return t;
            } else {
                String t = "t_"+IdCtrl.getTempId();
                MidCode.add(new FourUnit("mul", v1,v2 ,t));
                return t;
            }

        }
        else if (compute == 2){

            String v1 = mulExpNode.getMidCode();
            String v2 = unaryExpNode.getMidCode();
            if (!(v2.startsWith("a")||v2.startsWith("c")||v2.startsWith("t"))&&!(v1.startsWith("a")||v1.startsWith("c")||v1.startsWith("t"))){
//                String t = "t_"+IdCtrl.getTempId();
//                MidCode.add(new FourUnit("ass",String.valueOf(Integer.parseInt(v1)/Integer.parseInt(v2)) , null,t));
//                return t;
                return String.valueOf(Integer.parseInt(v1)/Integer.parseInt(v2));

            }else if (!(v2.startsWith("a")||v2.startsWith("c")||v2.startsWith("t"))){
                String t = "t_"+IdCtrl.getTempId();
                String t2 = "t_"+IdCtrl.getTempId();
                MidCode.add(new FourUnit("ass",v2 , null,t2));
                v2=t2;
                MidCode.add(new FourUnit("div", v1,v2 ,t));
                return t;
            }else if (!(v1.startsWith("a")||v1.startsWith("c")||v1.startsWith("t"))){
                String t = "t_"+IdCtrl.getTempId();
                String t1 = "t_"+IdCtrl.getTempId();
                MidCode.add(new FourUnit("ass",v1 , null,t1));
                v1=t1;
                MidCode.add(new FourUnit("div", v1,v2 ,t));
                return t;
            } else {
                String t = "t_"+IdCtrl.getTempId();
                MidCode.add(new FourUnit("div", v1,v2 ,t));
                return t;
            }

        }
        else {

            String v1 = mulExpNode.getMidCode();
            String v2 = unaryExpNode.getMidCode();
            if (!(v2.startsWith("a")||v2.startsWith("c")||v2.startsWith("t"))&&!(v1.startsWith("a")||v1.startsWith("c")||v1.startsWith("t"))){
//                String t = "t_"+IdCtrl.getTempId();
//                MidCode.add(new FourUnit("ass",String.valueOf(Integer.parseInt(v1)%Integer.parseInt(v2)) , null,t));
//                return t;
                return String.valueOf(Integer.parseInt(v1)%Integer.parseInt(v2));

            }else if (!(v2.startsWith("a")||v2.startsWith("c")||v2.startsWith("t"))){
                String t = "t_"+IdCtrl.getTempId();
                String t2 = "t_"+IdCtrl.getTempId();
                MidCode.add(new FourUnit("ass",v2 , null,t2));
                v2=t2;
                MidCode.add(new FourUnit("mod", v1,v2 ,t));
                return t;
            }else if (!(v1.startsWith("a")||v1.startsWith("c")||v1.startsWith("t"))){
                String t = "t_"+IdCtrl.getTempId();
                String t1 = "t_"+IdCtrl.getTempId();
                MidCode.add(new FourUnit("ass",v1 , null,t1));
                v1=t1;
                MidCode.add(new FourUnit("mod", v1,v2 ,t));
                return t;
            } else {
                String t = "t_"+IdCtrl.getTempId();
                MidCode.add(new FourUnit("mod", v1,v2 ,t));
                return t;
            }

        }
    }

    @Override
    public void intoMid() {

    }
}
