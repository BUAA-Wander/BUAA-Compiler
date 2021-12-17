package treeNode;

import error.Error;
import ir.IntermediateInstruction;
import symbol.SymbolTable;
import symbol.type.Symbol;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    public String outputAdaptToHomework() {
        StringBuilder builder = new StringBuilder();
        builder.append(constToken.outputAdaptToHomework()).append("\n");
        builder.append(bType.outputAdaptToHomework()).append("\n");
        for (int i = 0; i < constDefs.size(); i++) {
            builder.append(constDefs.get(i).outputAdaptToHomework()).append("\n");
            if (i + 1 < constDefs.size()) {
                builder.append(commas.get(i).outputAdaptToHomework()).append("\n");
            }
        }
        builder.append(semicolon.outputAdaptToHomework()).append("\n");
        builder.append("<ConstDecl>");
        return builder.toString();
    }

    public void createSymbolTable(int level, SymbolTable symbolTable
            , List<Error> errors) {
        for (int i = 0; i < constDefs.size(); i++) {
            constDefs.get(i).createSymbolTable(level, symbolTable, errors);
        }
    }

    public List<IntermediateInstruction> generateIr(int level) {
        List<IntermediateInstruction> instructions = new ArrayList<>();
        for (int i = 0; i < constDefs.size(); i++) {
            instructions.addAll(constDefs.get(i).generateIr(level));
        }
        return instructions;
    }
}
