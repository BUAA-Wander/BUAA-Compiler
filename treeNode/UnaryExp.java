package treeNode;

import exception.ValueTypeException;
import ir.AddIr;
import ir.BrIr;
import ir.GetReturnValueIr;
import ir.TmpVarGenerator;
import ir.IntermediateInstruction;
import ir.LoadContextIr;
import ir.NotIr;
import ir.SaveContextIr;
import ir.SubIr;
import ir.utils.LabelOp;
import ir.utils.Operand;
import ir.utils.TmpVariable;
import symbol.AddressPtr;
import symbol.GlobalSymbolTable;
import symbol.LocalSymbolTable;
import symbol.SymbolTable;
import symbol.SymbolTableItem;
import symbol.type.FuncSymbol;
import symbol.type.ParamType;
import symbol.type.Symbol;
import symbol.type.SymbolType;
import symbol.type.VarBTypeSymbol;

import java.util.List;

public class UnaryExp extends TreeNode {
    private UnaryExpType type; // PRIMARY, FUNCCALL, OTHER

    private PrimaryExp primaryExp = null;

    private Ident ident = null;
    private LeftParent leftParent = null;
    private FuncRParams funcRParams = null;
    private RightParent rightParent = null;

    private UnaryOp unaryOp = null;
    private UnaryExp unaryExp = null;

    public UnaryExp(int num, UnaryExpType type, PrimaryExp primaryExp) {
        super(num);
        this.type = type;
        this.primaryExp = primaryExp;
    }

    public UnaryExp(int num, UnaryExpType type, Ident ident, LeftParent leftParent, FuncRParams funcRParams,
                    RightParent rightParent) {
        super(num);
        this.type = type;
        this.ident = ident;
        this.leftParent = leftParent;
        this.funcRParams = funcRParams;
        this.rightParent = rightParent;
    }

    public UnaryExp(int num, UnaryExpType type, UnaryOp unaryOp, UnaryExp unaryExp) {
        super(num);
        this.type = type;
        this.unaryOp = unaryOp;
        this.unaryExp = unaryExp;
    }

    public ParamType getParamType(SymbolTable symbolTable) {
        if (type == UnaryExpType.FUNCCALL) {
            SymbolTableItem item = symbolTable.getItem(0, ident.getName(), SymbolType.FUNC);
            if (item.getSymbol() instanceof FuncSymbol) {
                FuncSymbol funcSymbol = ((FuncSymbol) item.getSymbol());
                if (funcSymbol.getReturnType() == FuncType.VOID) {
                    return ParamType.VOID;
                } else {
                    return ParamType.INT;
                }
            }
            return ParamType.INT;
        } else if (type == UnaryExpType.PRIMARY) {
            return primaryExp.getParamType(symbolTable);
        } else {
            return unaryExp.getParamType(symbolTable);
        }
    }

    public int getValue(int level) throws ValueTypeException {
        if (type == UnaryExpType.PRIMARY) {
            return primaryExp.getValue(level);
        } else if (type == UnaryExpType.FUNCCALL) {
            // TODO
            throw new ValueTypeException();
        } else {
            if (unaryOp.getType().equals(UnaryOpType.ADD)) {
                return unaryExp.getValue(level);
            } else if (unaryOp.getType().equals(UnaryOpType.SUB)) {
                return -unaryExp.getValue(level);
            } else {
                return ~unaryExp.getValue(level);
            }
        }
    }

    private Operand generateFunctionCallIr(int level, List<IntermediateInstruction> instructions, int used) {
        if (GlobalSymbolTable.isExist(0, ident.getName(), SymbolType.FUNC)) {
            // we need to alloc a unit for context first
            // then pass parameter
            // after that, save context in the unit we prepared before

            Operand raId = new TmpVariable(level, TmpVarGenerator.nextId(), (level == 0)); // save ra
            int addr = AddressPtr.getLocalAddr();
            AddressPtr.addLocalAddr(4);
            LocalSymbolTable.insert(level, raId.toString(), SymbolType.VAR,
                    new VarBTypeSymbol(-1, raId.toString()), addr, 4);

            if (funcRParams != null) {
                instructions.addAll(funcRParams.generateIr(level, used));
            }
            // save context: ra
            // offset is negative
            instructions.add(new SaveContextIr(-addr - used));
            //instructions.add(new SaveContextIr(-addr));

            // modify sp, jal, restore sp, ra
            instructions.add(new BrIr(new LabelOp(ident.getName()), -used));

            // load context: ra
            // offset is negative
            instructions.add(new LoadContextIr(-addr - used));

            // deal with return value
            // TODO bugs
            SymbolTableItem item = GlobalSymbolTable.getItem(ident.getName(), SymbolType.FUNC);
            Symbol symbol = item.getSymbol();
            if (symbol instanceof FuncSymbol) {
                if (((FuncSymbol) symbol).getReturnType().equals(FuncType.INT)) {
                    Operand resId = new TmpVariable(level, TmpVarGenerator.nextTmpVar(level), (level == 0));
                    instructions.add(new GetReturnValueIr(resId));
                    return resId;
                }
            }
            // void function
            return new TmpVariable(level, "#0", true);
        } else {
            System.out.println("No such function!");
            return null;
        }
    }

    private Operand generateOtherIr(int level, List<IntermediateInstruction> instructions, int used) {
        Operand id = new TmpVariable(level, TmpVarGenerator.nextTmpVar(level), (level == 0));

        if (unaryOp.getType().equals(UnaryOpType.NOT)) {
            instructions.add(new NotIr(unaryExp.generateIr(level, instructions, used), id));
        } else if (unaryOp.getType().equals(UnaryOpType.ADD)) {
            instructions.add(
                    new AddIr(unaryExp.generateIr(level, instructions, used), new TmpVariable(level, "#0", true), id));
        } else {
            instructions.add(
                    new SubIr(new TmpVariable(level, "#0", true), unaryExp.generateIr(level, instructions, used), id));
        }
        return id;
    }

    // return a memory unit id
    public Operand generateIr(int level, List<IntermediateInstruction> instructions, int used) {
        if (type == UnaryExpType.PRIMARY) {
            return primaryExp.generateIr(level, instructions);
        } else if (type == UnaryExpType.FUNCCALL) {
            return generateFunctionCallIr(level, instructions, used);
        } else {
            return generateOtherIr(level, instructions, used);
        }
    }
}
