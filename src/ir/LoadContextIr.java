package ir;

import mips.Lw;
import mips.MipsCode;

import java.util.ArrayList;
import java.util.List;

public class LoadContextIr extends IntermediateInstruction {
    private int offset;
    public LoadContextIr(int offset) {
        super();
        this.offset = offset;
    }

    public String toString() {
        return "LoadContext";
    }

    public List<MipsCode> toMips() {
        List<MipsCode> mipsCodes = new ArrayList<>();

        mipsCodes.add(new Lw("$sp", "$ra", offset));

        return mipsCodes;
    }
}
