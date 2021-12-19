package mips;

public class Mflo extends MipsCode {
    public Mflo(String res) {
        super(res);
    }

    public String toString() {
        return "mflo " + getRes();
    }
}