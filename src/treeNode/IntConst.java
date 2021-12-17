package treeNode;

public class IntConst extends TreeNode {
    private String value;

    public IntConst(int num, String value) {
        super(num);
        this.value = value;
    }

    public String outputAdaptToHomework() {
        return "INTCON " + value;
    }

    public int getValue() {
        return Integer.parseInt(value);
    }
}
