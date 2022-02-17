public class TermianlNode extends Node{
    public Word getWord() {
        return word;
    }

    private Word word;
    public TermianlNode(Word word) {
        super();
        this.word = word;
    }

    @Override
    public void print() {
        System.out.println(this.word.getSymbol()+" "+this.word.getWord());
    }
}
