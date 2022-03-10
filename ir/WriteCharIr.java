package ir;

import ir.utils.Operand;
import ir.utils.StringOp;
import mips.Addi;
import mips.MipsCode;
import mips.Syscall;

import java.util.ArrayList;
import java.util.List;

public class WriteCharIr extends IntermediateInstruction {
    public WriteCharIr(Operand ch) {
        super(ch);
    }

    public String toString() {
        return "WriteChar " + getRes();
    }

    public List<MipsCode> toMips() {
        List<MipsCode> mipsCodes = new ArrayList<>();
        Operand op = getRes();
        if (op instanceof StringOp) {
            char ch = ((StringOp) op).getValue().charAt(0);
            mipsCodes.add(new Addi("$0", "$a0", ch));
            mipsCodes.add(new Addi("$0", "$v0", 11));
            mipsCodes.add(new Syscall());
        }
        return mipsCodes;
    }
}
