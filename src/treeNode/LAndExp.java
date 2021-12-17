package treeNode;

import ir.AndIr;
import ir.IdGenerator;
import ir.IntermediateInstruction;
import ir.MovIr;
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

        for (int i = 0; i < eqExps.size(); i++) {
            // TODO
            String id = eqExps.get(i).generateIr(level, instructions);
            if (i != 0) {
                instructions.add(new AndIr(resId, id, resId));
            } else {
                instructions.add(new MovIr(id, resId));
            }
        }
        return resId;
    }
}
