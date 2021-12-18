package symbol;

import symbol.type.ConstArraySymbol;
import symbol.type.ConstBTypeSymbol;
import symbol.type.Symbol;
import symbol.type.SymbolType;

import java.util.ArrayList;
import java.util.List;

public class SymbolTable {
    private List<SymbolTableItem> symbolTable;

    public SymbolTable() {
        symbolTable = new ArrayList<>();
    }

    // judge if current ident exists in current level
    public boolean isExistInCurrentLevel(int level, String name, SymbolType symbolType) {
        for (int i = symbolTable.size() - 1; i >= 0; i--) {
            if (level != symbolTable.get(i).getLevel()) {
                continue;
            }
            if (name.equals(symbolTable.get(i).getName())
                    && symbolType.equals(symbolTable.get(i).getSymbolType())
                    && symbolTable.get(i).isValid()) {
                return true;
            }
        }
        return false;
    }

    // check symbols whose level <= level
    // if (flag) {int x;} else {x = 10;}
    public boolean isExist(int level, String name, SymbolType symbolType) {
        for (int i = symbolTable.size() - 1; i >= 0; i--) {
            if (level < symbolTable.get(i).getLevel()) {
                continue;
            }
            if (name.equals(symbolTable.get(i).getName())
                    && symbolType.equals(symbolTable.get(i).getSymbolType())
                    && symbolTable.get(i).isValid()) {
                return true;
            }
        }
        return false;
    }

    public void insert(int level, String name, SymbolType type, Symbol symbol) {
        symbolTable.add(new SymbolTableItem(level, name, type, symbol));
    }

    public void insert(int level, String name, SymbolType type, Symbol symbol, int addr, int size) {
        symbolTable.add(new SymbolTableItem(level, name, type, symbol, addr, size));
    }

    public void delete(int level) {
        for (int i = 0; i < symbolTable.size(); i++) {
            SymbolTableItem item = symbolTable.get(i);
            if (item.getLevel() > level) {
                item.disable();
            }
        }
    }

    public SymbolTableItem getItem(String name, SymbolType symbolType) {
        for (int i = symbolTable.size() - 1; i >= 0; i--) {
            if (name.equals(symbolTable.get(i).getName())
                    && symbolType.equals(symbolTable.get(i).getSymbolType())
                    && symbolTable.get(i).isValid()) {
                return symbolTable.get(i);
            }
        }
        return null;
    }

    public SymbolTableItem getItem(String name) {
        int level = -1;
        SymbolTableItem item = null;
        for (int i = symbolTable.size() - 1; i >= 0; i--) {
            if (symbolTable.get(i).getLevel() < level) {
                continue;
            }
            if (name.equals(symbolTable.get(i).getName()) && symbolTable.get(i).isValid()) {
                item = symbolTable.get(i);
            }
        }
        return item;
    }

    public boolean isConstTableItem(String name) {
        boolean flag = false;
        int level = -1;
        for (int i = symbolTable.size() - 1; i >= 0; i--) {
            if (name.equals(symbolTable.get(i).getName())
                    && symbolTable.get(i).getSymbolType().equals(SymbolType.VAR)) {
                Symbol symbol = symbolTable.get(i).getSymbol();
                if (symbolTable.get(i).getLevel() >= level) {
                    level = symbolTable.get(i).getLevel();
                } else {
                    continue;
                }
                if (symbol instanceof ConstArraySymbol || symbol instanceof ConstBTypeSymbol) {
                    flag = true;
                } else {
                    flag = false;
                }
            }
        }
        return flag;
    }

    public List<SymbolTableItem> getItems() {
        return symbolTable;
    }

    public int getSize() {
        return symbolTable.size();
    }
}