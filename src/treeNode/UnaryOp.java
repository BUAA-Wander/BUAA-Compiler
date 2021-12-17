package treeNode;

public class UnaryOp extends TreeNode {
    private UnaryOpType type; //

    public UnaryOp(int num, UnaryOpType type) {
        super(num);
        this.type = type;
    }

    public UnaryOpType getType() {
        return type;
    }
}