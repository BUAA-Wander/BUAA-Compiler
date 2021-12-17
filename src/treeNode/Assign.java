package treeNode;

public class Assign extends TreeNode {
    public Assign(int num) {
        super(num);
    }

    public String outputAdaptToHomework() {
        StringBuilder builder = new StringBuilder();
        builder.append("ASSIGN =");
        return builder.toString();
    }
}
