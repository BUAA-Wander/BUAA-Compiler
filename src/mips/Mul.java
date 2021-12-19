package mips;

public class Mul extends MipsCode {
    public Mul(String left, String right, String res) {
        super(left, right, res);
    }

    public String toString() {
        return "mul " + getRes() + " " + getLeft() + " " + getRight();
    }
}
