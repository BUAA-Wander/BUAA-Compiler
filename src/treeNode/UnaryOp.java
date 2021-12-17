package treeNode;

public class UnaryOp extends TreeNode {
    private UnaryOpType type; //

    public UnaryOp(int num, UnaryOpType type) {
        super(num);
        this.type = type;
    }

    public String outputAdaptToHomework() {
        StringBuilder builder = new StringBuilder();
        if (type == UnaryOpType.ADD) {
            builder.append("PLUS +\n");
        } else if (type == UnaryOpType.SUB) {
            builder.append("MINU -\n");
        } else {
            builder.append("NOT !\n");
        }
        builder.append("<UnaryOp>");
        return builder.toString();
    }

    public UnaryOpType getType() {
        return type;
    }
}