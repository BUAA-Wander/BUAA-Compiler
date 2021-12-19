package symbol.type;

import treeNode.FuncType;

import java.util.List;

public class FuncSymbol extends Symbol {
    private int paramCount;
    private List<ParamType> paramTypes;
    private FuncType type;

    public FuncSymbol(int lineNumber, String name, int paramCount, List<ParamType> paramTypes, FuncType type) {
        super(lineNumber, name);
        this.paramCount = paramCount;
        this.paramTypes = paramTypes;
        this.type = type;
    }

    public int getParamCount() {
        return paramCount;
    }

    public FuncType getReturnType() {
        return type;
    }

    public List<ParamType> getParamTypes() {
        return paramTypes;
    }
}
