package treeNode;

import error.Error;
import ir.BrIr;
import ir.CallStack;
import ir.IntermediateInstruction;
import ir.MovImmIr;
import ir.MovIr;
import ir.StopIr;
import symbol.AddressPtr;
import symbol.GlobalSymbolTable;
import symbol.SymbolTable;
import symbol.type.FuncSymbol;
import symbol.type.Symbol;
import symbol.type.SymbolType;
import symbol.type.VarBTypeSymbol;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public String outputAdaptToHomework() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < decls.size(); i++) {
            builder.append(decls.get(i).outputAdaptToHomework()).append("\n");
        }
        for (int i = 0; i < funcDefs.size(); i++) {
            builder.append(funcDefs.get(i).outputAdaptToHomework()).append("\n");
        }
        builder.append(mainFuncDef.outputAdaptToHomework()).append("\n");
        builder.append("<CompUnit>");
        return builder.toString();
    }

    public void createSymbolTable(int level, SymbolTable symbolTable
            , List<Error> errors) {
        symbolTable.insert(level, "getint", SymbolType.FUNC,
                new FuncSymbol(1, "getint", 0, null, FuncType.INT));

        // TODO printf

        for (int i = 0; i < decls.size(); i++) {
            decls.get(i).createSymbolTable(level, symbolTable, errors);
        }
        for (int i = 0; i < funcDefs.size(); i++) {
            funcDefs.get(i).createSymbolTable(level, symbolTable, errors);
        }
        mainFuncDef.createSymbolTable(level, symbolTable, errors);
    }

    public List<IntermediateInstruction> generateIr(int level) {
        List<IntermediateInstruction> instructions = new ArrayList<>();

        int addr = AddressPtr.getGlobalAddr();
        AddressPtr.addGlobalAddr(4);
        GlobalSymbolTable.insert(level, "#0", SymbolType.VAR,
                new VarBTypeSymbol(-1, "#0"), addr, 4);
        instructions.add(new MovImmIr(0, "#0"));

        for (int i = 0; i < decls.size(); i++) {
            // if level == 0, then declare a global variable
            instructions.addAll(decls.get(i).generateIr(level));
        }

        // after declare all global variables, jump to main and execute it
        instructions.add(new BrIr("main"));
        instructions.add(new StopIr());

        for (int i = 0; i < funcDefs.size(); i++) {
            instructions.addAll(funcDefs.get(i).generateIr(level));
        }
        instructions.addAll(mainFuncDef.generateIr(level));
        return instructions;
    }
}