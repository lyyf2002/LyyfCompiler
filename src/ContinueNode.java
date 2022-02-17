public class ContinueNode extends ASTNode{
    public ContinueNode(SymbolTable symbolTable) {
        super(symbolTable);
    }

    @Override
    public void intoMid() {
        MidCode.add(new FourUnit("j",null,null,IdCtrl.getBegin()));
    }
}
