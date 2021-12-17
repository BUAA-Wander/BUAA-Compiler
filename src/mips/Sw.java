package mips;

public class Sw extends MipsCode {
    public Sw(String left, String res, int imm) {
        super(left, res, imm);
    }

    public String toString() {
        return "sw " + getRes() + " " + getImm() + "(" + getLeft() + ")";
    }
}
