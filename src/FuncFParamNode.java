import java.util.ArrayList;

public class FuncFParamNode extends ASTNode{
    public int getDim() {
        return dim;
    }

    private int dim=0;
    private Word def;
    private ConstExpNode secDim = null;
    private VarSymbolInfo symbolInfo;

    public FuncFParamNode(Node root,SymbolTable symbolTable) {
        super(symbolTable);
        ArrayList<Node> subnodes = root.getSubNodes();
        def = ((TermianlNode)subnodes.get(1)).getWord();
        for (int i = 2;i<subnodes.size();i++){
            if (subnodes.get(i) instanceof TermianlNode&&((TermianlNode)subnodes.get(i)).getWord().getSymbol()==Symbol.LBRACK){
                dim++;

            }
            if (subnodes.get(i) instanceof NonTermianlNode){
                secDim = new ConstExpNode(subnodes.get(i),symbolTable);
            }
        }
        symbolInfo = new VarSymbolInfo(def,dim,0,secDim!=null?secDim.getConstValue():0);
        symbolTable.insert(symbolInfo);
    }

    @Override
    public void intoMid() {
        MidCode.add(new FourUnit("para",null,null,symbolInfo.Strid));
    }
}
