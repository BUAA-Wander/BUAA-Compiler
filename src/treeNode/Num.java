package treeNode;

public class Num extends TreeNode {
    private IntConst intConst;

    public Num(int num, IntConst intConst) {
        super(num);
        this.intConst = intConst;
    }

    public int getValue() {
        return intConst.getValue();
    }
}
