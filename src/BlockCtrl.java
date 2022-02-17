import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

public class BlockCtrl {
    public HashMap<String, ArrayList<BasicBlock>> getFunc2blocks() {
        return func2blocks;
    }

    private HashMap<String,ArrayList<BasicBlock>> func2blocks;
    private ArrayList<FourUnit> midCode;
    public BlockCtrl(ArrayList<FourUnit> midCode){
        this.midCode=midCode;

        func2blocks = new HashMap<>();
        func2blocks.put("decl",new ArrayList<>());
        BasicBlock declBlock = new BasicBlock();
        int i=0;
        for (;i<midCode.size();i++){

            if (midCode.get(i).getOp().equals("func")){
                break;
            }
            else {
                declBlock.addFourUnit(midCode.get(i));
            }
        }
        func2blocks.get("decl").add(declBlock);
        String curFunc="";
        BasicBlock curBlock=null;
        HashMap<String,Integer> label2blockIndex = new HashMap<>();
        for (;i<midCode.size();i++){
            if (midCode.get(i).getOp().equals("func")){
                if (curBlock!=null) func2blocks.get(curFunc).add(curBlock);
                curBlock = null;
                curFunc = midCode.get(i).getResult();
                func2blocks.put(curFunc,new ArrayList<>());
//                label2blockIndex = new HashMap<>();
                continue;
            }
            if ((i-1>=0&&midCode.get(i-1).getOp().equals("func"))||(midCode.get(i).getOp().equals("label"))||(i-1>=0&&midCode.get(i-1).getOp().startsWith("j"))){
                if (curBlock!=null) func2blocks.get(curFunc).add(curBlock);
                curBlock=new BasicBlock();

                if (midCode.get(i).getOp().equals("label")){
                    label2blockIndex.put(midCode.get(i).getResult(),func2blocks.get(curFunc).size());
                }
            }
            curBlock.addFourUnit(midCode.get(i));
        }
        if (curBlock!=null) func2blocks.get(curFunc).add(curBlock);
        this.func2blocks.forEach((k, v)->{
            if (!k.equals("decl")){
                for (int index = 0;index<v.size();index++){
                    if (v.get(index).getLastOne().getOp().startsWith("j")){
                        if (v.get(index).getLastOne().getOp().equals("j")) {
                            v.get(label2blockIndex.get(v.get(index).getLastOne().getResult())).addPre(v.get(index));
                            v.get(index).addSuf(v.get(label2blockIndex.get(v.get(index).getLastOne().getResult())));
                        }
                        else {
                            v.get(label2blockIndex.get(v.get(index).getLastOne().getResult())).addPre(v.get(index));
                            v.get(index).addSuf(v.get(label2blockIndex.get(v.get(index).getLastOne().getResult())));
                            if (index!=v.size()-1){
                                v.get(index+1).addPre(v.get(index));
                                v.get(index).addSuf(v.get(index+1));
                            }
                        }
                    }
                    else {
                        if (index!=v.size()-1){
                            v.get(index+1).addPre(v.get(index));
                            v.get(index).addSuf(v.get(index+1));
                        }
                    }
                }
            }

        });
    }

    public void printBlocks(){
        for (Object s: func2blocks.keySet().stream().sorted().toArray()){
            System.out.println(s+":");
            for (BasicBlock block:func2blocks.get(s)){
                block.printBlock();
            }
            System.out.println();
        }
    }
}
