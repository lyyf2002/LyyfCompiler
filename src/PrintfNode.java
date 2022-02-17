import java.util.ArrayList;

public class PrintfNode extends ASTNode{
    private String formatString;
    private int numOfd;
    private ArrayList<ExpNode> exps=new ArrayList<>();
    public PrintfNode(ArrayList<Node> nodes,SymbolTable symbolTable) {
        super(symbolTable);
        formatString = ((TermianlNode)nodes.get(2)).getWord().getWord();
        numOfd = formatString.split("%d").length - 1;
        for (int i = 4;i<nodes.size()-1;i+=2){
            if (nodes.get(i) instanceof NonTermianlNode){
                exps.add(new ExpNode(nodes.get(i),symbolTable));
            }
        }
        if (exps.size()!=numOfd){
            Errors.addError(((TermianlNode)nodes.get(0)).getWord().getLineNum(),"l");
        }
    }

    @Override
    public void intoMid() {
        int exp_i=exps.size()-1;
        String temp="";
        ArrayList<String > expt = new ArrayList<>();
        for (int i=exps.size()-1;i>=0;i--){
            expt.add(exps.get(i).getMidCode());
        }
        for (int i = 0 ;i<formatString.length();i++){
            if (formatString.charAt(i)!='%'){
                temp = temp.concat(String.valueOf(formatString.charAt(i)));
            }
            else {
                if (temp.replace("\"","").length()>0){
                    MidCode.add(new FourUnit("printstr",null,null,IdCtrl.putFormatStr(temp.replace("\"",""))));
                    temp="";
                }
                i++;
                MidCode.add(new FourUnit("printint",null,null,expt.get(exp_i)));
                exp_i--;
            }
        }
        if (temp.replace("\"","").length()>0){
            MidCode.add(new FourUnit("printstr",null,null,IdCtrl.putFormatStr(temp.replace("\"",""))));
            temp="";
        }
//        String []strs = formatString.split("%d");
//        if (!strs[0].equals("\"")){
//            MidCode.add(new FourUnit("printstr",null,null,IdCtrl.putFormatStr(strs[0].replace("\"",""))));
//        }
//        for (int i=1;i<strs.length; i++){
//            MidCode.add(new FourUnit("printint",null,null,exps.get(i-1).getMidCode()));
//            if (i == strs.length-1){
//                if (!strs[i].equals("\"")){
//                    MidCode.add(new FourUnit("printstr",null,null,IdCtrl.putFormatStr(strs[i].substring(0,strs.length))));
//                }
//            }else
//            MidCode.add(new FourUnit("printstr",null,null,IdCtrl.putFormatStr(strs[i])));
//
//        }

    }
}
