import java.util.ArrayList;

public class FourUnit {
    public String getOp() {
        return op;
    }

    public String getArg1() {
        return arg1;
    }

    public String getArg2() {
        return arg2;
    }

    public String getResult() {
        return result;
    }

    private String op;
    private String arg1;
    private String arg2;
    private String result;

    public FourUnit(String op, String arg1, String arg2, String result) {
        this.op = op;
        this.arg1 = arg1;
        this.arg2 = arg2;
        this.result = result;
    }

    @Override
    public String toString() {
        return  op + ' ' + arg1 + ' ' + arg2 + ' ' + result;
    }
}
