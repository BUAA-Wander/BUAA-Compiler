package treeNode;

import exception.ValueTypeException;
import ir.AddIr;
import ir.TmpVarGenerator;
import ir.IntermediateInstruction;
import ir.MovIr;
import ir.SubIr;
import symbol.AddressPtr;
import symbol.GlobalSymbolTable;
import symbol.LocalSymbolTable;
import symbol.SymbolTable;
import symbol.type.ParamType;
import symbol.type.SymbolType;
import symbol.type.VarBTypeSymbol;

import java.util.List;

public class AddExp extends TreeNode {
    private List<MulExp> mulExps;
    private List<Operator> operators;

    public AddExp(int num, List<MulExp> mulExps, List<Operator> operators) {
        super(num);
        this.mulExps = mulExps;
        this.operators = operators;
    }

    public ParamType getParamType(SymbolTable symbolTable) {
        if (mulExps != null) {
            if (mulExps.size() > 0) {
                return mulExps.get(0).getParamType(symbolTable);
            }
        }
        return null;
    }

    public int getValue(int level) throws ValueTypeException {
        int res = mulExps.get(0).getValue(level);
        for (int i = 1; i < mulExps.size(); i++) {
            if (operators.get(i - 1).getType().equals(OperatorType.ADD)) {
                res += mulExps.get(i).getValue(level);
            } else {
                res -= mulExps.get(i).getValue(level);
            }
        }
        return res;
    }

    public String generateIr(int level, List<IntermediateInstruction> instructions, int used) {
        String resId = TmpVarGenerator.nextTmpVar(level);

        for (int i = 0; i < mulExps.size(); i++) {
            String id = mulExps.get(i).generateIr(level, instructions, used);
            if (i != 0) {
                if (operators.get(i - 1).getType().equals(OperatorType.ADD)) {
                    instructions.add(new AddIr(resId, id, resId));
                } else {
                    instructions.add(new SubIr(resId, id, resId));
                }
            } else {
                instructions.add(new MovIr(id, resId));
            }
        }
        return resId;
    }
}
