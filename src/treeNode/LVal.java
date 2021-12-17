package treeNode;

import exception.ValueTypeException;
import ir.AddIr;
import ir.TmpVarGenerator;
import ir.IntermediateInstruction;
import ir.LoadArrayValueIr;
import ir.MovImmIr;
import ir.MovIr;
import ir.MulIr;
import ir.utils.Immediate;
import ir.utils.Operand;
import ir.utils.TmpVariable;
import ir.utils.Variable;
import symbol.GlobalSymbolTable;
import symbol.LocalSymbolTable;
import symbol.SymbolTable;
import symbol.SymbolTableItem;
import symbol.type.ConstArraySymbol;
import symbol.type.ConstBTypeSymbol;
import symbol.type.ParamType;
import symbol.type.Symbol;
import symbol.type.SymbolType;
import symbol.type.VarArraySymbol;
import symbol.type.VarBTypeSymbol;

import java.util.ArrayList;
import java.util.List;

public class LVal extends TreeNode {
    private Ident ident;
    private List<LeftBrack> leftBracks;
    private List<Exp> index;
    private List<RightBrack> rightBracks;

    public LVal(int num, Ident ident, List<LeftBrack> leftBracks, List<Exp> index, List<RightBrack> rightBracks) {
        super(num);
        this.ident = ident;
        this.leftBracks = leftBracks;
        this.index = index;
        this.rightBracks = rightBracks;
    }

    public boolean isConstLVal(SymbolTable symbolTable) {
        return symbolTable.isConstTableItem(ident.getName());
    }

    // TODO: check if it is right
    public ParamType getParamType(SymbolTable symbolTable) {
        SymbolTableItem item = symbolTable.getItem(ident.getName());
        Symbol symbol = item.getSymbol();
        if (symbol instanceof ConstBTypeSymbol || symbol instanceof VarBTypeSymbol) {
            return ParamType.INT;
        } else if (symbol instanceof ConstArraySymbol) {
            int dims = ((ConstArraySymbol) symbol).getDims();
            if (dims == 1) {
                return ParamType.ARRAY_1;
            } else {
                return ParamType.ARRAY_2;
            }
        } else if (symbol instanceof VarArraySymbol) {
            int dims = ((VarArraySymbol) symbol).getDims();
            if (dims == 1) {
                return ParamType.ARRAY_1;
            } else {
                return ParamType.ARRAY_2;
            }
        }
        return ParamType.INT;
    }

    public int getValue(int level) throws ValueTypeException {
        // const int a[1][2] = {{1, 2}};
        // const int b[a[1][0]] = {1};
        int value;
        if (leftBracks.size() != 0) {
            SymbolTableItem item;
            if (LocalSymbolTable.isExist(level, ident.getName(), SymbolType.ARRAY)) {
                item = LocalSymbolTable.getItem(ident.getName(), SymbolType.ARRAY);
            } else if (GlobalSymbolTable.isExist(level, ident.getName(), SymbolType.ARRAY)) {
                item = GlobalSymbolTable.getItem(ident.getName(), SymbolType.ARRAY);
            } else {
                throw new ValueTypeException();
            }

            Symbol symbol = item.getSymbol();
            if (symbol instanceof ConstArraySymbol) {
                int idx = 0;
                List<Integer> pos = new ArrayList<>();
                for (int i = 0; i < index.size(); i++) {
                    pos.add(index.get(i).getValue(level));
                }

                List<ConstExp> dimSizes = ((ConstArraySymbol) symbol).getDimSizes();
                List<Integer> sizes = new ArrayList<>();
                for (int i = 0; i < dimSizes.size(); i++) {
                    sizes.add(dimSizes.get(i).getValue(level));
                }

                for (int i = pos.size() - 1; i >= 0; i--) {
                    idx *= sizes.get(i);
                    idx += pos.get(i);
                }

                List<Integer> values = ((ConstArraySymbol) symbol).getValues();
                return values.get(idx);
            } else {
                throw new ValueTypeException();
            }
        } else {
            if (LocalSymbolTable.isExist(level, ident.getName(), SymbolType.VAR)) {
                SymbolTableItem item = LocalSymbolTable.getItem(ident.getName(), SymbolType.VAR);
                Symbol symbol = item.getSymbol();
                if (symbol instanceof ConstBTypeSymbol) {
                    value = ((ConstBTypeSymbol) symbol).getValue();
                } else {
                    throw new ValueTypeException();
                }
            } else if (GlobalSymbolTable.isExist(level, ident.getName(), SymbolType.VAR)) {
                SymbolTableItem item = GlobalSymbolTable.getItem(ident.getName(), SymbolType.VAR);
                Symbol symbol = item.getSymbol();
                if (symbol instanceof ConstBTypeSymbol) {
                    value = ((ConstBTypeSymbol) symbol).getValue();
                } else {
                    throw new ValueTypeException();
                }
            } else {
                throw new ValueTypeException();
            }
        }
        return value;
    }

