package mips;

public class Abs extends MipsCode {
    public Abs(String left, String res) {
        super(left, res);
    }

    public String toString() {
        return "abs " + getRes() + " " + getLeft();
    }
}
