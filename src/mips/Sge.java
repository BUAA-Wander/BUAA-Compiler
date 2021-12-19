package mips;

public class Sge extends MipsCode {
    public Sge(String left, String right, String res) {
        super(left, right, res);
    }

    public String toString() {
        return "sge " + getRes() + " " + getLeft() + " " + getRight();
    }
}