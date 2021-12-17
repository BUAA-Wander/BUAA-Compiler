package ir;

import mips.Addi;
import mips.MipsCode;
import mips.Syscall;

import java.util.ArrayList;
import java.util.List;

public class WriteCharIr extends IntermediateInstruction {
    public WriteCharIr(char ch) {
        super(String.valueOf(ch));
    }

    public String toString() {
        return "WriteChar " + getRes();
    }

    public List<MipsCode> toMips() {
        List<MipsCode> mipsCodes = new ArrayList<>();
        char ch = getRes().charAt(0);
        mipsCodes.add(new Addi("$0", "$a0", ch));
        mipsCodes.add(new Addi("$0", "$v0", 11));
        mipsCodes.add(new Syscall());
        return mipsCodes;
    }
}
