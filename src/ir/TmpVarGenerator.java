package ir;

import symbol.AddressPtr;
import symbol.GlobalSymbolTable;
import symbol.LocalSymbolTable;
import symbol.type.SymbolType;
import symbol.type.VarBTypeSymbol;

public class TmpVarGenerator {
    public static int idx = 1;

    public static String nextId() {
        // #0 is always 0
        String res = "#" + idx;
        idx++;
        return res;
    }

    public static String nextTmpVar(int level) {
        String res = TmpVarGenerator.nextId();

        // insert tmp variable into symbolTable
        if (level == 0) {
            int addr = AddressPtr.getGlobalAddr();
            AddressPtr.addGlobalAddr(4);
            GlobalSymbolTable.insert(level, res, SymbolType.VAR,
                    new VarBTypeSymbol(-1, res), addr, 4);
        } else {
            int addr = AddressPtr.getLocalAddr();
            AddressPtr.addLocalAddr(4);
            LocalSymbolTable.insert(level, res, SymbolType.VAR,
                    new VarBTypeSymbol(-1, res), addr, 4);
            // move sp only when we call function or call back!
        }
        return res;
    }
}