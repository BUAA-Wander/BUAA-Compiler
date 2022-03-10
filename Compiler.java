
import exception.NoMatchToken;
import exception.ParseError;
import exception.ParseOutOfBound;
import ir.IntermediateInstruction;
import ir.LoadArrayValueIr;
import ir.PassParamIr;
import ir.StorePointerValueIr;
import lex.Lexer;
import mips.Annotation;
import mips.MipsCode;
import mips.TargetCodeContainer;
import parse.Parser;
import symbol.FunctionLocalSymbolTables;
import symbol.GlobalSymbolTable;
import symbol.SymbolTable;
import symbol.SymbolTableItem;
import symbol.type.Symbol;
import token.Token;
import treeNode.CompUnit;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Compiler {
    public static void main(String[] args) throws IOException,
            NoMatchToken, ParseError, ParseOutOfBound {
        Lexer lexer = new Lexer();
        List<Token> tokens = lexer.tokenization();

        Parser parser = new Parser(tokens);
        CompUnit res = parser.parseCompUnit();
        List<IntermediateInstruction> ir = new ArrayList<>();
        if (res != null) {
            ir = res.generateIr(0);
        }
        outputIr(ir);
        toMips(ir);
        outputMips(TargetCodeContainer.codes);

        System.out.println("Global SymbolTable:");
        outputSymbolTable(GlobalSymbolTable.symbolTable);
        for (String i : FunctionLocalSymbolTables.allLocalSymbolTables.keySet()) {
            SymbolTable symbolTable = FunctionLocalSymbolTables.allLocalSymbolTables.get(i);
            System.out.println(i + " SymbolTable:");
            outputSymbolTable(symbolTable);
        }
    }

    public static void outputSymbolTable(SymbolTable symbolTable) {
        List<SymbolTableItem> items = symbolTable.getItems();
        for (int i = 0; i < items.size(); i++) {
            System.out.println("type: " + items.get(i).getSymbolType() + " name: "
                    + items.get(i).getName() + " headAddr: " + items.get(i).getAddr()
                    + " size: " + items.get(i).getSize());
        }
    }

    public static void toMips(List<IntermediateInstruction> ir) {
        // TargetCodeContainer.codes.add(new Add("$0", "$sp", "$fp"));
        // TargetCodeContainer.codes.add(new Add("$0", "$fp", "$s0"));
        int pos = 1;
        for (IntermediateInstruction i : ir) {
            try {
                if (i instanceof LoadArrayValueIr) {
                    TargetCodeContainer.codes.add((new Annotation("begin loadArrayValue")));
                } else if (i instanceof StorePointerValueIr) {
                    TargetCodeContainer.codes.add((new Annotation("begin StorePointerValue")));
                } else if (i instanceof PassParamIr) {
                    TargetCodeContainer.codes.add((new Annotation("begin PassParam")));
                }
                List<MipsCode> list = i.toMips();
                if (list != null) {
                    TargetCodeContainer.codes.addAll(list);
                }
                if (i instanceof LoadArrayValueIr) {
                    TargetCodeContainer.codes.add((new Annotation("END loadArrayValue")));
                } else if (i instanceof StorePointerValueIr) {
                    TargetCodeContainer.codes.add((new Annotation("END StorePointerValue")));
                } else if (i instanceof PassParamIr) {
                    TargetCodeContainer.codes.add((new Annotation("End PassParam")));
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
                System.out.println(i.toString());
                System.out.println(pos);
                break;
            }
//            if (i instanceof LoadArrayValueIr) {
//                TargetCodeContainer.codes.add((new Annotation("begin loadArrayValue")));
//            } else if (i instanceof StorePointerValueIr) {
//                TargetCodeContainer.codes.add((new Annotation("begin StorePointerValue")));
//            }
//            List<MipsCode> list = i.toMips();
//            if (list != null) {
//                TargetCodeContainer.codes.addAll(list);
//            }
//            if (i instanceof LoadArrayValueIr) {
//                TargetCodeContainer.codes.add((new Annotation("END loadArrayValue")));
//            } else if (i instanceof StorePointerValueIr) {
//                TargetCodeContainer.codes.add((new Annotation("END StorePointerValue")));
//            }

            pos++;
        }
    }

    public static void outputMips(List<MipsCode> codes) throws IOException {
        File file = new File("mips.txt");
        FileWriter writer = new FileWriter(file);
        for (MipsCode i : codes) {
            writer.write(i.toString());
            writer.write("\n");
        }
        writer.flush();
    }

    public static void outputIr(List<IntermediateInstruction> ir) throws IOException {
        File file = new File("ir.txt");
        FileWriter writer = new FileWriter(file);
        for (IntermediateInstruction i : ir) {
            writer.write(i.toString());
            writer.write("\n");
        }
        writer.flush();
    }

    public static void output(String str) throws IOException {
        File file = new File("output.txt");
        FileWriter writer = new FileWriter(file);
        writer.write(str);
        writer.flush();
    }
}
