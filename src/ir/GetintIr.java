package ir;

import ir.utils.Operand;
import mips.Add;
import mips.Addi;
import mips.MipsCode;
import mips.Syscall;
import symbol.SymbolTableItem;
import symbol.SymbolTableType;

import java.util.ArrayList;
import java.util.List;

public class GetintIr extends IntermediateInstruction {
    public GetintIr(Operand readIdx) {
        super(readIdx);
    }

    public String toString() {
        return "Getint " + getRes();
    }

    public List<MipsCode> toMips() {
        List<MipsCode> mipsCodes = new ArrayList<>();
        mipsCodes.add(new Addi("$0", "$v0", 5));
        mipsCodes.add(new Syscall());

        Operand op = getRes();

        mipsCodes.addAll(op.saveValue("$v0"));
        return mipsCodes;
    }
}
