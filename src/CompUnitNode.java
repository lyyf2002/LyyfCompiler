import java.util.ArrayList;

public class CompUnitNode extends ASTNode{
    private ArrayList<ConstDeclNode> constDecls = new ArrayList<>();
    private ArrayList<VarDeclNode> varDecls = new ArrayList<>();
    private ArrayList<FuncDefNode> funcDefs = new ArrayList<>();
    private FuncDefNode mainFuncDef;

    public CompUnitNode(Node root,SymbolTable symbolTable){
        super(symbolTable);


        for (Node node : root.getSubNodes()){
            if (((NonTermianlNode) node).getNonTermianlName().equals("ConstDecl")){
                for (int i = 2;i<node.getSubNodes().size();i+=2){
                    constDecls.add(new ConstDeclNode(node.getSubNodes().get(i),symbolTable));
                }
            }
            else if (((NonTermianlNode) node).getNonTermianlName().equals("VarDecl")){
                for (int i = 1;i< node.getSubNodes().size();i+=2){
                    varDecls.add(new VarDeclNode(node.getSubNodes().get(i),symbolTable,true));
                }
            }
            else if (((NonTermianlNode) node).getNonTermianlName().equals("FuncDef")){
                funcDefs.add(new FuncDefNode(node,symbolTable,false));
            } else {
                mainFuncDef = new FuncDefNode(node,symbolTable,true);
            }
        }
    }

    @Override
    public void intoMid() {
        for (ConstDeclNode node : constDecls){
            node.intoMid();
        }
        for (VarDeclNode node : varDecls){
            node.intoMid();
        }
        for (FuncDefNode node : funcDefs){
            node.intoMid();
        }
        mainFuncDef.intoMid();
    }
}
