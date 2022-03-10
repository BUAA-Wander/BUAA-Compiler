package ir;

import ir.utils.Operand;
import mips.Add;
import mips.Addi;
import mips.Beq;
import mips.J;
import mips.Label;
import mips.Lw;
import mips.MipsCode;
import mips.Slt;
import mips.Sub;
import java.util.ArrayList;
import java.util.List;

public class LoadArrayValueIr extends IntermediateInstruction {
    private int scope;
    private boolean isBasePointer;
    public LoadArrayValueIr(Operand base, Operand offset, Operand resIdx, int scope, boolean flag) {
        super(base, offset, resIdx);
        this.scope = scope;
        isBasePointer = flag;
    }

    public String toString() {
        return "LoadArrayValue " + getLeft() + " " + getRight() + " " + getRes();
    }

    @Override
    public List<MipsCode> toMips() {
        Operand base = getLeft();
        Operand offset = getRight();
        Operand res = getRes();


        List<MipsCode> mipsCodes = new ArrayList<>();
        String t0 = "$t0", t1 = "$t1", t2 = "$t2", t3 = "$t3", sp = "$sp", gp = "$gp";
        mipsCodes.addAll(base.loadToReg(t0));
        mipsCodes.addAll(offset.loadToReg(t1));

        // 根据是否为指针以及是否是全局来选择不同的计算方式
        if (!isBasePointer) {
            mipsCodes.add(new Add(t0, t1, t2));

//            int stackTop = 2147479548;
//            mipsCodes.add(new Addi("$0", t3, stackTop));
//            mipsCodes.add(new Slt(t2, ));

            if (scope == 0) {
                mipsCodes.add(new Add(gp, t2, t2));
            } else {
                mipsCodes.add(new Sub(sp, t2, t2));
            }
        } else {
            // 如果是指针，在考虑偏移的时候就应该考虑这个指针指向的是局部地址还是全局地址
            // 这个步骤并不是通过scope来看的，而是通过看t2的值来确定的
            // scope只是说这个指针变量是全局指针变量还是局部指针变量
            // 并不能说明这个指针变量指向的地址是局部还是全局
            int globalTop = 1073741824; // 2GB
            String t4 = "$t4", t5 = "$t5";
            mipsCodes.add(new Addi("$0", t3, globalTop));
            // 如果t2 < t3，判定为全局量，否则判定为局部量
            // beq $t3 $0 label_1
            // add t2 t0 t1
            // j label_2
            // label_1
            // sub t2 t0 t1
            // label_2
            mipsCodes.add(new Slt(t2, t3, t3));
            String label_1 = LabelGenerator.nextLabel(), label_2 = LabelGenerator.nextLabel();
            mipsCodes.add(new Beq(t3, "$0", label_1));
            // 处理全局量
            mipsCodes.add(new Add(t0, t1, t2));
            mipsCodes.add(new J(label_2));
            mipsCodes.add(new Label(label_1));
            mipsCodes.add(new Sub(t0, t1, t2));
            mipsCodes.add(new Label(label_2));
//            if (scope == 0) {
//
//            } else {
//
//            }
        }
        mipsCodes.add(new Lw(t2, t2, 0));
        mipsCodes.addAll(res.saveValue(t2));
        return mipsCodes;
    }
}
