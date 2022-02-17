import java.util.ArrayList;

public class BasicBlock {
    public ArrayList<FourUnit> getItems() {
        return items;
    }

    private ArrayList<FourUnit> items;
    private ArrayList<BasicBlock> sufBlocks;//后
    private ArrayList<BasicBlock> preBlocks;//前
    public void addSuf(BasicBlock block){
        this.sufBlocks.add(block);
    }
    public void addPre(BasicBlock block){
        this.preBlocks.add(block);
    }
    public BasicBlock(){
        items=new ArrayList<>();
        sufBlocks = new ArrayList<>();
        preBlocks = new ArrayList<>();

    }
    public void addFourUnit(FourUnit unit){
        items.add(unit);
    }
    public FourUnit getLastOne(){
        return this.items.get(items.size()-1);
    }

    public void printBlock(){
        for (FourUnit unit:items){
            System.out.println(unit);
        }

        System.out.println();
    }
    private DAG dag;
    public void genDag(){
        this.dag = new DAG(items);
        dag.back2Mid();
    }


}
