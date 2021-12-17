package mips;

public class Beq extends MipsCode {
    public Beq(String op1, String op2, String label) {
        super(op1, op2, label);
    }

    public String toString() {
        return "beq " + getLeft() + " " + getRight() + " " + getRes();
    }
}
