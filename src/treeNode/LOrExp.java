package treeNode;

import ir.TmpVarGenerator;
import ir.IntermediateInstruction;
import ir.MovIr;
import ir.OrIr;
import symbol.AddressPtr;
import symbol.GlobalSymbolTable;
import symbol.LocalSymbolTable;
import symbol.type.SymbolType;
import symbol.type.VarBTypeSymbol;

import java.util.List;

public class LOrExp extends TreeNode {
    private List<LAndExp> lAndExps;
    private List<Operator> operators;

    public LOrExp(int num, List<LAndExp> lAndExps, List<Operator> operators) {
        super(num);
        this.lAndExps = lAndExps;
        this.operators = operators;
    }

    public String generateIr(int level, List<IntermediateInstruction> instructions) {
        String resId = TmpVarGenerator.nextTmpVar(level);

        for (int i = 0; i < lAndExps.size(); i++) {
            // TODO
            String id = lAndExps.get(i).generateIr(level, instructions);
            if (i != 0) {
                instructions.add(new OrIr(resId, id, resId));
            } else {
                instructions.add(new MovIr(id, resId));
            }
        }
        return resId;
    }
}
