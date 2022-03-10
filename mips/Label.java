package mips;

public class Label extends MipsCode {
    public Label(String label) {
        super(label);
    }

    public String toString() {
        return getRes() + ":";
    }
}
