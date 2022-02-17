import java.util.ArrayList;

public class Parser {
    public NonTermianlNode getRoot() {
        return root;
    }

    private NonTermianlNode root;

    public CompUnitNode getAstRoot() {
        return astRoot;
    }

    private CompUnitNode astRoot;
    private int curSym;
    private ArrayList<Word> words;
    public Parser(ArrayList<Word> words){
        this.words = words;
    }
    public void printParser(){
        root.print();
    }
    public Symbol getSym(int index){
        if (index >= words.size()){
            return Symbol.EOF;
        }
        return words.get(index).getSymbol();
    }
    public void parsing(){
        root = new NonTermianlNode("CompUnit");
        curSym = 0;
        while (getSym(curSym) == Symbol.CONSTTK || getSym(curSym) == Symbol.INTTK||getSym(curSym) == Symbol.VOIDTK){
            if (getSym(curSym) == Symbol.CONSTTK){
                root.addSubNode(ConstDecl());
            }
            else if (getSym(curSym) == Symbol.INTTK&&getSym(curSym+1) == Symbol.MAINTK) {
                root.addSubNode(MainFuncDef());

            }
            else if (getSym(curSym) == Symbol.INTTK&&getSym(curSym+2) != Symbol.LPARENT){
                root.addSubNode(VarDecl());
            }
            else root.addSubNode(FuncDef());

        }
        astRoot = new CompUnitNode(root,new SymbolTable(null));


    }
    public Node FuncDef(){
        NonTermianlNode node = new NonTermianlNode("FuncDef");
        node.addSubNode(FuncType());
        if (getSym(curSym) == Symbol.IDENFR){
            node.addSubNode(new TermianlNode(words.get(curSym)));
            curSym++;
            if (getSym(curSym) == Symbol.LPARENT){
                node.addSubNode(new TermianlNode(words.get(curSym)));
                curSym++;
                if (getSym(curSym) == Symbol.RPARENT){
                    node.addSubNode(new TermianlNode(words.get(curSym)));
                    curSym++;
                }
                else {
                    node.addSubNode(FuncFParams());
                    if (getSym(curSym) == Symbol.RPARENT){
                        node.addSubNode(new TermianlNode(words.get(curSym)));
                        curSym++;
                    }
                    else error(words.get(curSym-1).getLineNum(),"j");
                }
                node.addSubNode(Block());
            }
            else error();
        }
        else error();
        return node;

    }

