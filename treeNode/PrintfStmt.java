package treeNode;

import ir.IntermediateInstruction;
import ir.WriteCharIr;
import ir.WriteIntIr;
import ir.utils.StringOp;

import java.util.ArrayList;
import java.util.List;

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
                            instructions.add(new WriteCharIr(new StringOp(String.valueOf('\n'))));
                        } else {
                            System.out.println("can't solve this convert character");
                        }
                    } else {
                        System.out.println("missing convert character");
                    }
                } else {
                    instructions.add(new WriteCharIr(new StringOp(String.valueOf(value.charAt(i)))));
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
