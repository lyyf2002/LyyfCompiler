import java.util.ArrayList;

public class ConstDeclNode extends ASTNode{
    /*ConstDef → Ident { '[' ConstExp ']' } '=' ConstInitVal

    ConstInitVal → ConstExp
 | '{' [ ConstInitVal { ',' ConstInitVal } ] '}'

a = i
a [ x ] = { x , x , x }
a [ x ] [ x ] = {i,i}
a [ x = i
     */


    private ArrayList<ConstExpNode> dims = new ArrayList<>();

    private ArrayList<ArrayList<ConstExpNode>> values = new ArrayList<>();
    private ArrayList<ArrayList<Integer>> realValues = new ArrayList<>();
    private Word def;
    private ConstSymbolInfo symbolInfo;
    public ConstDeclNode(Node root,SymbolTable symbolTable){
        super(symbolTable);
        ArrayList<Node> subnodes = root.getSubNodes();
        def = ((TermianlNode)subnodes.get(0)).getWord();
        int dim = 0;
        for (int i = 2;i<subnodes.size()-2;i++){
            if (subnodes.get(i) instanceof NonTermianlNode){
                dims.add(new ConstExpNode(subnodes.get(i),symbolTable));
                dim ++;
            }
        }
        subnodes = root.getSubNodes().get(subnodes.size()-1).getSubNodes();//ConstInitVal
        if (dim == 0){
            values.add(new ArrayList<>());
            realValues.add(new ArrayList<>());
            ConstExpNode t = new ConstExpNode(subnodes.get(0),symbolTable);
            values.get(0).add(t);
            realValues.get(0).add(t.getConstValue());
        }
        else if (dim == 1){
            values.add(new ArrayList<>());
            realValues.add(new ArrayList<>());
            for (int i = 1;i < subnodes.size();i+=2){
                ConstExpNode t = new ConstExpNode(subnodes.get(i).getSubNodes().get(0),symbolTable);
                values.get(0).add(t);
                realValues.get(0).add(t.getConstValue());
            }
        }
        else if (dim == 2){
            for (int i = 1;i<subnodes.size();i+=2){
                ArrayList<Node> subnode_j = subnodes.get(i).getSubNodes();
                values.add(new ArrayList<>());
                realValues.add(new ArrayList<>());
                for (int j = 1;j < subnode_j.size();j+=2){
                    ConstExpNode t = new ConstExpNode(subnode_j.get(j).getSubNodes().get(0),symbolTable);
                    values.get((i-1)/2).add(t);
                    realValues.get((i-1)/2).add(t.getConstValue());
                }
            }
        }
        symbolInfo = new ConstSymbolInfo(def,dim,realValues,dims.size()>0?dims.get(0).getConstValue():0,dims.size()>1?dims.get(1).getConstValue():0);
        symbolTable.insert(symbolInfo);
    }

    @Override
    public void intoMid() {
        if (dims.size()==0)
        MidCode.add(new FourUnit("conass",realValues.get(0).get(0).toString(),null,symbolInfo.Strid));
        else if (dims.size()==1){
            int i = dims.get(0).getConstValue();
            MidCode.add(new FourUnit("conarr",String.valueOf(i),null,symbolInfo.Strid));
            for (int i1=0;i1<i;i1++){
                MidCode.add(new FourUnit("assarr",String.valueOf(i1),realValues.get(0).get(i1).toString(),symbolInfo.Strid));
            }
        }
        else {
            int i = dims.get(0).getConstValue();
            int j = dims.get(1).getConstValue();
            MidCode.add(new FourUnit("conarr",String.valueOf(i),String.valueOf(j),symbolInfo.Strid));
            for (int i1=0;i1<i;i1++){
                for (int j1=0;j1<j;j1++){
                    MidCode.add(new FourUnit("assarr",String.valueOf(i1*j+j1),realValues.get(i1).get(j1).toString(),symbolInfo.Strid));
                }
            }
        }
    }
}
