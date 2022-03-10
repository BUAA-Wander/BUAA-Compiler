package mips;

public class Sub extends MipsCode {
    public Sub(String left, String right, String res) {
        super(left, right, res);
    }

    public String toString() {
        return "sub " + getRes() + " " + getLeft() + " " + getRight();
    }
}
