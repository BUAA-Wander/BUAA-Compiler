package treeNode;

import ir.IntermediateInstruction;

import java.util.ArrayList;
import java.util.List;

public class VarDecl extends TreeNode {
    private BType bType;
    private List<VarDef> varDefs;
    private List<Comma> commas;
    private Semicolon semicolon;

    public VarDecl(int num, BType bType, List<VarDef> varDefs, List<Comma> commas,
                   Semicolon semicolon) {
        super(num);
        this.bType = bType;
        this.varDefs = varDefs;
        this.commas = commas;
        this.semicolon = semicolon;
    }

    public List<IntermediateInstruction> generateIr(int level) {
        List<IntermediateInstruction> instructions = new ArrayList<>();
        for (int i = 0; i < varDefs.size(); i++) {
            instructions.addAll(varDefs.get(i).generateIr(level));
        }
        return instructions;
    }
}
