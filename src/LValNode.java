import java.util.ArrayList;

public class LValNode extends ASTExpNode {
    public DeclSymbolInfo getLvalInfo() {
        return lvalInfo;
    }

    //LVal → Ident {'[' Exp ']'}
    private DeclSymbolInfo lvalInfo;

    public int getLinenum() {
        return linenum;
    }

    private int linenum;

    public ArrayList<ExpNode> getDims() {
        return dims;
    }

    private ArrayList<ExpNode> dims = new ArrayList<>();

    //    private int dim;
    public LValNode(Node root, SymbolTable symbolTable) {
        super(symbolTable);
        ArrayList<Node> subnodes = root.getSubNodes();
        lvalInfo = (DeclSymbolInfo) symbolTable.find(((TermianlNode) subnodes.get(0)).getWord().getWord(), false);
        linenum = ((TermianlNode) subnodes.get(0)).getWord().getLineNum();
        if (lvalInfo == null) {
            Errors.addError(((TermianlNode) subnodes.get(0)).getWord().getLineNum(), "c");
        } else {
            for (int i = 2; i < subnodes.size() - 1; i++) {
                if (subnodes.get(i) instanceof NonTermianlNode) {
                    dims.add(new ExpNode(subnodes.get(i), symbolTable));
                }
            }
            dim = lvalInfo.dimNum - dims.size();
        }
    }

    @Override
    public int getConstValue() {
        if (dims.size() == 0)
            return ((ConstSymbolInfo) lvalInfo).getConstValue(0, 0);
        else if (dims.size() == 1) {
            return ((ConstSymbolInfo) lvalInfo).getConstValue(0, dims.get(0).getConstValue());
        } else {
            return ((ConstSymbolInfo) lvalInfo).getConstValue(dims.get(0).getConstValue(), dims.get(1).getConstValue());
        }
    }

    @Override
    public String getMidCode() {
        if (dim == 0) {
            if (lvalInfo instanceof ConstSymbolInfo && dims.size() == 0) {

                return String.valueOf(getConstValue());
            }

            if (dims.size() == 0) {
                return lvalInfo.Strid;
            } else if (dims.size() == 1) {
                String t = "t_" + IdCtrl.getTempId();
                MidCode.add(new FourUnit("getarr", lvalInfo.Strid, dims.get(0).getMidCode(), t));
                return t;
            } else {
                String t = "t_" + IdCtrl.getTempId();
                String i = dims.get(0).getMidCode();
                if (!(i.startsWith("a") || i.startsWith("c") || i.startsWith("t"))) {
                    int arrindex = Integer.parseInt(i) * lvalInfo.dim2;
                    String offset = dims.get(1).getMidCode();
                    if (!(offset.startsWith("a") || offset.startsWith("c") || offset.startsWith("t"))) {
                        arrindex += Integer.parseInt(offset);
                        MidCode.add(new FourUnit("getarr", lvalInfo.Strid, String.valueOf(arrindex), t));
                    } else {
                        MidCode.add(new FourUnit("add", offset, String.valueOf(arrindex), t));
                        MidCode.add(new FourUnit("getarr", lvalInfo.Strid, t, t));
                    }
                } else {
                    MidCode.add(new FourUnit("mul", i, String.valueOf(lvalInfo.dim2), t));
                    MidCode.add(new FourUnit("add", t, dims.get(1).getMidCode(), t));
                    MidCode.add(new FourUnit("getarr", lvalInfo.Strid, t, t));
                }


                return t;
            }
        } else if (dim == 1) {
            if (dims.size() == 1) {
                String t = "t_" + IdCtrl.getTempId();
                String i = dims.get(0).getMidCode();
                if (!(i.startsWith("a") || i.startsWith("c") || i.startsWith("t"))) {
                    MidCode.add(new FourUnit("getadd", lvalInfo.Strid, String.valueOf(Integer.parseInt(i) * lvalInfo.dim2), t));
                } else {
                    MidCode.add(new FourUnit("mul", i, String.valueOf(lvalInfo.dim2), t));
                    MidCode.add(new FourUnit("getadd", lvalInfo.Strid, t, t));
                }

                return t;
            } else {
                String t = "t_" + IdCtrl.getTempId();
                MidCode.add(new FourUnit("getadd", lvalInfo.Strid, "0", t));
                return t;
            }
        } else {
            String t = "t_" + IdCtrl.getTempId();
            MidCode.add(new FourUnit("getadd", lvalInfo.Strid, "0", t));
            return t;
        }

    }

    @Override
    public void intoMid() {

    }
}
