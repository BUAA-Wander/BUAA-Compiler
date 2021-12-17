package ir;

import mips.Addi;
import mips.MipsCode;
import symbol.SymbolTableItem;
import symbol.SymbolTableType;

import java.util.ArrayList;
import java.util.List;

public class MovImmIr extends IntermediateInstruction {
    public MovImmIr(int imm, String saveId) {
        super(String.valueOf(imm), saveId);
    }

    public String toString() {
        return "MovImm " + getLeft() + " " + getRes();
    }

    @Override
    public List<MipsCode> toMips() {
        String op1 = getLeft();
        String op3 = getRes();

        SymbolTableItem item3 = getItemFromSymbolTable(op3);

        SymbolTableType type3 = getOperandSymbolTable(op3);

        List<MipsCode> mipsCodes = new ArrayList<>();
        String t2 = "$t2", zero = "$0";
        mipsCodes.add(new Addi(zero, t2, Integer.parseInt(op1)));
        mipsCodes.addAll(save(item3, type3, t2));
        return mipsCodes;
    }
}
