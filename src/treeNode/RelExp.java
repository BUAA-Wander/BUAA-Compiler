package treeNode;

import ir.GeqIr;
import ir.IdGenerator;
import ir.IntermediateInstruction;
import ir.LargerIr;
import ir.LeqIr;
import ir.LessIr;
import ir.MovIr;
import symbol.AddressPtr;
import symbol.GlobalSymbolTable;
import symbol.LocalSymbolTable;
import symbol.SymbolTable;
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