    public Node FuncFParams(){
        NonTermianlNode node = new NonTermianlNode("FuncFParams");
        node.addSubNode(FuncFParam());
        while (getSym(curSym) == Symbol.COMMA){
            node.addSubNode(new TermianlNode(words.get(curSym)));
            curSym++;
            node.addSubNode(FuncFParam());
        }
        if (node.getSubNodes().size()==0){
            return null;
        }
        return node;

    }
    public Node FuncFParam(){
        NonTermianlNode node = new NonTermianlNode("FuncFParam");
        if (getSym(curSym) == Symbol.INTTK){
            node.addSubNode(new TermianlNode(words.get(curSym)));
            curSym++;
            if (getSym(curSym) == Symbol.IDENFR){
                node.addSubNode(new TermianlNode(words.get(curSym)));
                curSym++;
                if (getSym(curSym) == Symbol.LBRACK){
                    node.addSubNode(new TermianlNode(words.get(curSym)));
                    curSym++;
                    if (getSym(curSym) == Symbol.RBRACK){
                        node.addSubNode(new TermianlNode(words.get(curSym)));
                        curSym++;

                    }
                    else error(words.get(curSym-1).getLineNum(),"k");
                    if (getSym(curSym) == Symbol.LBRACK){
                        node.addSubNode(new TermianlNode(words.get(curSym)));
                        curSym++;
                        node.addSubNode(ConstExp());
                        if (getSym(curSym) == Symbol.RBRACK){
                            node.addSubNode(new TermianlNode(words.get(curSym)));
                            curSym++;
                        }
                        else error(words.get(curSym-1).getLineNum(),"k");
                    }
                }
            }
            else error();
        }
        else {
            error();
            return null;
        }
        return node;
    }
    public Node Block(){
        NonTermianlNode node = new NonTermianlNode("Block");
        if (getSym(curSym) == Symbol.LBRACE){
            node.addSubNode(new TermianlNode(words.get(curSym)));
            curSym++;
            while (getSym(curSym) != Symbol.RBRACE){
                node.addSubNode(BlockItem());
            }
            node.addSubNode(new TermianlNode(words.get(curSym)));
            curSym++;
        }
        else error();
        return node;
    }
    public Node BlockItem(){
        NonTermianlNode node = new NonTermianlNode("BlockItem");
        if (getSym(curSym) == Symbol.CONSTTK){
            node.addSubNode(ConstDecl());
        }else if (getSym(curSym) == Symbol.INTTK){
            node.addSubNode(VarDecl());
        }else node.addSubNode(Stmt());
        return node;
    }
    public Node Stmt(){
        NonTermianlNode node = new NonTermianlNode("Stmt");
        if (getSym(curSym) == Symbol.SEMICN){
            node.addSubNode(new TermianlNode(words.get(curSym)));
            curSym++;
        }
        else if(getSym(curSym) == Symbol.LBRACE){
            node.addSubNode(Block());
        }
        else if(getSym(curSym) == Symbol.IFTK){
            node.addSubNode(new TermianlNode(words.get(curSym)));
            curSym++;
            if (getSym(curSym) == Symbol.LPARENT){
                node.addSubNode(new TermianlNode(words.get(curSym)));
                curSym++;
                node.addSubNode(Cond());
                if (getSym(curSym) == Symbol.RPARENT){
                    node.addSubNode(new TermianlNode(words.get(curSym)));
                    curSym++;
                }
                else error(words.get(curSym-1).getLineNum(),"j");
                node.addSubNode(Stmt());
                if (getSym(curSym) == Symbol.ELSETK){
                    node.addSubNode(new TermianlNode(words.get(curSym)));
                    curSym++;
                    node.addSubNode(Stmt());
                }
            }
            else error();
        }
        else if (getSym(curSym) == Symbol.WHILETK){
            node.addSubNode(new TermianlNode(words.get(curSym)));
            curSym++;
            if (getSym(curSym) == Symbol.LPARENT){
                node.addSubNode(new TermianlNode(words.get(curSym)));
                curSym++;
                node.addSubNode(Cond());
                if (getSym(curSym) == Symbol.RPARENT){
                    node.addSubNode(new TermianlNode(words.get(curSym)));
                    curSym++;
                }
                else error(words.get(curSym-1).getLineNum(),"j");
                node.addSubNode(Stmt());
            }
            else error();
        }
        else if (getSym(curSym) == Symbol.BREAKTK){
            node.addSubNode(new TermianlNode(words.get(curSym)));
            curSym++;
            if (getSym(curSym) == Symbol.SEMICN){
                node.addSubNode(new TermianlNode(words.get(curSym)));
                curSym++;
            }else error(words.get(curSym-1).getLineNum(),"i");
        }
        else if (getSym(curSym) == Symbol.CONTINUETK){
            node.addSubNode(new TermianlNode(words.get(curSym)));
            curSym++;
            if (getSym(curSym) == Symbol.SEMICN){
                node.addSubNode(new TermianlNode(words.get(curSym)));
                curSym++;
            }else error(words.get(curSym-1).getLineNum(),"i");
        }
        else if (getSym(curSym) == Symbol.RETURNTK){
            node.addSubNode(new TermianlNode(words.get(curSym)));
            curSym++;
            if (getSym(curSym) == Symbol.SEMICN){
                node.addSubNode(new TermianlNode(words.get(curSym)));
                curSym++;
            }else{
                node.addSubNode(Exp());
                if (getSym(curSym) == Symbol.SEMICN){
                    node.addSubNode(new TermianlNode(words.get(curSym)));
                    curSym++;
                }else error(words.get(curSym-1).getLineNum(),"i");
            }
        }
        else if (getSym(curSym) == Symbol.PRINTFTK){
            node.addSubNode(new TermianlNode(words.get(curSym)));
            curSym++;
            if (getSym(curSym) == Symbol.LPARENT){
                node.addSubNode(new TermianlNode(words.get(curSym)));
                curSym++;
                if (getSym(curSym) == Symbol.STRCON){
                    node.addSubNode(new TermianlNode(words.get(curSym)));
                    curSym++;
                }
                else error();
                while (getSym(curSym) == Symbol.COMMA){
                    node.addSubNode(new TermianlNode(words.get(curSym)));
                    curSym++;
                    node.addSubNode(Exp());
                }
                if (getSym(curSym) == Symbol.RPARENT){
                    node.addSubNode(new TermianlNode(words.get(curSym)));
                    curSym++;
                }
                else error(words.get(curSym-1).getLineNum(),"j");
                if (getSym(curSym) == Symbol.SEMICN){
                    node.addSubNode(new TermianlNode(words.get(curSym)));
                    curSym++;
                }
                else error(words.get(curSym-1).getLineNum(),"i");
            }
            else error();
        }
        else if (getSym(curSym) != Symbol.IDENFR || (getSym(curSym) == Symbol.IDENFR&&getSym(curSym+1) == Symbol.LPARENT)){
            node.addSubNode(Exp());
            if (getSym(curSym) == Symbol.SEMICN){
                node.addSubNode(new TermianlNode(words.get(curSym)));
                curSym++;
            }else error(words.get(curSym-1).getLineNum(),"i");
        }
        else {
            int temp = curSym;
            Node LvalNode = LVal();
            if (getSym(curSym) == Symbol.ASSIGN){
                if (getSym(curSym+1) == Symbol.GETINTTK){
                    node.addSubNode(LvalNode);
                    node.addSubNode(new TermianlNode(words.get(curSym)));
                    curSym++;
                    node.addSubNode(new TermianlNode(words.get(curSym)));
                    curSym++;
                    if (getSym(curSym) == Symbol.LPARENT){
                        node.addSubNode(new TermianlNode(words.get(curSym)));
                        curSym++;
                        if (getSym(curSym) == Symbol.RPARENT){
                            node.addSubNode(new TermianlNode(words.get(curSym)));
                            curSym++;
                        }
                        else error(words.get(curSym-1).getLineNum(),"j");
                        if (getSym(curSym) == Symbol.SEMICN){
                            node.addSubNode(new TermianlNode(words.get(curSym)));
                            curSym++;
                        }
                        else error(words.get(curSym-1).getLineNum(),"i");
                    }
                    else error();
                }
                else {
                    node.addSubNode(LvalNode);
                    node.addSubNode(new TermianlNode(words.get(curSym)));
                    curSym++;
                    node.addSubNode(Exp());
                    if (getSym(curSym) == Symbol.SEMICN){
                        node.addSubNode(new TermianlNode(words.get(curSym)));
                        curSym++;
                    }
                    else error(words.get(curSym-1).getLineNum(),"i");
                }
            }else{
                curSym = temp;
                node.addSubNode(Exp());
                if (getSym(curSym) == Symbol.SEMICN){
                    node.addSubNode(new TermianlNode(words.get(curSym)));
                    curSym++;
                }
                else error(words.get(curSym-1).getLineNum(),"i");
            }
        }
        return node;
    }
    public Node Cond(){
        NonTermianlNode node = new NonTermianlNode("Cond");
        node.addSubNode(LOrExp());
        return node;
    }
    public Node LOrExp(){
        NonTermianlNode node = new NonTermianlNode("LOrExp");
        Node root = node;
        node.addSubNode(LAndExp());
        while (getSym(curSym) == Symbol.OR){
            node = new NonTermianlNode("LOrExp");
            node.addSubNode(root);
            node.addSubNode(new TermianlNode(words.get(curSym)));
            curSym++;
            node.addSubNode(LAndExp());
            root = node;
        }
        return root;

    }
    public Node LAndExp(){
        NonTermianlNode node = new NonTermianlNode("LAndExp");
        Node root = node;
        node.addSubNode(EqExp());
        while (getSym(curSym) == Symbol.AND){
            node = new NonTermianlNode("LAndExp");
            node.addSubNode(root);
            node.addSubNode(new TermianlNode(words.get(curSym)));
            curSym++;
            node.addSubNode(EqExp());
            root = node;
        }
        return root;
    }
    public Node EqExp(){
        NonTermianlNode node = new NonTermianlNode("EqExp");
        Node root = node;
        node.addSubNode(RelExp());
        while (getSym(curSym) == Symbol.EQL || getSym(curSym) == Symbol.NEQ){
            node = new NonTermianlNode("EqExp");
            node.addSubNode(root);
            node.addSubNode(new TermianlNode(words.get(curSym)));
            curSym++;
            node.addSubNode(RelExp());
            root = node;
        }
        return root;
    }
    public Node RelExp(){
        NonTermianlNode node = new NonTermianlNode("RelExp");
        Node root = node;
        node.addSubNode(AddExp());
        while (getSym(curSym) == Symbol.LSS || getSym(curSym) == Symbol.LEQ || getSym(curSym) == Symbol.GRE || getSym(curSym) == Symbol.GEQ){
            node = new NonTermianlNode("RelExp");
            node.addSubNode(root);
            node.addSubNode(new TermianlNode(words.get(curSym)));
            curSym++;
            node.addSubNode(AddExp());
            root = node;
        }
        return root;
    }
    public Node FuncType(){
        NonTermianlNode node = new NonTermianlNode("FuncType");
        if (getSym(curSym) == Symbol.VOIDTK ||getSym(curSym) == Symbol.INTTK){
            node.addSubNode(new TermianlNode(words.get(curSym)));
            curSym++;
        }else error();
        return node;

    }
    public Node VarDecl(){
        NonTermianlNode node = new NonTermianlNode("VarDecl");
        if (getSym(curSym) == Symbol.INTTK){
            node.addSubNode(new TermianlNode(words.get(curSym)));
            curSym++;
            node.addSubNode(VarDef());
            while (getSym(curSym) == Symbol.COMMA){
                node.addSubNode(new TermianlNode(words.get(curSym)));
                curSym++;
                node.addSubNode(VarDef());
            }
            if (getSym(curSym) == Symbol.SEMICN){
                node.addSubNode(new TermianlNode(words.get(curSym)));
                curSym++;
            }
            else error(words.get(curSym-1).getLineNum(),"i");
        }
        else error();
        return node;

    }
    public Node VarDef(){
        NonTermianlNode node = new NonTermianlNode("VarDef");
        if (getSym(curSym) == Symbol.IDENFR) {
            node.addSubNode(new TermianlNode(words.get(curSym)));
            curSym++;
            while (getSym(curSym) == Symbol.LBRACK){
                node.addSubNode(new TermianlNode(words.get(curSym)));
                curSym++;
                node.addSubNode(ConstExp());
                if (getSym(curSym) == Symbol.RBRACK){
                    node.addSubNode(new TermianlNode(words.get(curSym)));
                    curSym++;
                }
                else error(words.get(curSym-1).getLineNum(),"k");
            }
            if (getSym(curSym) == Symbol.ASSIGN){
                node.addSubNode(new TermianlNode(words.get(curSym)));
                curSym++;
                node.addSubNode(InitVal());
            }
        }
        else error();
        return node;

    }
    public Node InitVal(){
        NonTermianlNode node = new NonTermianlNode("InitVal");
        if(getSym(curSym) == Symbol.LBRACE){
            node.addSubNode(new TermianlNode(words.get(curSym)));
            curSym++;
            if (getSym(curSym) == Symbol.RBRACE){
                node.addSubNode(new TermianlNode(words.get(curSym)));
                curSym++;
            }
            else {
                node.addSubNode(InitVal());
                while (getSym(curSym) == Symbol.COMMA){
                    node.addSubNode(new TermianlNode(words.get(curSym)));
                    curSym++;
                    node.addSubNode(InitVal());
                }
                if (getSym(curSym) == Symbol.RBRACE){
                    node.addSubNode(new TermianlNode(words.get(curSym)));
                    curSym++;
                }
                else error();
            }
        }
        else node.addSubNode(Exp());
        return node;

    }
    public Node MainFuncDef(){
        NonTermianlNode node = new NonTermianlNode("MainFuncDef");
        if (getSym(curSym) == Symbol.INTTK){
            node.addSubNode(new TermianlNode(words.get(curSym)));
            curSym++;
            if (getSym(curSym) == Symbol.MAINTK){
                node.addSubNode(new TermianlNode(words.get(curSym)));
                curSym++;
                if (getSym(curSym) == Symbol.LPARENT){
                    node.addSubNode(new TermianlNode(words.get(curSym)));
                    curSym++;
                    if (getSym(curSym) == Symbol.RPARENT){
                        node.addSubNode(new TermianlNode(words.get(curSym)));
                        curSym++;

                    }
                    else error(words.get(curSym-1).getLineNum(),"j");
                    node.addSubNode(Block());
                }
                else error();
            }
            else error();
        }
        else error();
        return node;

    }
    public Node ConstDecl(){
        NonTermianlNode node = new NonTermianlNode("ConstDecl");
        node.addSubNode(new TermianlNode(words.get(curSym)));
        curSym++;
        if (getSym(curSym) == Symbol.INTTK){
            node.addSubNode(new TermianlNode(words.get(curSym)));
            curSym++;
            node.addSubNode(ConstDef());
            while (getSym(curSym) == Symbol.COMMA){
                node.addSubNode(new TermianlNode(words.get(curSym)));
                curSym++;
                node.addSubNode(ConstDef());
            }
            if (getSym(curSym) == Symbol.SEMICN){
                node.addSubNode(new TermianlNode(words.get(curSym)));
                curSym++;
            }
            else error(words.get(curSym-1).getLineNum(),"i");
        }
        else error();
        return node;
    }
    public Node ConstDef(){
        NonTermianlNode node = new NonTermianlNode("ConstDef");
        if (getSym(curSym) == Symbol.IDENFR) {
            node.addSubNode(new TermianlNode(words.get(curSym)));
            curSym++;
            while (getSym(curSym) == Symbol.LBRACK){
                node.addSubNode(new TermianlNode(words.get(curSym)));
                curSym++;
                node.addSubNode(ConstExp());
                if (getSym(curSym) == Symbol.RBRACK){
                    node.addSubNode(new TermianlNode(words.get(curSym)));
                    curSym++;
                }
                else error(words.get(curSym-1).getLineNum(),"k");
            }
            if (getSym(curSym) == Symbol.ASSIGN){
                node.addSubNode(new TermianlNode(words.get(curSym)));
                curSym++;
                node.addSubNode(ConstInitVal());
            }
            else error();

        }
        else error();
        return node;
    }
    public Node ConstExp(){
        NonTermianlNode node = new NonTermianlNode("ConstExp");
        node.addSubNode(AddExp());
        return node;
    }

