package treeNode;

import error.Error;
import ir.BrIr;
import ir.CallStack;
import ir.InsertLabelIr;
import ir.IntermediateInstruction;
import ir.ReturnIr;
import symbol.AddressPtr;
import symbol.FunctionLocalSymbolTables;
import symbol.GlobalSymbolTable;
import symbol.LocalSymbolTable;
import symbol.SymbolTable;
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

    public String outputAdaptToHomework() {
        StringBuilder builder = new StringBuilder();
        if (funcType == FuncType.VOID) {
            builder.append("VOIDTK void\n");
        } else {
            builder.append("INTTK int\n");
        }
        builder.append("<FuncType>\n");

        builder.append(ident.outputAdaptToHomework()).append("\n");
        builder.append(leftParent.outputAdaptToHomework()).append("\n");
        if (funcFParams != null) {
            builder.append(funcFParams.outputAdaptToHomework()).append("\n");
        }
        builder.append(rightParent.outputAdaptToHomework()).append("\n");
        builder.append(block.outputAdaptToHomework()).append("\n");
        builder.append("<FuncDef>");
        return builder.toString();
    }

    public void createSymbolTable(int level, SymbolTable symbolTable
            , List<Error> errors) {
        if (!dealWithErrorB(level, symbolTable, errors)) {
            return;
        }
        dealWithErrorM(errors, false);
        dealWithErrorF(funcType, errors);
        dealWithErrorG(funcType, errors);

        if (funcFParams != null) {
            funcFParams.createSymbolTable(level, symbolTable, errors);
            symbolTable.insert(level, ident.getName(), SymbolType.FUNC,
                    new FuncSymbol(getLineNumber(), ident.getName()
                            , funcFParams.getParamsCount(), funcFParams.getParamTypes(), funcType));
        } else {
            symbolTable.insert(level, ident.getName(), SymbolType.FUNC,
                    new FuncSymbol(getLineNumber(), ident.getName()
                            , 0, null, funcType));
        }
        block.createSymbolTable(level, symbolTable, errors);
    }

    public boolean dealWithErrorF(FuncType funcType, List<Error> errors) {
        if (block.dealWithErrorF(funcType, errors)) {
            return true;
        }
        int num = block.getReturnLineNumber();
        errors.add(new Error(num, "f"));
        return false;
    }

    public boolean dealWithErrorG(FuncType funcType, List<Error> errors) {
        if (block.dealWithErrorG(funcType, errors)) {
            return true;
        }
        int num = block.getLastRBraceLineNumber();
        errors.add(new Error(num, "g"));
        return false;
    }

    public boolean dealWithErrorB(int level, SymbolTable symbolTable, List<Error> errors) {
        if (symbolTable.isExistInCurrentLevel(level, ident.getName(), SymbolType.FUNC)) {
            errors.add(new Error(ident.getLineNumber(), "b"));
            return false;
        }
        return true;
    }

    public void dealWithErrorM(List<Error> errors, boolean isLoop) {
        block.dealWithErrorM(errors, isLoop);
    }

    public List<IntermediateInstruction> generateIr(int level) {
        List<IntermediateInstruction> instructions = new ArrayList<>();
        instructions.add(new InsertLabelIr(ident.getName()));

        // change local symbol table
        LocalSymbolTable.createNewLocalSymbolTable();
        LocalSymbolTable.setFunctionName(ident.getName());
        FunctionLocalSymbolTables.addLocalSymbolTable(ident.getName(), LocalSymbolTable.getCurrentLocalSymbolTable());
        AddressPtr.resetLocalAddr(0);
        
        // save **function name** in global symbolTable
        GlobalSymbolTable.insert(level, ident.getName(), SymbolType.FUNC,
                new FuncSymbol(getLineNumber(), ident.getName(),
                        (funcFParams == null ? 0 : funcFParams.getParamsCount()),
                        (funcFParams == null ? null : funcFParams.getParamTypes()),
                        funcType));

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