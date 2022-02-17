import java.util.ArrayList;

public class FuncCallNode extends ASTExpNode{
    //Ident '(' [FuncRParams] ')'
    //FuncRParams → Exp { ',' Exp }
    private ArrayList<ExpNode> funcRParamNodes=new ArrayList<>();

    public FuncSymbolInfo getFuncSymbolInfo() {
        return funcSymbolInfo;
    }

    private FuncSymbolInfo funcSymbolInfo;

    public FuncCallNode(ArrayList<Node> nodes, SymbolTable symbolTable) {
        super(symbolTable);
        funcSymbolInfo = (FuncSymbolInfo) symbolTable.find(((TermianlNode)nodes.get(0)).getWord().getWord(),true);
        if (funcSymbolInfo == null){
            Errors.addError(((TermianlNode)nodes.get(0)).getWord().getLineNum(),"c");
        }
        else {
            dim = funcSymbolInfo.isReturnInt()?0:-1;
            if (nodes.get(2) instanceof NonTermianlNode){
                ArrayList<Node> subnodes = nodes.get(2).getSubNodes();
                for (int i=0;i<subnodes.size();i+=2){
                    funcRParamNodes.add(new ExpNode(subnodes.get(i),symbolTable));
                }
            }

            if (funcSymbolInfo.getParaDims().size()==funcRParamNodes.size()){
                for (int i = 0 ;i<funcRParamNodes.size();i++){
                    if (funcRParamNodes.get(i).getDim() != funcSymbolInfo.getParaDims().get(i)){
                        Errors.addError(((TermianlNode)nodes.get(0)).getWord().getLineNum(),"e");
                        break;
                    }
                }

            }
            else {
                Errors.addError(((TermianlNode)nodes.get(0)).getWord().getLineNum(),"d");
            }
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
        ArrayList<String > paras = new ArrayList<>();
        for (ExpNode expNode:funcRParamNodes){
            String t = expNode.getMidCode();
            if (t.startsWith("a")||t.startsWith("t")||t.startsWith("c")){
                paras.add(t);
            }
            else if (t.equals("RET")){
                String tep = "t_"+IdCtrl.getTempId();
                MidCode.add(new FourUnit("ass","RET",null,tep));
                paras.add(tep);
            }
            //MidCode.add(new FourUnit("push",null,null,t));
            else {
                String tep = "t_"+IdCtrl.getTempId();
                MidCode.add(new FourUnit("ass",t,null,tep));
                paras.add(tep);
            }
        }
        for (String str:paras){
            MidCode.add(new FourUnit("push",null,null,str));
        }
        MidCode.add(new FourUnit("call",null,null,funcSymbolInfo.Strid));

    }
}
