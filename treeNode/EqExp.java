package treeNode;

import ir.EqIr;
import ir.TmpVarGenerator;
import ir.IntermediateInstruction;
import ir.MovIr;
import ir.NeqIr;
import ir.utils.Operand;
import ir.utils.TmpVariable;
import symbol.AddressPtr;
import symbol.GlobalSymbolTable;
import symbol.LocalSymbolTable;
import symbol.type.SymbolType;
import symbol.type.VarBTypeSymbol;

import java.util.List;

public class EqExp extends TreeNode {
    private List<RelExp> relExps;
    private List<Operator> operators;

    public EqExp(int num, List<RelExp> relExps, List<Operator> operators) {
        super(num);
        this.relExps = relExps;
        this.operators = operators;
    }

    public Operand generateIr(int level, List<IntermediateInstruction> instructions) {
        Operand resId = new TmpVariable(level, TmpVarGenerator.nextTmpVar(level), (level == 0));

        for (int i = 0; i < relExps.size(); i++) {
            // TODO
            Operand id = relExps.get(i).generateIr(level, instructions);
            if (i != 0) {
                if (operators.get(i - 1).getType().equals(OperatorType.EQ)) {
                    instructions.add(new EqIr(resId, id, resId));
                } else {
                    instructions.add(new NeqIr(resId, id, resId));
                }
            } else {
                if (relExps.size() == 1) {
                    return id;
                } else {
                    instructions.add(new MovIr(id, resId));
                }
            }
        }
        return resId;
    }
}
