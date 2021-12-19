package mips;

public class J extends MipsCode {
    public J(String label) {
        super(label);
    }

    public String toString() {
        return "j " + getRes();
    }
}
