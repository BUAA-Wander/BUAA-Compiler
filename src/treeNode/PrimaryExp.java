package treeNode;

import exception.ValueTypeException;
import ir.IdGenerator;
import ir.IntermediateInstruction;
import ir.MovImmIr;
import symbol.AddressPtr;
import symbol.GlobalSymbolTable;
import symbol.LocalSymbolTable;
import symbol.SymbolTable;
import symbol.type.ParamType;
import symbol.type.SymbolType;
import symbol.type.VarBTypeSymbol;

import java.util.List;

public class PrimaryExp extends TreeNode {
    private PrimaryExpType type; // EXP, LVAL, NUMBER
    private LeftParent leftParent = null;
    private Exp exp = null;
    private RightParent rightParent = null;

    private LVal lVal = null;

    private Num num = null;

    public PrimaryExp(int num, PrimaryExpType type, LeftParent leftParent, Exp exp, RightParent rightParent) {
        super(num);
        this.type = type;
        this.leftParent = leftParent;
        this.exp = exp;
        this.rightParent = rightParent;
    }

    public PrimaryExp(int num, PrimaryExpType type, LVal lVal) {
        super(num);
        this.type = type;
        this.lVal = lVal;
    }

    public PrimaryExp(int number, PrimaryExpType type, Num num) {
        super(number);
        this.type = type;
        this.num = num;
    }

    public ParamType getParamType(SymbolTable symbolTable) {
        if (type == PrimaryExpType.NUMBER) {
            return ParamType.INT;
        } else if (type == PrimaryExpType.EXP) {
            return exp.getParamType(symbolTable);
        } else {
            return lVal.getParamType(symbolTable);
        }
    }

    public int getValue(int level) throws ValueTypeException {
        if (type == PrimaryExpType.NUMBER) {
            return num.getValue();
        } else if (type == PrimaryExpType.EXP) {
            return exp.getValue(level);
        } else {
            return lVal.getValue(level);
        }
    }

    public String generateIr(int level, List<IntermediateInstruction> instructions) {
        if (type == PrimaryExpType.NUMBER) {
            String id = IdGenerator.nextId();

            // insert tmp variable into symbolTable
            if (level == 0) {
                int addr = AddressPtr.getGlobalAddr();
                AddressPtr.addGlobalAddr(4);
                GlobalSymbolTable.insert(level, id, SymbolType.VAR,
                        new VarBTypeSymbol(-1, id), addr, 4);
            } else {
                int addr = AddressPtr.getLocalAddr();
                AddressPtr.addLocalAddr(4);
                LocalSymbolTable.insert(level, id, SymbolType.VAR,
                        new VarBTypeSymbol(-1, id), addr, 4);
            }

            int value = num.getValue();
            instructions.add(new MovImmIr(value, id));
            return id;
        } else if (type == PrimaryExpType.EXP) {
            return exp.generateIr(level, instructions);
        } else {
            return lVal.generateIr(level, instructions);
        }
    }
}
