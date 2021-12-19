package ir;

import mips.MipsCode;
import mips.Sw;

import java.util.ArrayList;
import java.util.List;

public class SaveContextIr extends IntermediateInstruction {
    // offset is negative
    private int offset;

    public SaveContextIr(int offset) {
        super();
        this.offset = offset;
    }

    public String toString() {
        return "SaveContext";
    }

    public List<MipsCode> toMips() {
        List<MipsCode> mipsCodes = new ArrayList<>();

        mipsCodes.add(new Sw("$sp", "$ra", offset));

        return mipsCodes;
    }
}
