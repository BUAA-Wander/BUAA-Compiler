package mips;

public class And extends MipsCode {
    public And(String left, String right, String res) {
        super(left, right, res);
    }

    public String toString() {
        return "and " + getRes() + " " + getLeft() + " " + getRight();
    }
}
