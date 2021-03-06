package treeNode;

import exception.ValueTypeException;
import ir.IntermediateInstruction;
import ir.MovIr;
import ir.utils.Operand;
import ir.utils.Variable;
import symbol.AddressPtr;
import symbol.GlobalSymbolTable;
import symbol.LocalSymbolTable;
import symbol.type.SymbolType;
import symbol.type.VarArraySymbol;
import symbol.type.VarBTypeSymbol;

import java.util.ArrayList;
import java.util.List;

public class VarDef extends TreeNode {
    // 这里有可能是定义指针类型（指的是函数参数传递的形参）
    private Ident ident;
    private List<LeftBrack> leftBracks;
    private List<ConstExp> dimSizes;
    private List<RightBrack> rightBracks;

    private Assign assignToken;
    private InitVal initVal;  // no initVal

    public VarDef(int num, Ident ident, List<LeftBrack> lb, List<ConstExp> exps,
                  List<RightBrack> rb) {
        super(num);
        this.ident = ident;
        this.leftBracks = lb;
        this.dimSizes = exps;
        this.rightBracks = rb;
        assignToken = null;
        initVal = null;
    }

    public VarDef(int num, Ident ident, List<LeftBrack> lb, List<ConstExp> exps,
                  List<RightBrack> rb, Assign assign, InitVal val) {
        this(num, ident, lb, exps, rb);
        this.assignToken = assign;
        this.initVal = val;
    }

    public boolean hasInitVal() {
        return initVal != null;
    }

    private int calcTotalSize(int level) {
        int res = 1;
        for (int i = 0; i < dimSizes.size(); i++) {
            try {
                res *= dimSizes.get(i).getValue(level);
            } catch (ValueTypeException e) {
                System.out.println(dimSizes.get(i).toString());
                System.out.println("fuck calcTotalSize");
            }
        }
        return res * 4;
    }

    private List<IntermediateInstruction> generateArrayIr(int level) {
        List<IntermediateInstruction> instructions = new ArrayList<>();

        int addr;
        if (level == 0) {
            addr = AddressPtr.getGlobalAddr();
            int size = calcTotalSize(level);
            AddressPtr.addGlobalAddr(size);
            GlobalSymbolTable.insert(level, ident.getName(), SymbolType.ARRAY,
                    new VarArraySymbol(getLineNumber(), ident.getName(),
                            leftBracks.size(), dimSizes), addr, size);
        } else {
            addr = AddressPtr.getLocalAddr();
            int size = calcTotalSize(level);
            AddressPtr.addLocalAddr(size);
            LocalSymbolTable.insert(level, ident.getName(), SymbolType.ARRAY,
                    new VarArraySymbol(getLineNumber(), ident.getName(),
                            leftBracks.size(), dimSizes), addr, size);
        }

        int lastDimSize = 0;
        try {
            lastDimSize = dimSizes.get(dimSizes.size() - 1).getValue(level);
        } catch (ValueTypeException e) {
            e.printStackTrace();
        }
        if (initVal != null) {
            boolean isPoiner = LocalSymbolTable.isExist(level, ident.getName(), SymbolType.POINTER);
            initVal.generateIr(level, instructions, ident.getName(),
                    addr, leftBracks.size(), lastDimSize, 0, isPoiner);
        }
        return instructions;
    }

    private List<IntermediateInstruction> generateVarIr(int level) {
        List<IntermediateInstruction> instructions = new ArrayList<>();
        // int define
        int addr;
        if (level == 0) {
            addr = AddressPtr.getGlobalAddr();
            AddressPtr.addGlobalAddr(4);
            GlobalSymbolTable.insert(level, ident.getName(), SymbolType.VAR,
                    new VarBTypeSymbol(getLineNumber(), ident.getName()), addr, 4);
        } else {
            addr = AddressPtr.getLocalAddr();
            AddressPtr.addLocalAddr(4);
            LocalSymbolTable.insert(level, ident.getName(), SymbolType.VAR,
                    new VarBTypeSymbol(getLineNumber(), ident.getName()), addr, 4);
        }

        // assign init value
        if (initVal != null) {
            Operand srcId = initVal.generateIr(level, instructions);
            if (level == 0) {
                instructions.add(new MovIr(srcId, new Variable(ident.getName(), addr, true)));
            } else {
                instructions.add(new MovIr(srcId, new Variable(ident.getName(), addr, false)));
            }
        }
        return instructions;
    }

    public List<IntermediateInstruction> generateIr(int level) {
        if (leftBracks.size() != 0) {
            return generateArrayIr(level);
        } else {
            return generateVarIr(level);
        }
    }
}
