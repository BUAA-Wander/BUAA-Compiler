package ir;

import ir.utils.Operand;
import mips.Lw;
import mips.MipsCode;
import mips.Sw;
import symbol.GlobalSymbolTable;
import symbol.LocalSymbolTable;
import symbol.SymbolTableItem;
import symbol.SymbolTableType;
import symbol.type.SymbolType;

import java.util.ArrayList;
import java.util.List;

public class IntermediateInstruction {
    private Operand left;
    private Operand right;
    private Operand res;

    public IntermediateInstruction(Operand left, Operand right, Operand res) {
        this.left = left;
        this.right = right;
        this.res = res;
    }

    public IntermediateInstruction(Operand left, Operand res) {
        this.left = left;
        this.res = res;
    }

    public IntermediateInstruction(Operand res) {
        this.res = res;
    }

    public IntermediateInstruction() {

    }

    public Operand getLeft() {
        return left;
    }

    public Operand getRight() {
        return right;
    }

    public Operand getRes() {
        return res;
    }

    public List<MipsCode> toMips() {
        return null;
    }
//
//    public SymbolTableItem getItemFromSymbolTable(String name) {
//        SymbolTableItem item;
//        if (name.charAt(0) == '#') {
//            // if op1 is tmp variable
//            item = LocalSymbolTable.getItem(name, SymbolType.VAR);
//            if (item == null) {
//                item = GlobalSymbolTable.getItem(name, SymbolType.VAR);
//            }
//        } else if (name.charAt(0) == '@') {
//            // if op1 is the variable we defined
//            String[] strings = name.split("@");
//            if (strings[2].equals("local")) {
//                item = LocalSymbolTable.getItem(strings[1], SymbolType.VAR);
//                if (item == null) {
//                    item = LocalSymbolTable.getItem(strings[1], SymbolType.ARRAY);
//                }
//            } else {
//                item = GlobalSymbolTable.getItem(strings[1], SymbolType.VAR);
//                if (item == null) {
//                    item = GlobalSymbolTable.getItem(strings[1], SymbolType.ARRAY);
//                }
//            }
//        } else {
//            System.out.println("It is imm!");
//            return null;
//        }
//        if (item == null) {
//            System.out.println("Not Found this variable!");
//        }
//        return item;
//    }
//
//    public SymbolTableType getOperandSymbolTable(String name) {
//        if (name.charAt(0) == '#') {
//            SymbolTableItem item = LocalSymbolTable.getItem(name, SymbolType.VAR);
//            if (item == null) {
//                return SymbolTableType.GLOBAL;
//            }
//            return SymbolTableType.LOCAL;
//        } else if (name.charAt(0) == '@') {
//            // if op1 is the variable we defined
//            String[] strings = name.split("@");
//            if (strings[2].equals("local")) {
//                return SymbolTableType.LOCAL;
//            } else {
//                return SymbolTableType.GLOBAL;
//            }
//        }
//        System.out.println("Can't find this operand's SymbolTable");
//        return null;
//    }
//
//    public List<MipsCode> load(SymbolTableItem item, SymbolTableType type, String regName) {
//        List<MipsCode> mipsCodes = new ArrayList<>();
//        // TODO: array index offset!!!!!!!
//        if (type == SymbolTableType.GLOBAL) {
//            mipsCodes.add(new Lw("$gp", regName, item.getAddr()));
//        } else {
//            mipsCodes.add(new Lw("$sp", regName, -item.getAddr()));
//        }
//        return mipsCodes;
//    }
//
//    public List<MipsCode> load(SymbolTableItem item, SymbolTableType type, String regName, String name) {
//        int offset = 0;
//        if (name.charAt(0) == '@') {
//            // if op1 is the variable we defined
//            String[] strings = name.split("@");
//            if (strings.length > 4) {
//                System.out.println("fuckfuckfuck");
//                offset = Integer.parseInt(strings[4]);
//            }
//        }
//        List<MipsCode> mipsCodes = new ArrayList<>();
//        // TODO: array index offset!!!!!!!
//        if (type == SymbolTableType.GLOBAL) {
//            mipsCodes.add(new Lw("$gp", regName, item.getAddr() + offset));
//        } else {
//            mipsCodes.add(new Lw("$sp", regName, -item.getAddr() - offset));
//        }
//        return mipsCodes;
//    }
//
//    public List<MipsCode> save(SymbolTableItem item, SymbolTableType type, String regName, String name) {
//        int offset = 0;
//        if (name.charAt(0) == '@') {
//            // if op1 is the variable we defined
//            String[] strings = name.split("@");
//            if (strings.length > 4) {
//                offset = Integer.parseInt(strings[4]);
//            }
//        }
//
//        List<MipsCode> mipsCodes = new ArrayList<>();
//        if (type == SymbolTableType.GLOBAL) {
//            // gp add or minu????
//            mipsCodes.add(new Sw("$gp", regName, item.getAddr() + offset));
//        } else {
//            mipsCodes.add(new Sw("$sp", regName, -item.getAddr() - offset));
//        }
//        return mipsCodes;
//    }
//
//    public List<MipsCode> save(SymbolTableItem item, SymbolTableType type, String regName) {
//        List<MipsCode> mipsCodes = new ArrayList<>();
//        if (type == SymbolTableType.GLOBAL) {
//            // gp add or minu????
//            mipsCodes.add(new Sw("$gp", regName, item.getAddr()));
//        } else {
//            mipsCodes.add(new Sw("$sp", regName, -item.getAddr()));
//        }
//        return mipsCodes;
//    }
}
