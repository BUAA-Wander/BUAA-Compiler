package treeNode;

import ir.GeqIr;
import ir.TmpVarGenerator;
import ir.IntermediateInstruction;
import ir.LargerIr;
import ir.LeqIr;
import ir.LessIr;
import ir.MovIr;
import symbol.AddressPtr;
import symbol.GlobalSymbolTable;
import symbol.LocalSymbolTable;
import symbol.type.SymbolType;
import symbol.type.VarBTypeSymbol;

import java.util.List;

public class RelExp extends TreeNode {
    private List<AddExp> addExps;
    private List<Operator> operators;

    public RelExp(int num, List<AddExp> addExps, List<Operator> operators) {
        super(num);
        this.addExps = addExps;
        this.operators = operators;
    }

    public String generateIr(int level, List<IntermediateInstruction> instructions) {
        String resId = TmpVarGenerator.nextTmpVar(level);

        for (int i = 0; i < addExps.size(); i++) {
            // TODO
            String id = addExps.get(i).generateIr(level, instructions);
            if (i != 0) {
                OperatorType type = operators.get(i - 1).getType();
                if (type.equals(OperatorType.LSS)) {
                    instructions.add(new LessIr(resId, id, resId));
                } else if (type.equals(OperatorType.LEQ)) {
                    instructions.add(new LeqIr(resId, id, resId));
                } else if (type.equals(OperatorType.GRE)) {
                    instructions.add(new LargerIr(resId, id, resId));
                } else {
                    instructions.add(new GeqIr(resId, id, resId));
                }
            } else {
                instructions.add(new MovIr(id, resId));
            }
        }
        return resId;
    }
}