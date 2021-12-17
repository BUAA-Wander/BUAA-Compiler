package treeNode;

import ir.IntermediateInstruction;
import ir.PassParamIr;
import symbol.SymbolTable;
import symbol.type.ParamType;

import java.util.ArrayList;
import java.util.List;

public class FuncRParams extends TreeNode {
    private List<Exp> exps;
    private List<Comma> commas;

    public FuncRParams(int num, List<Exp> exps, List<Comma> commas) {
        super(num);
        this.exps = exps;
        this.commas = commas;
    }

    public int getParamCount() {
        return exps.size();
    }

    public List<ParamType> getParamTypes(SymbolTable symbolTable) {
        List<ParamType> paramTypes = new ArrayList<>();
        if (exps != null) {
            for (int i = 0; i < exps.size(); i++) {
                paramTypes.add(exps.get(i).getParamType(symbolTable));
            }
        }
        return paramTypes;
    }

    public List<IntermediateInstruction> generateIr(int level, int used) {
        List<IntermediateInstruction> instructions = new ArrayList<>();
        for (int i = 0; i < exps.size(); i++) {
            String memAddr = exps.get(i).generateIr(level, instructions, i * 4 + used);
            instructions.add(new PassParamIr(memAddr, -i * 4 - used));
        }
        return instructions;
    }

    public List<IntermediateInstruction> generateIr(int level) {
        List<IntermediateInstruction> instructions = new ArrayList<>();
        for (int i = 0; i < exps.size(); i++) {
            // i * 4 means we deal with i params now
            String memAddr = exps.get(i).generateIr(level, instructions, i * 4);
            instructions.add(new PassParamIr(memAddr, -i * 4));
        }
        return instructions;
    }
}