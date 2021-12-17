package treeNode;

import exception.ValueTypeException;
import ir.AddIr;
import ir.IdGenerator;
import ir.IntermediateInstruction;
import ir.LoadArrayValueIr;
import ir.MovImmIr;
import ir.MovIr;
import ir.MulIr;
import ir.ToAbsoluteAddrIr;
import symbol.AddressPtr;
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

    public String outputAdaptToHomework() {
        StringBuilder builder = new StringBuilder();
        builder.append(ident.outputAdaptToHomework()).append("\n");
        for (int i = 0; i < leftBracks.size(); i++) {
            builder.append(leftBracks.get(i).outputAdaptToHomework()).append("\n");
            builder.append(index.get(i).outputAdaptToHomework()).append("\n");
            builder.append(rightBracks.get(i).outputAdaptToHomework()).append("\n");
        }
        builder.append("<LVal>");
        return builder.toString();
    }

//    public void createSymbolTable(int level, SymbolTable symbolTable,
//                                  List<Error> errors) {
//        if (!dealWithErrorC(symbolTable, errors)) {
//            return;
//        }
//
//        for (int i = 0; i < index.size(); i++) {
//            index.get(i).createSymbolTable(level, symbolTable, errors);
//        }
//    }

    public boolean isConstLVal(SymbolTable symbolTable) {
        return symbolTable.isConstTableItem(ident.getName());
    }

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

//    public boolean dealWithErrorC(SymbolTable symbolTable, List<Error> errors) {
//        if (!symbolTable.isExist(ident.getName(), SymbolType.VAR)) {
//            errors.add(new Error(ident.getLineNumber(), "c"));
//            return false;
//        }
//        return true;
//    }

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

    public String generateIr(int level, List<IntermediateInstruction> instructions) {
        try {
            if (leftBracks.size() == 0) {
                // TODO why next TODO
                // TODO delete expired variable!!!!!!!!!!!!
                if (LocalSymbolTable.isExist(level, ident.getName(), SymbolType.VAR)) {
                    SymbolTableItem item = LocalSymbolTable.getItem(ident.getName(), SymbolType.VAR);
                    // func is current function
//                    SymbolTableItem func = GlobalSymbolTable.getItem(
//                            LocalSymbolTable.getCurrentFunctionName(), SymbolType.FUNC);
//                    Symbol funcSymbol = func.getSymbol();
//                    if (funcSymbol instanceof FuncSymbol) {
//                        int paramAddr = ((FuncSymbol) funcSymbol).getParamCount() * 4;
//                        // item is function param
//                        if (item.getAddr() < paramAddr) {
//                            int pos = item.getAddr() / 4;
//                            ParamType paramType = ((FuncSymbol) funcSymbol).getParamTypes().get(pos);
//                            if (paramType == ParamType.INT) {
//                                // item is int param
//                                return "@" + item.getName() + "@local@" + item.getAddr();
//                            } else {
//                                // item is int[] or int[][]
//                                if (paramType == ParamType.ARRAY_1) {
//
//                                } else if (paramType == ParamType.ARRAY_2) {
//
//                                } else {
//
//                                }
//                            }
//                        } else {
//                            return "@" + item.getName() + "@local@" + item.getAddr();
//                        }
//                    }
                    return "@" + item.getName() + "@local@" + item.getAddr();
                } else if (GlobalSymbolTable.isExist(level, ident.getName(), SymbolType.VAR)) {
                    SymbolTableItem item = GlobalSymbolTable.getItem(ident.getName(), SymbolType.VAR);
                    return "@" + item.getName() + "@global@" + item.getAddr();
                } else {
                    System.out.println("TODO: implement array param pass");
                    SymbolTableItem item;
                    if (GlobalSymbolTable.isExist(level, ident.getName(), SymbolType.ARRAY)) {
                        item = GlobalSymbolTable.getItem(ident.getName(), SymbolType.ARRAY);
                        String res = "@" + item.getName() + "@global@" + item.getAddr();
                        instructions.add(new ToAbsoluteAddrIr(res, true));
                        return res;
                    } else if (LocalSymbolTable.isExist(level, ident.getName(), SymbolType.ARRAY)) {
                        item = LocalSymbolTable.getItem(ident.getName(), SymbolType.ARRAY);
                        String res = "@" + item.getName() + "@local@" + item.getAddr();
                        instructions.add(new ToAbsoluteAddrIr(res, false));
                        return res;
                    }
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

                String offsetId = IdGenerator.nextId();
                if (level == 0) {
                    int addr = AddressPtr.getGlobalAddr();
                    AddressPtr.addGlobalAddr(4);
                    GlobalSymbolTable.insert(level, offsetId, SymbolType.VAR,
                            new VarBTypeSymbol(-1, offsetId), addr, 4);
                } else {
                    int addr = AddressPtr.getLocalAddr();
                    AddressPtr.addLocalAddr(4);
                    LocalSymbolTable.insert(level, offsetId, SymbolType.VAR,
                            new VarBTypeSymbol(-1, offsetId), addr, 4);
                    // move sp only when we call function or call back!
                }
                instructions.add(new MovIr("#0", offsetId));

                String base = IdGenerator.nextId();
                if (level == 0) {
                    int addr = AddressPtr.getGlobalAddr();
                    AddressPtr.addGlobalAddr(4);
                    GlobalSymbolTable.insert(level, base, SymbolType.VAR,
                            new VarBTypeSymbol(-1, base), addr, 4);
                } else {
                    int addr = AddressPtr.getLocalAddr();
                    AddressPtr.addLocalAddr(4);
                    LocalSymbolTable.insert(level, base, SymbolType.VAR,
                            new VarBTypeSymbol(-1, base), addr, 4);
                    // move sp only when we call function or call back!
                }

                // calculate index!!!!!
                if (index.size() == 1) {
                    offsetId = index.get(0).generateIr(level, instructions);
                } else {
                    String tmp0 = index.get(0).generateIr(level, instructions);
                    String tmp1 = index.get(1).generateIr(level, instructions);
                    String size = dimSizes.get(1).generateIr(level, instructions);
                    instructions.add(new MulIr(tmp0, size, tmp0));
                    instructions.add(new AddIr(tmp0, tmp1, offsetId));
                }

                String headAddr = IdGenerator.nextId();
                if (level == 0) {
                    int addr = AddressPtr.getGlobalAddr();
                    AddressPtr.addGlobalAddr(4);
                    GlobalSymbolTable.insert(level, headAddr, SymbolType.VAR,
                            new VarBTypeSymbol(-1, headAddr), addr, 4);
                } else {
                    int addr = AddressPtr.getLocalAddr();
                    AddressPtr.addLocalAddr(4);
                    LocalSymbolTable.insert(level, headAddr, SymbolType.VAR,
                            new VarBTypeSymbol(-1, headAddr), addr, 4);
                    // move sp only when we call function or call back!
                }

                // base is not the headAddr of array!!! base = 4
                instructions.add(new MovImmIr(4, base));
                instructions.add(new MovImmIr(item.getAddr(), headAddr));
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
