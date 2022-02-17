import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;

public class Lexer {
    public ArrayList<Word> getWords() {
        return words;
    }

    private ArrayList<Word> words;
    private ArrayList<String> sourceCode;
    private HashMap<String,Symbol> word2symbol;

    public Lexer(ArrayList<String> sourceCode) {
        this.words = new ArrayList<>();
        this.sourceCode = sourceCode;
        this.word2symbol = new HashMap<>();
        word2symbol.put("main",Symbol.MAINTK);
        word2symbol.put("const",Symbol.CONSTTK);
        word2symbol.put("int",Symbol.INTTK);
        word2symbol.put("break",Symbol.BREAKTK);
        word2symbol.put("continue",Symbol.CONTINUETK);
        word2symbol.put("if",Symbol.IFTK);
        word2symbol.put("else",Symbol.ELSETK);
        word2symbol.put("!",Symbol.NOT);
        word2symbol.put("&&",Symbol.AND);
        word2symbol.put("||",Symbol.OR);
        word2symbol.put("while",Symbol.WHILETK);
        word2symbol.put("getint",Symbol.GETINTTK);
        word2symbol.put("printf",Symbol.PRINTFTK);
        word2symbol.put("return",Symbol.RETURNTK);
        word2symbol.put("+",Symbol.PLUS);
        word2symbol.put("-",Symbol.MINU);
        word2symbol.put("void",Symbol.VOIDTK);
        word2symbol.put("*",Symbol.MULT);
        word2symbol.put("/",Symbol.DIV);
        word2symbol.put("%",Symbol.MOD);
        word2symbol.put("<",Symbol.LSS);
        word2symbol.put("<=",Symbol.LEQ);
        word2symbol.put(">",Symbol.GRE);
        word2symbol.put(">=",Symbol.GEQ);
        word2symbol.put("==",Symbol.EQL);
        word2symbol.put("!=",Symbol.NEQ);
        word2symbol.put("=",Symbol.ASSIGN);
        word2symbol.put(";",Symbol.SEMICN);
        word2symbol.put(",",Symbol.COMMA);
        word2symbol.put("(",Symbol.LPARENT);
        word2symbol.put(")",Symbol.RPARENT);
        word2symbol.put("[",Symbol.LBRACK);
        word2symbol.put("]",Symbol.RBRACK);
        word2symbol.put("{",Symbol.LBRACE);
        word2symbol.put("}",Symbol.RBRACE);

    }
    public void beginLexer(){
        boolean isUseless = false;

        String token = "";
        String curstr;
        for (int line = 0;line<sourceCode.size();line++) {
            curstr = sourceCode.get(line);
            int curi = 0;
            for (;curi<curstr.length();curi++){
                if (isUseless){
                    if (curstr.charAt(curi) == '*' && curi+1<curstr.length() && curstr.charAt(curi+1)=='/'){
                        isUseless = false;
                        curi+=1;
                    }
                }
                else {
                    if (curstr.charAt(curi) == '/'){

                        if (curi+1 <curstr.length() && curstr.charAt(curi+1)=='*'){
                            isUseless = true;
                            curi+=1;
                        }
                        else if (curi+1<curstr.length() && curstr.charAt(curi+1)=='/'){
                            curi = curstr.length();
                        }
                        else {
                            token= String.valueOf(curstr.charAt(curi));
                            getToken(token,line);
                        }
                    }
                    else if (curstr.charAt(curi) == '+' ||curstr.charAt(curi) == '-'||curstr.charAt(curi) == '*' || curstr.charAt(curi) == '%' || curstr.charAt(curi) == ';' ||curstr.charAt(curi) == ',' ||curstr.charAt(curi) == '('||curstr.charAt(curi) == ')'||curstr.charAt(curi) == '['||curstr.charAt(curi) == ']'||curstr.charAt(curi) == '{'||curstr.charAt(curi) == '}'){
                        token= String.valueOf(curstr.charAt(curi));
                        getToken(token,line);
                    }
                    else if (curstr.charAt(curi) == '!'||curstr.charAt(curi) == '='||curstr.charAt(curi) == '<'||curstr.charAt(curi) == '>'){
                        token = String.valueOf(curstr.charAt(curi));
                        if (curi+1<curstr.length()&&curstr.charAt(curi+1)=='='){
                            token = token.concat("=");
                            curi+=1;
                        }
                        getToken(token,line);
                    }
                    else if (curstr.charAt(curi) == '&'||curstr.charAt(curi) =='|'){
                        if (curi+1<curstr.length()&&curstr.charAt(curi+1)==curstr.charAt(curi)){
                            token = String.valueOf(curstr.charAt(curi));
                            token = token.concat(token);
                            getToken(token,line);
                            curi+=1;
                        }
                    }
                    else if (isStartIdentChar(curstr.charAt(curi))){
                        token = String.valueOf(curstr.charAt(curi));
                        curi+=1;
                        while (curi<curstr.length()&&isIdentChar(curstr.charAt(curi))){
                            token = token.concat(String.valueOf(curstr.charAt(curi)));
                            curi+=1;
                        }
                        curi-=1;
                        getToken(token,line);
                    }
                    else if (isNumChar(curstr.charAt(curi))){
                        token = String.valueOf(curstr.charAt(curi));
                        curi+=1;
                        while (curi<curstr.length()&&isNumChar(curstr.charAt(curi))){
                            token = token.concat(String.valueOf(curstr.charAt(curi)));
                            curi+=1;
                        }
                        curi-=1;
                        getToken(token,line);
                    }
                    else if (curstr.charAt(curi) == '\"'){
                        token = "\"";
                        curi+=1;
                        while (curstr.charAt(curi) != '\"'){
                            if(curstr.charAt(curi)<32||(curstr.charAt(curi)>33 && curstr.charAt(curi)<37)||(curstr.charAt(curi)>37 && curstr.charAt(curi)<40)||curstr.charAt(curi)>126){
                                Errors.addError(line,"a");
                            }
                            token = token.concat(String.valueOf(curstr.charAt(curi)));
                            curi+=1;
                        }
                        token = token.concat("\"");
                        getToken(token,line);
                    }

                }


            }
        }
    }
    public void getToken(String token,int lineNum){
        if (word2symbol.containsKey(token)){
            words.add(new Word(token,word2symbol.get(token),lineNum));
        } else if (token.charAt(0) == '\"'){
            String[] temps = token.split("\\\\");
            boolean flag = true;
            for (int i=1;i<temps.length;i++){
                if (!temps[i].startsWith("n")){
                    Errors.addError(lineNum,"a");
                    flag = false;
                    break;
                }
            }
            if (flag){
                temps = token.split("%");
                for (int i=1;i<temps.length;i++){
                    if (!temps[i].startsWith("d")){
                        Errors.addError(lineNum,"a");
                        break;
                    }
                }
            }

            words.add(new Word(token,Symbol.STRCON,lineNum));
        } else if (isStartIdentChar(token.charAt(0))){
            words.add(new Word(token,Symbol.IDENFR,lineNum));
        } else if (isNumChar(token.charAt(0))){
            words.add(new Word(token,Symbol.INTCON,lineNum));
        }
    }
    public boolean isStartIdentChar(char c) {
        return ('A' <= c && c <= 'Z') || ('a' <= c && c <= 'z') || c == '_';
    }
    public  boolean isNumChar(char c){
        return '0'<=c && c<='9';
    }
    public boolean isNoZeroNumChar(char c){
        return '1'<=c && c<='9';
    }
    public boolean isIdentChar(char c){
        return isStartIdentChar(c) || isNumChar(c);
    }
    public void printLexer(){
        for (Word temp:words
             ) {
            System.out.println(temp.getSymbol()+" "+temp.getWord());
        }
    }

}
