import java.util.ArrayList;

public class FuncSymbolInfo extends SymbolInfo{
    private boolean isReturnInt;

    public boolean isReturnInt() {
        return isReturnInt;
    }

    public ArrayList<Integer> getParaDims() {
        return paraDims;
    }

    private ArrayList<Integer> paraDims = new ArrayList<>();

    public FuncSymbolInfo(Word ident,boolean isReturnInt,ArrayList<Integer> paraDim) {
        super(ident);
        this.isReturnInt = isReturnInt;
        this.paraDims.addAll(paraDim);
        this.id = IdCtrl.getFunId();
        this.Strid = "f_"+this.id;
    }
}
