package mips;

public class Lw extends MipsCode {
    public Lw(String left, String res, int imm) {
        super(left, res, imm);
    }

    public String toString() {
        return "lw " + getRes() + " " + getImm() + "(" + getLeft() + ")";
    }
}
