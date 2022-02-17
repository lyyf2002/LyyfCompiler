import java.util.ArrayList;

public class MidCode {
    public static ArrayList<FourUnit> midCode=new ArrayList<>();
    public static void add(FourUnit fourUnit){
        midCode.add(fourUnit);
    }
    public static void add(ArrayList<FourUnit> units){
        midCode.addAll(units);
    }
    public static void printMid(){
        for (FourUnit fourUnit:midCode){
            System.out.println(fourUnit.toString());
        }
    }
    public static void deleteJNextLabel(){
        for (int i=midCode.size()-2;i>=0;i--){
            if (midCode.get(i).getOp().startsWith("j")&&midCode.get(i+1).getOp().equals("label")&&midCode.get(i).getResult().equals(midCode.get(i+1).getResult())){
                midCode.remove(i);
            }
        }
    }
    public static void deleteRet(){
        for (int i=midCode.size()-1;i>0;i--){
            if (midCode.get(i).getOp().startsWith("ret")&&midCode.get(i-1).getOp().equals("ret")){
                midCode.remove(i);
            }
        }
    }

}
