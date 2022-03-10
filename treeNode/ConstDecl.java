package treeNode;

import ir.IntermediateInstruction;

import java.util.ArrayList;
import java.util.List;

public class ConstDecl extends TreeNode {
    private Const constToken;
    private BType bType;
    private List<ConstDef> constDefs;
    private List<Comma> commas;
    private Semicolon semicolon;

    public ConstDecl(int num, Const constToken, BType bType, List<ConstDef> constDefs,
                     List<Comma> commas, Semicolon semicolon) {
        super(num);
        this.constToken = constToken;
        this.bType = bType;
        this.constDefs = constDefs;
        this.commas = commas;
        this.semicolon = semicolon;
    }

    public List<IntermediateInstruction> generateIr(int level) {
        List<IntermediateInstruction> instructions = new ArrayList<>();
        for (int i = 0; i < constDefs.size(); i++) {
            instructions.addAll(constDefs.get(i).generateIr(level));
        }
        return instructions;
    }
}
