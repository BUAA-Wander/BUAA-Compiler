package symbol;

import symbol.type.Symbol;
import symbol.type.SymbolType;

public class LocalSymbolTable {
    private static String functionName;
    private static SymbolTable symbolTable = new SymbolTable();

    public static void createNewLocalSymbolTable() {
        symbolTable = new SymbolTable();
    }

    public static SymbolTable getCurrentLocalSymbolTable() {
        return symbolTable;
    }

    public static String getCurrentFunctionName() {
        return functionName;
    }

    public static void setSymbolTable(SymbolTable table) {
        symbolTable = table;
    }

    public static void setFunctionName(String name) {
        functionName = name;
    }

    public static boolean isExistInCurrentLevel(int level, String name, SymbolType symbolType) {
        return symbolTable.isExistInCurrentLevel(level, name, symbolType);
    }

    public static boolean isExist(int level, String name, SymbolType symbolType) {
        return symbolTable.isExist(level, name, symbolType);
    }

    public static void insert(int level, String name, SymbolType type, Symbol symbol) {
        symbolTable.insert(level, name, type, symbol);
    }

    public static void insert(int level, String name, SymbolType type, Symbol symbol, int addr, int size) {
        symbolTable.insert(level, name, type, symbol, addr, size);
    }

    public static void delete(int level) {
        symbolTable.delete(level);
    }

    public static SymbolTableItem getItem(String name, SymbolType symbolType) {
        return symbolTable.getItem(name, symbolType);
    }

    public static SymbolTableItem getItem(String name) {
        return symbolTable.getItem(name);
    }

    public static boolean isConstTableItem(String name) {
        return symbolTable.isConstTableItem(name);
    }
}
