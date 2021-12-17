package treeNode;

import error.Error;
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

public class MainFuncDef extends TreeNode {
    private FuncType funcType; // VOID, INT
    private Ident ident;
    private LeftParent leftParent;
    private RightParent rightParent;
    private Block block;

    public MainFuncDef(int num, FuncType funcType, Ident ident,
                       LeftParent lp, RightParent rp, Block block) {
        super(num);
        this.funcType = funcType;
        this.ident = ident;
        this.leftParent = lp;
        this.rightParent = rp;
        this.block = block;
    }

    public String outputAdaptToHomework() {
        StringBuilder builder = new StringBuilder();
        if (funcType == FuncType.VOID) {
            builder.append("VOIDTK void\n");
        } else {
            builder.append("INTTK int\n");
        }

        builder.append("MAINTK main\n");
        builder.append(leftParent.outputAdaptToHomework()).append("\n");
        builder.append(rightParent.outputAdaptToHomework()).append("\n");
        builder.append(block.outputAdaptToHomework()).append("\n");
        builder.append("<MainFuncDef>");
        return builder.toString();
    }

//    public void createSymbolTable(int level, SymbolTable symbolTable
//            , List<Error> errors) {
//        dealWithErrorG(funcType, errors);
//        dealWithErrorM(errors, false);
//
//        symbolTable.insert(level, "main", SymbolType.FUNC,
//                new FuncSymbol(getLineNumber(), "main"
//                , 0, null, FuncType.INT));
//        try {
//            block.createSymbolTable(level, symbolTable, errors);
//        } catch (NullPointerException e) {
//            System.out.println("fuck");
//        }
//    }

//    public boolean dealWithErrorG(FuncType funcType, List<Error> errors) {
//        if (block.dealWithErrorG(funcType, errors)) {
//            return true;
//        }
//        int num = block.getLastRBraceLineNumber();
//        errors.add(new Error(num, "g"));
//        return false;
//    }

//    public void dealWithErrorM(List<Error> errors, boolean isLoop) {
//        block.dealWithErrorM(errors, false);
//    }

    public List<IntermediateInstruction> generateIr(int level) {
        GlobalSymbolTable.insert(level, "main", SymbolType.FUNC,
                new FuncSymbol(getLineNumber(), "main"
                        , 0, null, FuncType.INT));

        List<IntermediateInstruction> instructions = new ArrayList<>();

        instructions.add(new InsertLabelIr("main"));
        LocalSymbolTable.createNewLocalSymbolTable(); // create a new Local Symbol Table
        AddressPtr.resetLocalAddr(0);

        instructions.addAll(block.generateIr(level));

        FunctionLocalSymbolTables.addLocalSymbolTable("main",
                LocalSymbolTable.getCurrentLocalSymbolTable());
        return instructions;
    }
}
