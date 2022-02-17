import javax.swing.table.TableRowSorter;
import java.util.ArrayList;

public class BlockNode extends ASTNode {
    private boolean isFuncBlock;
    private boolean isReturnInt;
    private boolean isLoop;
    private ArrayList<ASTNode> blockItems = new ArrayList<>();

    public BlockNode(Node root, SymbolTable symbolTable, boolean isFuncBlock, boolean isReturnInt, boolean isLoop) {
        super(symbolTable);
        this.isFuncBlock = isFuncBlock;
        this.isReturnInt = isReturnInt;
        ArrayList<Node> subnodes = root.getSubNodes();
        for (int i = 1; i < subnodes.size() - 1; i++) {
            Node stmt = subnodes.get(i).getSubNodes().get(0);
            if (((NonTermianlNode) stmt).getNonTermianlName().equals("ConstDecl")) {
                for (int j = 2; j < stmt.getSubNodes().size(); j += 2) {
                    blockItems.add(new ConstDeclNode(stmt.getSubNodes().get(j), symbolTable));
                }
            } else if (((NonTermianlNode) stmt).getNonTermianlName().equals("VarDecl")) {
                for (int j = 1; j < stmt.getSubNodes().size(); j += 2) {
                    blockItems.add(new VarDeclNode(stmt.getSubNodes().get(j), symbolTable));
                }
            } else {
                blockItems.add(new StmtNode(stmt, symbolTable, false, isReturnInt, isLoop));

            }
        }
        if (isFuncBlock && isReturnInt && blockItems.size() > 0 && blockItems.get(blockItems.size() - 1) instanceof StmtNode && !(((StmtNode) blockItems.get(blockItems.size() - 1)).getStmtItem() instanceof ReturnNode)) {
            Errors.addError(((TermianlNode) subnodes.get(subnodes.size() - 1)).getWord().getLineNum(), "g");
        } else if (isFuncBlock && isReturnInt && blockItems.size() == 0) {
            Errors.addError(((TermianlNode) subnodes.get(subnodes.size() - 1)).getWord().getLineNum(), "g");

        } else if (isFuncBlock && isReturnInt && !(blockItems.get(blockItems.size() - 1) instanceof StmtNode)){
            Errors.addError(((TermianlNode) subnodes.get(subnodes.size() - 1)).getWord().getLineNum(), "g");
        }

    }

    @Override
    public void intoMid() {
        for (ASTNode node:blockItems){
            node.intoMid();
        }
    }
}

