import java.util.HashMap;
import java.util.Objects;
import java.util.Scanner;

public class MidRegisters {
    public HashMap<String, Integer> getId2value() {
        return id2value;
    }

    private final HashMap<String ,Integer> id2value=new HashMap<>();
    private MidRegisters prev;

    public int getNpc() {
        return npc;
    }

    public void setNpc(int npc) {
        this.npc = npc;
    }

    private int npc;
    public MidRegisters(MidRegisters midRegisters){
        prev = midRegisters;
    }
    public void compute2(FourUnit unit){
        String result = unit.getResult();
        String arg1 = unit.getArg1();
        int v1,v2;
        if (arg1.startsWith("a")||arg1.startsWith("c")||arg1.startsWith("t")){
            v1 = get(arg1);
        }else if (arg1.equals("RET")){
            v1 = MidRunner.ret;
        }
        else v1 = Integer.parseInt(arg1);
        String arg2 = unit.getArg2();
        if (arg2.startsWith("a")||arg2.startsWith("c")||arg2.startsWith("t")){
            v2 = get(arg2);
        }else if (arg2.equals("RET")){
            v2 = MidRunner.ret;
        }
        else v2 = Integer.parseInt(arg2);
        int v;
        switch (unit.getOp()) {
            case "add":
                v = v1 + v2;
                break;
            case "sub":
                v = v1 - v2;
                break;
            case "mul":
                v = v1 * v2;
                break;
            case "div":
                v = v1 / v2;
                break;
            case "mod":
                v = v1 % v2;
                break;
            case "seq":
                v = (Objects.equals(v1, v2)) ? 1 : 0;
                break;
            case "sne":
                v = (!Objects.equals(v1, v2)) ? 1 : 0;
                break;
            case "sgt":
                v = (v1 > v2) ? 1 : 0;
                break;
            case "sge":
                v = (v1 >= v2) ? 1 : 0;
                break;
            case "slt":
                v = (v1 < v2) ? 1 : 0;
                break;
            case "sle":
                v = (v1 <= v2) ? 1 : 0;
                break;
            default:
                System.out.println("no compute 2 : " + unit);
                v = 0;
                break;
        }
        put(result,v);
    }
    public void compute(FourUnit unit){
        String result = unit.getResult();
        String arg1 = unit.getArg1();
        int v1;
        if (arg1 == null){
            v1=0;
        }
        else if (arg1.equals("getint")){

            v1 = MidRunner.in.nextInt();

        }
        else if (arg1.equals("RET")){
            v1 = MidRunner.ret;
        }
        else if (arg1.startsWith("a")||arg1.startsWith("c")||arg1.startsWith("t")){
            v1 = get(arg1);
        }
        else v1 = Integer.parseInt(arg1);
        int v;
        if ("neg".equals(unit.getOp())) {
            v = -v1;
        }
        else if("ass".equals(unit.getOp())){
            v = v1;
        }
        else if("var".equals(unit.getOp())){
            v = v1;
        }
        else if("varass".equals(unit.getOp())){
            v = v1;
        }
        else if("conass".equals(unit.getOp())){
            v = v1;
        }
        else {
            System.out.println("no compute 1 : " + unit);
            v = 0;
        }
        put(result,v);

    }
    public int get(String str){
        if (this.id2value.containsKey(str)){
            return this.id2value.get(str);
        }
        else if (this.prev!=null){
            return prev.get(str);
        }
        else return 0;
    }
    public void put(String str,int v){
        if (this.id2value.containsKey(str)){
            this.id2value.put(str,v);
        }
        else if (prev!=null&&prev.id2value.containsKey(str)){
            prev.id2value.put(str,v);
        }
        else this.id2value.put(str,v);
//        System.out.println("put "+str+" "+v);
    }
}
