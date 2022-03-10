package ir.utils;

public class LabelOp extends Operand {
    private String labelName;

    public LabelOp(String labelName) {
        super();
        this.labelName = labelName;
    }

    public String toString() {
        return labelName;
    }

    public String getLabelName() {
        return labelName;
    }
}
