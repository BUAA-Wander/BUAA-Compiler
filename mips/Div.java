package mips;

public class Div extends MipsCode {
    public Div(String left,  String res) {
        super(left, res);
    }

    public String toString() {
        return "div " + getLeft() + " " + getRes();
    }
}
