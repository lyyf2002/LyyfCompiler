public class DAGNode {
    private String name;
    private int r;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getR() {
        return r;
    }

    public void setR(int r) {
        this.r = r;
    }

    public int getL() {
        return l;
    }

    public void setL(int l) {
        this.l = l;
    }

    private int l;
    public DAGNode(String name){
        this.name = name;
    }
    public DAGNode(String name,int l,int r){
        this.name = name;
        this.l = l;
        this.r = r;
    }

}
