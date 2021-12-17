package ir.utils;

public class StringOp extends Operand {
    private String value;

    public StringOp(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public String toString() {
        return value;
    }
}
