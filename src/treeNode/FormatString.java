package treeNode;

public class FormatString extends TreeNode {
    private String value;

    public FormatString(int num, String value) {
        super(num);
        this.value = value;
    }

    public String outputAdaptToHomework() {
        return "STRCON " + value;
    }

    public String getValue() {
        return value;
    }
}
