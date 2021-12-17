package ir;

import mips.Addi;
import mips.Jal;
import mips.MipsCode;
import symbol.LocalSymbolTable;

import java.util.ArrayList;
import java.util.List;

public class BrIr extends IntermediateInstruction {
    private int offset;
    public BrIr(String label) {
        super(label);
    }

    public BrIr(String label, int offset) {
        super(label);
        this.offset = offset;
    }

    public String toString() {
        return "Br " + getRes();
    }

    @Override
    public List<MipsCode> toMips() {
        List<MipsCode> mipsCodes = new ArrayList<>();

        // move to caller's end
        // e.g. if main calls func, then sp = sp - 4 * main.size
        int off = -4 * LocalSymbolTable.getCurrentLocalSymbolTable().getSize();

        mipsCodes.add(new Addi("$sp", "$sp", off + offset));
        mipsCodes.add(new Jal(getRes()));
        mipsCodes.add(new Addi("$sp", "$sp", -off - offset));

        return mipsCodes;
    }
}
