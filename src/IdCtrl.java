import java.util.ArrayList;
import java.util.HashMap;

public class IdCtrl {
    static public int funId=0;
    static public int varId=0;
    static public int constId=0;
    static public int tempId=0;
    static public int labelId=0;

    public static int getFunId(){
        return funId++;
    }
    public static int getVarId(){
        return varId++;
    }
    public static int getConstId(){
        return constId++;
    }
    public static int getTempId(){
        MidCode.add(new FourUnit("var",null,null,"t_"+tempId));
        return tempId++;
    }
    public static int getLabelId(){
        return labelId++;
    }

    public static ArrayList<String> loop_begins=new ArrayList<>();
    public static ArrayList<String> loop_ends=new ArrayList<>();
    public static void put(String begin,String end){
        loop_begins.add(begin);
        loop_ends.add(end);
    }
    public static void push(){
        loop_ends.remove(loop_ends.size()-1);
        loop_begins.remove(loop_begins.size()-1);
    }
    public static String getBegin(){
        return loop_begins.get(loop_begins.size()-1);
    }
    public static String getEnd(){
        return loop_ends.get(loop_ends.size()-1);
    }
    static HashMap<String ,String> formatStrs = new HashMap<>();
    public static String putFormatStr(String str){
        formatStrs.put("str_"+formatStrs.size(),str);
        return "str_"+(formatStrs.size()-1);
    }
}
