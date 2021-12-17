package treeNode;

import error.Error;
import ir.IntermediateInstruction;
import symbol.AddressPtr;
import symbol.LocalSymbolTable;
import symbol.SymbolTable;
import symbol.type.FParamArraySymbol;
import symbol.type.ParamType;
import symbol.type.SymbolType;

import java.util.ArrayList;
import java.util.List;

public class FuncFParam extends TreeNode {
    private Ident ident;
    private int dims;
    private List<LeftBrack> leftBracks;
    private List<RightBrack> rightBracks;
    private ConstExp lastDimSize;

    public FuncFParam(int num, Ident ident, int dims, List<LeftBrack> leftBracks, List<RightBrack> rightBracks,
                      ConstExp lastDimSize) {
        super(num);
        this.ident = ident;
        this.dims = dims;
        this.leftBracks = leftBracks;
        this.rightBracks = rightBracks;
        this.lastDimSize = lastDimSize;
    }

    public String outputAdaptToHomework() {
        StringBuilder builder = new StringBuilder();
        builder.append("INTTK int\n");
        builder.append(ident.outputAdaptToHomework()).append("\n");
        for (int i = 0; i < leftBracks.size(); i++) {
            builder.append(leftBracks.get(i).outputAdaptToHomework()).append("\n");
            if (i == leftBracks.size() - 1) {
                if (lastDimSize != null) {
                    builder.append(lastDimSize.outputAdaptToHomework()).append("\n");
                }
            }
            builder.append(rightBracks.get(i).outputAdaptToHomework()).append("\n");
        }
        builder.append("<FuncFParam>");
        return builder.toString();
    }

    public void createSymbolTable(int level, SymbolTable symbolTable
            , List<Error> errors) {
        if (!dealWithErrorB(level + 1, symbolTable, errors)) {
            return;
        }

        if (lastDimSize != null) {
            lastDimSize.createSymbolTable(level, symbolTable, errors);
        }
        symbolTable.insert(level + 1, ident.getName(), SymbolType.VAR,
                new FParamArraySymbol(getLineNumber(), ident.getName()
                , lastDimSize, dims));
    }

    public ParamType getParamType() {
        if (dims == 0) {
            return ParamType.INT;
        } else if (dims == 1) {
            return ParamType.ARRAY_1;
        } else {
            return ParamType.ARRAY_2;
        }
    }

    public boolean dealWithErrorB(int level, SymbolTable symbolTable, List<Error> errors) {
        if (symbolTable.isExistInCurrentLevel(level, ident.getName(), SymbolType.VAR)) {
            errors.add(new Error(ident.getLineNumber(), "b"));
            return false;
        }
        return true;
    }

    public List<IntermediateInstruction> generateIr(int level) {
        List<IntermediateInstruction> instructions = new ArrayList<>();
        if (lastDimSize != null) {
            // array todo
            System.out.println("Array FuncFParam hasn't implemented yet!");
            return new ArrayList<>();
        }

        int addr = AddressPtr.getLocalAddr();
        AddressPtr.addLocalAddr(4);
        LocalSymbolTable.insert(level + 1, ident.getName(), SymbolType.VAR,
                new FParamArraySymbol(getLineNumber(), ident.getName()
                        , lastDimSize, dims), addr, 4);

        return instructions;
    }
}
