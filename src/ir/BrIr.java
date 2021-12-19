package ir;

import ir.utils.LabelOp;
import ir.utils.Operand;
import mips.Addi;
import mips.Jal;
import mips.MipsCode;
import symbol.LocalSymbolTable;

import java.util.ArrayList;
import java.util.List;

public class BrIr extends IntermediateInstruction {
    private int offset;
    public BrIr(Operand label) {
        super(label);
    }

    public BrIr(Operand label, int offset) {
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
        Operand label = getRes();
        if (label instanceof LabelOp) {
            mipsCodes.add(new Jal(((LabelOp) label).getLabelName()));
        }
        mipsCodes.add(new Addi("$sp", "$sp", -off - offset));

        return mipsCodes;
    }
}
