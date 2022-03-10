package treeNode;

import exception.ValueTypeException;
import ir.AddIr;
import ir.OffsetIr;
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
import symbol.type.PointerSymbol;
import symbol.type.Symbol;
import symbol.type.SymbolType;
import symbol.type.VarArraySymbol;
import symbol.type.VarBTypeSymbol;
import treeNode.util.LValAnalyseMode;
import treeNode.util.PassingValueMode;

import java.util.ArrayList;
import java.util.List;

public class LVal extends TreeNode {
    private Ident ident;
    private List<LeftBrack> leftBracks;
    private List<Exp> index;
    private List<RightBrack> rightBracks;
    private boolean analyseMode;
    private boolean isGlobal;

    public LVal(int num, Ident ident, List<LeftBrack> leftBracks, List<Exp> index, List<RightBrack> rightBracks) {
        super(num);
        this.ident = ident;
        this.leftBracks = leftBracks;
        this.index = index;
        this.rightBracks = rightBracks;
    }

    public void setAnalyseMode(boolean flag) {
        analyseMode = flag;
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
                item = LocalSymbolTable.getItem(level, ident.getName(), SymbolType.ARRAY);
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
                SymbolTableItem item = LocalSymbolTable.getItem(level, ident.getName(), SymbolType.VAR);
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

    // 判断此左值是不是指针，由此判断调用该左值的代码生成方法得到的是一个已有的变量还是指向已有变量的指针
    // a[1][2] 这种数组元素也算是指针，要返回a[1][2]的地址，然后赋值的时候往存储的这个地址写值
    public boolean isPointer(int level) {
        try {
            if (leftBracks.size() == 0) {
                // TODO why next TODO
                // TODO delete expired variable!!!!!!!!!!!!
                if (LocalSymbolTable.isExist(level, ident.getName(), SymbolType.VAR)) {
                    return false;
                } else {
                    return !GlobalSymbolTable.isExist(level, ident.getName(), SymbolType.VAR);
                }
            } else {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isGlobalVar() {
        return GlobalSymbolTable.isExist(0, ident.getName(), SymbolType.ARRAY)
                || GlobalSymbolTable.isExist(0, ident.getName(), SymbolType.VAR)
                || GlobalSymbolTable.isExist(0, ident.getName(), SymbolType.POINTER);
    }

    private Operand analyseRightMode(int level, List<IntermediateInstruction> instructions)
            throws ValueTypeException {
        // return a value
        SymbolTableItem item;
        int scope;
        if (LocalSymbolTable.isExist(level, ident.getName(), SymbolType.ARRAY)) {
            item = LocalSymbolTable.getItem(level, ident.getName(), SymbolType.ARRAY);
            scope = 1; // 1是局部，0是全局
        } else if (GlobalSymbolTable.isExist(level, ident.getName(), SymbolType.ARRAY)) {
            item = GlobalSymbolTable.getItem(ident.getName(), SymbolType.ARRAY);
            scope = 0;
        } else {
            // 左值是指针类型，在本文法下只可能是数组作为参数传入某个函数，然后该函数使用该参数
            if (LocalSymbolTable.isExist(level, ident.getName(), SymbolType.POINTER)) {
                // System.out.println("Haven't implement pointer LVal yet!");
                item = LocalSymbolTable.getItem(level, ident.getName(), SymbolType.POINTER);
                scope = 1;
            } else {
                throw new ValueTypeException();
            }
        }

        Symbol symbol = item.getSymbol();
        List<ConstExp> dimSizes = null;
        ConstExp lastDimSize = null;
        if (symbol instanceof ConstArraySymbol) {
            dimSizes = ((ConstArraySymbol) symbol).getDimSizes();
        } else if (symbol instanceof VarArraySymbol) {
            dimSizes = ((VarArraySymbol) symbol).getDimSizes();
        } else {
            // 指针类型
            if (symbol instanceof PointerSymbol) {
                lastDimSize = ((PointerSymbol) symbol).getLastDimSize();
            } else {
                throw new ValueTypeException();
            }
        }

        Operand offsetId = new TmpVariable(level, TmpVarGenerator.nextTmpVar(level), (level == 0));
        instructions.add(new MovIr(new TmpVariable(level, "#0", true), offsetId));

        Operand base = new TmpVariable(level, TmpVarGenerator.nextTmpVar(level), (level == 0));

        // calculate index!!!!!
        if (index.size() == 1) {
            offsetId = index.get(0).generateIr(level, instructions);
        } else {
            Operand tmp0 = index.get(0).generateIr(level, instructions);
            Operand tmp1 = index.get(1).generateIr(level, instructions);
            Operand size = null;
            if (dimSizes != null) {
                size = dimSizes.get(1).generateIr(level, instructions);
            } else {
                try {
                    size = lastDimSize.generateIr(level, instructions);
                } catch (NullPointerException e) {
                    System.out.println("Can't find this PointerSymbol");
                }
            }
            instructions.add(new MulIr(tmp0, size, tmp0));
            instructions.add(new AddIr(tmp0, tmp1, offsetId));
        }

        Operand headAddr = new TmpVariable(level, TmpVarGenerator.nextTmpVar(level), (level == 0));

        // base is not the headAddr of array!!! base = 4
        instructions.add(new MovImmIr(new Immediate(4), base));

        instructions.add(new MovImmIr(new Immediate(item.getAddr()), headAddr));
        instructions.add(new MulIr(base, offsetId, offsetId));

        // offsetId is offset to array head
        // TODO: find value by offset and array head
        // 如果是函数的形参数组，则需要先通过符号表项，拿出来这个指针的值（即指向的绝对地址）
        // 然后再通过这个绝对地址去取数组的值
        // 当前的headAddr不是绝对地址
        // 注意如果是全局数组则不能传scope = 1
        boolean isBasePointer = (symbol instanceof PointerSymbol);
        if (isBasePointer) {
            // 当前的headAddr是个偏移量
            // 传false的原因是当前headAddr不是绝对地址，所以需要让它在该中间指令内部确定自己按照什么方式算绝对地址
            instructions.add(new LoadArrayValueIr(headAddr, new Immediate(0), headAddr, 1, false));
            // instructions.add(new LoadArrayValueIr(headAddr, new Immediate(0), headAddr, 1, true));
        }

        // TODO 当isBasePointer = true的时候，如何告诉中间代码他现在处理的这个pointer指向的是局部还是全局地址
        if (level == 0) {
            instructions.add(new LoadArrayValueIr(headAddr, offsetId, offsetId, scope, isBasePointer));
        } else {
            instructions.add(new LoadArrayValueIr(headAddr, offsetId, offsetId, scope, isBasePointer));
        }

        return offsetId;
    }

    private Operand analyseLeftMode(int level, List<IntermediateInstruction> instructions)
            throws ValueTypeException {
        SymbolTableItem item;
        int scope;
        if (LocalSymbolTable.isExist(level, ident.getName(), SymbolType.ARRAY)) {
            item = LocalSymbolTable.getItem(level, ident.getName(), SymbolType.ARRAY);
            scope = 1;
        } else if (GlobalSymbolTable.isExist(level, ident.getName(), SymbolType.ARRAY)) {
            item = GlobalSymbolTable.getItem(ident.getName(), SymbolType.ARRAY);
            scope = 0;
        } else {
            // 左值是指针类型，在本文法下只可能是数组作为参数传入某个函数，然后该函数使用该参数
            if (LocalSymbolTable.isExist(level, ident.getName(), SymbolType.POINTER)) {
                // System.out.println("Haven't implement pointer LVal yet!");
                item = LocalSymbolTable.getItem(level, ident.getName(), SymbolType.POINTER);
                scope = 1;
            } else {
                throw new ValueTypeException();
            }
        }

        Symbol symbol = item.getSymbol();
        List<ConstExp> dimSizes = null;
        ConstExp lastDimSize = null;
        int dims = 0;
        if (symbol instanceof ConstArraySymbol) {
            dimSizes = ((ConstArraySymbol) symbol).getDimSizes();
            dims = dimSizes.size();
        } else if (symbol instanceof VarArraySymbol) {
            dimSizes = ((VarArraySymbol) symbol).getDimSizes();
            dims = dimSizes.size();
        } else {
            // 指针类型
            if (symbol instanceof PointerSymbol) {
                lastDimSize = ((PointerSymbol) symbol).getLastDimSize();
                dims = ((PointerSymbol) symbol).getDims();
            } else {
                throw new ValueTypeException();
            }
        }

        Operand offsetId = new TmpVariable(level, TmpVarGenerator.nextTmpVar(level), (level == 0));
        instructions.add(new MovIr(new TmpVariable(level, "#0", true), offsetId));

        Operand base = new TmpVariable(level, TmpVarGenerator.nextTmpVar(level), (level == 0));

        // calculate index!!!!!
        if (index.size() == 1) {
            offsetId = index.get(0).generateIr(level, instructions);
            if (dimSizes != null && dimSizes.size() > 1) {
                ConstExp secondSize = dimSizes.get(1);
                instructions.add(new MulIr(offsetId, new Immediate(secondSize.getValue(level)), offsetId));
            } else if (lastDimSize != null) {
                // TODO 二维数组先被作为参数传到一个函数，然后在这个函数中又做了部分数组传参
                if (dims == 2) {
                    // 二维数组变成的指针被用了一维
                    instructions.add(new MulIr(offsetId, new Immediate(lastDimSize.getValue(level)), offsetId));
                } else {
                    // TODO
                }
            }
        } else {
            Operand tmp0 = index.get(0).generateIr(level, instructions);
            Operand tmp1 = index.get(1).generateIr(level, instructions);
            Operand size = null;
            if (dimSizes != null) {
                size = dimSizes.get(1).generateIr(level, instructions);
            } else {
                try {
                    size = lastDimSize.generateIr(level, instructions);
                } catch (NullPointerException e) {
                    System.out.println("Can't find this PointerSymbol");
                }
            }
            instructions.add(new MulIr(tmp0, size, tmp0));
            instructions.add(new AddIr(tmp0, tmp1, offsetId));
        }

        Operand headAddr = new TmpVariable(level, TmpVarGenerator.nextTmpVar(level), (level == 0));

        // base is not the headAddr of array!!! base = 4
        instructions.add(new MovImmIr(new Immediate(4), base));
        instructions.add(new MovImmIr(new Immediate(item.getAddr()), headAddr));
        instructions.add(new MulIr(base, offsetId, offsetId));

        Operand res = new TmpVariable(level, TmpVarGenerator.nextTmpVar(level), (level == 0));
        boolean isGlobal = (scope == 0);
        boolean isBasePointer = (symbol instanceof PointerSymbol);

        if (isBasePointer) {
            // ?
            instructions.add(new LoadArrayValueIr(headAddr, new Immediate(0), headAddr, 1, false));

            // instructions.add(new LoadArrayValueIr(headAddr, new Immediate(0), headAddr, 1, true));
        }

        if (PassingValueMode.getAnalyseMode() && dims != 0 && dims == index.size()) {
//        if (PassingValueMode.getAnalyseMode() && dimSizes != null && dimSizes.size() == index.size()) {
            if (level == 0) {
                instructions.add(new LoadArrayValueIr(headAddr, offsetId, res, scope, isBasePointer));
            } else {
                instructions.add(new LoadArrayValueIr(headAddr, offsetId, res, scope, isBasePointer));
            }
        } else {
            instructions.add(new OffsetIr(headAddr, offsetId, res, isGlobal, isBasePointer));
        }


        return res;
    }

    public Operand generateIr(int level, List<IntermediateInstruction> instructions) {
        try {
            if (leftBracks.size() == 0) {
                // TODO why next TODO
                // TODO delete expired variable!!!!!!!!!!!!
                // 先看是不是基本变量
                if (LocalSymbolTable.isExist(level, ident.getName(), SymbolType.VAR)) {
                    SymbolTableItem item = LocalSymbolTable.getItem(level, ident.getName(), SymbolType.VAR);
                    return new Variable(item.getName(), item.getAddr(), false);
                } else if (GlobalSymbolTable.isExist(level, ident.getName(), SymbolType.VAR)) {
                    SymbolTableItem item = GlobalSymbolTable.getItem(ident.getName(), SymbolType.VAR);
                    return new Variable(item.getName(), item.getAddr(), true);
                } else {
                    // 再看是不是数组
                    if (LocalSymbolTable.isExist(level, ident.getName(), SymbolType.ARRAY)) {
                        SymbolTableItem item = LocalSymbolTable.getItem(level, ident.getName(), SymbolType.ARRAY);
                        int addr = item.getAddr();
                        Operand res = new TmpVariable(level, TmpVarGenerator.nextTmpVar(level), (level == 0));
                        instructions.add(new OffsetIr(
                                new Immediate(addr), new Immediate(0), res, false, false));
                        return res;
                    } else if (GlobalSymbolTable.isExist(level, ident.getName(), SymbolType.ARRAY)) {
                        SymbolTableItem item = GlobalSymbolTable.getItem(ident.getName(), SymbolType.ARRAY);
                        int addr = item.getAddr();
                        Operand res = new TmpVariable(level, TmpVarGenerator.nextTmpVar(level), (level == 0));
                        instructions.add(new OffsetIr(
                                new Immediate(addr), new Immediate(0), res, true, false));
                        // 这种情况下res里面存的是一个地址
                        return res;
                    } else {
                        // 最后看是不是指针
                        if (LocalSymbolTable.isExist(level, ident.getName(), SymbolType.POINTER)) {
                            SymbolTableItem item = LocalSymbolTable.getItem(level, ident.getName(), SymbolType.POINTER);
                            int addr = item.getAddr();
                            Operand res = new TmpVariable(level, TmpVarGenerator.nextTmpVar(level), (level == 0));
                            // TODO 十分迷惑的地方
                            instructions.add(new OffsetIr(
                                    new Immediate(addr), new Immediate(0), res, false, false));
                            // scope is useless
                            instructions.add(new LoadArrayValueIr(res, new Immediate(0), res, 0, true));
                            //instructions.add(new OffsetIr(
                            //        new Immediate(addr), new Immediate(0), res, false, true));
                            return res;
                        } else {
                            System.out.println("mei you zhen mei you");
                            throw new ValueTypeException();
                        }
                    }
                }
            } else {
                if (analyseMode || LValAnalyseMode.getAnalyseMode()) {
                    return analyseLeftMode(level, instructions);
                } else {
                    return analyseRightMode(level, instructions);
                }
            }
        } catch (ValueTypeException e) {
            e.printStackTrace();
        }
        return null;
    }
}