    public Node AddExp(){
        NonTermianlNode node = new NonTermianlNode("AddExp");
        Node root = node;
        node.addSubNode(MulExp());
        while (getSym(curSym) == Symbol.PLUS || getSym(curSym) == Symbol.MINU){
            node = new NonTermianlNode("AddExp");
            node.addSubNode(root);
            node.addSubNode(new TermianlNode(words.get(curSym)));
            curSym++;
            node.addSubNode(MulExp());
            root = node;
        }
        if (root.getSubNodes().size()==0){
            return null;
        }
        return root;
    }
    public Node MulExp(){
        NonTermianlNode node = new NonTermianlNode("MulExp");
        Node root = node;
        node.addSubNode(UnaryExp());
        while (getSym(curSym) == Symbol.MULT || getSym(curSym) == Symbol.DIV|| getSym(curSym) == Symbol.MOD){
            node = new NonTermianlNode("MulExp");
            node.addSubNode(root);
            node.addSubNode(new TermianlNode(words.get(curSym)));
            curSym++;
            node.addSubNode(UnaryExp());
            root = node;
        }
        if (root.getSubNodes().size()==0){
            return null;
        }
        return root;
    }
    public Node UnaryExp(){
        NonTermianlNode node = new NonTermianlNode("UnaryExp");
        if (getSym(curSym) == Symbol.NOT||getSym(curSym) == Symbol.PLUS || getSym(curSym) == Symbol.MINU){
            node.addSubNode(UnaryOp());
            node.addSubNode(UnaryExp());
        }
        else if(getSym(curSym) == Symbol.IDENFR&&getSym(curSym+1) == Symbol.LPARENT){
            node.addSubNode(new TermianlNode(words.get(curSym)));
            curSym++;
            node.addSubNode(new TermianlNode(words.get(curSym)));
            curSym++;
            if (getSym(curSym) == Symbol.RPARENT){
                node.addSubNode(new TermianlNode(words.get(curSym)));
                curSym++;
            }
            else {
                node.addSubNode(FuncRParams());
                if (getSym(curSym) == Symbol.RPARENT){
                    node.addSubNode(new TermianlNode(words.get(curSym)));
                    curSym++;
                }
                else error(words.get(curSym-1).getLineNum(),"j");
            }
        }
        else node.addSubNode(PrimaryExp());
        if (node.getSubNodes().size()==0){
            return null;
        }
        return node;
    }
    public Node FuncRParams(){
        NonTermianlNode node = new NonTermianlNode("FuncRParams");
        node.addSubNode(Exp());
        while (getSym(curSym) == Symbol.COMMA){
            node.addSubNode(new TermianlNode(words.get(curSym)));
            curSym++;
            node.addSubNode(Exp());
        }
        return node;
    }
    public Node PrimaryExp(){
        NonTermianlNode node = new NonTermianlNode("PrimaryExp");
        if (getSym(curSym) == Symbol.LPARENT){
            node.addSubNode(new TermianlNode(words.get(curSym)));
            curSym++;
            node.addSubNode(Exp());
            if (getSym(curSym) == Symbol.RPARENT){
                node.addSubNode(new TermianlNode(words.get(curSym)));
                curSym++;
            }
            else error(words.get(curSym-1).getLineNum(),"j");
        }
        else if (getSym(curSym) == Symbol.INTCON){
            node.addSubNode(Number());
        }
        else node.addSubNode(LVal());
        if (node.getSubNodes().size() == 0){
            return null;
        }
        return node;
    }
    public Node UnaryOp(){
        NonTermianlNode node = new NonTermianlNode("UnaryOp");
        node.addSubNode(new TermianlNode(words.get(curSym)));
        curSym++;
        return node;
    }
    public Node Exp(){
        NonTermianlNode node = new NonTermianlNode("Exp");
        node.addSubNode(AddExp());
        if (node.getSubNodes().size()==0){
            return null;
        }
        return node;
    }
    public Node LVal(){
        NonTermianlNode node = new NonTermianlNode("LVal");
        if (getSym(curSym) == Symbol.IDENFR){
            node.addSubNode(new TermianlNode(words.get(curSym)));
            curSym++;
            while (getSym(curSym) == Symbol.LBRACK){
                node.addSubNode(new TermianlNode(words.get(curSym)));
                curSym++;
                node.addSubNode(Exp());
                if (getSym(curSym) == Symbol.RBRACK){
                    node.addSubNode(new TermianlNode(words.get(curSym)));
                    curSym++;
                }
                else error(words.get(curSym-1).getLineNum(),"k");
            }
        }
        else {
            error();
            return null;
        }
        return node;

    }
    public Node Number(){
        NonTermianlNode node = new NonTermianlNode("Number");
        node.addSubNode(new TermianlNode(words.get(curSym)));
        curSym++;
        return node;
    }
    public Node ConstInitVal(){
        NonTermianlNode node = new NonTermianlNode("ConstInitVal");
        if(getSym(curSym) == Symbol.LBRACE){
            node.addSubNode(new TermianlNode(words.get(curSym)));
            curSym++;
            if (getSym(curSym) == Symbol.RBRACE){
                node.addSubNode(new TermianlNode(words.get(curSym)));
                curSym++;
            }
            else {
                node.addSubNode(ConstInitVal());
                while (getSym(curSym) == Symbol.COMMA){
                    node.addSubNode(new TermianlNode(words.get(curSym)));
                    curSym++;
                    node.addSubNode(ConstInitVal());
                }
                if (getSym(curSym) == Symbol.RBRACE){
                    node.addSubNode(new TermianlNode(words.get(curSym)));
                    curSym++;
                }
                else error();
            }
        }
        else node.addSubNode(ConstExp());
        return node;

    }

    public void error(){
        System.out.println("_________________________________ERROR___________________________________" + curSym);
    }
    public void error(int line,String errorStr){
        Errors.addError(line,errorStr);
    }

}
