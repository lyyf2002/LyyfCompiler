import java.util.ArrayList;
import java.util.HashMap;

public class DAG {
    private HashMap<String ,Integer> value2index;
    private ArrayList<DAGNode> nodes;
    public DAG(ArrayList<FourUnit> items){
        value2index = new HashMap<>();
        nodes = new ArrayList<>();
        for (FourUnit unit:items){
            int i,j,k=-1;
            if (value2index.containsKey(unit.getArg1())){
                i = value2index.get(unit.getArg1());
            }
            else {
                nodes.add(new DAGNode(unit.getArg1()));
                value2index.put(unit.getArg1(),nodes.size()-1);
                i = nodes.size()-1;
            }
            if (value2index.containsKey(unit.getArg2())){
                j = value2index.get(unit.getArg2());
            }
            else {
                nodes.add(new DAGNode(unit.getArg2()));
                value2index.put(unit.getArg2(),nodes.size()-1);
                j = nodes.size()-1;
            }
            for (int t =0 ;t<nodes.size();t++){
                DAGNode node = nodes.get(t);
                if (node.getName().equals(unit.getOp())&&node.getL() == i&&node.getR() == j){
                    k=t;
                }
            }
            if (k == -1){
                k = nodes.size();
                nodes.add(new DAGNode(unit.getOp(),i,j));
            }
            value2index.put(unit.getResult(),k);
        }
    }
    public void back2Mid(){

    }
}
