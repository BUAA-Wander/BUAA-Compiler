package treeNode;

import ir.IntermediateInstruction;
import symbol.AddressPtr;
import symbol.LocalSymbolTable;
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

    public ParamType getParamType() {
        if (dims == 0) {
            return ParamType.INT;
        } else if (dims == 1) {
            return ParamType.ARRAY_1;
        } else {
            return ParamType.ARRAY_2;
        }
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
