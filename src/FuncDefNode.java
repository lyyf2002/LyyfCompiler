import java.util.ArrayList;

public class FuncDefNode extends ASTNode{
    private boolean isReturnInt;
    private boolean isMain;
    private Word def;
    private ArrayList<FuncFParamNode> paramNodes=new ArrayList<>();
    private ArrayList<Integer> paramDims=new ArrayList<>();
    private BlockNode block;
    private FuncSymbolInfo symbolInfo ;
    public FuncDefNode(Node root,SymbolTable symbolTable,boolean isMain) {
        super(symbolTable);
        ArrayList<Node> subnodes = root.getSubNodes();
        this.isMain = isMain;
        if (isMain){
            isReturnInt = true;
        }else {
            isReturnInt = ((TermianlNode) subnodes.get(0).getSubNodes().get(0)).getWord().getSymbol() == Symbol.INTTK;
        }
        def = ((TermianlNode)subnodes.get(1)).getWord();
        SymbolTable subTable = new SymbolTable(symbolTable);
        if (subnodes.size()>3&&subnodes.get(3) instanceof NonTermianlNode && ((NonTermianlNode)subnodes.get(3)).getNonTermianlName().equals("FuncFParams")){
            ArrayList<Node> nodes = subnodes.get(3).getSubNodes();
            for (int i =0;i<nodes.size();i+=2){
                FuncFParamNode t = new FuncFParamNode(nodes.get(i),subTable);
                paramNodes.add(t);
                paramDims.add(t.getDim());
            }
        }
        symbolInfo = new FuncSymbolInfo(def,isReturnInt,paramDims);
        symbolTable.insert(symbolInfo);
        block = new BlockNode(subnodes.get(subnodes.size()-1),subTable,true,isReturnInt,false);


    }

    @Override
    public void intoMid() {
        MidCode.add(new FourUnit("func",isReturnInt?"int":"void",null,isMain?"f_main":symbolInfo.Strid));
        for (FuncFParamNode funcFParamNode:paramNodes){
            funcFParamNode.intoMid();
        }
        block.intoMid();
        MidCode.add(new FourUnit("ret",null,null,null));
    }
}
