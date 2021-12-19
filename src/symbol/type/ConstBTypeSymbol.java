package symbol.type;

public class ConstBTypeSymbol extends Symbol {
    private int value;
    public ConstBTypeSymbol(int lineNumber, String name) {
        super(lineNumber, name);
    }

    public ConstBTypeSymbol(int lineNumber, String name, int value) {
        super(lineNumber, name);
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
