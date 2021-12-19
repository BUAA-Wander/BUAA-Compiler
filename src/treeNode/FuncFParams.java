package treeNode;

import ir.IntermediateInstruction;
import symbol.type.ParamType;

import java.util.ArrayList;
import java.util.List;

public class FuncFParams extends TreeNode {
    private List<FuncFParam> funcFParams;
    private List<Comma> commas;

    public FuncFParams(int num, List<FuncFParam> funcFParams, List<Comma> commas) {
        super(num);
        this.funcFParams = funcFParams;
        this.commas = commas;
    }

    public int getParamsCount() {
        return funcFParams.size();
    }

    public List<ParamType> getParamTypes() {
        List<ParamType> types = new ArrayList<>();
        if (funcFParams != null) {
            for (int i = 0; i < funcFParams.size(); i++) {
                types.add(funcFParams.get(i).getParamType());
            }
        }
        return types;
    }

    public List<IntermediateInstruction> generateIr(int level) {
        List<IntermediateInstruction> instructions = new ArrayList<>();
        if (funcFParams == null) {
            return instructions;
        }

        // order!!!
        for (int i = 0; i < funcFParams.size(); i++) {
            instructions.addAll(funcFParams.get(i).generateIr(level));
        }

        return instructions;
    }
}
