package treeNode;

import error.Error;
import ir.AndIr;
import ir.IdGenerator;
import ir.IntermediateInstruction;
import ir.MovIr;
import ir.OrIr;
import symbol.AddressPtr;
import symbol.GlobalSymbolTable;
import symbol.LocalSymbolTable;
import symbol.SymbolTable;
import symbol.type.Symbol;
import symbol.type.SymbolType;
import symbol.type.VarBTypeSymbol;

import java.util.List;
import java.util.Map;

public class LAndExp extends TreeNode {
    private List<EqExp> eqExps;
    private List<Operator> operators;

    public LAndExp(int num, List<EqExp> eqExps, List<Operator> operators) {
        super(num);
        this.eqExps = eqExps;
        this.operators = operators;
    }

    public String outputAdaptToHomework() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < eqExps.size(); i++) {
            builder.append(eqExps.get(i).outputAdaptToHomework()).append("\n");
            if (i + 1 < eqExps.size()) {
                builder.append(operators.get(i).outputAdaptToHomework()).append("\n");
            }
        }
        builder.append("<LAndExp>");
        return builder.toString();
    }

    public void createSymbolTable(int level, SymbolTable symbolTable
            , List<Error> errors) {
        for (int i = 0; i < eqExps.size(); i++) {
            eqExps.get(i).createSymbolTable(level, symbolTable, errors);
        }
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
