package treeNode;

import error.Error;
import ir.IntermediateInstruction;
import symbol.SymbolTable;
import symbol.type.Symbol;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Decl extends TreeNode {
    private DeclType type; // CONST, VAR, avoid multi-extends
    private ConstDecl constDecl;
    private VarDecl varDecl;

    public Decl(int num, DeclType type, ConstDecl constDecl) {
        super(num);
        this.type = type;
        this.constDecl = constDecl;
    }

    public Decl(int num, DeclType type, VarDecl varDecl) {
        super(num);
        this.type = type;
        this.varDecl = varDecl;
    }

    public String outputAdaptToHomework() {
        if (type == DeclType.CONST) {
            return constDecl.outputAdaptToHomework();
        } else {
            return varDecl.outputAdaptToHomework();
        }
    }

    public void createSymbolTable(int level, SymbolTable symbolTable
            , List<Error> errors) {
        if (type == DeclType.CONST) {
            constDecl.createSymbolTable(level, symbolTable, errors);
        } else {
            varDecl.createSymbolTable(level, symbolTable, errors);
        }
    }

    public List<IntermediateInstruction> generateIr(int level) {
        List<IntermediateInstruction> instructions = new ArrayList<>();
        if (type == DeclType.CONST) {
            instructions.addAll(constDecl.generateIr(level));
        } else {
            instructions.addAll(varDecl.generateIr(level));
        }
        return instructions;
    }
}
