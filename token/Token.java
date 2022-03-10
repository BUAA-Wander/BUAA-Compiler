package token;

public class Token {
    private TokenType type;
    private int lineNumber;
    private String value;

    public Token(TokenType type, int lineNumber, String value) {
        this.type = type;
        this.lineNumber = lineNumber;
        this.value = value;
    }

    public TokenType getTokenType() {
        return type;
    }

    public String getTokenValue() {
        return value;
    }

    public int getTokenLineNumber() {
        return lineNumber;
    }

    public String getResult() {
        if ((type == TokenType.CompUnit)
                || (type == TokenType.ConstDecl)
                || (type == TokenType.ConstDef)
                || (type == TokenType.ConstInitVal)
                || (type == TokenType.VarDecl)
                || (type == TokenType.VarDef)
                || (type == TokenType.InitVal)
                || (type == TokenType.FuncDef)
                || (type == TokenType.MainFuncDef)
                || (type == TokenType.FuncType)
                || (type == TokenType.FuncFParams)
                || (type == TokenType.FuncFParam)
                || (type == TokenType.Block)
                || (type == TokenType.Stmt)
                || (type == TokenType.Exp)
                || (type == TokenType.Cond)
                || (type == TokenType.LVal)
                || (type == TokenType.PrimaryExp)
                || (type == TokenType.Number)
                || (type == TokenType.UnaryExp)
                || (type == TokenType.UnaryOp)
                || (type == TokenType.FuncRParams)
                || (type == TokenType.MulExp)
                || (type == TokenType.AddExp)
                || (type == TokenType.RelExp)
                || (type == TokenType.EqExp)
                || (type == TokenType.LAndExp)
                || (type == TokenType.LOrExp)
                || (type == TokenType.ConstExp)) {
            return value;
        } else {
            return getTokenType() + " " + getTokenValue();
        }
    }
}
