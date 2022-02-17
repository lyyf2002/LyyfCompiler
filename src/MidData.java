import java.util.ArrayList;

public class MidData {
    public ArrayList<Integer> myData=new ArrayList<>();
    public MidData(){

    }
    public int getFromAddr(int addr){
        return myData.get(addr);
    }
    public void setToAddr(int addr,int data){
        myData.set(addr,data);
    }
    public int alloc(int size){
        int addr = myData.size();
        for (int t=0;t<size;t++){
            myData.add(0);
        }
        return addr;
    }
}
