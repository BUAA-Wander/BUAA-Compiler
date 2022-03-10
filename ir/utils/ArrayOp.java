package ir.utils;

import mips.Lw;
import mips.MipsCode;
import mips.Sw;

import java.util.ArrayList;
import java.util.List;

public class ArrayOp extends Operand {
    // absolute addr
    private int addr;
    private boolean isGlobal;

    public ArrayOp(int addr, boolean isGlobal) {
        this.addr = addr;
        this.isGlobal = isGlobal;
    }

    @Override
    public List<MipsCode> loadToReg(String reg) {
        List<MipsCode> mipsCodes = new ArrayList<>();
        mipsCodes.add(new Lw("$0", reg, addr));
        return mipsCodes;
    }

    @Override
    public List<MipsCode> saveValue(String reg) {
        List<MipsCode> mipsCodes = new ArrayList<>();
        mipsCodes.add(new Sw("$0", reg, addr));
        return mipsCodes;
    }
}
