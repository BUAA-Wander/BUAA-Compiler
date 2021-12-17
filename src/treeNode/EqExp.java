package treeNode;

import ir.EqIr;
import ir.IdGenerator;
import ir.IntermediateInstruction;
import ir.MovIr;
import ir.NeqIr;
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

    public String generateIr(int level, List<IntermediateInstruction> instructions) {
        String resId = IdGenerator.nextId();

        // insert tmp variable into symbolTable
        if (level == 0) {
            int addr = AddressPtr.getGlobalAddr();
            AddressPtr.addGlobalAddr(4);
            GlobalSymbolTable.insert(level, resId, SymbolType.VAR,
                    new VarBTypeSymbol(-1, resId), addr, 4);
        } else {
            int addr = AddressPtr.getLocalAddr();
            AddressPtr.addLocalAddr(4);
            LocalSymbolTable.insert(level, resId, SymbolType.VAR,
                    new VarBTypeSymbol(-1, resId), addr, 4);
            // move sp only when we call function or call back!
        }

        for (int i = 0; i < relExps.size(); i++) {
            // TODO
            String id = relExps.get(i).generateIr(level, instructions);
            if (i != 0) {
                if (operators.get(i - 1).getType().equals(OperatorType.EQ)) {
                    instructions.add(new EqIr(resId, id, resId));
                } else {
                    instructions.add(new NeqIr(resId, id, resId));
                }
            } else {
                instructions.add(new MovIr(id, resId));
            }
        }
        return resId;
    }
}
