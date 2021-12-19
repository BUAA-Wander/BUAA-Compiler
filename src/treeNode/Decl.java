package treeNode;

import ir.IntermediateInstruction;

import java.util.ArrayList;
import java.util.List;

public class Decl extends TreeNode {
    private DeclType type; // CONST, VAR, avoid multi-extends
    private ConstDecl constDecl;
    private VarDecl varDecl;

    public Decl(int num, DeclType type, ConstDecl constDecl) {
        super(num);
        this.type = type;
        this.constDecl = constDecl;
    }

    public Decl(int num, DeclType type, VarDecl varDecl) {
        super(num);
        this.type = type;
        this.varDecl = varDecl;
    }

    public List<IntermediateInstruction> generateIr(int level) {
        List<IntermediateInstruction> instructions = new ArrayList<>();
        if (type == DeclType.CONST) {
            instructions.addAll(constDecl.generateIr(level));
        } else {
            instructions.addAll(varDecl.generateIr(level));
        }
        return instructions;
    }
}
