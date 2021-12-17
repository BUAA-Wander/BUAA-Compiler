package treeNode;

import error.Error;
import ir.IntermediateInstruction;
import symbol.SymbolTable;
import symbol.type.Symbol;

import java.util.List;
import java.util.Map;

public class Cond extends TreeNode {
    private LOrExp lOrExp;

    public Cond(int num, LOrExp lOrExp) {
        super(num);
        this.lOrExp = lOrExp;
    }

    public String outputAdaptToHomework() {
        StringBuilder builder = new StringBuilder();
        builder.append(lOrExp.outputAdaptToHomework()).append("\n");
        builder.append("<Cond>");
        return builder.toString();
    }

    public void createSymbolTable(int level, SymbolTable symbolTable
            , List<Error> errors) {
        lOrExp.createSymbolTable(level, symbolTable, errors);
    }

    public String generateIr(int level, List<IntermediateInstruction> instructions) {
        return lOrExp.generateIr(level, instructions);
    }
}
