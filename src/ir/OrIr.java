package ir;

public class OrIr extends IntermediateInstruction {
    public OrIr(String oprandIdx1, String oprandIdx2, String resIdx) {
        super(oprandIdx1, oprandIdx2, resIdx);
    }

    public String toString() {
        return "Or " + getLeft() + " " + getRight() + " " + getRes();
    }
}
