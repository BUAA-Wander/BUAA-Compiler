package ir.utils;

import mips.Addi;
import mips.MipsCode;

import java.util.ArrayList;
import java.util.List;

public class Immediate extends Operand {
    private int value;

    public Immediate(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public String toString() {
        return String.valueOf(value);
    }

    @Override
    public List<MipsCode> loadToReg(String reg) {
        List<MipsCode> mipsCodes = new ArrayList<>();
        mipsCodes.add(new Addi("$0", reg, value));
        return mipsCodes;
    }

    @Override
    public List<MipsCode> saveValue(String reg) {
        return super.saveValue(reg);
    }
}
