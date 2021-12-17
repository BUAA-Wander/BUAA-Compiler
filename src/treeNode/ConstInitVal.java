package treeNode;

import exception.ValueTypeException;
import ir.IntermediateInstruction;
import ir.MovIr;

import java.util.ArrayList;
import java.util.List;

public class ConstInitVal extends TreeNode {
    private ConstInitValType type; // ARRAY, SIMPLE
    private ConstExp simpleConstInitVal;

    private LeftBrace leftBrace;
    private List<ConstInitVal> arrayConstInitVal;
    private List<Comma> commas;
    private RightBrace rightBrace;

    public ConstInitVal(int num, ConstInitValType type, ConstExp exp) {
        super(num);
        this.type = type;
        this.simpleConstInitVal = exp;
        leftBrace = null;
        arrayConstInitVal = null;
        commas = null;
        rightBrace = null;
    }

    public ConstInitVal(int num, ConstInitValType type, LeftBrace lb, List<ConstInitVal> vals, List<Comma> commas,
                        RightBrace rb) {
        super(num);
        this.type = type;
        this.leftBrace = lb;
        this.arrayConstInitVal = vals;
        this.commas = commas;
        this.rightBrace = rb;
        this.simpleConstInitVal = null;
    }

    public int getValue(int level) throws ValueTypeException {
        if (type == ConstInitValType.SIMPLE) {
            return simpleConstInitVal.getValue(level);
        } else {
            throw new ValueTypeException();
        }
    }

    public List<Integer> calcValues(int level) {
        List<Integer> res = new ArrayList<>();
        if (type == ConstInitValType.ARRAY) {
            if (arrayConstInitVal != null) {
                for (int i = 0; i < arrayConstInitVal.size(); i++) {
                    List<Integer> values = arrayConstInitVal.get(i).calcValues(level);
                    if (values != null) {
                        for (int j = 0; j < values.size(); j++) {
                            res.add(values.get(j));
                        }
                    }
                }
            }
        } else {
            try {
                res.add(getValue(level));
            } catch (ValueTypeException e) {
                System.out.println("Can't call array's getValue function!!!");
            }
        }
        return res;
    }

    // curId is the id which will be assigned value
    public int generateIr(int level, List<IntermediateInstruction> instructions,
                          String arrayName, int headAddr, int dims, int lastDimSize, int curId) {
        int id = curId;
        if (arrayConstInitVal != null) {
            for (int i = 0; i < arrayConstInitVal.size(); i++) {
                ConstInitVal initVal = arrayConstInitVal.get(i);
                // {{1, 2}, {3, 4}}
                if (initVal.getType() == ConstInitValType.ARRAY) {
                    int size = initVal.generateIr(
                            level, instructions, arrayName, headAddr, dims, lastDimSize, id);
                    id += size;
                } else {
                    // {1, 2}
                    String srcId = initVal.generateIr(level, instructions);
                    int addr = headAddr + id * 4;
                    if (level == 0) {
                        instructions.add(
                                new MovIr(srcId, "@" + arrayName + "@global" + "@" + addr + "@" + (addr - headAddr)));
                    } else {
                        instructions.add(
                                new MovIr(srcId, "@" + arrayName + "@local" + "@" + addr + "@" + (addr - headAddr)));
                    }
                    id++;
                }
            }
        }
        // return how many units have been assigned value
        return id - curId;
    }

    public ConstInitValType getType() {
        return type;
    }

    // return tmp memory unit id
    public String generateIr(int level, List<IntermediateInstruction> instructions) {
        if (type == ConstInitValType.ARRAY) {
            // TODO

            try {
                throw new ValueTypeException();
            } catch (ValueTypeException e) {
                e.printStackTrace();
            }
            return "it's array's init_val!";
        } else {
            return simpleConstInitVal.generateIr(level, instructions);
        }
    }
}
