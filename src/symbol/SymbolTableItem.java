package symbol;

import symbol.type.Symbol;
import symbol.type.SymbolType;

public class SymbolTableItem {
    private int level; // scope
    private String name; // the name of variable or array or function
    private SymbolType type; // variable, function, array
    private int addr; // offset according to localAddrBase or globalAddrBase, function's is 0
    private int size; // variable or array size, function's size is 0?
    private Symbol symbol; // more detail information

    public SymbolTableItem(int level, String name, SymbolType type, Symbol symbol) {
        this.level = level;
        this.type = type;
        this.name = name;
        this.symbol = symbol;
    }

    public SymbolTableItem(int level, String name, SymbolType type, Symbol symbol,  int addr, int size) {
        this.level = level;
        this.type = type;
        this.name = name;
        this.symbol = symbol;
        this.addr = addr;
        this.size = size;
    }

    public int getLevel() {
        return level;
    }

    public String getName() {
        return name;
    }

    public SymbolType getSymbolType() {
        return type;
    }

    public Symbol getSymbol() {
        return symbol;
    }

    public int getAddr() {
        return addr;
    }

    public int getSize() {
        return size;
    }
}
