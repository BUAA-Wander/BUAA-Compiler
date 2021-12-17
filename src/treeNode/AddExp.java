package treeNode;

import error.Error;
import exception.ValueTypeException;
import ir.AddIr;
import ir.IdGenerator;
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

    public String outputAdaptToHomework() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < mulExps.size(); i++) {
            MulExp mulExp = mulExps.get(i);
            builder.append(mulExp.outputAdaptToHomework()).append("\n");
            builder.append("<AddExp>");
            if (i + 1 < mulExps.size()) {
                builder.append("\n");
                Operator operator = operators.get(i);
                builder.append(operator.outputAdaptToHomework()).append("\n");
            }
        }
        return builder.toString();
    }

    public void createSymbolTable(int level, SymbolTable symbolTable,
                                  List<Error> errors) {
        for (int i = 0; i < mulExps.size(); i++) {
            mulExps.get(i).createSymbolTable(level, symbolTable, errors);
        }
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

        for (int i = 0; i < mulExps.size(); i++) {
            String id = mulExps.get(i).generateIr(level, instructions);
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
            // move sp only when we call function or call back!
        }

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
