public class Word {
    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public Symbol getSymbol() {
        return symbol;
    }

    public void setSymbol(Symbol symbol) {
        this.symbol = symbol;
    }

    private String word;
    private Symbol symbol;

    public int getLineNum() {
        return lineNum;
    }

    private int lineNum;


    
    public Word(String word,Symbol symbol,int lineNum){
        this.word = word;
        this.symbol = symbol;
        this.lineNum = lineNum;
    }


}
