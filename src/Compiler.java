import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;

public class Compiler {
    public static void main(String[] args) throws FileNotFoundException {
        InputStream sysIn = System.in;


//        int i = Integer.parseInt(args[1]);
//        System.setIn(new FileInputStream(args[0]+"/testfile"+i+".txt"));

        System.setIn(new FileInputStream("testfile.txt"));


//        System.setOut(new PrintStream("output.txt"));
        Scanner scanner = new Scanner(System.in);
        ArrayList<String> sourceCode = new ArrayList<>();
        while (scanner.hasNextLine()){
            sourceCode.add(scanner.nextLine());
        }

        Lexer lexer = new Lexer(sourceCode);
        lexer.beginLexer();
        //lexer.printLexer();

        Parser parser = new Parser(lexer.getWords());
        parser.parsing();
        //parser.printParser();



//        System.setOut(new PrintStream("error.txt"));
//        Errors.printErrors();

//        System.setOut(new PrintStream("mid.txt"));
        parser.getAstRoot().intoMid();

        MidCode.deleteJNextLabel();
        MidCode.deleteRet();

//        MidCode.printMid();




//        System.setIn(new FileInputStream(args[0]+"/input"+i+".txt"));
//        System.setIn(sysIn);
//
//        System.setOut(new PrintStream("pcoderesult.txt"));
//        MidRunner midRunner = new MidRunner(MidCode.midCode);
//        midRunner.run();



//        System.setOut(new PrintStream("block.txt"));
        BlockCtrl blockCtrl = new BlockCtrl(MidCode.midCode);

//        blockCtrl.printBlocks();
        System.setOut(new PrintStream("mips.txt"));
        Mid2MIPS mid2MIPS = new Mid2MIPS(blockCtrl.getFunc2blocks());
        mid2MIPS.intoMIPS();




    }
}
