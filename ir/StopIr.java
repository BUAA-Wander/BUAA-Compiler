package ir;

import mips.Addi;
import mips.MipsCode;
import mips.Syscall;

import java.util.ArrayList;
import java.util.List;

public class StopIr extends IntermediateInstruction {
    public StopIr() {
        super();
    }

    public String toString() {
        return "Stop";
    }

    public List<MipsCode> toMips() {
        List<MipsCode> mipsCodes = new ArrayList<>();
        mipsCodes.add(new Addi("$0", "$v0", 10));
        mipsCodes.add(new Syscall());
        return mipsCodes;
    }
}
