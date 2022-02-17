import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class VarCtrl {


    private HashMap<String,Var> id2var;
    private int curOffset=0;
    private int push=4;
    private ArrayList<String> usingRegs;
    private HashMap<String, Var> reg2used;


    public HashMap<String, Var> getId2var() {
        return id2var;
    }

    public int getCurOffset() {
        return curOffset;
    }

    public void setCurOffset(int curOffset) {
        this.curOffset = curOffset;
    }

    public int getPush() {
        return push;
    }

    public void setPush(int push) {
        this.push = push;
    }
    public void addPush(){
        this.push+=4;
    }
    public void cleanPush(){
        this.push=4;
    }

    public VarCtrl(){
        id2var = new HashMap<>();
        usingRegs  =new ArrayList<>();
        reg2used = new HashMap<>();
        reg2used.put("$t0",null);
        reg2used.put("$t1",null);
        reg2used.put("$t2",null);
        reg2used.put("$t3",null);
        reg2used.put("$t4",null);
        reg2used.put("$t5",null);
        reg2used.put("$t6",null);
        reg2used.put("$t7",null);
        reg2used.put("$t8",null);
        reg2used.put("$t9",null);
        reg2used.put("$s0",null);
        reg2used.put("$s1",null);
        reg2used.put("$s2",null);
        reg2used.put("$s3",null);
        reg2used.put("$s4",null);
        reg2used.put("$s5",null);
        reg2used.put("$s6",null);
        reg2used.put("$s7",null);
        reg2used.put("$fp",null);


    }
    public void saveRegs(){
        for (Map.Entry<String, Var> entry : reg2used.entrySet()){
            String reg = entry.getKey();
            Var var = entry.getValue();
            if (var!=null){
                if (!var.isGlobal())
                    System.out.println("    sw "+reg+", -"+var.getOffset()+"($sp)");
                else
                    System.out.println("    sw "+reg+", "+var.getVarid());
                var.setReg(null);
                reg2used.put(reg,null);
            }
        }
        usingRegs.clear();

    }
    public String loadValue(String varid){
        if (id2var.containsKey(varid)){
            if (id2var.get(varid).getReg()!=null) {
                usingRegs.remove(id2var.get(varid).getReg());
                usingRegs.add(id2var.get(varid).getReg());
                return id2var.get(varid).getReg();
            }
            else {
                return allocReg(id2var.get(varid));
            }
        } else if (Mid2MIPS.globalVars.containsKey(varid)){
            if (Mid2MIPS.globalVars.get(varid).getReg()!=null){
                usingRegs.remove(Mid2MIPS.globalVars.get(varid).getReg());
                usingRegs.add(Mid2MIPS.globalVars.get(varid).getReg());
                return Mid2MIPS.globalVars.get(varid).getReg();
            }
            else {
                return allocReg(Mid2MIPS.globalVars.get(varid));
            }
        }
        else {
            Var var = new Var(curOffset,null,false,varid,false);
            curOffset+=4;
            id2var.put(varid,var);
            return allocReg(var);
//            return "error-nosuchvar";
        }
    }
    public String allocReg(Var var){

        for (Map.Entry<String, Var> entry : reg2used.entrySet()) {
            String k = entry.getKey();
            Var v = entry.getValue();
            if (v==null) {
                usingRegs.add(k);
                reg2used.put(k,var);
                var.setReg(k);
                if (!var.isGlobal()){
                    if (var.init()) System.out.println("    lw "+k+",-"+var.getOffset()+"($sp)");
                    else {
                        var.setInit(true);
                        System.out.println("    move "+k+",$0");
                    }
                }


                else
                    System.out.println("    lw "+k+", "+var.getVarid());
                return k;
            }
        }
        String reg = usingRegs.get(0);
        usingRegs.remove(0);

        Var temp = reg2used.get(reg);
        if (!temp.isGlobal())
            System.out.println("    sw "+reg+", -"+temp.getOffset()+"($sp)");
        else
            System.out.println("    sw "+reg+", "+temp.getVarid());
        temp.setReg(null);


        var.setReg(reg);
        if (!var.isGlobal()){
            if (var.init()) System.out.println("    lw "+reg+",-"+var.getOffset()+"($sp)");
            else {
                var.setInit(true);
                System.out.println("    move "+reg+",$0");
            }
        }
        else
            System.out.println("    lw "+reg+", "+var.getVarid());
        reg2used.put(reg,var);
        usingRegs.add(reg);
        return reg;



    }
    public void addVar(String varid){
        id2var.put(varid,new Var(curOffset,null,false,varid,false));
        curOffset+=4;
    }
    public void addVar(String varid,boolean init){
        id2var.put(varid,new Var(curOffset,null,false,varid,init));
        curOffset+=4;
    }
    public void addArr(FourUnit unit){
        /*
        *sp
        * ...
        * 4
        * 3
        * 2
        * 1
        * 0
        * addr
        * */

        curOffset+=4+4*Integer.parseInt(unit.getArg1())*(unit.getArg2()!=null?Integer.parseInt(unit.getArg2()):1);
        System.out.println("    sub $a0, $sp, "+(curOffset-8));
        System.out.println("    sw $a0, -"+(curOffset-4)+"($sp)");
        id2var.put(unit.getResult(),new Var(curOffset-4,null,false,unit.getResult(),true));
//        curOffset+=;
    }

}
