package treeNode;

import error.Error;
import ir.IntermediateInstruction;
import symbol.SymbolTable;

import java.util.List;

public class BlockStmt extends Stmt {
    private Block block;

    public BlockStmt(int num, Block block) {
        super(num, StmtType.BLOCK);
        this.block = block;
    }

    public String outputAdaptToHomework() {
        StringBuilder builder = new StringBuilder();
        builder.append(block.outputAdaptToHomework()).append("\n");
        builder.append("<Stmt>");
        return builder.toString();
    }

    public void createSymbolTable(int level, SymbolTable symbolTable
            , List<Error> errors) {
        block.createSymbolTable(level, symbolTable, errors);
    }

    public boolean dealWithErrorF(FuncType funcType, List<Error> errors) {
        return block.dealWithErrorF(funcType, errors);
    }

    public boolean dealWithErrorG(FuncType funcType, List<Error> errors) {
        return block.dealWithErrorG(funcType, errors);
    }

    public void dealWithErrorM(List<Error> errors, boolean isLoop) {
        block.dealWithErrorM(errors, isLoop);
    }

    public List<IntermediateInstruction> generateIr(int level) {
        return block.generateIr(level);
    }

    public List<IntermediateInstruction> generateIr(int level, String label_1, String label_2) {
        return block.generateIr(level, label_1, label_2);
    }
}