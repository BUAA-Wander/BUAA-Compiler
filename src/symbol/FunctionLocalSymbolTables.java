package symbol;

import java.util.HashMap;
import java.util.Map;

public class FunctionLocalSymbolTables {
    public static Map<String, SymbolTable> allLocalSymbolTables = new HashMap<>();

    public static boolean hasLocalSymbolTable(String name) {
        return allLocalSymbolTables.containsKey(name);
    }

    public static SymbolTable getLocalSymbolTableByFunctionName(String name) {
        return allLocalSymbolTables.get(name);
    }

    public static void addLocalSymbolTable(String name, SymbolTable symbolTable) {
        allLocalSymbolTables.put(name, symbolTable);
    }
}