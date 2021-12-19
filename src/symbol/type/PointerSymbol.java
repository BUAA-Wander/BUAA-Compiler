package symbol.type;

import treeNode.ConstExp;

public class PointerSymbol extends Symbol {
    private ConstExp lastDimSize;
    private int dims;
    // 标记这个指针指向的是局部地址还是全局地址
    private boolean isGlobal;

    public PointerSymbol(int lineNumber, String name, ConstExp lastDimSize, int dims) {
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
