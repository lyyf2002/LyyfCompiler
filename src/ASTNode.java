public abstract class ASTNode {
    public SymbolTable getSymbolTable() {
        return symbolTable;
    }

    public void setSymbolTable(SymbolTable symbolTable) {
        this.symbolTable = symbolTable;
    }

    private SymbolTable symbolTable;
    public ASTNode(SymbolTable symbolTable){
        this.symbolTable = symbolTable;
    }
    public abstract void intoMid();
}
