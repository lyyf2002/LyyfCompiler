import java.util.ArrayList;

public class AssignNode extends ASTNode{
    private LValNode lValNode;
    private ExpNode exp=null;
    private boolean isGetInt=false;
    public AssignNode(Node root,SymbolTable symbolTable) {
        super(symbolTable);
        ArrayList<Node> subnodes = root.getSubNodes();
        lValNode = new LValNode(subnodes.get(0),symbolTable);
        DeclSymbolInfo lvalInfo = lValNode.getLvalInfo();
        if (lvalInfo!=null){
            if (lvalInfo instanceof ConstSymbolInfo){
                Errors.addError(lValNode.getLinenum(),"h");
            }
            else {
                if (subnodes.get(2) instanceof TermianlNode){
                    isGetInt = true;
                }
                else {
                    exp = new ExpNode(subnodes.get(2),symbolTable);
                }
            }
        }
    }

    @Override
    public void intoMid() {
        if (lValNode.getLvalInfo().dimNum==0){
            MidCode.add(new FourUnit("ass",isGetInt?"getint":exp.getMidCode(),null,lValNode.getLvalInfo().Strid));
        }
        else if (lValNode.getLvalInfo().dimNum==1){
            MidCode.add(new FourUnit("assarr",lValNode.getDims().get(0).getMidCode(),isGetInt?"getint":exp.getMidCode(),lValNode.getLvalInfo().Strid));

        }
        else {
            //result[arg1] = arg2
            String t = "t_"+IdCtrl.getTempId();
            String i = lValNode.getDims().get(0).getMidCode();
            if (!(i.startsWith("a")||i.startsWith("c")||i.startsWith("t"))){
                int arrindex = Integer.parseInt(i)*lValNode.getLvalInfo().dim2;
                String offset = lValNode.getDims().get(1).getMidCode();
                if (!(offset.startsWith("a")||offset.startsWith("c")||offset.startsWith("t"))){
                    arrindex+=Integer.parseInt(offset);
//                    MidCode.add(new FourUnit("getarr",lValNode.getLvalInfo().Strid,String.valueOf(arrindex),t));
                    MidCode.add(new FourUnit("assarr",String.valueOf(arrindex),isGetInt?"getint":exp.getMidCode(),lValNode.getLvalInfo().Strid));

                }
                else {
                    MidCode.add(new FourUnit("add",offset,String.valueOf(arrindex),t));
//                    MidCode.add(new FourUnit("getarr",lValNode.getLvalInfo().Strid,t,t));
                    MidCode.add(new FourUnit("assarr",t,isGetInt?"getint":exp.getMidCode(),lValNode.getLvalInfo().Strid));

                }
            }
            else {
                MidCode.add(new FourUnit("mul",i,String.valueOf(lValNode.getLvalInfo().dim2),t));
                MidCode.add(new FourUnit("add",t,lValNode.getDims().get(1).getMidCode(),t));
                MidCode.add(new FourUnit("assarr",t,isGetInt?"getint":exp.getMidCode(),lValNode.getLvalInfo().Strid));

            }


        }

    }
}
