package ir.utils;

public class Immediate extends Operand {
    private int value;

    public Immediate(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
