package symbol.type;

import treeNode.ConstExp;

public class FParamArraySymbol extends Symbol {
    private ConstExp lastDimSize;
    private int dims;

    public FParamArraySymbol(int lineNumber, String name, ConstExp lastDimSize, int dims) {
        super(lineNumber, name);
        this.lastDimSize = lastDimSize;
        this.dims = dims;
    }

    public int getDims() {
        return dims;
    }

    public ConstExp getLastDimSize() {
        return lastDimSize;
    }
}