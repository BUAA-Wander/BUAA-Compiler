package mips;

public class MipsCode {
    private String left;
    private String right;
    private String res;
    private int imm;

    public MipsCode(String left, String right, String res) {
        this.left = left;
        this.right = right;
        this.res = res;
    }

    public MipsCode(String left, String res) {
        this.left = left;
        this.res = res;
    }

    public MipsCode(String left, String res, int imm) {
        this.left = left;
        this.res = res;
        this.imm = imm;
    }

    public MipsCode(String res) {
        this.res = res;
    }

    public MipsCode() {

    }

    public String getLeft() {
        return left;
    }

    public String getRight() {
        return right;
    }

    public String getRes() {
        return res;
    }

    public int getImm() {
        return imm;
    }
}