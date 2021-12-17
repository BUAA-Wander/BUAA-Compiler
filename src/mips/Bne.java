package mips;

public class Bne extends MipsCode {
    public Bne(String op1, String op2, String label) {
        super(op1, op2, label);
    }

    public String toString() {
        return "bne " + getLeft() + " " + getRight() + " " + getRes();
    }
}
