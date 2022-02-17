import java.util.ArrayList;

public class ConstSymbolInfo extends DeclSymbolInfo{
    private ArrayList<ArrayList<Integer>> values;

    public ConstSymbolInfo(Word ident,int dimNum,ArrayList<ArrayList<Integer>> values,int dim1,int dim2) {
        super(ident);
        this.dimNum = dimNum;
        this.id = IdCtrl.getConstId();
        this.Strid = "c_"+this.id;
        this.values = values;
        this.dim1 = dim1;
        this.dim2 = dim2;
    }
    public int getConstValue(int i,int j){
        return values.get(i).get(j);
    }
}
