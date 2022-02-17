import java.util.ArrayList;

public class WhileNode extends ASTNode{
    private CondNode condNode;
    private StmtNode loopStmt;

    public WhileNode(ArrayList<Node> nodes, SymbolTable symbolTable, boolean isReturnInt) {
        super(symbolTable);
        int i;
        for (i=2;i<nodes.size();i++){
            if (nodes.get(i) instanceof NonTermianlNode){
                condNode = new CondNode(nodes.get(i),symbolTable);
                break;
            }
        }
        for (i++;i<nodes.size();i++){
            if (nodes.get(i) instanceof NonTermianlNode){
                loopStmt = new StmtNode(nodes.get(i),symbolTable,false,isReturnInt,true);
                break;
            }
        }



    }

    @Override
    public void intoMid() {
        String begin="loop_begin"+IdCtrl.getLabelId();
        String end="loop_end"+IdCtrl.getLabelId();
        String THEN = "then"+IdCtrl.getLabelId();

        MidCode.add(new FourUnit("label",null,null,begin));
        condNode.cond2Mid(THEN,end);
        MidCode.add(new FourUnit("label",null,null,THEN));

        IdCtrl.put(begin,end);
        loopStmt.intoMid();
        IdCtrl.push();

        MidCode.add(new FourUnit("j",null,null,begin));
        MidCode.add(new FourUnit("label",null,null,end));

    }
}
