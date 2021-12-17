package treeNode;

import exception.ValueTypeException;
import ir.IdGenerator;
import ir.IntermediateInstruction;
import ir.MovIr;
import ir.MulIr;
import ir.DivIr;
import ir.ModIr;
import symbol.AddressPtr;
import symbol.GlobalSymbolTable;
import symbol.LocalSymbolTable;
import symbol.SymbolTable;
import symbol.type.ParamType;
import symbol.type.SymbolType;
import symbol.type.VarBTypeSymbol;

import java.util.List;

public class MulExp extends TreeNode {
    private List<UnaryExp> unaryExps;
    private List<Operator> operators;

    public MulExp(int num, List<UnaryExp> unaryExps, List<Operator> operators) {
        super(num);
        this.unaryExps = unaryExps;
        this.operators = operators;
    }

    public ParamType getParamType(SymbolTable symbolTable) {
        if (unaryExps != null) {
            if (unaryExps.size() > 0) {
                return unaryExps.get(0).getParamType(symbolTable);
            }
        }
        return null;
    }

    public int getValue(int level) throws ValueTypeException  {
        int res = unaryExps.get(0).getValue(level);
        for (int i = 1; i < unaryExps.size(); i++) {
            if (operators.get(i - 1).getType().equals(OperatorType.MUL)) {
                res *= unaryExps.get(i).getValue(level);
            } else if (operators.get(i - 1).getType().equals(OperatorType.DIV)) {
                res /= unaryExps.get(i).getValue(level);
            } else {
                res %= unaryExps.get(i).getValue(level);
            }
        }
        return res;
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
        }

        for (int i = 0; i < unaryExps.size(); i++) {
            String id = unaryExps.get(i).generateIr(level, instructions);
            if (i != 0) {
                if (operators.get(i - 1).getType().equals(OperatorType.MUL)) {
                    instructions.add(new MulIr(resId, id, resId));
                } else if (operators.get(i - 1).getType().equals(OperatorType.DIV)) {
                    instructions.add(new DivIr(resId, id, resId));
                } else {
                    instructions.add(new ModIr(resId, id, resId));
                }
            } else {
                instructions.add(new MovIr(id, resId));
            }
        }
        return resId;
    }

    public String generateIr(int level, List<IntermediateInstruction> instructions, int used) {
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
        }

        for (int i = 0; i < unaryExps.size(); i++) {
            String id = unaryExps.get(i).generateIr(level, instructions, used);
            if (i != 0) {
                if (operators.get(i - 1).getType().equals(OperatorType.MUL)) {
                    instructions.add(new MulIr(resId, id, resId));
                } else if (operators.get(i - 1).getType().equals(OperatorType.DIV)) {
                    instructions.add(new DivIr(resId, id, resId));
                } else {
                    instructions.add(new ModIr(resId, id, resId));
                }
            } else {
                instructions.add(new MovIr(id, resId));
            }
        }
        return resId;
    }
}
