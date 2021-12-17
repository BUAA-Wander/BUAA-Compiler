package mips;

public class Seq extends MipsCode {
    public Seq(String left, String right, String res) {
        super(left, right, res);
    }

    public String toString() {
        return "seq " + getRes() + " " + getLeft() + " " + getRight();
    }
}
