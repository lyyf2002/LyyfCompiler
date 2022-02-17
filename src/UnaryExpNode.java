import java.util.ArrayList;

public class UnaryExpNode extends ASTExpNode{
        /*
AddExp → MulExp | AddExp ('+' | '−') MulExp
MulExp → UnaryExp | MulExp ('*' | '/' | '%') UnaryExp
UnaryExp → PrimaryExp | Ident '(' [FuncRParams] ')' | UnaryOp UnaryExp
PrimaryExp → '(' Exp ')' | LVal | Number
LVal → Ident {'[' Exp ']'}
 */
    private boolean flag = true;//true为+ f为-
    private boolean isNot = false;
    private boolean isCond = false;
    private boolean isPrimaryExp; //true 为primaryExp
    private ASTExpNode subExp;

    public UnaryExpNode(Node root,SymbolTable symbolTable) {
        super(symbolTable);
        ArrayList<Node> subnodes = root.getSubNodes();

        while (subnodes.get(0)instanceof NonTermianlNode &&((NonTermianlNode)subnodes.get(0)).getNonTermianlName().equals("UnaryOp")){
            if(((TermianlNode)subnodes.get(0).getSubNodes().get(0)).getWord().getSymbol() == Symbol.MINU){
                flag = !flag;
            }
            else if (((TermianlNode)subnodes.get(0).getSubNodes().get(0)).getWord().getSymbol() == Symbol.NOT){
                isNot = !isNot;
                isCond = true;
            }
            subnodes = subnodes.get(1).getSubNodes();
        }
        if (subnodes.size() == 1){
            isPrimaryExp = true;
            subExp = new PrimaryExpNode(subnodes.get(0),symbolTable);

        }
        else {
            isPrimaryExp = false;
            subExp = new FuncCallNode(subnodes,symbolTable);
        }
        dim = subExp.getDim();

    }
    @Override
    public int getConstValue() {
        if (!isCond){
            if (!flag) return -subExp.getConstValue();
            return subExp.getConstValue();
        }
        else return 0;

    }

    @Override
    public String getMidCode() {
        if (!isCond){
            if (isPrimaryExp){
                if (!flag) {
                    String t = subExp.getMidCode();
                    if (t.startsWith("a")||t.startsWith("t")||t.startsWith("c")){
                        String r = "t_"+IdCtrl.getTempId();
                        MidCode.add(new FourUnit("neg",t,null,r));
                        return r;
                    }
                    else {
                        return String.valueOf(-Integer.parseInt(t));
                    }
//                    String r = "t_"+IdCtrl.getTempId();
//                        MidCode.add(new FourUnit("neg",t,null,r));
//                        return r;

                }
                return subExp.getMidCode();
            }
            else{
                subExp.intoMid();
                if (((FuncCallNode)subExp).getFuncSymbolInfo().isReturnInt()){
                    String t = "t_"+IdCtrl.getTempId();
                    if (!flag) {
                        MidCode.add(new FourUnit("neg","RET",null,t));
                        return t;
                    }
                    MidCode.add(new FourUnit("ass","RET",null,t));
                    return t;
                }
                else return null;

            }
        }
        else {
            if (isPrimaryExp){
                if (isNot) {
                    String t = subExp.getMidCode();
                    if (t.startsWith("a")||t.startsWith("t")||t.startsWith("c")){
                        String r = "t_"+IdCtrl.getTempId();
                        MidCode.add(new FourUnit("seq",t,"0",r));
                        return r;
                    }
                    else {
                        return t.equals("0")?"1":"0";
                    }

//                    String r = "t_"+IdCtrl.getTempId();
//                    MidCode.add(new FourUnit("seq",t,"0",r));
//                    return r;
                }
                return subExp.getMidCode();
            }
            else{
                if (isNot) {
                    subExp.intoMid();
                    String t = "t_"+IdCtrl.getTempId();
                    MidCode.add(new FourUnit("seq","RET","0",t));
                    return t;
                }
                String t = "t_"+IdCtrl.getTempId();
                MidCode.add(new FourUnit("sne","RET","0",t));
                return t;
            }

        }
    }

    @Override
    public void intoMid() {

    }
}
