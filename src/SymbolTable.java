import java.util.ArrayList;
import java.util.HashMap;

public class SymbolTable {
    private HashMap<String,SymbolInfo> varTable;
    private HashMap<String,FuncSymbolInfo> funcTable;
    private SymbolTable prev;
    private ArrayList<SymbolTable> subs;
    public SymbolTable(SymbolTable prev){
        this.prev = prev;
        this.subs = new ArrayList<>();
        this.varTable = new HashMap<>();
        this.funcTable = new HashMap<>();
    }
    public void insert(SymbolInfo symbolInfo){
        if (symbolInfo instanceof FuncSymbolInfo){
            if (funcTable.containsKey(symbolInfo.getIdent().getWord())){
                Errors.addError(symbolInfo.getIdent().getLineNum(),"b");
            }
            else funcTable.put(symbolInfo.getIdent().getWord(),(FuncSymbolInfo) symbolInfo);
        }
        else {
            if (varTable.containsKey(symbolInfo.getIdent().getWord())){
                Errors.addError(symbolInfo.getIdent().getLineNum(),"b");
            }
            else varTable.put(symbolInfo.getIdent().getWord(),symbolInfo);
        }

    }
    public SymbolInfo find(String ident,boolean isFunc){

        if (isFunc){
            if (funcTable.containsKey(ident)){
                return funcTable.get(ident);
            }
            else if (prev!=null){
                return prev.find(ident,isFunc);
            }
        }
        else {
            if (varTable.containsKey(ident)){
                return varTable.get(ident);
            }
            else if (prev!=null){
                return prev.find(ident,isFunc);
            }
        }
        return null;
    }
}
