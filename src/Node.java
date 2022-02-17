import java.util.ArrayList;

public abstract class Node {
    public ArrayList<Node> getSubNodes() {
        return subNodes;
    }

    private ArrayList<Node> subNodes;
    public void addSubNode(Node node){
        if (node!=null)
        subNodes.add(node);
    }
    public Node(){
        this.subNodes = new ArrayList<>();
    }

    public abstract void print();
}
