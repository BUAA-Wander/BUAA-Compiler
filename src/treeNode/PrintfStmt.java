package treeNode;

import error.Error;
import exception.ValueTypeException;
import ir.IntermediateInstruction;
import ir.WriteCharIr;
import ir.WriteIntIr;
import symbol.SymbolTable;
import symbol.type.Symbol;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PrintfStmt extends Stmt {
    private Printf printToken;
    private LeftParent leftParent;
    private FormatString formatString;
    private List<Comma> commas;
    private List<Exp> exps;
    private RightParent rightParent;
    private Semicolon semicolon;

    public PrintfStmt(int num, Printf printToken, LeftParent leftParent, FormatString formatString, List<Comma> commas,
                      List<Exp> exps, RightParent rightParent, Semicolon semicolon) {
        super(num, StmtType.PRINTF);
        this.printToken = printToken;
        this.leftParent = leftParent;
        this.formatString = formatString;
        this.commas = commas;
        this.exps = exps;
        this.rightParent = rightParent;
        this.semicolon = semicolon;
    }

    public String outputAdaptToHomework() {
        StringBuilder builder = new StringBuilder();
        builder.append(printToken.outputAdaptToHomework()).append("\n");
        builder.append(leftParent.outputAdaptToHomework()).append("\n");
        builder.append(formatString.outputAdaptToHomework()).append("\n");
        for (int i = 0; i < commas.size(); i++) {
            builder.append(commas.get(i).outputAdaptToHomework()).append("\n");
            builder.append(exps.get(i).outputAdaptToHomework()).append("\n");
        }
        builder.append(rightParent.outputAdaptToHomework()).append("\n");
        builder.append(semicolon.outputAdaptToHomework()).append("\n");
        builder.append("<Stmt>");
        return builder.toString();
    }

    public void createSymbolTable(int level, SymbolTable symbolTable
            , List<Error> errors) {
        dealWithErrorL(errors);

        if (exps == null) {
            return;
        }
        for (int i = 0; i < exps.size(); i++) {
            exps.get(i).createSymbolTable(level, symbolTable, errors);
        }
    }

    public boolean dealWithErrorF(FuncType funcType, List<Error> errors) {
        return true;
    }

    public void dealWithErrorL(List<Error> errors) {
        int expCount = 0;
        if (exps != null) {
            expCount = exps.size();
        }
        int formatCharCount = 0;
        String value = formatString.getValue();
        for (int i = 0; i < value.length(); i++) {
            if (value.charAt(i) == '%') {
                if (i + 1 < value.length() && value.charAt(i + 1) == 'd') {
                    formatCharCount++;
                }
            }
        }
        if (formatCharCount != expCount) {
            errors.add(new Error(printToken.getLineNumber(), "l"));
        }
    }

    public List<IntermediateInstruction> generateIr(int level) {
        List<IntermediateInstruction> instructions = new ArrayList<>();
        String value = formatString.getValue();
        int len = value.length();
        int idx = 0;
        for (int i = 0; i < len; i++) {
            if (value.charAt(i) == '"') {
                continue;
            }
            if (value.charAt(i) != '%') {
                if (value.charAt(i) == '\\') {
                    if (i + 1 < len) {
                        i++;
                        if (value.charAt(i) == 'n') {
                            instructions.add(new WriteCharIr('\n'));
                        } else {
                            System.out.println("can't solve this convert character");
                        }
                    } else {
                        System.out.println("missing convert character");
                    }
                } else {
                    instructions.add(new WriteCharIr(value.charAt(i)));
                }
            } else {
                if (i + 1 < len && value.charAt(i + 1) == 'd') {
                    i++;
                    IntermediateInstruction it = new WriteIntIr(
                            exps.get(idx).generateIr(level, instructions));
                    instructions.add(it);
                    idx++;
                }
            }
        }
        return instructions;
    }

    public List<IntermediateInstruction> generateIr(int level, String label_1, String label_2) {
        return generateIr(level);
    }
}
