package treeNode;

import error.Error;
import ir.IntermediateInstruction;
import symbol.SymbolTable;
import symbol.type.Symbol;

import java.util.List;
import java.util.Map;

public class BlockItem extends TreeNode {
    private BlockItemType type = null; // DECL, STMT
    private Decl decl = null;
    private Stmt stmt = null;

    public BlockItem(int num, BlockItemType type, Decl decl) {
        super(num);
        this.type = type;
        this.decl = decl;
    }

    public BlockItem(int num, BlockItemType type, Stmt stmt) {
        super(num);
        this.type = type;
        this.stmt = stmt;
    }

    public String outputAdaptToHomework() {
        if (type == BlockItemType.STMT) {
            return stmt.outputAdaptToHomework();
        } else {
            return decl.outputAdaptToHomework();
        }
    }

    public void createSymbolTable(int level, SymbolTable symbolTable
            , List<Error> errors) {
        if (type == BlockItemType.DECL) {
            decl.createSymbolTable(level, symbolTable, errors);
        } else {
            stmt.createSymbolTable(level, symbolTable, errors);
        }
    }

    public boolean dealWithErrorF(FuncType funcType, List<Error> errors) {
        if (stmt == null) {
            return true;
        }
        return stmt.dealWithErrorF(funcType, errors);
    }

    public boolean dealWithErrorG(FuncType funcType, List<Error> errors) {
        if (stmt == null) {
            return false;
        }
        return stmt.dealWithErrorG(funcType, errors);
    }

    public void dealWithErrorM(List<Error> errors, boolean isLoop) {
        if (stmt != null) {
            stmt.dealWithErrorM(errors, isLoop);
        }
    }

    public int getReturnLineNumber() {
        return stmt.getReturnLineNumber();
    }

    public List<IntermediateInstruction> generateIr(int level) {
        if (type == BlockItemType.DECL) {
            return decl.generateIr(level);
        } else {
            return stmt.generateIr(level);
        }
    }

    public List<IntermediateInstruction> generateIr(int level, String label_1, String label_2) {
        if (type == BlockItemType.DECL) {
            return decl.generateIr(level);
        } else {
            return stmt.generateIr(level, label_1, label_2);
        }
    }
}
