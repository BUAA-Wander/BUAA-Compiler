package treeNode;

import error.Error;
import exception.ValueTypeException;
import ir.IntermediateInstruction;
import ir.MovIr;
import symbol.AddressPtr;
import symbol.GlobalSymbolTable;
import symbol.LocalSymbolTable;
import symbol.SymbolTable;
import symbol.type.ConstArraySymbol;
import symbol.type.ConstBTypeSymbol;
import symbol.type.SymbolType;

import java.util.ArrayList;
import java.util.List;

public class ConstDef extends TreeNode {
    private Ident ident;
    private List<LeftBrack> leftBracks;
    private List<ConstExp> dimSizes;
    private List<RightBrack> rightBracks;
    private Assign assignToken;
    private ConstInitVal constInitVal;

    public ConstDef(int num, Ident ident, List<LeftBrack> lb, List<ConstExp> sizes,
                    List<RightBrack> rb, Assign assign, ConstInitVal val) {
        super(num);
        this.ident = ident;
        this.leftBracks = lb;
        this.dimSizes = sizes;
        this.rightBracks = rb;
        this.assignToken = assign;
        this.constInitVal = val;
    }

    public String outputAdaptToHomework() {
        StringBuilder builder = new StringBuilder();
        builder.append(ident.outputAdaptToHomework()).append("\n");
        for (int i = 0; i < leftBracks.size(); i++) {
            builder.append(leftBracks.get(i).outputAdaptToHomework()).append("\n");
            builder.append(dimSizes.get(i).outputAdaptToHomework()).append("\n");
            builder.append(rightBracks.get(i).outputAdaptToHomework()).append("\n");
        }
        builder.append(assignToken.outputAdaptToHomework()).append("\n");
        builder.append(constInitVal.outputAdaptToHomework()).append("\n");
        builder.append("<ConstDef>");
        return builder.toString();
    }

    public void createSymbolTable(int level, SymbolTable symbolTable
            , List<Error> errors) {
//        if (!dealWithErrorB(level, symbolTable, errors)) {
//            return;
//        }

        if (dimSizes.size() != 0) {
            for (int i = 0; i < dimSizes.size(); i++) {
                dimSizes.get(i).createSymbolTable(level, symbolTable, errors);
            }
            symbolTable.insert(level, ident.getName(), SymbolType.VAR, new ConstArraySymbol(getLineNumber(), ident.getName()
                    , dimSizes.size(), dimSizes));
        } else {
            symbolTable.insert(level, ident.getName(), SymbolType.VAR, new ConstBTypeSymbol(getLineNumber(),
                    ident.getName()));
//            try {
//                System.out.println(getValue());
//            } catch (ValueTypeException e) {
//                e.printStackTrace();
//            }
        }
    }

    public boolean dealWithErrorB(int level, SymbolTable symbolTable, List<Error> errors) {
        if (symbolTable.isExistInCurrentLevel(level, ident.getName(), SymbolType.VAR)) {
            errors.add(new Error(ident.getLineNumber(), "b"));
            return false;
        }
        return true;
    }

    public int getValue(int level) throws ValueTypeException {
        if (leftBracks.size() == 0) {
            return constInitVal.getValue(level);
        } else {
            throw new ValueTypeException();
        }
    }

    private int calcTotalSize(int level) {
        int res = 1;
        for (int i = 0; i < dimSizes.size(); i++) {
            try {
                res *= dimSizes.get(i).getValue(level);
            } catch (ValueTypeException e) {
                System.out.println(dimSizes.get(i).toString());
                System.out.println("fuck calcTotalSize");
            }
        }
        return res * 4;
    }

    public List<IntermediateInstruction> generateIr(int level) {
        List<IntermediateInstruction> instructions = new ArrayList<>();
        if (dimSizes.size() != 0) {
            int addr;
            if (level == 0) {
                // save var in global symbol table
                // get addr, alloc mem, insert into symbol table
                addr = AddressPtr.getGlobalAddr();
                // calculate the size of array
                int size = calcTotalSize(level);
                AddressPtr.addGlobalAddr(size);
//                GlobalSymbolTable.insert(level, ident.getName(), SymbolType.ARRAY,
//                        new ConstArraySymbol(getLineNumber(), ident.getName(), leftBracks.size(), dimSizes),
//                        addr, size);
                // TODO initVal parse
                // TODO lastDimSize
                int lastDimSize = 0;

                try {
                    lastDimSize = dimSizes.get(dimSizes.size() - 1).getValue(level);
                } catch (ValueTypeException e) {
                    e.printStackTrace();
                }

                constInitVal.generateIr(level, instructions, ident.getName(),
                        addr, leftBracks.size(), lastDimSize, 0);
                List<Integer> values = constInitVal.calcValues(level);
                GlobalSymbolTable.insert(level, ident.getName(), SymbolType.ARRAY,
                        new ConstArraySymbol(getLineNumber(), ident.getName(), leftBracks.size(), dimSizes, values),
                        addr, size);
            } else {
                addr = AddressPtr.getLocalAddr();
                int size = 0;
                AddressPtr.addLocalAddr(size);
//                LocalSymbolTable.insert(level, ident.getName(), SymbolType.ARRAY,
//                        new ConstArraySymbol(getLineNumber(), ident.getName(), leftBracks.size(), dimSizes),
//                        addr, size);
                // TODO initVal parse
                // TODO lastDimSize
                int lastDimSize = 0;

                try {
                    lastDimSize = dimSizes.get(dimSizes.size() - 1).getValue(level);
                } catch (ValueTypeException e) {
                    e.printStackTrace();
                }

                constInitVal.generateIr(level, instructions, ident.getName(),
                        addr, leftBracks.size(), lastDimSize, 0);
                List<Integer> values = constInitVal.calcValues(level);
                LocalSymbolTable.insert(level, ident.getName(), SymbolType.ARRAY,
                        new ConstArraySymbol(getLineNumber(), ident.getName(), leftBracks.size(), dimSizes, values),
                        addr, size);
            }
        } else {
            // this try catch has ever caught a NullPointerException
            int addr;
            if (level == 0) {
                // save var in global symbol table
                // get addr, alloc mem, insert into symbol table
                addr = AddressPtr.getGlobalAddr();
                AddressPtr.addGlobalAddr(4);
                int value = 0;
                try {
                    value = constInitVal.getValue(level);
                } catch (ValueTypeException e) {
                    System.out.println("It's not a const variable!");
                    e.printStackTrace();
                }
                GlobalSymbolTable.insert(level, ident.getName(), SymbolType.VAR, new ConstBTypeSymbol(getLineNumber(),
                        ident.getName(), value), addr, 4);
                String id = constInitVal.generateIr(level, instructions);
                instructions.add(new MovIr(id, "@" + ident.getName() + "@global" +"@" + addr));
            } else {
                addr = AddressPtr.getLocalAddr();
                AddressPtr.addLocalAddr(4);
                LocalSymbolTable.insert(level, ident.getName(), SymbolType.VAR, new ConstBTypeSymbol(getLineNumber(),
                        ident.getName()), addr, 4);
                String id = constInitVal.generateIr(level, instructions);
                instructions.add(new MovIr(id, "@" + ident.getName() + "@local" +"@" + addr));
                // todo: is this sentence redundant?
            }
        }
        return instructions;
    }
}