    public Operand generateIr(int level, List<IntermediateInstruction> instructions) {
        try {
            if (leftBracks.size() == 0) {
                // TODO why next TODO
                // TODO delete expired variable!!!!!!!!!!!!
                if (LocalSymbolTable.isExist(level, ident.getName(), SymbolType.VAR)) {
                    SymbolTableItem item = LocalSymbolTable.getItem(ident.getName(), SymbolType.VAR);
                    return new Variable(item.getName(), item.getAddr(), false);
                } else if (GlobalSymbolTable.isExist(level, ident.getName(), SymbolType.VAR)) {
                    SymbolTableItem item = GlobalSymbolTable.getItem(ident.getName(), SymbolType.VAR);
                    return new Variable(item.getName(), item.getAddr(), true);
                } else {
                    System.out.println("TODO: implement array param pass");
                    throw new ValueTypeException();
                }
            } else {
                SymbolTableItem item;
                int scope;
                if (LocalSymbolTable.isExist(level, ident.getName(), SymbolType.ARRAY)) {
                    item = LocalSymbolTable.getItem(ident.getName(), SymbolType.ARRAY);
                    scope = 1;
                } else if (GlobalSymbolTable.isExist(level, ident.getName(), SymbolType.ARRAY)) {
                    item = GlobalSymbolTable.getItem(ident.getName(), SymbolType.ARRAY);
                    scope = 0;
                } else {
                    throw new ValueTypeException();
                }

                Symbol symbol = item.getSymbol();
                List<ConstExp> dimSizes;
                if (symbol instanceof ConstArraySymbol) {
                    dimSizes = ((ConstArraySymbol) symbol).getDimSizes();
                } else if (symbol instanceof VarArraySymbol) {
                    dimSizes = ((VarArraySymbol) symbol).getDimSizes();
                } else {
                    throw new ValueTypeException();
                }

                Operand offsetId = new TmpVariable(TmpVarGenerator.nextTmpVar(level), (level == 0));
                instructions.add(new MovIr(new TmpVariable("#0", true), offsetId));

                Operand base = new TmpVariable(TmpVarGenerator.nextTmpVar(level), (level == 0));

                // calculate index!!!!!
                if (index.size() == 1) {
                    offsetId = index.get(0).generateIr(level, instructions);
                } else {
                    Operand tmp0 = index.get(0).generateIr(level, instructions);
                    Operand tmp1 = index.get(1).generateIr(level, instructions);
                    Operand size =  dimSizes.get(1).generateIr(level, instructions);
                    instructions.add(new MulIr(tmp0, size, tmp0));
                    instructions.add(new AddIr(tmp0, tmp1, offsetId));
                }

                Operand headAddr = new TmpVariable(TmpVarGenerator.nextTmpVar(level), (level == 0));

                // base is not the headAddr of array!!! base = 4
                instructions.add(new MovImmIr(new Immediate(4), base));
                instructions.add(new MovImmIr(new Immediate(item.getAddr()), headAddr));
                instructions.add(new MulIr(base, offsetId, offsetId));

                // offsetId is offset to array head
                // TODO: find value by offset and array head
                if (level == 0) {
                    instructions.add(new LoadArrayValueIr(headAddr, offsetId, offsetId, scope));
                } else {
                    instructions.add(new LoadArrayValueIr(headAddr, offsetId, offsetId, scope));
                }

                return offsetId;
            }
        } catch (ValueTypeException e) {
            e.printStackTrace();
        }
        return null;
    }
}
