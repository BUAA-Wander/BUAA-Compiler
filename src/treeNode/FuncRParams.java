package treeNode;

import ir.IntermediateInstruction;
import ir.PassParamIr;
import ir.utils.Operand;
import symbol.SymbolTable;
import symbol.type.ParamType;
import treeNode.util.LValAnalyseMode;

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
            // TODO 必须把数组当成左值分析？
            // TODO 数组必须传进去地址
            LValAnalyseMode.setAnalyseMode(true);
            Operand memAddr = exps.get(i).generateIr(level, instructions, i * 4 + used);
            LValAnalyseMode.setAnalyseMode(false);
            instructions.add(new PassParamIr(memAddr, -i * 4 - used));
        }
        return instructions;
    }

    public List<IntermediateInstruction> generateIr(int level) {
        List<IntermediateInstruction> instructions = new ArrayList<>();
        for (int i = 0; i < exps.size(); i++) {
            // i * 4 means we deal with i params now
            Operand memAddr = exps.get(i).generateIr(level, instructions, i * 4);
            instructions.add(new PassParamIr(memAddr, -i * 4));
        }
        return instructions;
    }
}