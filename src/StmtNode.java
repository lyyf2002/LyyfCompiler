import java.util.ArrayList;

public class StmtNode extends ASTNode{
    public ASTNode getStmtItem() {
        return stmtItem;
    }

    private ASTNode stmtItem;
    public StmtNode(Node stmt,SymbolTable symbolTable,boolean isFuncBlock,boolean isReturnInt,boolean isLoop) {
        super(symbolTable);
        ArrayList<Node> nodes = stmt.getSubNodes();
        if (nodes.get(0) instanceof NonTermianlNode){
                    /*
                    Stmt →
                    LVal '=' Exp ';'
                    LVal '=' 'getint''('')'';'
                    [Exp] ';'
                    Block
                     */
            if (((NonTermianlNode)nodes.get(0)).getNonTermianlName().equals("LVal")){
                stmtItem = new AssignNode(stmt,symbolTable);
            } else if (((NonTermianlNode)nodes.get(0)).getNonTermianlName().equals("Exp")){
                stmtItem = new ExpNode(nodes.get(0),symbolTable);
            } else {
                stmtItem = new BlockNode(nodes.get(0),new SymbolTable(symbolTable),isFuncBlock,isReturnInt,isLoop);
            }


        }
        else {/*
                     | 'if' '(' Cond ')' Stmt [ 'else' Stmt ] // 1.有else 2.⽆else//ifNode
                     | 'while' '(' Cond ')' Stmt//whileNode
                     | 'break' ';' | 'continue' ';'//breakNode //continueNode
                     | 'return' [Exp] ';' // 1.有Exp 2.⽆Exp//returnNode
                     | 'printf''('FormatString{','Exp}')'';' // 1.有Exp 2.⽆Exp // printfNode
                     */
            Symbol firstSymbol = ((TermianlNode)nodes.get(0)).getWord().getSymbol();
            if (firstSymbol==Symbol.IFTK){
                stmtItem =new IfNode(nodes,symbolTable,isReturnInt,isLoop);
            }
            else if(firstSymbol==Symbol.WHILETK){
                stmtItem =new WhileNode(nodes,symbolTable,isReturnInt);
            }
            else if (firstSymbol==Symbol.BREAKTK){
                stmtItem =new BreakNode(symbolTable);
                if (!isLoop){
                    Errors.addError(((TermianlNode)nodes.get(0)).getWord().getLineNum(),"m");
                }
            }
            else if (firstSymbol==Symbol.CONTINUETK){
                stmtItem =new ContinueNode(symbolTable);
                if (!isLoop){
                    Errors.addError(((TermianlNode)nodes.get(0)).getWord().getLineNum(),"m");
                }
            }
            else if (firstSymbol == Symbol.RETURNTK){
                stmtItem =new ReturnNode(nodes,symbolTable);
                if (!isReturnInt&&((ReturnNode)stmtItem).isReturnInt()){
                    Errors.addError(((TermianlNode)nodes.get(0)).getWord().getLineNum(),"f");
                }
            }
            else if (firstSymbol == Symbol.PRINTFTK){
                stmtItem =new PrintfNode(nodes,symbolTable);
            }
            else {
                stmtItem = new NullNode(symbolTable);
            }
        }
    }

    @Override
    public void intoMid() {
        stmtItem.intoMid();
    }
}
