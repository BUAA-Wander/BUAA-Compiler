package mips;

public class Addi extends MipsCode {
    public Addi(String left, String res, int imm) {
        super(left, res, imm);
    }

    public String toString() {
        return "addi " + getRes() + " " + getLeft() + " " + getImm();
    }
}
