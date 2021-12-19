package treeNode;

public class Operator extends TreeNode {
    private OperatorType type;
    // ADD, SUB, NOT, MUL, DIV, MOD, LSS, GRE, LEQ, GEQ, EQ, NEQ, AND, OR

    public Operator(int num, OperatorType type) {
        super(num);
        this.type = type;
    }

    public OperatorType getType() {
        return type;
    }
}