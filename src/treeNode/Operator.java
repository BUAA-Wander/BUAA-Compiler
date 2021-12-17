package treeNode;

public class Operator extends TreeNode {
    private OperatorType type;
    // ADD, SUB, NOT, MUL, DIV, MOD, LSS, GRE, LEQ, GEQ, EQ, NEQ, AND, OR

    public Operator(int num, OperatorType type) {
        super(num);
        this.type = type;
    }

    public String outputAdaptToHomework() {
        if (type == OperatorType.ADD) {
            return "PLUS +";
        } else if (type == OperatorType.SUB) {
            return "MINU -";
        } else if (type == OperatorType.NOT) {
            return "NOT !";
        } else if (type == OperatorType.MUL) {
            return "MULT *";
        } else if (type == OperatorType.DIV) {
            return "DIV /";
        } else if (type == OperatorType.MOD) {
            return "MOD %";
        } else if (type == OperatorType.LSS) {
            return "LSS <";
        } else if (type == OperatorType.LEQ) {
            return "LEQ <=";
        } else if (type == OperatorType.GRE) {
            return "GRE >";
        } else if (type == OperatorType.GEQ) {
            return "GEQ >=";
        } else if (type == OperatorType.EQ) {
            return "EQL ==";
        } else if (type == OperatorType.NEQ) {
            return "NEQ !=";
        } else if (type == OperatorType.AND) {
            return "AND &&";
        } else if (type == OperatorType.OR) {
            return "OR ||";
        }
        return null;
    }

    public OperatorType getType() {
        return type;
    }
}