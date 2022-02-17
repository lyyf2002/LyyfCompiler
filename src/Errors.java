import java.util.*;
import java.util.stream.Collectors;

public class Errors {

    private static final HashMap<Integer,HashSet<String>> line2error = new HashMap<>();

    private Errors(){}

    public static void addError(int line,String error){
        if (!line2error.containsKey(line)) {
            line2error.put(line, new HashSet<>());
        }
        line2error.get(line).add(error);

    }

    public static void printErrors(){
        Object[] key_arr = line2error.keySet().toArray();
        Arrays.sort(key_arr);
        for  (Object key : key_arr) {
            //line2error.get(key).sort(Comparator.naturalOrder());
            for (String error:line2error.get(key)){
                //if (error.equals("a")||error.equals("g")||error.equals("m"))
                System.out.println(((int)key+1)+" "+error);
            }

        }
    }


}
