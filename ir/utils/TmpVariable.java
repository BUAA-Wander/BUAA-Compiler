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
    private int level;

    public TmpVariable(int level, String name, boolean isGlobal) {
        super();
        this.level = level;
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
            mipsCodes.add(new Lw("$gp", reg, item.getAddr()));
        } else {
            item = LocalSymbolTable.getItem(level, name, SymbolType.VAR);
            mipsCodes.add(new Lw("$sp", reg, -item.getAddr()));
        }
        return mipsCodes;
    }

    @Override
    public List<MipsCode> saveValue(String reg) {
        List<MipsCode> mipsCodes = new ArrayList<>();
        SymbolTableItem item;
        if (isGlobal) {
            item = GlobalSymbolTable.getItem(name, SymbolType.VAR);
            mipsCodes.add(new Sw("$gp", reg, item.getAddr()));
        } else {
            item = LocalSymbolTable.getItem(level, name, SymbolType.VAR);
            mipsCodes.add(new Sw("$sp", reg, -item.getAddr()));
        }
        return mipsCodes;
    }

    @Override
    public String toString() {
        return name;
    }
}
