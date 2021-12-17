package ir;

import ir.utils.Immediate;
import ir.utils.Operand;
import mips.Addi;
import mips.MipsCode;
import symbol.SymbolTableItem;
import symbol.SymbolTableType;

import java.util.ArrayList;
import java.util.List;

public class MovImmIr extends IntermediateInstruction {
    public MovImmIr(Operand imm, Operand saveId) {
        super(imm, saveId);
    }

    public String toString() {
        return "MovImm " + getLeft() + " " + getRes();
    }

    @Override
    public List<MipsCode> toMips() {
        Operand op1 = getLeft();
        Operand op3 = getRes();

        List<MipsCode> mipsCodes = new ArrayList<>();
        String t2 = "$t2", zero = "$0";
        if (op1 instanceof Immediate) {
            mipsCodes.add(new Addi(zero, t2, ((Immediate) op1).getValue()));
            mipsCodes.addAll(op3.saveValue(t2));
        }

        return mipsCodes;
    }
}
