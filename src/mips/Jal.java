package mips;

public class Jal extends MipsCode {
    public Jal(String label) {
        super(label);
    }

    public String toString() {
        return "jal " + getRes();
    }
}
