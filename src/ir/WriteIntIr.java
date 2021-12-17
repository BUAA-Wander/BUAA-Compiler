package ir;

import ir.utils.Operand;
import mips.Addi;
import mips.MipsCode;
import mips.Syscall;
import symbol.SymbolTableItem;
import symbol.SymbolTableType;

import java.util.ArrayList;
import java.util.List;

public class WriteIntIr extends IntermediateInstruction {
    public WriteIntIr(Operand valueAddr) {
        super(valueAddr);
    }

    public String toString() {
        return "WriteInt " + getRes();
    }

    public List<MipsCode> toMips() {
        List<MipsCode> mipsCodes = new ArrayList<>();

        Operand op = getRes();
        mipsCodes.addAll(op.loadToReg("$a0"));

        mipsCodes.add(new Addi("$0", "$v0", 1));
        mipsCodes.add(new Syscall());
        return mipsCodes;
    }
}
