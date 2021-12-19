package mips;

public class Jr extends MipsCode {
    public Jr(String dst) {
        super(dst);
    }

    public String toString() {
        return "jr " + getRes();
    }
}
