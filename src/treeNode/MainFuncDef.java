package treeNode;

import ir.InsertLabelIr;
import ir.IntermediateInstruction;
import ir.utils.LabelOp;
import symbol.AddressPtr;
import symbol.FunctionLocalSymbolTables;
import symbol.GlobalSymbolTable;
import symbol.LocalSymbolTable;
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

    public List<IntermediateInstruction> generateIr(int level) {
        GlobalSymbolTable.insert(level, "main", SymbolType.FUNC,
                new FuncSymbol(getLineNumber(), "main"
                        , 0, null, FuncType.INT));

        List<IntermediateInstruction> instructions = new ArrayList<>();

        instructions.add(new InsertLabelIr(new LabelOp("main")));
        LocalSymbolTable.createNewLocalSymbolTable(); // create a new Local Symbol Table
        AddressPtr.resetLocalAddr(0);

        instructions.addAll(block.generateIr(level));

        FunctionLocalSymbolTables.addLocalSymbolTable("main",
                LocalSymbolTable.getCurrentLocalSymbolTable());
        return instructions;
    }
}
