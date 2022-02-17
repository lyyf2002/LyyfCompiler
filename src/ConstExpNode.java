import java.util.ArrayList;

public class ConstExpNode extends ASTExpNode{
    /*
    AddExp → MulExp | AddExp ('+' | '−') MulExp
    MulExp → UnaryExp | MulExp ('*' | '/' | '%') UnaryExp
    UnaryExp → PrimaryExp | Ident '(' [FuncRParams] ')' | UnaryOp UnaryExp
    PrimaryExp → '(' Exp ')' | LVal | Number
    LVal → Ident {'[' Exp ']'}
     */
    private AddExpNode addExpNode;
    private int value;
    public ConstExpNode(Node root,SymbolTable symbolTable){
        super(symbolTable);
        addExpNode = new AddExpNode(root.getSubNodes().get(0),symbolTable);
        value = addExpNode.getConstValue();
    }

    @Override
    public int getConstValue() {
        return value;
    }

    @Override
    public String getMidCode() {
        return String.valueOf(value);
    }

    @Override
    public void intoMid() {

    }
}
