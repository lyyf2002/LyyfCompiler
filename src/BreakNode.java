public class BreakNode extends ASTNode{
    public BreakNode(SymbolTable symbolTable) {
        super(symbolTable);
    }

    @Override
    public void intoMid() {
        MidCode.add(new FourUnit("j",null,null,IdCtrl.getEnd()));
    }
}
