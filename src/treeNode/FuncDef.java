package treeNode;

import ir.InsertLabelIr;
import ir.IntermediateInstruction;
import ir.ReturnIr;
import symbol.AddressPtr;
import symbol.FunctionLocalSymbolTables;
import symbol.GlobalSymbolTable;
import symbol.LocalSymbolTable;
import symbol.type.FuncSymbol;
import symbol.type.SymbolType;

import java.util.ArrayList;
import java.util.List;

public class FuncDef extends TreeNode {
    private FuncType funcType; // VOID, INT
    private Ident ident;
    private LeftParent leftParent;
    private FuncFParams funcFParams;
    private RightParent rightParent;
    private Block block;

    public FuncDef(int num, FuncType funcType, Ident ident, LeftParent leftParent, FuncFParams funcFParams,
                   RightParent rightParent, Block block) {
        super(num);
        this.funcType = funcType;
        this.ident = ident;
        this.leftParent = leftParent;
        this.funcFParams = funcFParams;
        this.rightParent = rightParent;
        this.block = block;
    }

    private void changeLocalSymbolTable(int level) {
        LocalSymbolTable.createNewLocalSymbolTable();
        LocalSymbolTable.setFunctionName(ident.getName());
        FunctionLocalSymbolTables.addLocalSymbolTable(ident.getName(), LocalSymbolTable.getCurrentLocalSymbolTable());
        AddressPtr.resetLocalAddr(0);

        GlobalSymbolTable.insert(level, ident.getName(), SymbolType.FUNC,
                new FuncSymbol(getLineNumber(), ident.getName(),
                        (funcFParams == null ? 0 : funcFParams.getParamsCount()),
                        (funcFParams == null ? null : funcFParams.getParamTypes()),
                        funcType));
    }

    public List<IntermediateInstruction> generateIr(int level) {
        List<IntermediateInstruction> instructions = new ArrayList<>();
        instructions.add(new InsertLabelIr(ident.getName()));

        // change local symbol table and save **function name** in global symbolTable
        changeLocalSymbolTable(level);

        if (funcFParams != null) {
            instructions.addAll(funcFParams.generateIr(level));
        }

        int sz = instructions.size();
        boolean flag = false;
        instructions.addAll(block.generateIr(level));
        for (int i = sz; i < instructions.size(); i++) {
            if (instructions.get(i) instanceof ReturnIr) {
                flag = true;
                break;
            }
        }
        if (!flag) {
            instructions.add(new ReturnIr());
        }

        return instructions;
    }
}