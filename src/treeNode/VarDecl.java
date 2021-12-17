package treeNode;

import error.Error;
import ir.IntermediateInstruction;
import symbol.SymbolTable;

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

    public String outputAdaptToHomework() {
        StringBuilder builder = new StringBuilder();
        builder.append(bType.outputAdaptToHomework()).append("\n");
        for (int i = 0; i < varDefs.size(); i++) {
            builder.append(varDefs.get(i).outputAdaptToHomework()).append("\n");
            if (i + 1 < varDefs.size()) {
                builder.append(commas.get(i).outputAdaptToHomework()).append("\n");
            }
        }
        builder.append(semicolon.outputAdaptToHomework()).append("\n");
        builder.append("<VarDecl>");
        return builder.toString();
    }

    public void createSymbolTable(int level, SymbolTable symbolTable,
                                  List<Error> errors) {
        for (int i = 0; i < varDefs.size(); i++) {
            varDefs.get(i).createSymbolTable(level, symbolTable, errors);
        }
    }

    public List<IntermediateInstruction> generateIr(int level) {
        List<IntermediateInstruction> instructions = new ArrayList<>();
        for (int i = 0; i < varDefs.size(); i++) {
            instructions.addAll(varDefs.get(i).generateIr(level));
        }
        return instructions;
    }
}
