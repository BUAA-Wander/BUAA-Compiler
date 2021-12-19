package mips;

public class Or extends MipsCode {
    public Or(String left, String right, String res) {
        super(left, right, res);
    }

    public String toString() {
        return "or " + getRes() + " " + getLeft() + " " + getRight();
    }
}
