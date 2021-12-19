package mips;

public class Sne extends MipsCode {
    public Sne(String left, String right, String res) {
        super(left, right, res);
    }

    public String toString() {
        return "sne " + getRes() + " " + getLeft() + " " + getRight();
    }
}
