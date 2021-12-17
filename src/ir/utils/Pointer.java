package ir.utils;

import mips.MipsCode;

import java.util.List;

public class Pointer extends Operand {
    private int offset;
    private boolean isGlobal;

    public Pointer(int offset, boolean isGlobal) {
        this.offset = offset;
        this.isGlobal = isGlobal;
    }

    @Override
    public List<MipsCode> loadToReg(String reg) {
        return null;
    }
}
