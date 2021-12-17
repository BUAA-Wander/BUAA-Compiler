package treeNode;

import ir.AndIr;
import ir.BranchIfEqIr;
import ir.BranchIfNotEqIr;
import ir.InsertLabelIr;
import ir.LabelGenerator;
import ir.TmpVarGenerator;
import ir.IntermediateInstruction;
import ir.MovIr;
import ir.utils.LabelOp;
import ir.utils.Operand;
import ir.utils.TmpVariable;
import symbol.AddressPtr;
import symbol.GlobalSymbolTable;
import symbol.LocalSymbolTable;
import symbol.type.SymbolType;
import symbol.type.VarBTypeSymbol;

import java.util.List;

public class LAndExp extends TreeNode {
    private List<EqExp> eqExps;
    private List<Operator> operators;

    public LAndExp(int num, List<EqExp> eqExps, List<Operator> operators) {
        super(num);
        this.eqExps = eqExps;
        this.operators = operators;
    }

    public Operand generateIr(int level, List<IntermediateInstruction> instructions) {
        Operand resId = new TmpVariable(TmpVarGenerator.nextTmpVar(level), (level == 0));
        LabelOp label = new LabelOp(LabelGenerator.nextLabel());
        for (int i = 0; i < eqExps.size(); i++) {
            // TODO
            Operand id = eqExps.get(i).generateIr(level, instructions);
            if (i != 0) {
                instructions.add(new AndIr(resId, id, resId));
            } else {
                instructions.add(new MovIr(id, resId));
            }
            // 短路求值
            instructions.add(
                    new BranchIfEqIr(resId, new TmpVariable("#0", true), label));
        }
        instructions.add(new InsertLabelIr(label));
        return resId;
    }
}
