public class VarSymbolInfo extends DeclSymbolInfo{
    public VarSymbolInfo(Word ident,int dimNum,int dim1,int dim2) {
        super(ident);
        this.dimNum = dimNum;
        this.id = IdCtrl.getVarId();
        this.Strid = "a_"+this.id;
        this.dim1 = dim1;
        this.dim2 = dim2;
    }
}
