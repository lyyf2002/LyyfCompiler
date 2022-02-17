import java.util.ArrayList;
import java.util.TreeSet;

public class VarDeclNode extends ASTNode{
    /*
    VarDef → Ident { '[' ConstExp ']' }
    | Ident { '[' ConstExp ']' } '=' InitVal

    InitVal → Exp | '{' [ InitVal { ',' InitVal } ] '}'
    a = i
    a [ x ] = i
    a [ x ] [ x ] = i
    a
    a [ x ]
    a [ x ] [ x ]
    a [ x = i
     */
    private ArrayList<ConstExpNode> dims = new ArrayList<>();
    private ArrayList<ArrayList<ExpNode>> values = null;
    private VarSymbolInfo symbolInfo;
    private boolean isAssign;
    private boolean isGlobal;
    public VarDeclNode(Node root,SymbolTable symbolTable) {
        super(symbolTable);
        this.isGlobal = false;
        ArrayList<Node> subnodes = root.getSubNodes();
        Word def = ((TermianlNode)subnodes.get(0)).getWord();

        int dim = 0;
        isAssign=false;
        for (int i = 1;i<subnodes.size();i++){
            if (subnodes.get(i) instanceof NonTermianlNode && ((NonTermianlNode)subnodes.get(i)).getNonTermianlName().equals("ConstExp")){
                dims.add(new ConstExpNode(subnodes.get(i),symbolTable));
                dim ++;
            }
            if (subnodes.get(i) instanceof TermianlNode && ((TermianlNode)subnodes.get(i)).getWord().getSymbol()==Symbol.ASSIGN){
                isAssign = true;
                break;
            }
        }
        if (isAssign){
            subnodes = root.getSubNodes().get(subnodes.size()-1).getSubNodes();//ConstInitVal
            values = new ArrayList<>();
            if (dim == 0){
                values.add(new ArrayList<>());
                values.get(0).add(new ExpNode(subnodes.get(0),symbolTable));
            }
            else if (dim == 1){
                values.add(new ArrayList<>());
                for (int i = 1;i < subnodes.size();i+=2){
                    values.get(0).add(new ExpNode(subnodes.get(i).getSubNodes().get(0),symbolTable));
                }
            }
            else if (dim == 2){
                for (int i = 1;i<subnodes.size();i+=2){
                    ArrayList<Node> subnode_j = subnodes.get(i).getSubNodes();
                    values.add(new ArrayList<>());
                    for (int j = 1;j < subnode_j.size();j+=2){
                        values.get((i-1)/2).add(new ExpNode(subnode_j.get(j).getSubNodes().get(0),symbolTable));
                    }
                }
            }
        }
        symbolInfo = new VarSymbolInfo(def,dim,dims.size()>0?dims.get(0).getConstValue():0,dims.size()>1?dims.get(1).getConstValue():0);
        symbolTable.insert(symbolInfo);
    }
    public VarDeclNode(Node root,SymbolTable symbolTable,boolean isGlobal) {
        super(symbolTable);
        this.isGlobal = isGlobal;
        ArrayList<Node> subnodes = root.getSubNodes();
        Word def = ((TermianlNode)subnodes.get(0)).getWord();

        int dim = 0;
        isAssign=false;
        for (int i = 1;i<subnodes.size();i++){
            if (subnodes.get(i) instanceof NonTermianlNode && ((NonTermianlNode)subnodes.get(i)).getNonTermianlName().equals("ConstExp")){
                dims.add(new ConstExpNode(subnodes.get(i),symbolTable));
                dim ++;
            }
            if (subnodes.get(i) instanceof TermianlNode && ((TermianlNode)subnodes.get(i)).getWord().getSymbol()==Symbol.ASSIGN){
                isAssign = true;
                break;
            }
        }
        if (isAssign){
            subnodes = root.getSubNodes().get(subnodes.size()-1).getSubNodes();//ConstInitVal
            values = new ArrayList<>();
            if (dim == 0){
                values.add(new ArrayList<>());
                values.get(0).add(new ExpNode(subnodes.get(0),symbolTable));
            }
            else if (dim == 1){
                values.add(new ArrayList<>());
                for (int i = 1;i < subnodes.size();i+=2){
                    values.get(0).add(new ExpNode(subnodes.get(i).getSubNodes().get(0),symbolTable));
                }
            }
            else if (dim == 2){
                for (int i = 1;i<subnodes.size();i+=2){
                    ArrayList<Node> subnode_j = subnodes.get(i).getSubNodes();
                    values.add(new ArrayList<>());
                    for (int j = 1;j < subnode_j.size();j+=2){
                        values.get((i-1)/2).add(new ExpNode(subnode_j.get(j).getSubNodes().get(0),symbolTable));
                    }
                }
            }
        }
        symbolInfo = new VarSymbolInfo(def,dim,dims.size()>0?dims.get(0).getConstValue():0,dims.size()>1?dims.get(1).getConstValue():0);
        symbolTable.insert(symbolInfo);
    }



