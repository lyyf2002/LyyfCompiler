import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;

public class MidRunner {
    public ArrayList<FourUnit> midCode;
    public MidRegisters midRegisters;
    public ArrayList<MidRegisters> registersStack;
    public static int ret=0;
    public HashMap<String,Integer> func2line=new HashMap<>();
    public HashMap<String,Integer> label2line=new HashMap<>();
    public MidData data;
    public int pc = 0;
    public ArrayList<Integer> paraValues=new ArrayList<>();
    public static Scanner in;
    public MidRunner(ArrayList<FourUnit> midCode){
        this.midCode=midCode;
        midRegisters = new MidRegisters(null);
        registersStack = new ArrayList<>();
        registersStack.add(new MidRegisters(midRegisters));
        data=new MidData();
        in = new Scanner(System.in);

    }

    public void run(){
        for (int i=0;i<midCode.size();i++){
            if (midCode.get(i).getOp().equals("func")){
                func2line.put(midCode.get(i).getResult(),i);
            }
            else if (midCode.get(i).getOp().equals("label")){
                label2line.put(midCode.get(i).getResult(),i);
            }
        }
        while (!midCode.get(pc).getOp().equals("func")){
            String arg1 = midCode.get(pc).getArg1();
            int v1;
            if (arg1==null){
                v1=0;
            }
            else if (arg1.startsWith("a")||arg1.startsWith("c")||arg1.startsWith("t")){
                v1 = midRegisters.get(arg1);
            }
            else v1 = Integer.parseInt(arg1);
            String arg2 = midCode.get(pc).getArg2();
            int v2;
            if (arg2==null){
                v2=0;
            }else
            if (arg2.startsWith("a")||arg2.startsWith("c")||arg2.startsWith("t")){
                v2 = midRegisters.get(arg2);
            }
            else v2 = Integer.parseInt(arg2);
            switch (midCode.get(pc).getOp()) {
                case "add":
                case "sub":
                case "mul":
                case "div":
                case "mod":
                    midRegisters.compute2(midCode.get(pc));
                    break;
                case "neg":
                case "ass":
                case "var":
                case "varass":
                case "conass":
                    midRegisters.compute(midCode.get(pc));
                    break;
                case "conarr":
                case "arr":
                    int addr = data.alloc(Integer.parseInt(midCode.get(pc).getArg1())*(midCode.get(pc).getArg2()!=null?Integer.parseInt(midCode.get(pc).getArg2()):1));
                    midRegisters.put(midCode.get(pc).getResult(),addr);

                    break;
                case "assarr":
                    data.setToAddr(midRegisters.get(midCode.get(pc).getResult())+v1,v2);

                    break;
                case "getarr":
                    midRegisters.put(midCode.get(pc).getResult(),data.getFromAddr(midRegisters.get(arg1)+v2));

                    break;
                case "getadd":
                    midRegisters.put(midCode.get(pc).getResult(),midRegisters.get(arg1)+v2);

                    break;
                default:
                    System.out.println("error ------" + midCode.get(pc));
                    break;
            }
            pc++;
        }
        pc=func2line.get("f_main")+1;
        while (pc!=midCode.size()-1){
//            System.out.println(pc);
            String arg1 = midCode.get(pc).getArg1();
            String arg2 = midCode.get(pc).getArg2();
            int v1;
            if (arg1==null){
                v1=0;
            }
            else if (arg1.equals("RET")){
                v1=ret;
            }else if (arg1.equals("getint")){
                v1=0;
            }
            else if (arg1.startsWith("a")||arg1.startsWith("c")||arg1.startsWith("t")){
                v1 = getLastMidRegisters().get(arg1);
            }
            else v1 = Integer.parseInt(arg1);

            int v2;
            if (arg2==null){
                v2=0;
            }
            else if (arg2.equals("RET")){
                v2=ret;
            }else if (arg2.equals("getint")){
                v2=in.nextInt();
            }
            else if (arg2.startsWith("a")||arg2.startsWith("c")||arg2.startsWith("t")){
                v2 = getLastMidRegisters().get(arg2);
            }
            else v2 = Integer.parseInt(arg2);
            switch (midCode.get(pc).getOp()) {
                case "add":
                case "sub":
                case "mul":
                case "div":
                case "mod":
                case "seq":
                case "sne":
                case "sgt":
                case "sge":
                case "sle":
                case "slt":

                    getLastMidRegisters().compute2(midCode.get(pc));
                    break;
                case "neg":
                case "ass":
                case "var":
                case "varass":
                case "conass":
                    getLastMidRegisters().compute(midCode.get(pc));
                    break;
                case "conarr":
                case "arr":
                    int addr = data.alloc(Integer.parseInt(midCode.get(pc).getArg1())*(midCode.get(pc).getArg2()!=null?Integer.parseInt(midCode.get(pc).getArg2()):1));
                    getLastMidRegisters().put(midCode.get(pc).getResult(),addr);

                    break;
                case "assarr":
                    data.setToAddr(getLastMidRegisters().get(midCode.get(pc).getResult())+v1,v2);

                    break;
                case "getarr":
                    getLastMidRegisters().put(midCode.get(pc).getResult(),data.getFromAddr(getLastMidRegisters().get(arg1)+v2));

                    break;
                case "getadd":
                    getLastMidRegisters().put(midCode.get(pc).getResult(),getLastMidRegisters().get(arg1)+v2);

                    break;
                case "para":
                    getLastMidRegisters().put(midCode.get(pc).getResult(),paraValues.get(0));
                    paraValues.remove(0);
                    break;
                case "push":
                    paraValues.add((midCode.get(pc).getResult().startsWith("a")||midCode.get(pc).getResult().startsWith("t")||midCode.get(pc).getResult().startsWith("c"))?getLastMidRegisters().get(midCode.get(pc).getResult()):Integer.parseInt(midCode.get(pc).getResult()));
                    break;
                case "call":
                    registersStack.add(new MidRegisters(midRegisters));
                    getLastMidRegisters().setNpc(pc);
                    pc=func2line.get(midCode.get(pc).getResult());
                    break;
                case "ret":
                    int npc = getLastMidRegisters().getNpc();
                    if (midCode.get(pc).getResult()!=null){
                        ret = (midCode.get(pc).getResult().startsWith("a")||midCode.get(pc).getResult().startsWith("t")||midCode.get(pc).getResult().startsWith("c"))?getLastMidRegisters().get(midCode.get(pc).getResult()):Integer.parseInt(midCode.get(pc).getResult());
                    }
                    pc=npc;
                    registersStack.remove(registersStack.size()-1);
                    if (registersStack.size()==0) return;
                    break;
                case "label":
                    break;
                case "j":
                    pc=label2line.get(midCode.get(pc).getResult());
                    break;
                case "jgt":
                    if (v1>v2)
                        pc=label2line.get(midCode.get(pc).getResult());
                    break;
                case "jge":
                    if (v1>=v2)
                        pc=label2line.get(midCode.get(pc).getResult());
                    break;
                case "jlt":
                    if (v1<v2)
                        pc=label2line.get(midCode.get(pc).getResult());
                    break;
                case "jle":
                    if (v1<=v2)
                        pc=label2line.get(midCode.get(pc).getResult());
                    break;
                case "jne":
                    if (v1!=v2)
                        pc=label2line.get(midCode.get(pc).getResult());
                    break;
                case "jeq":
                    if (v1==v2)
                        pc=label2line.get(midCode.get(pc).getResult());
                    break;
                case "printint":
                    System.out.print(midCode.get(pc).getResult().equals("RET")?ret:(midCode.get(pc).getResult().startsWith("a")||midCode.get(pc).getResult().startsWith("t")||midCode.get(pc).getResult().startsWith("c"))?getLastMidRegisters().get(midCode.get(pc).getResult()):Integer.parseInt(midCode.get(pc).getResult()));
                    break;
                case "printstr":
                    for (int i=0;i<IdCtrl.formatStrs.get(midCode.get(pc).getResult()).length();i++){
                        if (IdCtrl.formatStrs.get(midCode.get(pc).getResult()).charAt(i)=='\\'){
                            System.out.println();
                            i++;
                        }else
                        System.out.print(IdCtrl.formatStrs.get(midCode.get(pc).getResult()).charAt(i));
                    }

                    break;
                default:
                    System.out.println("error ------" + pc);
                    break;
            }
            pc++;

        }

    }
    public MidRegisters getLastMidRegisters(){
        return registersStack.get(registersStack.size()-1);
    }
}
