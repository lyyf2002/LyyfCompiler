public abstract class ASTExpNode extends ASTNode{
    public int getDim() {
        return dim;
    }

    protected int dim;
    public ASTExpNode(SymbolTable symbolTable) {
        super(symbolTable);
    }
    public abstract int getConstValue();
    public abstract String getMidCode();
}
