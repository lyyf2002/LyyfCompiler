public class Var {
    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public String getReg() {
        return reg;
    }

    public void setReg(String reg) {
        this.reg = reg;
    }

    private int offset;
    private String reg;//null即未分配寄存器

    public boolean isGlobal() {
        return isGlobal;
    }

    private boolean isGlobal;

    public String getVarid() {
        return varid;
    }

    private String varid;

    public boolean init() {
        return init;
    }

    public void setInit(boolean init) {
        this.init = init;
    }

    private boolean init;
    public Var(int offset,String reg,boolean isGlobal,String varid ,boolean init){
        this.offset = offset;
        this.reg = reg;
        this.isGlobal = isGlobal;
        this.varid = varid;
        this.init = init;
    }


}
