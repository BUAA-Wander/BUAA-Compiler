package ir.utils;

import mips.Lw;
import mips.MipsCode;

import java.util.ArrayList;
import java.util.List;

public class Variable extends Operand {
    private String name;
    private int offset;
    private boolean isGlobal;

    public Variable(String name, int offset, boolean isGlobal) {
        this.name = name;
        this.offset = offset;
        this.isGlobal = isGlobal;
    }

    @Override
    public List<MipsCode> loadToReg(String reg) {
        List<MipsCode> mipsCodes = new ArrayList<>();
        if (isGlobal) {
            mipsCodes.add(new Lw("$gp", reg, offset));
        } else {
            mipsCodes.add(new Lw("$sp", reg, -offset));
        }
        return mipsCodes;
    }
}
