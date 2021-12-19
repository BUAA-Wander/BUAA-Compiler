package symbol.type;

import treeNode.ConstExp;

import java.util.List;

public class ConstArraySymbol extends Symbol {
    private int dims;
    private List<ConstExp> dimSizes;
    private List<Integer> values;

    public ConstArraySymbol(int lineNumber, String name, int dims, List<ConstExp> dimSizes
        , List<Integer> values) {
        super(lineNumber, name);
        this.dims = dims;
        this.dimSizes = dimSizes;
        this.values = values;
    }

    public ConstArraySymbol(int lineNumber, String name, int dims, List<ConstExp> dimSizes) {
        super(lineNumber, name);
        this.dims = dims;
        this.dimSizes = dimSizes;
    }

    public int getDims() {
        return dims;
    }

    public List<Integer> getValues() {
        return values;
    }

    public List<ConstExp> getDimSizes() {
        return dimSizes;
    }
}