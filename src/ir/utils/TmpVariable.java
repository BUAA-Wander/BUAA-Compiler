package ir.utils;

import mips.Lw;
import mips.MipsCode;
import mips.Sw;
import symbol.GlobalSymbolTable;
import symbol.LocalSymbolTable;
import symbol.SymbolTableItem;
import symbol.type.SymbolType;

import java.util.ArrayList;
import java.util.List;

public class TmpVariable extends Operand {
    private String name;
    private boolean isGlobal;

    public TmpVariable(String name, boolean isGlobal) {
        super();
        this.name = name;
        this.isGlobal = isGlobal;
    }

    @Override
    public List<MipsCode> loadToReg(String reg) {
        List<MipsCode> mipsCodes = new ArrayList<>();
        // if op1 is tmp variable
        SymbolTableItem item;
        if (isGlobal) {
            item = GlobalSymbolTable.getItem(name, SymbolType.VAR);
            mipsCodes.add(new Lw(reg, "$gp", item.getAddr()));
        } else {
            item = LocalSymbolTable.getItem(name, SymbolType.VAR);
            mipsCodes.add(new Lw(reg, "$sp", -item.getAddr()));
        }
        return mipsCodes;
    }

    @Override
    public List<MipsCode> saveValue(String reg) {
        List<MipsCode> mipsCodes = new ArrayList<>();
        SymbolTableItem item;
        if (isGlobal) {
            item = GlobalSymbolTable.getItem(name, SymbolType.VAR);
            mipsCodes.add(new Sw(reg, "$gp", item.getAddr()));
        } else {
            item = LocalSymbolTable.getItem(name, SymbolType.VAR);
            mipsCodes.add(new Sw(reg, "$sp", -item.getAddr()));
        }
        return mipsCodes;
    }

    @Override
    public String toString() {
        return name;
    }
}
