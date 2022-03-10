package symbol.type;

import treeNode.ConstExp;

import java.util.List;

public class VarArraySymbol extends Symbol {
    private int dims;
    private List<ConstExp> dimSizes;

    public VarArraySymbol(int lineNumber, String name, int dims, List<ConstExp> dimSizes) {
        super(lineNumber, name);
        this.dims = dims;
        this.dimSizes = dimSizes;
    }

    public int getDims() {
        return dims;
    }

    public List<ConstExp> getDimSizes() {
        return dimSizes;
    }
}