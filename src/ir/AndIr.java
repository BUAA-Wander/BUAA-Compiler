package ir;

public class AndIr extends IntermediateInstruction {
    public AndIr(String oprandIdx1, String oprandIdx2, String resIdx) {
        super(oprandIdx1, oprandIdx2, resIdx);
    }

    public String toString() {
        return "Or " + getLeft() + " " + getRight() + " " + getRes();
    }
}
