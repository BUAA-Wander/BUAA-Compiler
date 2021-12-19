package mips;

public class Slt extends MipsCode {
    public Slt(String left, String right, String res) {
        super(left, right, res);
    }

    public String toString() {
        return "slt " + getRes() + " " + getLeft() + " " + getRight();
    }
}
