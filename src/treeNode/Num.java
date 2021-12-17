package treeNode;

public class Num extends TreeNode {
    private IntConst intConst;

    public Num(int num, IntConst intConst) {
        super(num);
        this.intConst = intConst;
    }

    public String outputAdaptToHomework() {
        StringBuilder builder = new StringBuilder();
        builder.append(intConst.outputAdaptToHomework()).append("\n");
        builder.append("<Number>");
        return builder.toString();
    }

    public int getValue() {
        return intConst.getValue();
    }
}
