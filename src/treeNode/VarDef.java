package treeNode;

import error.Error;
import exception.ValueTypeException;
import ir.IntermediateInstruction;
import ir.MovIr;
import symbol.AddressPtr;
import symbol.GlobalSymbolTable;
import symbol.LocalSymbolTable;
import symbol.type.ConstArraySymbol;
import symbol.type.SymbolType;
import symbol.type.VarArraySymbol;
import symbol.type.VarBTypeSymbol;
import symbol.SymbolTable;

import java.util.ArrayList;
import java.util.List;

public class VarDef extends TreeNode {
    private Ident ident;
    private List<LeftBrack> leftBracks;
    private List<ConstExp> dimSizes;
    private List<RightBrack> rightBracks;

    private Assign assignToken;
    private InitVal initVal;  // no initVal

    public VarDef(int num, Ident ident, List<LeftBrack> lb, List<ConstExp> exps,
                  List<RightBrack> rb) {
        super(num);
        this.ident = ident;
        this.leftBracks = lb;
        this.dimSizes = exps;
        this.rightBracks = rb;
        assignToken = null;
        initVal = null;
    }

    public VarDef(int num, Ident ident, List<LeftBrack> lb, List<ConstExp> exps,
                  List<RightBrack> rb, Assign assign, InitVal val) {
        this(num, ident, lb, exps, rb);
        this.assignToken = assign;
        this.initVal = val;
    }

    public boolean hasInitVal() {
        return initVal != null;
    }

    public String outputAdaptToHomework() {
        StringBuilder builder = new StringBuilder();
        builder.append(ident.outputAdaptToHomework()).append("\n");
        for (int i = 0; i < leftBracks.size(); i++) {
            builder.append(leftBracks.get(i).outputAdaptToHomework()).append("\n");
            builder.append(dimSizes.get(i).outputAdaptToHomework()).append("\n");
            builder.append(rightBracks.get(i).outputAdaptToHomework()).append("\n");
        }

        if (hasInitVal()) {
            builder.append(assignToken.outputAdaptToHomework()).append("\n");
            builder.append(initVal.outputAdaptToHomework()).append("\n");
        }

        builder.append("<VarDef>");
        return builder.toString();
    }

    public void createSymbolTable(int level, SymbolTable symbolTable,
                                  List<Error> errors) {
        if (!dealWithErrorB(level, symbolTable, errors)) {
            return;
        }

        if (leftBracks.size() != 0) {
            // array define
            int dims = leftBracks.size();
            symbolTable.insert(level, ident.getName(), SymbolType.VAR,
                    new VarArraySymbol(getLineNumber()
                    , ident.getName(), dims, dimSizes));
        } else {
            // int define
            symbolTable.insert(level, ident.getName(), SymbolType.VAR,
                    new VarBTypeSymbol(getLineNumber(), ident.getName()));
        }

        if (initVal != null) {
            if (leftBracks.size() != 0) {
                for (int i = 0; i < dimSizes.size(); i++) {
                    dimSizes.get(i).createSymbolTable(level, symbolTable, errors);
                }
            } else {
                initVal.createSymbolTable(level, symbolTable, errors);
            }
        }
    }

    public boolean dealWithErrorB(int level, SymbolTable symbolTable, List<Error> errors) {
        if (symbolTable.isExistInCurrentLevel(level, ident.getName(),
                SymbolType.VAR)) {
            errors.add(new Error(ident.getLineNumber(), "b"));
            return false;
        }
        return true;
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
        if (leftBracks.size() != 0) {
            int addr;
            if (level == 0) {
                addr = AddressPtr.getGlobalAddr();
                int size = calcTotalSize(level);
                AddressPtr.addGlobalAddr(size);
                GlobalSymbolTable.insert(level, ident.getName(), SymbolType.ARRAY,
                        new VarArraySymbol(getLineNumber(), ident.getName(),
                                leftBracks.size(), dimSizes), addr, size);
            } else {
                addr = AddressPtr.getLocalAddr();
                int size = calcTotalSize(level);
                AddressPtr.addLocalAddr(size);
                LocalSymbolTable.insert(level, ident.getName(), SymbolType.ARRAY,
                        new VarArraySymbol(getLineNumber(), ident.getName(),
                                leftBracks.size(), dimSizes), addr, size);
            }
            int lastDimSize = 0;
            try {
                lastDimSize = dimSizes.get(dimSizes.size() - 1).getValue(level);
            } catch (ValueTypeException e) {
                e.printStackTrace();
            }
            if (initVal != null) {
                initVal.generateIr(level, instructions, ident.getName(),
                        addr, leftBracks.size(), lastDimSize, 0);
            }
        } else {
            // int define
            int addr;
            if (level == 0) {
                addr = AddressPtr.getGlobalAddr();
                AddressPtr.addGlobalAddr(4);
                GlobalSymbolTable.insert(level, ident.getName(), SymbolType.VAR,
                        new VarBTypeSymbol(getLineNumber(), ident.getName()), addr, 4);
            } else {
                addr = AddressPtr.getLocalAddr();
                AddressPtr.addLocalAddr(4);
                LocalSymbolTable.insert(level, ident.getName(), SymbolType.VAR,
                        new VarBTypeSymbol(getLineNumber(), ident.getName()), addr, 4);
            }

            // assign init value
            if (initVal != null) {
                String srcId = initVal.generateIr(level, instructions);
                if (level == 0) {
                    instructions.add(new MovIr(srcId, "@" + ident.getName() + "@global" + "@" + addr));
                } else {
                    instructions.add(new MovIr(srcId, "@" + ident.getName() + "@local" + "@" + addr));
                }
            }
        }
        return instructions;
    }
}
