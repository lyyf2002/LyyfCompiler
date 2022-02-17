public class ExpNode extends ASTExpNode{
    /*
    AddExp → MulExp | AddExp ('+' | '−') MulExp
    MulExp → UnaryExp | MulExp ('*' | '/' | '%') UnaryExp
    UnaryExp → PrimaryExp | Ident '(' [FuncRParams] ')' | UnaryOp UnaryExp
    PrimaryExp → '(' Exp ')' | LVal | Number
    LVal → Ident {'[' Exp ']'}
     */
    private AddExpNode addExpNode;


    public ExpNode(Node root,SymbolTable symbolTable){
        super(symbolTable);
        addExpNode = new AddExpNode(root.getSubNodes().get(0),symbolTable);
        dim = addExpNode.getDim();
    }

    @Override
    public int getConstValue() {
        return addExpNode.getConstValue();
    }

    @Override
    public String getMidCode() {
        return addExpNode.getMidCode();
    }

    @Override
    public void intoMid() {
        getMidCode();
    }
}