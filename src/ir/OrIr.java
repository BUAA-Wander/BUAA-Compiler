package ir;

import ir.utils.Operand;

public class OrIr extends IntermediateInstruction {
    public OrIr(Operand oprandIdx1, Operand oprandIdx2, Operand resIdx) {
        super(oprandIdx1, oprandIdx2, resIdx);
    }

    public String toString() {
        return "Or " + getLeft() + " " + getRight() + " " + getRes();
    }
}
