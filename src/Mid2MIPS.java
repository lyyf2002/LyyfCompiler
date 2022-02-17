import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Mid2MIPS {
    private HashMap<String, ArrayList<BasicBlock>> func2blocks;
    private HashMap<String,VarCtrl> func2varCtrl;
    private VarCtrl curVarCtrl;
    public static HashMap<String,Var> globalVars=new HashMap<>();

    public Mid2MIPS(HashMap<String, ArrayList<BasicBlock>> func2blocks) {
        this.func2blocks = func2blocks;
        this.func2varCtrl = new HashMap<>();

    }

    public void intoMIPS() {
        System.out.println(".data");
        setDecl();
        setStrs();

        System.out.println(".text");
        initDecls();
        genFuncs();
        System.out.println("end:");


    }
    public void initDecls(){
        BasicBlock decl = func2blocks.get("decl").get(0);
        for (FourUnit unit : decl.getItems()) {
//            if (unit.getOp().equals("varass"))
            initDecl(unit);
        }
    }
    public void  genFuncs(){
        genFunc("f_main");
        for (Object s: func2blocks.keySet().stream().sorted().toArray()){
//            System.out.println(s+":");
            if (!s.equals("f_main")&&!s.equals("decl"))
                genFunc(String.valueOf(s));
//            System.out.println();
        }
    }
    public void genFunc(String funcid){
//        func2varCtrl.put(funcid,new VarCtrl());
        curVarCtrl = new VarCtrl();
        ArrayList<BasicBlock> blocks = func2blocks.get(funcid);
        System.out.println(funcid + ":");
        for (BasicBlock block:blocks){


            for (FourUnit unit:block.getItems()){
                switch (unit.getOp()) {
                    case "add":
                    case "sub":
                    case "mul":
                    case "div":
                    case "mod":
                    case "seq":
                    case "sne":
                    case "sgt":
                    case "sge":
                    case "slt":
                    case "sle":

                        compute2(unit);
                        break;
                    case "neg":
                    case "ass":
                    case "var":
                    case "varass":
                    case "conass":
                        compute(unit);
                        break;
                    case "conarr":
                    case "arr":
                        allocArr(unit);

                        break;
                    case "assarr":
                        assarr(unit);

                        break;
                    case "getarr":
                        getarr(unit);
                        break;
                    case "getadd":
                        getadd(unit);

                        break;
                        /*
                        sp
                        ...
                        ...
                        ...
                        para1
                        para2



                         */
                    case "para":
                        curVarCtrl.addVar(unit.getResult(),true);

                        break;
                    case "push":
                        System.out.println("    sw "+getReg(unit.getResult())+", "+(-(curVarCtrl.getCurOffset()+curVarCtrl.getPush()))+"($sp)");
                        curVarCtrl.addPush();

                        break;
                    case "call":
//                        sw $ra,0($sp)		#存$ra
//                        subi $sp,$sp,4
//                        sw $t0,0($sp)		#存这一层函数的参数
//                        subi $sp,$sp,4
//                        addi $t1,$t0,1		#将n+1存入$t1
//                        move $a0,$t1		#传值
//                        jal factorial		#下一层函数的参数便是n+1了，当下一层函数运行到return（jr $31）时将回到这一层
//                        addi $sp,$sp,4
//                        lw $t0,0($sp)		#读回这一层的参数
//                        addi $sp,$sp,4
//                        lw $ra,0($sp)		#读回这一层的$ra
                        curVarCtrl.saveRegs();//保存寄存器
                        System.out.println("    sub $sp,$sp,"+(curVarCtrl.getCurOffset()+4));//多压一次存ra
                        System.out.println("    sw $ra,4($sp)");
                        System.out.println("    jal "+unit.getResult());
                        curVarCtrl.cleanPush();
                        System.out.println("    lw $ra,4($sp)");
                        System.out.println("    addu $sp,$sp,"+(curVarCtrl.getCurOffset()+4));
                        //局部变量全在内存中


                        break;
                    case "ret":
//                        int npc = getLastMidRegisters().getNpc();
                        if (funcid.equals("f_main")){
                            System.out.println("b end");
                        }
                        else {
                            if (unit.getResult()!=null){
                                if (unit.getResult().startsWith("a")||unit.getResult().startsWith("t")||unit.getResult().startsWith("c")){
                                    System.out.println("    move $v0, "+getReg(unit.getResult()));
                                }
                                else {
                                    System.out.println("    li $v0, "+unit.getResult());
                                }
                            }
                            curVarCtrl.saveRegs();
                            System.out.println("    jr $ra");
                        }

                        break;
                    case "label":
                        System.out.println(unit.getResult()+":");
                        break;
                    case "j":
                        curVarCtrl.saveRegs();
                        System.out.println("    b "+unit.getResult());
                        break;
                    case "jgt":
                        if ((unit.getArg1().startsWith("a")||unit.getArg1().startsWith("t")||unit.getArg1().startsWith("c"))&&(unit.getArg2().startsWith("a")||unit.getArg2().startsWith("t")||unit.getArg2().startsWith("c"))){
                            String v1 = getReg(unit.getArg1());
                            String v2 = getReg(unit.getArg2());
                            curVarCtrl.saveRegs();
                            System.out.println("    bgt "+v1+", "+v2+", "+unit.getResult());
                        }
                        else if ((!(unit.getArg1().startsWith("a")||unit.getArg1().startsWith("t")||unit.getArg1().startsWith("c")))&&(!(unit.getArg2().startsWith("a")||unit.getArg2().startsWith("t")||unit.getArg2().startsWith("c")))){
                            if (Integer.parseInt(unit.getArg1())>Integer.parseInt(unit.getArg2())){
                                System.out.println("    b "+unit.getResult());
                            }
                        }
                        else if (unit.getArg1().startsWith("a")||unit.getArg1().startsWith("t")||unit.getArg1().startsWith("c")){
                            String v1 = getReg(unit.getArg1());
                            curVarCtrl.saveRegs();
                            System.out.println("    bgt "+v1+", "+unit.getArg2()+", "+unit.getResult());
                        }
                        else {
                            String v2 = getReg(unit.getArg2());
                            curVarCtrl.saveRegs();
                            System.out.println("    blt "+v2+", "+unit.getArg1()+", "+unit.getResult());
                        }


                            break;
                    case "jge":
                        if ((unit.getArg1().startsWith("a")||unit.getArg1().startsWith("t")||unit.getArg1().startsWith("c"))&&(unit.getArg2().startsWith("a")||unit.getArg2().startsWith("t")||unit.getArg2().startsWith("c"))){
                            String v1 = getReg(unit.getArg1());
                            String v2 = getReg(unit.getArg2());
                            curVarCtrl.saveRegs();
                            System.out.println("    bge "+v1+", "+v2+", "+unit.getResult());
                        }
                        else if ((!(unit.getArg1().startsWith("a")||unit.getArg1().startsWith("t")||unit.getArg1().startsWith("c")))&&(!(unit.getArg2().startsWith("a")||unit.getArg2().startsWith("t")||unit.getArg2().startsWith("c")))){
                            if (Integer.parseInt(unit.getArg1())>=Integer.parseInt(unit.getArg2())){
                                System.out.println("    b "+unit.getResult());
                            }
                        }
                        else if (unit.getArg1().startsWith("a")||unit.getArg1().startsWith("t")||unit.getArg1().startsWith("c")){
                            String v1 = getReg(unit.getArg1());
                            curVarCtrl.saveRegs();
                            System.out.println("    bge "+v1+", "+unit.getArg2()+", "+unit.getResult());
                        }
                        else {
                            String v2 = getReg(unit.getArg2());
                            curVarCtrl.saveRegs();
                            System.out.println("    ble "+v2+", "+unit.getArg1()+", "+unit.getResult());
                        }
                        break;
                    case "jlt":
                        if ((unit.getArg1().startsWith("a")||unit.getArg1().startsWith("t")||unit.getArg1().startsWith("c"))&&(unit.getArg2().startsWith("a")||unit.getArg2().startsWith("t")||unit.getArg2().startsWith("c"))){
                            String v1 = getReg(unit.getArg1());
                            String v2 = getReg(unit.getArg2());
                            curVarCtrl.saveRegs();
                            System.out.println("    blt "+v1+", "+v2+", "+unit.getResult());
                        }
                        else if ((!(unit.getArg1().startsWith("a")||unit.getArg1().startsWith("t")||unit.getArg1().startsWith("c")))&&(!(unit.getArg2().startsWith("a")||unit.getArg2().startsWith("t")||unit.getArg2().startsWith("c")))){
                            if (Integer.parseInt(unit.getArg1())<Integer.parseInt(unit.getArg2())){
                                System.out.println("    b "+unit.getResult());
                            }
                        }
                        else if (unit.getArg1().startsWith("a")||unit.getArg1().startsWith("t")||unit.getArg1().startsWith("c")){
                            String v1 = getReg(unit.getArg1());
                            curVarCtrl.saveRegs();
                            System.out.println("    blt "+v1+", "+unit.getArg2()+", "+unit.getResult());
                        }
                        else {
                            String v2 = getReg(unit.getArg2());
                            curVarCtrl.saveRegs();
                            System.out.println("    bgt "+v2+", "+unit.getArg1()+", "+unit.getResult());
                        }
                        break;
                    case "jle":
                        if ((unit.getArg1().startsWith("a")||unit.getArg1().startsWith("t")||unit.getArg1().startsWith("c"))&&(unit.getArg2().startsWith("a")||unit.getArg2().startsWith("t")||unit.getArg2().startsWith("c"))){
                            String v1 = getReg(unit.getArg1());
                            String v2 = getReg(unit.getArg2());
                            curVarCtrl.saveRegs();
                            System.out.println("    ble "+v1+", "+v2+", "+unit.getResult());
                        }
                        else if ((!(unit.getArg1().startsWith("a")||unit.getArg1().startsWith("t")||unit.getArg1().startsWith("c")))&&(!(unit.getArg2().startsWith("a")||unit.getArg2().startsWith("t")||unit.getArg2().startsWith("c")))){
                            if (Integer.parseInt(unit.getArg1())<=Integer.parseInt(unit.getArg2())){
                                System.out.println("    b "+unit.getResult());
                            }
                        }
                        else if (unit.getArg1().startsWith("a")||unit.getArg1().startsWith("t")||unit.getArg1().startsWith("c")){
                            String v1 = getReg(unit.getArg1());
                            curVarCtrl.saveRegs();
                            System.out.println("    ble "+v1+", "+unit.getArg2()+", "+unit.getResult());
                        }
                        else {
                            String v2 = getReg(unit.getArg2());
                            curVarCtrl.saveRegs();
                            System.out.println("    bge "+v2+", "+unit.getArg1()+", "+unit.getResult());
                        }
                        break;
                    case "jne":

                        if ((unit.getArg1().startsWith("a")||unit.getArg1().startsWith("t")||unit.getArg1().startsWith("c"))&&(unit.getArg2().startsWith("a")||unit.getArg2().startsWith("t")||unit.getArg2().startsWith("c"))){
                            String v1 = getReg(unit.getArg1());
                            String v2 = getReg(unit.getArg2());
                            curVarCtrl.saveRegs();
                            System.out.println("    bne "+v1+", "+v2+", "+unit.getResult());

                        }
                        else if ((!(unit.getArg1().startsWith("a")||unit.getArg1().startsWith("t")||unit.getArg1().startsWith("c")))&&(!(unit.getArg2().startsWith("a")||unit.getArg2().startsWith("t")||unit.getArg2().startsWith("c")))){
                            if (Integer.parseInt(unit.getArg1())!=Integer.parseInt(unit.getArg2())){
                                System.out.println("    b "+unit.getResult());
                            }
                        }
                        else if (unit.getArg1().startsWith("a")||unit.getArg1().startsWith("t")||unit.getArg1().startsWith("c")){
                            String v1 = getReg(unit.getArg1());
                            curVarCtrl.saveRegs();
                            System.out.println("    bne "+v1+", "+unit.getArg2()+", "+unit.getResult());
                        }
                        else {
                            String v2 = getReg(unit.getArg2());
                            curVarCtrl.saveRegs();
                            System.out.println("    bne "+v2+", "+unit.getArg1()+", "+unit.getResult());
                        }
                        break;
                    case "jeq":

                        if ((unit.getArg1().startsWith("a")||unit.getArg1().startsWith("t")||unit.getArg1().startsWith("c"))&&(unit.getArg2().startsWith("a")||unit.getArg2().startsWith("t")||unit.getArg2().startsWith("c"))){
                            String v1 = getReg(unit.getArg1());
                            String v2 = getReg(unit.getArg2());
                            curVarCtrl.saveRegs();
                            System.out.println("    beq "+v1+", "+v2+", "+unit.getResult());
                        }
                        else if ((!(unit.getArg1().startsWith("a")||unit.getArg1().startsWith("t")||unit.getArg1().startsWith("c")))&&(!(unit.getArg2().startsWith("a")||unit.getArg2().startsWith("t")||unit.getArg2().startsWith("c")))){
                            if (Integer.parseInt(unit.getArg1())==Integer.parseInt(unit.getArg2())){
                                curVarCtrl.saveRegs();
                                System.out.println("    b "+unit.getResult());
                            }
                        }
                        else if (unit.getArg1().startsWith("a")||unit.getArg1().startsWith("t")||unit.getArg1().startsWith("c")){
                            String v1 = getReg(unit.getArg1());
                            curVarCtrl.saveRegs();
                            System.out.println("    beq "+v1+", "+unit.getArg2()+", "+unit.getResult());
                        }
                        else {
                            String v2 = getReg(unit.getArg2());
                            curVarCtrl.saveRegs();
                            System.out.println("    beq "+v2+", "+unit.getArg1()+", "+unit.getResult());
                        }
                        break;
                    case "printint":
                        System.out.println("    li $v0, 1");
                        String v;
                        if (unit.getResult().startsWith("a")||unit.getResult().startsWith("t")||unit.getResult().startsWith("c")){
                            v = getReg(unit.getResult());
                            System.out.println("    move $a0, "+v);
                        }
                        else if (unit.getResult().equals("RET")){
                            v = "$v0";
                            System.out.println("    move $a0, "+v);
                        }else {
                            System.out.println("    li $a0, "+unit.getResult());
                        }
                        System.out.println("    syscall");
                        break;
                    case "printstr":
                        System.out.println("    li $v0, 4");
                        System.out.println("    la $a0, "+unit.getResult());
                        System.out.println("    syscall");

                        break;
                    default:
                        System.out.println("error ------" + unit);
                        break;
                }


            }
            curVarCtrl.saveRegs();
        }
    }
    public void compute2(FourUnit unit){

//        String v1 = getReg(unit.getArg1());
        String v1;
        if (unit.getArg1().startsWith("a")||unit.getArg1().startsWith("c")||unit.getArg1().startsWith("t"))
            v1 = getReg(unit.getArg1());
        else if (unit.getArg1().equals("RET"))
            v1 = "$v0";
//        else if (unit.getArg1().equals("getint")){
//            System.out.println("    li $v0, 5");
//            System.out.println("    syscall");
//            v1 = "$v0";
//        }
//        else v1 = unit.getArg1();
        else {
            v1 = "error";

        }
        String v2;
        if (unit.getArg2().startsWith("a")||unit.getArg2().startsWith("c")||unit.getArg2().startsWith("t"))
            v2 = getReg(unit.getArg2());
        else if (unit.getArg2().equals("RET"))
            v2 = "$v0";
        else if (unit.getArg2().equals("getint")){
            System.out.println("    li $v0, 5");
            System.out.println("    syscall");
            v2 = "$v0";
        }
        else v2 = unit.getArg2();
        String tar = getReg(unit.getResult());
        switch (unit.getOp()) {
            case "add":
                System.out.println("    "+unit.getOp()+"u "+tar+", "+v1+", "+v2);
                break;
            case "sub":

            case "mul":
            case "div":
            case "seq":
            case "sne":
            case "sgt":
            case "sge":

            case "sle":
                System.out.println("    "+unit.getOp()+" "+tar+", "+v1+", "+v2);
                break;

            case "slt":
                if (v2.startsWith("$"))
                    System.out.println("    slt"+" "+tar+", "+v1+", "+v2);
                else System.out.println("    slti"+" "+tar+", "+v1+", "+v2);
                break;
            case "mod":
                System.out.println("    div "+v1+", "+v2);
                System.out.println("    mfhi "+tar);
                break;
            default:
                System.out.println("error compute2 :"+unit);
        }

    }
    public void compute(FourUnit unit){

        String v1;
        if (unit.getArg1()==null)
            v1 = null;
        else if (unit.getArg1().startsWith("a")||unit.getArg1().startsWith("c")||unit.getArg1().startsWith("t"))
            v1 = getReg(unit.getArg1());
        else if (unit.getArg1().equals("RET"))
            v1 = "$v0";
        else if (unit.getArg1().equals("getint")){
            System.out.println("    li $v0, 5");
            System.out.println("    syscall");
            v1 = "$v0";
        }
        else v1 = unit.getArg1();
//        String tar = getReg(unit.getResult());
        switch (unit.getOp()) {
            case "var":
                curVarCtrl.addVar(unit.getResult());
                break;
            case "neg":
                if (v1==null){
                    System.out.println("error neq :no v1--"+unit);
                }
                else if (v1.startsWith("$")){
                    System.out.println("    neg "+getReg(unit.getResult())+", "+v1);
                }
                else System.out.println("    li "+getReg(unit.getResult())+", "+(-Integer.parseInt(v1)));
                break;
            case "ass":
            case "varass":
            case "conass":
                if (v1==null){
                    System.out.println("error ass :no v1--"+unit);
                }
                else if (v1.startsWith("$")){
                    System.out.println("    move "+getReg(unit.getResult())+", "+v1);
                }
                else System.out.println("    li "+getReg(unit.getResult())+", "+(Integer.parseInt(v1)));
                break;
            default:
                System.out.println("error compute :"+unit);
        }

    }
    public void allocArr(FourUnit unit){
        curVarCtrl.addArr(unit);
    }
    public void assarr(FourUnit unit){

        //result[arg1] = arg2

        String v2;
        if (unit.getArg2().startsWith("a")||unit.getArg2().startsWith("c")||unit.getArg2().startsWith("t"))
            v2 = getReg(unit.getArg2());
        else if (unit.getArg2().equals("RET"))
            v2 = "$v0";
        else if (unit.getArg2().equals("getint")){
            System.out.println("    li $v0, 5");
            System.out.println("    syscall");
            v2 = "$v0";
        }
        else {
            v2 = unit.getArg2();
            System.out.println("    li $v0, "+v2);
            v2 = "$v0";
        }
//        System.out.println("    sll "+v1+", "+v1+", 2");
//        System.out.println("    sub $a0, $sp, "+v1);
//        System.out.println("    sw "+v2+" ,-"+curVarCtrl.getId2var().get(unit.getResult()).getOffset()+"($a0)");
        String v1;
        if (unit.getArg1()==null){
            v1 = null;
            System.out.println("error no v1 :"+unit);
        }

        else if (unit.getArg1().startsWith("a")||unit.getArg1().startsWith("c")||unit.getArg1().startsWith("t")){
            v1 = getReg(unit.getArg1());
            if (curVarCtrl.getId2var().containsKey(unit.getResult())){//局部数组
                String tarAddr = getReg(unit.getResult());
                System.out.println("    sll "+"$v1"+", "+v1+", 2");
                System.out.println("    addu "+"$v1"+", "+tarAddr+", "+"$v1");
                System.out.println("    sw "+v2+", "+"("+"$v1"+")");
            }
            else {//全局数组
                System.out.println("    sll "+"$v1"+", "+v1+", 2");
                System.out.println("    sw "+v2+", "+unit.getResult()+"("+"$v1"+")");
            }
        }

        else {
            v1 = unit.getArg1();//下标
            if (curVarCtrl.getId2var().containsKey(unit.getResult())){//局部数组
                String tarAddr = getReg(unit.getResult());//地址
//                System.out.println("    sll "+v1+", "+v1+", 2");
//                System.out.println("    add "+v1+", "+tarAddr+", "+v1);
                //sw $t0,100($t1)
                System.out.println("    sw "+v2+", "+(Integer.parseInt(v1)*4)+"("+tarAddr+")");
            }
            else {//全局数组
//                System.out.println("    sll "+v1+", "+v1+", 2");
                System.out.println("    sw "+v2+", "+unit.getResult()+"+"+(Integer.parseInt(v1)*4));
            }
        }

    }
    public void getarr(FourUnit unit){
        //result = arg1[arg2]
        String tar = getReg(unit.getResult());

        String v2;
        if (unit.getArg2().startsWith("a")||unit.getArg2().startsWith("c")||unit.getArg2().startsWith("t")){
            v2 = getReg(unit.getArg2());
            if (curVarCtrl.getId2var().containsKey(unit.getArg1())){//局部数组
                String v1 = getReg(unit.getArg1());
                System.out.println("    sll "+"$v1"+", "+v2+", 2");
                System.out.println("    addu "+"$v1"+", "+v1+", "+"$v1");
                System.out.println("    lw "+tar+", "+"("+"$v1"+")");
            }
            else {//全局数组
                System.out.println("    sll "+"$v1"+", "+v2+", 2");
                System.out.println("    lw "+tar+", "+unit.getArg1()+"("+"$v1"+")");
            }
        }

        else{
            v2 = unit.getArg2();
            if (curVarCtrl.getId2var().containsKey(unit.getArg1())){//局部数组
                String v1 = getReg(unit.getArg1());
//                System.out.println("    sll "+v2+", "+v2+", 2");
//                System.out.println("    add "+v2+", "+v1+", "+(Integer.parseInt(v2)*4));
                System.out.println("    lw "+tar+", "+(Integer.parseInt(v2)*4)+"("+v1+")");
            }
            else {//全局数组
//                System.out.println("    sll "+v2+", "+v2+", 2");
                if (!v2.equals("0"))
                    System.out.println("    lw "+tar+", "+unit.getArg1()+"+"+(Integer.parseInt(v2)*4));
                else
                    System.out.println("    lw "+tar+", "+unit.getArg1());
            }
        }





    }

    public void getadd(FourUnit unit){
        //result = &arg1[arg2]
        String tar = getReg(unit.getResult());

        String v2;
        if (unit.getArg2().startsWith("a")||unit.getArg2().startsWith("c")||unit.getArg2().startsWith("t")){
            v2 = getReg(unit.getArg2());
            if (curVarCtrl.getId2var().containsKey(unit.getArg1())){//局部数组
                String v1 = getReg(unit.getArg1());
                System.out.println("    sll "+"$v1"+", "+v2+", 2");
                System.out.println("    addu "+tar+", "+v1+", "+"$v1");
//            System.out.println("    lw "+tar+", "+"("+v2+")");
            }
            else {//全局数组
                System.out.println("    sll "+"$v1"+", "+v2+", 2");
//            la $t1,a($t0)
                System.out.println("    la "+tar+", "+unit.getArg1()+"("+"$v1"+")");
            }
        }

        else{
            v2 = unit.getArg2();
            if (curVarCtrl.getId2var().containsKey(unit.getArg1())){//局部数组
                String v1 = getReg(unit.getArg1());
//                System.out.println("    sll "+v2+", "+v2+", 2");
                System.out.println("    addu "+tar+", "+v1+", "+(Integer.parseInt(v2)*4));
//            System.out.println("    lw "+tar+", "+"("+v2+")");
            }
            else {//全局数组
//                System.out.println("    sll "+v2+", "+v2+", 2");
//            la $t1,a($t0)
                if (!v2.equals("0"))
                    System.out.println("    la "+tar+", "+unit.getArg1()+"+"+(Integer.parseInt(v2)*4));
                else System.out.println("    la "+tar+", "+unit.getArg1());
            }
        }





    }

    public String getReg(String varid){
        return curVarCtrl.loadValue(varid);
    }
    public void initDecl(FourUnit unit){
        switch (unit.getOp()) {
            case "conass":

                break;
            case "varass"://
                System.out.println("    li $t0,"+unit.getArg1());
                System.out.println("    sw $t0,"+unit.getResult());
                break;
            case "var":
//                System.out.println("    "+unit.getResult()+": .space 4");
                break;
            case "conarr":
            case "arr":
//                System.out.println("    "+unit.getResult()+": .space "+4*Integer.parseInt(unit.getArg1())*(unit.getArg2()!=null?Integer.parseInt(unit.getArg2()):1));

                break;

            case "assarr"://
                System.out.println("    li $t0,"+unit.getArg2());
                if (unit.getArg1().equals("0"))
                    System.out.println("    sw $t0,"+unit.getResult());
                else
                    System.out.println("    sw $t0,"+unit.getResult()+"+"+4*Integer.parseInt(unit.getArg1()));

                break;
            default:
                System.out.println("error-" + unit);
                break;
        }


    }

    public void setDecl() {
        BasicBlock decl = func2blocks.get("decl").get(0);
        for (FourUnit unit : decl.getItems()) {
            allocDecl(unit);
        }

    }

    public void allocDecl(FourUnit unit) {

        switch (unit.getOp()) {
            case "conass":
                System.out.println("    "+unit.getResult()+": .word "+unit.getArg1());
                globalVars.put(unit.getResult(),new Var(0,null,true,unit.getResult(),true));
                break;
            case "varass"://
            case "var":
                System.out.println("    "+unit.getResult()+": .space 4");
                globalVars.put(unit.getResult(),new Var(0,null,true, unit.getResult(),true));
                break;
            case "conarr":
            case "arr":
                System.out.println("    "+unit.getResult()+": .space "+4*Integer.parseInt(unit.getArg1())*(unit.getArg2()!=null?Integer.parseInt(unit.getArg2()):1));
                globalVars.put(unit.getResult(),new Var(0,null,true, unit.getResult(),true));
                break;

            case "assarr"://

                break;
            default:
                System.out.println("error-" + unit);
                break;
        }
    }

    public void setStrs() {
        for (String strkey :IdCtrl.formatStrs.keySet()){
            System.out.println("    "+strkey+": .asciiz \""+IdCtrl.formatStrs.get(strkey)+"\"");
        }

    }

}
