package mips;

public class Mfhi extends MipsCode {
    public Mfhi(String res) {
        super(res);
    }

    public String toString() {
        return "mfhi " + getRes();
    }
}
