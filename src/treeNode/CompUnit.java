package treeNode;

import ir.BrIr;
import ir.IntermediateInstruction;
import ir.MovImmIr;
import ir.StopIr;
import ir.utils.Immediate;
import ir.utils.LabelOp;
import ir.utils.TmpVariable;
import symbol.AddressPtr;
import symbol.GlobalSymbolTable;
import symbol.type.SymbolType;
import symbol.type.VarBTypeSymbol;

import java.util.ArrayList;
import java.util.List;

public class CompUnit extends TreeNode {
    private List<Decl> decls;
    private List<FuncDef> funcDefs;
    private MainFuncDef mainFuncDef;

    public CompUnit(int num, List<Decl> decls, List<FuncDef> funcDefs, MainFuncDef mainFuncDef) {
        super(num);
        this.decls = decls;
        this.funcDefs = funcDefs;
        this.mainFuncDef = mainFuncDef;
    }

    public List<IntermediateInstruction> generateIr(int level) {
        List<IntermediateInstruction> instructions = new ArrayList<>();

        int addr = AddressPtr.getGlobalAddr();
        AddressPtr.addGlobalAddr(4);
        GlobalSymbolTable.insert(level, "#0", SymbolType.VAR,
                new VarBTypeSymbol(-1, "#0"), addr, 4);
        instructions.add(new MovImmIr(new Immediate(0), new TmpVariable("#0", true)));

        for (int i = 0; i < decls.size(); i++) {
            // if level == 0, then declare a global variable
            instructions.addAll(decls.get(i).generateIr(level));
        }

        // after declare all global variables, jump to main and execute it
        instructions.add(new BrIr(new LabelOp("main")));
        instructions.add(new StopIr());

        for (int i = 0; i < funcDefs.size(); i++) {
            instructions.addAll(funcDefs.get(i).generateIr(level));
        }
        instructions.addAll(mainFuncDef.generateIr(level));
        return instructions;
    }
}