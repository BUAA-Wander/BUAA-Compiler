package ir;

import mips.Add;
import mips.Addi;
import mips.MipsCode;
import symbol.SymbolTableItem;
import symbol.SymbolTableType;

import java.util.ArrayList;
import java.util.List;

public class MovIr extends IntermediateInstruction {
    public MovIr(String srcId, String dstId) {
        super(srcId, dstId);
    }

    public String toString() {
        return "Mov " + getLeft() + " " + getRes();
    }

    public List<MipsCode> toMips() {
        String op1 = getLeft();
        String op3 = getRes();

        // if op1 is array element, then item is array head
        SymbolTableItem item1 = getItemFromSymbolTable(op1);
        SymbolTableItem item3 = getItemFromSymbolTable(op3);
        if (item1 == null || item3 == null) {
            System.out.println(this.toString());
            throw new NullPointerException();
        }

        SymbolTableType type1 = getOperandSymbolTable(op1);
        SymbolTableType type3 = getOperandSymbolTable(op3);

        List<MipsCode> mipsCodes = new ArrayList<>();
        String t2 = "$t2", t0 = "$t0", zero = "0";
        mipsCodes.addAll(load(item1, type1, t0, op1));
        mipsCodes.add(new Add(t0, zero, t2));
        mipsCodes.addAll(save(item3, type3, t2, op3));
        return mipsCodes;
    }
}
