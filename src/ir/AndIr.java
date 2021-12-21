package ir;

import ir.utils.Operand;
import mips.Abs;
import mips.Add;
import mips.And;
import mips.Bne;
import mips.J;
import mips.Label;
import mips.MipsCode;

import java.util.ArrayList;
import java.util.List;

public class AndIr extends IntermediateInstruction {
    public AndIr(Operand oprandIdx1, Operand oprandIdx2, Operand resIdx) {
        super(oprandIdx1, oprandIdx2, resIdx);
    }

    public String toString() {
        return "And " + getLeft() + " " + getRight() + " " + getRes();
    }

    @Override
    public List<MipsCode> toMips() {
        Operand op1 = getLeft();
        Operand op2 = getRight();
        Operand op3 = getRes();

        String t0 = "$t0", t1 = "$t1", t2 = "$t2";

        List<MipsCode> mipsCodes = new ArrayList<>();
        mipsCodes.addAll(op1.loadToReg(t0));
        mipsCodes.addAll(op2.loadToReg(t1));
        // TODO
        mipsCodes.add(new Abs(t0, t0));
        mipsCodes.add(new Abs(t1, t1));
        String label_1 = LabelGenerator.nextLabel();
        String label_2 = LabelGenerator.nextLabel();
        String label_3 = LabelGenerator.nextLabel();
        mipsCodes.add(new Bne(t0, "$0", label_1));
        mipsCodes.add(new Add("$0", "$0", t2));
        mipsCodes.add(new J(label_3));
        mipsCodes.add(new Label(label_1));
        mipsCodes.add(new Bne(t1, "$0", label_2));
        mipsCodes.add(new Add("$0", "$0", t2));
        mipsCodes.add(new J(label_3));
        mipsCodes.add(new Label(label_2));
        mipsCodes.add(new Add(t0, "$0", t2));
        mipsCodes.add(new Label(label_3));
        mipsCodes.addAll(op3.saveValue(t2));

        return mipsCodes;
    }
}
