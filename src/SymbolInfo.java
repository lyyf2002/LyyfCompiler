public abstract class SymbolInfo {
    private Word ident;
    protected int id;
    protected String Strid;
    public SymbolInfo(Word ident){
        this.ident = ident;
    }
    public Word getIdent(){
        return ident;
    }

}
