package mips;

public class Add extends MipsCode {
    public Add(String left, String right, String res) {
        super(left, right, res);
    }

    public String toString() {
        return "addu " + getRes() + " " + getLeft() + " " + getRight();
    }
}
