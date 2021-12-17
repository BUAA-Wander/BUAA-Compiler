package treeNode;

import exception.ValueTypeException;
import ir.IntermediateInstruction;
import ir.MovIr;
import java.util.List;

public class InitVal extends TreeNode {
    private InitValType type; // ARRAY, SIMPLE
    private Exp simpleInitVal;

    private LeftBrace leftBrace;
    private List<InitVal> arrayInitVal;
    private List<Comma> commas;
    private RightBrace rightBrace;

    public InitVal(int num, InitValType type, Exp exp) {
        super(num);
        this.type = type;
        simpleInitVal = exp;
        leftBrace = null;
        arrayInitVal = null;
        commas = null;
        rightBrace = null;
    }

    public InitVal(int num, InitValType type, LeftBrace lb, List<InitVal> vals, List<Comma> commas,
                   RightBrace rb) {
        super(num);
        simpleInitVal = null;
        this.type = type;
        leftBrace = lb;
        arrayInitVal = vals;
        this.commas = commas;
        rightBrace = rb;
    }

    public String generateIr(int level, List<IntermediateInstruction> instructions) {
        if (type == InitValType.ARRAY) {
            // it's impossible to reach to this line!
            try {
                throw new ValueTypeException();
            } catch (ValueTypeException e) {
                e.printStackTrace();
            }
            return "it's array's init_val!";
        } else {
            return simpleInitVal.generateIr(level, instructions);
        }
    }

    public InitValType getType() {
        return type;
    }

    public int generateIr(int level, List<IntermediateInstruction> instructions,
                          String arrayName, int headAddr, int dims, int lastDimSize, int curId) {
        int id = curId;
        if (arrayInitVal != null) {
            for (int i = 0; i < arrayInitVal.size(); i++) {
                InitVal initVal = arrayInitVal.get(i);
                // {{1, 2}, {3, 4}}
                if (initVal.getType() == InitValType.ARRAY) {
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
}
