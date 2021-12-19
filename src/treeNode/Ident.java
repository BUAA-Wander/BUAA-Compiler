package treeNode;

public class Ident extends TreeNode {
    private String name;

    public Ident(int num, String name) {
        super(num);
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
