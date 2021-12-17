package treeNode;

import exception.ValueTypeException;
import ir.IntermediateInstruction;
import ir.MovIr;
import symbol.AddressPtr;
import symbol.GlobalSymbolTable;
import symbol.LocalSymbolTable;
import symbol.type.ConstArraySymbol;
import symbol.type.ConstBTypeSymbol;
import symbol.type.SymbolType;

import java.util.ArrayList;
import java.util.List;

public class ConstDef extends TreeNode {
    private Ident ident;
    private List<LeftBrack> leftBracks;
    private List<ConstExp> dimSizes;
    private List<RightBrack> rightBracks;
    private Assign assignToken;
    private ConstInitVal constInitVal;

    public ConstDef(int num, Ident ident, List<LeftBrack> lb, List<ConstExp> sizes,
                    List<RightBrack> rb, Assign assign, ConstInitVal val) {
        super(num);
        this.ident = ident;
        this.leftBracks = lb;
        this.dimSizes = sizes;
        this.rightBracks = rb;
        this.assignToken = assign;
        this.constInitVal = val;
    }

    public int getValue(int level) throws ValueTypeException {
        if (leftBracks.size() == 0) {
            return constInitVal.getValue(level);
        } else {
            throw new ValueTypeException();
        }
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

    private List<IntermediateInstruction> generateConstArrayIr(int level) {
        List<IntermediateInstruction> instructions = new ArrayList<>();
        // if array const define
        int addr, size;

        if (level == 0) {
            addr = AddressPtr.getGlobalAddr();
            // calculate the size of array
            size = calcTotalSize(level);
            AddressPtr.addGlobalAddr(size);
        } else {
            addr = AddressPtr.getLocalAddr();
            // calculate the size of array
            size = calcTotalSize(level);
            AddressPtr.addLocalAddr(size);
        }

        // calculate lastDimSize of array
        int lastDimSize = 0;
        try {
            lastDimSize = dimSizes.get(dimSizes.size() - 1).getValue(level);
        } catch (ValueTypeException e) {
            e.printStackTrace();
        }

        // calculate init value during compiling
        constInitVal.generateIr(level, instructions, ident.getName(),
                addr, leftBracks.size(), lastDimSize, 0);
        List<Integer> values = constInitVal.calcValues(level);
        if (level == 0) {
            GlobalSymbolTable.insert(level, ident.getName(), SymbolType.ARRAY,
                    new ConstArraySymbol(getLineNumber(), ident.getName(), leftBracks.size(), dimSizes, values),
                    addr, size);
        } else {
            LocalSymbolTable.insert(level, ident.getName(), SymbolType.ARRAY,
                    new ConstArraySymbol(getLineNumber(), ident.getName(), leftBracks.size(), dimSizes, values),
                    addr, size);
        }
        return instructions;
    }

    private List<IntermediateInstruction> generateConstVarIr(int level) {
        List<IntermediateInstruction> instructions = new ArrayList<>();
        int addr;
        if (level == 0) {
            addr = AddressPtr.getGlobalAddr();
            AddressPtr.addGlobalAddr(4);
        } else {
            addr = AddressPtr.getLocalAddr();
            AddressPtr.addLocalAddr(4);
        }

        int value = 0;
        try {
            value = constInitVal.getValue(level);
        } catch (ValueTypeException e) {
            System.out.println("It's not a const variable!");
            e.printStackTrace();
        }

        if (level == 0) {
            GlobalSymbolTable.insert(level, ident.getName(), SymbolType.VAR, new ConstBTypeSymbol(getLineNumber(),
                    ident.getName(), value), addr, 4);
            String id = constInitVal.generateIr(level, instructions);
            instructions.add(new MovIr(id, "@" + ident.getName() + "@global" +"@" + addr));
        } else {
            LocalSymbolTable.insert(level, ident.getName(), SymbolType.VAR, new ConstBTypeSymbol(getLineNumber(),
                    ident.getName(), value), addr, 4);
            String id = constInitVal.generateIr(level, instructions);
            instructions.add(new MovIr(id, "@" + ident.getName() + "@local" +"@" + addr));
        }
        return instructions;
    }

    public List<IntermediateInstruction> generateIr(int level) {
        if (dimSizes.size() != 0) {
            return generateConstArrayIr(level);
        } else {
            return generateConstVarIr(level);
        }
    }
}