    @Override
    public void intoMid() {
        if(isAssign){
            if (!isGlobal){
                if (dims.size()==0)
                    MidCode.add(new FourUnit("varass",values.get(0).get(0).getMidCode(),null,symbolInfo.Strid));
                else if (dims.size()==1){
                    int i = dims.get(0).getConstValue();
                    MidCode.add(new FourUnit("arr",String.valueOf(i),null,symbolInfo.Strid));
                    for (int i1=0;i1<i;i1++){
                        MidCode.add(new FourUnit("assarr",String.valueOf(i1),values.get(0).get(i1).getMidCode(),symbolInfo.Strid));
                    }
                }
                else {
                    int i = dims.get(0).getConstValue();
                    int j = dims.get(1).getConstValue();
                    MidCode.add(new FourUnit("arr",String.valueOf(i),String.valueOf(j),symbolInfo.Strid));
                    for (int i1=0;i1<i;i1++){
                        for (int j1=0;j1<j;j1++){
                            MidCode.add(new FourUnit("assarr",String.valueOf(i1*j+j1),values.get(i1).get(j1).getMidCode(),symbolInfo.Strid));
                        }
                    }
                }
            }
            else {
                if (dims.size()==0)
                    MidCode.add(new FourUnit("varass",String.valueOf(values.get(0).get(0).getConstValue()),null,symbolInfo.Strid));
                else if (dims.size()==1){
                    int i = dims.get(0).getConstValue();
                    MidCode.add(new FourUnit("arr",String.valueOf(i),null,symbolInfo.Strid));
                    for (int i1=0;i1<i;i1++){
                        MidCode.add(new FourUnit("assarr",String.valueOf(i1),String.valueOf(values.get(0).get(i1).getConstValue()),symbolInfo.Strid));
                    }
                }
                else {
                    int i = dims.get(0).getConstValue();
                    int j = dims.get(1).getConstValue();
                    MidCode.add(new FourUnit("arr",String.valueOf(i),String.valueOf(j),symbolInfo.Strid));
                    for (int i1=0;i1<i;i1++){
                        for (int j1=0;j1<j;j1++){
                            MidCode.add(new FourUnit("assarr",String.valueOf(i1*j+j1),String.valueOf(values.get(i1).get(j1).getConstValue()),symbolInfo.Strid));
                        }
                    }
                }
            }
        }
        else {
            if (dims.size()==0)
                MidCode.add(new FourUnit("var",null,null,symbolInfo.Strid));
            else if (dims.size()==1){
                int i = dims.get(0).getConstValue();
                MidCode.add(new FourUnit("arr",String.valueOf(i),null,symbolInfo.Strid));
            }
            else {
                int i = dims.get(0).getConstValue();
                int j = dims.get(1).getConstValue();
                MidCode.add(new FourUnit("arr",String.valueOf(i),String.valueOf(j),symbolInfo.Strid));
            }
        }
    }
}
