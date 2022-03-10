package treeNode;

import ir.IntermediateInstruction;
import symbol.AddressPtr;
import symbol.LocalSymbolTable;
import symbol.type.FParamArraySymbol;
import symbol.type.ParamType;
import symbol.type.PointerSymbol;
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

    public ParamType getParamType() {
        if (dims == 0) {
            return ParamType.INT;
        } else if (dims == 1) {
            return ParamType.ARRAY_1;
        } else {
            return ParamType.ARRAY_2;
        }
    }

    private List<IntermediateInstruction> generateVarParamIr(int level) {
        List<IntermediateInstruction> instructions = new ArrayList<>();
        int addr = AddressPtr.getLocalAddr();
        AddressPtr.addLocalAddr(4);
        LocalSymbolTable.insert(level + 1, ident.getName(), SymbolType.VAR,
                new FParamArraySymbol(getLineNumber(), ident.getName()
                        , lastDimSize, dims), addr, 4);
        return instructions;
    }

    // TODO pointer param
    public List<IntermediateInstruction> generatePointerParamIr(int level) {
        // System.out.println("Array FuncFParam hasn't implemented yet!");
        List<IntermediateInstruction> instructions = new ArrayList<>();
        int addr = AddressPtr.getLocalAddr();
        AddressPtr.addLocalAddr(4);
        LocalSymbolTable.insert(level + 1, ident.getName(), SymbolType.POINTER,
                new PointerSymbol(getLineNumber(), ident.getName()
                        , lastDimSize, dims), addr, 4);

        return instructions;
    }

    public List<IntermediateInstruction> generateIr(int level) {
        if (leftBracks.size() != 0) {
            return generatePointerParamIr(level);
        } else {
            return generateVarParamIr(level);
        }
    }
}
