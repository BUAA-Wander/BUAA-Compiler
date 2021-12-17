package ir;

import mips.Jr;
import mips.MipsCode;

import java.util.ArrayList;
import java.util.List;

public class ReturnIr extends IntermediateInstruction {
    public ReturnIr() {
        super();
    }

    public String toString() {
        return "Return";
    }

    public List<MipsCode> toMips() {
        List<MipsCode> mipsCodes = new ArrayList<>();
        mipsCodes.add(new Jr("$31"));
        return mipsCodes;
    }
}
