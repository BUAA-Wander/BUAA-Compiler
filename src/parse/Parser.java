package parse;

import error.Error;
import exception.ParseError;
import exception.ParseOutOfBound;
import token.Token;
import token.TokenType;
import treeNode.AddExp;
import treeNode.Assign;
import treeNode.AssignValueStmt;
import treeNode.BType;
import treeNode.Block;
import treeNode.BlockItem;
import treeNode.BlockItemType;
import treeNode.BlockStmt;
import treeNode.Break;
import treeNode.BreakStmt;
import treeNode.Comma;
import treeNode.CompUnit;
import treeNode.Cond;
import treeNode.Const;
import treeNode.ConstDecl;
import treeNode.ConstDef;
import treeNode.ConstExp;
import treeNode.ConstInitVal;
import treeNode.ConstInitValType;
import treeNode.Continue;
import treeNode.ContinueStmt;
import treeNode.Decl;
import treeNode.DeclType;
import treeNode.Else;
import treeNode.EqExp;
import treeNode.Exp;
import treeNode.ExpStmt;
import treeNode.FormatString;
import treeNode.FuncDef;
import treeNode.FuncFParam;
import treeNode.FuncFParams;
import treeNode.FuncRParams;
import treeNode.FuncType;
import treeNode.Getint;
import treeNode.Ident;
import treeNode.If;
import treeNode.IfStmt;
import treeNode.InitVal;
import treeNode.InitValType;
import treeNode.IntConst;
import treeNode.LAndExp;
import treeNode.LOrExp;
import treeNode.LVal;
import treeNode.LeftBrace;
import treeNode.LeftBrack;
import treeNode.LeftParent;
import treeNode.MainFuncDef;
import treeNode.MulExp;
import treeNode.Num;
import treeNode.Operator;
import treeNode.OperatorType;
import treeNode.PrimaryExp;
import treeNode.PrimaryExpType;
import treeNode.Printf;
import treeNode.PrintfStmt;
import treeNode.ReadValueStmt;
import treeNode.RelExp;
import treeNode.Return;
import treeNode.ReturnStmt;
import treeNode.RightBrace;
import treeNode.RightBrack;
import treeNode.RightParent;
import treeNode.Semicolon;
import treeNode.Stmt;
import treeNode.UnaryExp;
import treeNode.UnaryExpType;
import treeNode.UnaryOp;
import treeNode.UnaryOpType;
import treeNode.VarDecl;
import treeNode.VarDef;
import treeNode.While;
import treeNode.WhileStmt;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Parser {
    private List<Token> tokens;
    private int idx;
    private TokenType sym;
    private Stack<Token> res;
    private List<Error> errors;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
        idx = -1;
        sym = null;
    }

    public void initialize() {
        idx = -1;
        sym = null;
        res = new Stack<>();
        errors = new ArrayList<>();
    }

    /////////////////////////////////////////

    public List<Error> getErrors() {
        return errors;
    }

    public Semicolon dealWithErrorI() throws ParseOutOfBound {
        lastToken();
        int num = currentToken().getTokenLineNumber();
        errors.add(new Error(num, "i"));
        nextToken();
        return new Semicolon(num);
    }

    public RightBrack dealWithErrorK() throws ParseOutOfBound {
        lastToken();
        int num = currentToken().getTokenLineNumber();
        errors.add(new Error(num, "k"));
        nextToken();
        return new RightBrack(num);
    }

    public RightParent dealWithErrorJ() throws ParseOutOfBound {
        lastToken();
        int num = currentToken().getTokenLineNumber();
        errors.add(new Error(num, "j"));
        nextToken();
        return new RightParent(num);
    }

    /////////////////////////////////////////



    public void pushResult(Token token) {
        // when we meet terminate token or parse finished, we should use this method
        res.push(token);
    }

    public void popResult() {
        res.pop();
    }

    public Token currentToken() throws ParseOutOfBound {
        if (idx < 0 || idx >= tokens.size()) {
            throw new ParseOutOfBound();
        }
        return tokens.get(idx);
    }

    public void nextToken() throws ParseOutOfBound {
        idx++;
        if (idx < tokens.size() && idx >= 0) {
            sym = tokens.get(idx).getTokenType();
        } else {
            throw new ParseOutOfBound();
        }
    }

    public boolean hasNextToken() {
        return ((idx + 1) < tokens.size());
    }

    public void lastToken() throws ParseOutOfBound {
        idx--;
        if (idx < tokens.size() && idx >= 0) {
            sym = tokens.get(idx).getTokenType();
        } else if (idx == -1) {
            sym = null;
        } else {
            throw new ParseOutOfBound();
        }
    }

    public boolean hasDecl() throws ParseOutOfBound {
        // there is a next token
        if (sym == TokenType.CONSTTK) {
            // ConstDecl
            return true;
        } else if (sym == TokenType.INTTK) {
            // maybe VarDecl
            // in this case we need to peek more token
            nextToken();
            nextToken();
            if (sym == TokenType.LPARENT) {
                // main function or other function
                lastToken();
                lastToken();
                return false;
            } else {
                // maybe VarDecl, if the procedure is under the limit of L
                lastToken();
                lastToken();
                return true;
            }
        } else {
            return false;
        }
    }

    public boolean hasFuncDef() throws ParseOutOfBound {
        if (sym == TokenType.VOIDTK) {
            // must be normal function
            return true;
        } else if (sym == TokenType.INTTK) {
            // maybe main function
            nextToken();
            if (sym == TokenType.MAINTK) {
                // main function
                lastToken();
                return false;
            } else {
                // not main function, maybe function
                lastToken();
                return true;
            }
        } else {
            return false;
        }
    }

    public boolean hasFuncFParams() throws ParseOutOfBound {
        return hasFuncFParam();
    }

    public boolean hasFuncFParam() throws ParseOutOfBound {
        return (sym == TokenType.INTTK);
    }

    public boolean hasConstExp() {
        // include []
        return (sym == TokenType.LBRACK);
    }

    public boolean hasLVal() {
        return (sym == TokenType.IDENFR);
    }





    // the code under this line is the parse procedure




    public CompUnit parseCompUnit() throws ParseOutOfBound, ParseError {
        initialize();
        nextToken();
        // parse declaration
        List<Decl> decls = new ArrayList<>();
        while (hasDecl()) {
            decls.add(parseDecl());
        }

        List<FuncDef> funcDefs = new ArrayList<>();
        // parse function definition
        while (hasFuncDef()) {
            funcDefs.add(parseFuncDef());
        }

        // parse main function definition
        MainFuncDef mainFuncDef = parseMainFuncDef();

        // push CompUnit into res
        // line number?
         pushResult(new Token(TokenType.CompUnit, 0, "<CompUnit>"));

        // still have something to parse
        if (idx != tokens.size() - 1) {
            throw new ParseError();
        }

        return new CompUnit(1, decls, funcDefs, mainFuncDef);
//        Stack<Token> tmp = new Stack<>();
//        while (!res.empty()) {
//            tmp.push(res.peek());
//            res.pop();
//        }
//        List<Token> ans = new ArrayList<>();
//        while (!tmp.empty()) {
//            ans.add(tmp.peek());
//            tmp.pop();
//        }
//        return ans;
    }

    public MainFuncDef parseMainFuncDef() throws ParseOutOfBound, ParseError {
        BType bType = null;
        Ident ident = null;
        LeftParent leftParent = null;
        List<FuncFParams> funcFParams = null;
        RightParent rightParent = null;
        Block block = null;

        if (sym == TokenType.INTTK) {
            bType = new BType(currentToken().getTokenLineNumber());
            pushResult(currentToken());

            nextToken();
            if (sym == TokenType.MAINTK) {
                ident = new Ident(currentToken().getTokenLineNumber(), "main");
                pushResult(currentToken());

                nextToken();
                if (sym == TokenType.LPARENT) {
                    leftParent = new LeftParent(currentToken().getTokenLineNumber());
                    pushResult(currentToken());

                    nextToken();
                    if (sym == TokenType.RPARENT) {
                        rightParent = new RightParent(currentToken().getTokenLineNumber());
                        pushResult(currentToken());

                        nextToken();
                    } else {
                        rightParent = dealWithErrorJ();
                    }
                    block = parseBlock();
                } else {
                    throw new ParseError();
                }
            } else {
                throw new ParseError();
            }
        } else {
            throw new ParseError();
        }

        pushResult(new Token(TokenType.MainFuncDef, 0, "<MainFuncDef>"));
        return new MainFuncDef(ident.getLineNumber(), FuncType.INT, ident, leftParent, rightParent, block);
    }

    public Decl parseDecl() throws ParseOutOfBound, ParseError {
        int num = currentToken().getTokenLineNumber();
        if (sym == TokenType.CONSTTK) {
            return new Decl(num, DeclType.CONST, parseConstDecl());
        } else if (sym == TokenType.INTTK) {
            return new Decl(num, DeclType.VAR, parseVarDecl());
        } else {
            throw new ParseError();
        }
    }

    public FuncDef parseFuncDef() throws ParseError, ParseOutOfBound {
        FuncType type = parseFuncType();

        Ident ident = parseIdent();

        LeftParent leftParent = null;
        FuncFParams funcFParams = null;
        RightParent rightParent = null;
        Block block = null;
        if (sym == TokenType.LPARENT) {
            pushResult(currentToken());
            leftParent = new LeftParent(currentToken().getTokenLineNumber());
            nextToken();
            if (hasFuncFParams()) {
                funcFParams = parseFuncFParams();
            }
            if (sym == TokenType.RPARENT) {
                pushResult(currentToken());
                rightParent = new RightParent(currentToken().getTokenLineNumber());
                nextToken();
            } else {
                rightParent = dealWithErrorJ();
            }
            block = parseBlock();
        } else {
            throw new ParseError();
        }

        pushResult(new Token(TokenType.FuncDef, 0, "<FuncDef>"));
        return new FuncDef(ident.getLineNumber(), type, ident, leftParent, funcFParams, rightParent, block);
    }

    public ConstDecl parseConstDecl() throws ParseOutOfBound, ParseError {
        Const constToken = null;
        BType bType = null;
        List<ConstDef> constDefs = new ArrayList<>();
        List<Comma> commas = new ArrayList<>();
        Semicolon semicolon = null;

        if (sym == TokenType.CONSTTK) {
            pushResult(currentToken());
            constToken = new Const(currentToken().getTokenLineNumber());
            nextToken();
            if (sym == TokenType.INTTK) {
                pushResult(currentToken());
                bType = new BType(currentToken().getTokenLineNumber());
                nextToken();
                constDefs.add(parseConstDef());
                while (sym == TokenType.COMMA) {
                    commas.add(new Comma(currentToken().getTokenLineNumber()));
                    pushResult(currentToken());
                    nextToken();
                    constDefs.add(parseConstDef());
                }
                if (sym == TokenType.SEMICN) {
                    pushResult(currentToken());
                    semicolon = new Semicolon(currentToken().getTokenLineNumber());
                    nextToken();
                } else {
                    semicolon = dealWithErrorI();
                }
            } else {
                throw new ParseError();
            }
        } else {
            throw new ParseError();
        }

        pushResult(new Token(TokenType.ConstDecl, 0, "<ConstDecl>"));
        return new ConstDecl(constToken.getLineNumber(), constToken, bType, constDefs, commas, semicolon);
    }

    public VarDecl parseVarDecl() throws ParseError, ParseOutOfBound {
        BType bType = null;
        List<VarDef> varDefs = new ArrayList<>();
        List<Comma> commas = new ArrayList<>();
        Semicolon semicolon = null;
        if (sym == TokenType.INTTK) {
            pushResult(currentToken());
            bType = new BType(currentToken().getTokenLineNumber());

            nextToken();
            varDefs.add(parseVarDef());

            while (sym == TokenType.COMMA) {
                pushResult(currentToken());
                commas.add(new Comma(currentToken().getTokenLineNumber()));
                nextToken();
                varDefs.add(parseVarDef());
            }

            if (sym == TokenType.SEMICN) {
                pushResult(currentToken());
                semicolon = new Semicolon(currentToken().getTokenLineNumber());
                nextToken();
            } else {
                semicolon = dealWithErrorI();
            }
        } else {
            throw new ParseError();
        }

        pushResult(new Token(TokenType.VarDecl, 0, "<VarDecl>"));
        return new VarDecl(bType.getLineNumber(), bType, varDefs, commas, semicolon);
    }

    public FuncType parseFuncType() throws ParseError, ParseOutOfBound {
        if (sym == TokenType.VOIDTK) {
            pushResult(currentToken());
            nextToken();
            return FuncType.VOID;
        } else if (sym == TokenType.INTTK) {
            pushResult(currentToken());
            nextToken();
            return FuncType.INT;
        } else {
            throw new ParseError();
        }

        // put FuncType into res
//        pushResult(new Token(TokenType.FuncType, 0, "<FuncType>"));
    }

    public Ident parseIdent() throws ParseOutOfBound, ParseError {
        if (sym == TokenType.IDENFR) {
            pushResult(currentToken());
            Ident ident = new Ident(currentToken().getTokenLineNumber(), currentToken().getTokenValue());
            nextToken();
            return ident;
        } else {
            throw new ParseError();
        }
    }

    public FuncFParams parseFuncFParams() throws ParseError, ParseOutOfBound {
        List<FuncFParam> funcFParams = new ArrayList<>();
        funcFParams.add(parseFuncFParam());
        List<Comma> commas = new ArrayList<>();

        while (sym == TokenType.COMMA) {
            pushResult(currentToken());
            commas.add(new Comma(currentToken().getTokenLineNumber()));
            nextToken();
            funcFParams.add(parseFuncFParam());
        }

        // put FuncFParams
        pushResult(new Token(TokenType.FuncFParams, 0, "<FuncFParams>"));
        return new FuncFParams(funcFParams.get(0).getLineNumber(), funcFParams, commas);
    }

    public Block parseBlock() throws ParseOutOfBound, ParseError {
        LeftBrace leftBrace = null;
        List<BlockItem> blockItems = new ArrayList<>();
        RightBrace rightBrace = null;
        if (sym == TokenType.LBRACE) {
            pushResult(currentToken());
            leftBrace = new LeftBrace(currentToken().getTokenLineNumber());

            nextToken();
            // strange
            while ((sym != TokenType.RBRACE) && (idx < tokens.size())) {
                blockItems.add(parseBlockItem());
            }

            if (sym == TokenType.RBRACE) {
                pushResult(currentToken());
                rightBrace = new RightBrace(currentToken().getTokenLineNumber());
                if (hasNextToken()) {
                    nextToken();
                }
            } else {
                throw new ParseError();
            }
        } else {
            throw new ParseError();
        }

        pushResult(new Token(TokenType.Block, 0, "<Block>"));
        return new Block(leftBrace.getLineNumber(), leftBrace, blockItems, rightBrace);
    }

    public ConstDef parseConstDef() throws ParseError, ParseOutOfBound {
        Ident ident = parseIdent();
        List<LeftBrack> leftBracks = new ArrayList<>();
        List<ConstExp> dimSizes = new ArrayList<>();
        List<RightBrack> rightBracks = new ArrayList<>();
        Assign assign = null;
        ConstInitVal constInitVal = null;

        while (hasConstExp()) {
            // parse [
            if (sym != TokenType.LBRACK) {
                throw new ParseError();
            }
            pushResult(currentToken());
            leftBracks.add(new LeftBrack(currentToken().getTokenLineNumber()));

            // parse ConstExp
            nextToken();
            dimSizes.add(parseConstExp());

            // parse ]
            if (sym != TokenType.RBRACK) {
                rightBracks.add(dealWithErrorK());
            } else {
                pushResult(currentToken());
                rightBracks.add(new RightBrack(currentToken().getTokenLineNumber()));
                nextToken();
            }
        }

        // parse =
        if (sym == TokenType.ASSIGN) {
            pushResult(currentToken());
            assign = new Assign(currentToken().getTokenLineNumber());

            // parse value to be assigned
            nextToken();
            constInitVal = parseConstInitVal();
        } else {
            throw new ParseError();
        }

        pushResult(new Token(TokenType.ConstDef, 0, "<ConstDef>"));
        return new ConstDef(ident.getLineNumber(), ident, leftBracks, dimSizes, rightBracks, assign, constInitVal);
    }

    public VarDef parseVarDef() throws ParseOutOfBound, ParseError {
        Ident ident = null;
        List<LeftBrack> leftBracks = new ArrayList<>();
        List<ConstExp> dimSizes = new ArrayList<>();
        List<RightBrack> rightBracks = new ArrayList<>();
        Assign assign = null;
        InitVal val = null;


        if (sym == TokenType.IDENFR) {
            pushResult(currentToken());
            ident = new Ident(currentToken().getTokenLineNumber(), currentToken().getTokenValue());

            nextToken();
            while (hasConstExp()) {
                // parse [
                if (sym != TokenType.LBRACK) {
                    throw new ParseError();
                }
                pushResult(currentToken());
                leftBracks.add(new LeftBrack(currentToken().getTokenLineNumber()));

                // parse ConstExp
                nextToken();
                dimSizes.add(parseConstExp());

                // parse ]
                if (sym != TokenType.RBRACK) {
                    rightBracks.add(dealWithErrorK());
                } else {
                    pushResult(currentToken());
                    rightBracks.add(new RightBrack(currentToken().getTokenLineNumber()));
                    nextToken();
                }
            }

            // parse =
            if (sym == TokenType.ASSIGN) {
                pushResult(currentToken());
                assign = new Assign(currentToken().getTokenLineNumber());

                // parse value to be assigned
                nextToken();
                val = parseInitVal();
            } else {
                return new VarDef(ident.getLineNumber(), ident, leftBracks, dimSizes, rightBracks);
            }
        } else {
            throw new ParseError();
        }

        pushResult(new Token(TokenType.VarDef, 0, "<VarDef>"));
        return new VarDef(ident.getLineNumber(), ident, leftBracks, dimSizes, rightBracks, assign, val);
    }

    public FuncFParam parseFuncFParam() throws ParseError, ParseOutOfBound {
        Ident ident = null;
        List<LeftBrack> leftBracks = new ArrayList<>();
        List<RightBrack> rightBracks = new ArrayList<>();
        ConstExp constExp = null;
        int dim = 0;

        if (sym == TokenType.INTTK) {
            pushResult(currentToken());

            nextToken();
            if (sym == TokenType.IDENFR) {
                pushResult(currentToken());
                ident = new Ident(currentToken().getTokenLineNumber(), currentToken().getTokenValue());

                nextToken();
                if (sym == TokenType.LBRACK) {
                    dim++;
                    pushResult(currentToken());
                    leftBracks.add(new LeftBrack(currentToken().getTokenLineNumber()));

                    nextToken();
                    if (sym == TokenType.RBRACK) {
                        pushResult(currentToken());
                        rightBracks.add(new RightBrack(currentToken().getTokenLineNumber()));
                        nextToken();
                    } else {
                        rightBracks.add(dealWithErrorK());
                    }

                    while (hasConstExp()) {
                        // parse [
                        if (sym != TokenType.LBRACK) {
                            throw new ParseError();
                        }
                        dim++;
                        pushResult(currentToken());
                        leftBracks.add(new LeftBrack(currentToken().getTokenLineNumber()));

                        // parse ConstExp
                        nextToken();
                        constExp = parseConstExp();

                        // parse ]
                        if (sym != TokenType.RBRACK) {
                            rightBracks.add(dealWithErrorK());
                        } else {
                            pushResult(currentToken());
                            rightBracks.add(new RightBrack(currentToken().getTokenLineNumber()));
                            nextToken();
                        }
                    }
                }
            }
        } else {
            throw new ParseError();
        }

        pushResult(new Token(TokenType.FuncFParam, 0, "<FuncFParam>"));
        return new FuncFParam(ident.getLineNumber(), ident, dim, leftBracks, rightBracks, constExp);
    }

    public ConstExp parseConstExp() throws ParseError, ParseOutOfBound {
        AddExp addExp = parseAddExp();

        pushResult(new Token(TokenType.ConstExp, 0, "<ConstExp>"));
        return new ConstExp(addExp.getLineNumber(), addExp);
    }

    public ConstInitVal parseConstInitVal() throws ParseOutOfBound, ParseError {
        // ident, '(', number
        // '{'
        if ((sym == TokenType.IDENFR)
                || (sym == TokenType.LPARENT)
                || (sym == TokenType.INTCON)
                || (sym == TokenType.PLUS)
                || (sym == TokenType.MINU)
                || (sym == TokenType.NOT)) {
            int num = currentToken().getTokenLineNumber();
            return new ConstInitVal(num, ConstInitValType.SIMPLE, parseConstExp());

        } else if (sym == TokenType.LBRACE) {
            int num = currentToken().getTokenLineNumber();
            pushResult(currentToken());
            LeftBrace leftBrace = new LeftBrace(currentToken().getTokenLineNumber());
            RightBrace rightBrace = null;
            List<Comma> commas = new ArrayList<>();
            List<ConstInitVal> vals = new ArrayList<>();

            nextToken();
            if (sym != TokenType.RBRACE) {
                vals.add(parseConstInitVal());

                while (sym == TokenType.COMMA) {
                    pushResult(currentToken());
                    commas.add(new Comma(currentToken().getTokenLineNumber()));

                    nextToken();
                    vals.add(parseConstInitVal());
                }

                if (sym == TokenType.RBRACE) {
                    pushResult(currentToken());
                    rightBrace = new RightBrace(currentToken().getTokenLineNumber());
                    nextToken();
                } else {
                    throw new ParseError();
                }
            } else {
                pushResult(currentToken());
                rightBrace = new RightBrace(currentToken().getTokenLineNumber());
                nextToken();
            }

            return new ConstInitVal(num, ConstInitValType.ARRAY, leftBrace, vals, commas, rightBrace);

        } else {
            throw new ParseError();
        }

        // put ConstInitVal into res
//        pushResult(new Token(TokenType.ConstInitVal, 0, "<ConstInitVal>"));
    }

    public InitVal parseInitVal() throws ParseError, ParseOutOfBound {
        LeftBrace leftBrace = null;
        RightBrace rightBrace = null;
        List<InitVal> vals = new ArrayList<>();
        List<Comma> commas = new ArrayList<>();
        Exp exp = null;

        if (sym == TokenType.LBRACE) {
            int num = currentToken().getTokenLineNumber();
            pushResult(currentToken());
            leftBrace = new LeftBrace(currentToken().getTokenLineNumber());

            nextToken();
            if (sym == TokenType.RBRACE) {
                // no initVal
                pushResult(currentToken());
                rightBrace = new RightBrace(currentToken().getTokenLineNumber());

                nextToken();
                return new InitVal(num, InitValType.ARRAY, leftBrace, vals, commas, rightBrace);
            } else {
                vals.add(parseInitVal());

                while (sym == TokenType.COMMA) {
                    pushResult(currentToken());
                    commas.add(new Comma(currentToken().getTokenLineNumber()));

                    nextToken();
                    vals.add(parseInitVal());
                }

                if (sym == TokenType.RBRACE) {
                    pushResult(currentToken());
                    rightBrace = new RightBrace(currentToken().getTokenLineNumber());
                    nextToken();
                    return new InitVal(num, InitValType.ARRAY, leftBrace, vals, commas, rightBrace);
                } else {
                    throw new ParseError();
                }
            }
        } else if ((sym == TokenType.IDENFR)
                || (sym == TokenType.LPARENT)
                || (sym == TokenType.INTCON)
                || (sym == TokenType.PLUS)
                || (sym == TokenType.MINU)
                || (sym == TokenType.NOT)) {
            int num = currentToken().getTokenLineNumber();
            exp = parseExp();
            return new InitVal(num, InitValType.SIMPLE, exp);
        } else {
            System.out.println(currentToken().getTokenLineNumber() + " " + currentToken().getTokenValue());
            throw new ParseError();
        }

        // put InitVal into res
//        pushResult(new Token(TokenType.InitVal, 0, "<InitVal>"));
    }

    public BlockItem parseBlockItem() throws ParseOutOfBound, ParseError {
        int num = currentToken().getTokenLineNumber();
        if ((sym == TokenType.CONSTTK) || (sym == TokenType.INTTK)) {
            return new BlockItem(num, BlockItemType.DECL, parseDecl());
        } else {
            // lazy
            return new BlockItem(num, BlockItemType.STMT, parseStmt());
        }
        // don't need to put Block Item into res
    }

    public AddExp parseAddExp() throws ParseOutOfBound, ParseError {
        List<MulExp> mulExps = new ArrayList<>();
        List<Operator> operators = new ArrayList<>();
        mulExps.add(parseMulExp());
        pushResult(new Token(TokenType.AddExp, 0, "<AddExp>"));

        int num = currentToken().getTokenLineNumber();
        while ((sym == TokenType.PLUS) || (sym == TokenType.MINU)) {
            pushResult(currentToken());
            if (sym == TokenType.PLUS) {
                operators.add(new Operator(currentToken().getTokenLineNumber(), OperatorType.ADD));
            } else {
                operators.add(new Operator(currentToken().getTokenLineNumber(), OperatorType.SUB));
            }

            nextToken();
            mulExps.add(parseMulExp());
            pushResult(new Token(TokenType.AddExp, 0, "<AddExp>"));
        }

        return new AddExp(num, mulExps, operators);
    }

    public Exp parseExp() throws ParseError, ParseOutOfBound {
        AddExp addExp = parseAddExp();

        // put Exp into res
        pushResult(new Token(TokenType.Exp, 0, "<Exp>"));
        return new Exp(addExp.getLineNumber(), addExp);
    }

    public MulExp parseMulExp() throws ParseError, ParseOutOfBound {
        List<UnaryExp> unaryExps = new ArrayList<>();
        List<Operator> operators = new ArrayList<>();

        unaryExps.add(parseUnaryExp());
        pushResult(new Token(TokenType.MulExp, 0, "<MulExp>"));

        while ((sym == TokenType.MULT)
                || (sym == TokenType.DIV)
                || (sym == TokenType.MOD)) {
            pushResult(currentToken());
            if (sym == TokenType.MULT) {
                operators.add(new Operator(currentToken().getTokenLineNumber(), OperatorType.MUL));
            } else if (sym == TokenType.DIV) {
                operators.add(new Operator(currentToken().getTokenLineNumber(), OperatorType.DIV));
            } else {
                operators.add(new Operator(currentToken().getTokenLineNumber(), OperatorType.MOD));
            }

            nextToken();
            unaryExps.add(parseUnaryExp());
            pushResult(new Token(TokenType.MulExp, 0, "<MulExp>"));
        }
        return new MulExp(unaryExps.get(0).getLineNumber(), unaryExps, operators);
    }

    public UnaryExp parseUnaryExp() throws ParseOutOfBound, ParseError {
        int num = currentToken().getTokenLineNumber();
        if ((sym == TokenType.LPARENT) || (sym == TokenType.INTCON)) {
            // must be primary expression
            return new UnaryExp(num, UnaryExpType.PRIMARY, parsePrimaryExp());
        } else if (sym == TokenType.IDENFR) {
            // may be primary expression or ident ( params )
            Ident ident = new Ident(currentToken().getTokenLineNumber(), currentToken().getTokenValue());
            LeftParent leftParent = null;
            RightParent rightParent = null;
            FuncRParams funcRParams = null;
            nextToken();
            if (sym == TokenType.LPARENT) {
                // put identifier into res
                lastToken();
                pushResult(currentToken());

                // put ( into res
                nextToken();
                pushResult(currentToken());
                leftParent = new LeftParent(currentToken().getTokenLineNumber());

                // parse FuncRParams
                nextToken();
                if (sym == TokenType.RPARENT) {
                    pushResult(currentToken());
                    rightParent = new RightParent(currentToken().getTokenLineNumber());

                    nextToken();
                } else {
                    funcRParams = parseFuncRParams();

                    if (sym == TokenType.RPARENT) {
                        pushResult(currentToken());
                        rightParent = new RightParent(currentToken().getTokenLineNumber());
                        nextToken();
                    } else {
                        rightParent = dealWithErrorJ();
                    }
                }
                return new UnaryExp(num, UnaryExpType.FUNCCALL, ident, leftParent, funcRParams, rightParent);
            } else {
                // primary
                lastToken();
                return new UnaryExp(num, UnaryExpType.PRIMARY, parsePrimaryExp());
            }
        } else if ((sym == TokenType.PLUS)
                || (sym == TokenType.MINU)
                || (sym == TokenType.NOT)) {
            UnaryOp op = parseUnaryOp();

            UnaryExp exp = parseUnaryExp();

            return new UnaryExp(num, UnaryExpType.OTHER, op, exp);
        } else {
            throw new ParseError();
        }
    }

    public PrimaryExp parsePrimaryExp() throws ParseError, ParseOutOfBound {
        int num = currentToken().getTokenLineNumber();
        if (sym == TokenType.LPARENT) {
            LeftParent leftParent = new LeftParent(currentToken().getTokenLineNumber());
            pushResult(currentToken());

            nextToken();
            Exp exp = parseExp();

            RightParent rightParent = null;
            if (sym == TokenType.RPARENT) {
                pushResult(currentToken());
                rightParent = new RightParent(currentToken().getTokenLineNumber());
                nextToken();
            } else {
                rightParent = dealWithErrorJ();
            }
            return new PrimaryExp(num, PrimaryExpType.EXP, leftParent, exp, rightParent);
        } else if (sym == TokenType.IDENFR) {
            LVal lVal = parseLVal();
            return new PrimaryExp(num, PrimaryExpType.LVAL, lVal);
        } else if (sym == TokenType.INTCON) {
            Num number = parseNumber();
            return new PrimaryExp(num, PrimaryExpType.NUMBER, number);
        } else {
            throw new ParseError();
        }

//        pushResult(new Token(TokenType.PrimaryExp, 0, "<PrimaryExp>"));
    }

    public FuncRParams parseFuncRParams() throws ParseOutOfBound, ParseError {
        int num = currentToken().getTokenLineNumber();
        List<Exp> exps = new ArrayList<>();
        List<Comma> commas = new ArrayList<>();
        exps.add(parseExp());

        while (sym == TokenType.COMMA) {
            pushResult(currentToken());
            commas.add(new Comma(currentToken().getTokenLineNumber()));

            nextToken();
            exps.add(parseExp());
        }

        // put FuncRParams into res
        pushResult(new Token(TokenType.FuncRParams, 0, "<FuncRParams>"));
        return new FuncRParams(num, exps, commas);
    }

    public LVal parseLVal() throws ParseOutOfBound, ParseError {
        int num = currentToken().getTokenLineNumber();
        Ident ident = parseIdent();
        List<LeftBrack> leftBracks = new ArrayList<>();
        List<RightBrack> rightBracks = new ArrayList<>();
        List<Exp> exps = new ArrayList<>();

        while (sym == TokenType.LBRACK) {
            pushResult(currentToken());
            leftBracks.add(new LeftBrack(currentToken().getTokenLineNumber()));

            nextToken();
            exps.add(parseExp());

            if (sym == TokenType.RBRACK) {
                pushResult(currentToken());
                rightBracks.add(new RightBrack(currentToken().getTokenLineNumber()));
                nextToken();
            } else {
                rightBracks.add(dealWithErrorK());
            }
        }

        // put LVal into res
//        pushResult(new Token(TokenType.LVal, 0, "<LVal>"));
        return new LVal(num, ident, leftBracks, exps, rightBracks);
    }

    public Num parseNumber() throws ParseOutOfBound, ParseError {
        int num = currentToken().getTokenLineNumber();
        IntConst intConst = parseIntConst();

        // put Number into res
        pushResult(new Token(TokenType.Number, 0, "<Number>"));
        return new Num(num, intConst);
    }

    public IntConst parseIntConst() throws ParseError, ParseOutOfBound {
        int num = currentToken().getTokenLineNumber();
        if (sym == TokenType.INTCON) {
            pushResult(currentToken());
            String value = currentToken().getTokenValue();
            nextToken();
            return new IntConst(num, value);
        } else {
            throw new ParseError();
        }
    }

    public UnaryOp parseUnaryOp() throws ParseOutOfBound, ParseError {
        int num = currentToken().getTokenLineNumber();
        if ((sym == TokenType.PLUS)
                || (sym == TokenType.MINU)
                || (sym == TokenType.NOT)) {
            pushResult(currentToken());
            UnaryOpType type = null;
            if (sym == TokenType.PLUS) {
                type = UnaryOpType.ADD;
            } else if (sym == TokenType.MINU) {
                type = UnaryOpType.SUB;
            } else {
                type = UnaryOpType.NOT;
            }

            nextToken();
            return new UnaryOp(num, type);
        } else {
            throw new ParseError();
        }
    }

    public Stmt parseStmt() throws ParseError, ParseOutOfBound {
        int num = currentToken().getTokenLineNumber();
        if (sym == TokenType.IFTK) {
            If ifToken = null;
            LeftParent leftParent = null;
            Cond cond = null;
            RightParent rightParent = null;
            Stmt stmt = null;

            pushResult(currentToken());
            ifToken = new If(currentToken().getTokenLineNumber());

            nextToken();
            if (sym == TokenType.LPARENT) {
                pushResult(currentToken());
                leftParent = new LeftParent(currentToken().getTokenLineNumber());

                nextToken();
                cond = parseCond();

                if (sym == TokenType.RPARENT) {
                    pushResult(currentToken());
                    rightParent = new RightParent(currentToken().getTokenLineNumber());

                    nextToken();
                } else {
                    rightParent = dealWithErrorJ();
                }
                stmt = parseStmt();

                if (sym == TokenType.ELSETK) {
                    pushResult(currentToken());
                    Else elseToken = new Else(currentToken().getTokenLineNumber());

                    nextToken();
                    Stmt elseStmt = parseStmt();
                    return new IfStmt(num, ifToken, leftParent, cond, rightParent, stmt, elseToken, elseStmt);
                } else {
                    return new IfStmt(num, ifToken, leftParent, cond, rightParent, stmt);
                }
            } else {
                throw new ParseError();
            }
        } else if (sym == TokenType.WHILETK) {
            While whileToken = new While(currentToken().getTokenLineNumber());
            LeftParent leftParent = null;
            Cond cond = null;
            RightParent rightParent = null;
            Stmt stmt = null;
            pushResult(currentToken());

            nextToken();
            if (sym == TokenType.LPARENT) {
                pushResult(currentToken());
                leftParent = new LeftParent(currentToken().getTokenLineNumber());

                nextToken();
                cond = parseCond();

                if (sym == TokenType.RPARENT) {
                    rightParent = new RightParent(currentToken().getTokenLineNumber());
                    pushResult(currentToken());

                    nextToken();
                } else {
                    rightParent = dealWithErrorJ();
                }
                stmt = parseStmt();
                return new WhileStmt(num, whileToken, leftParent, cond, rightParent, stmt);
            } else {
                throw new ParseError();
            }
        } else if (sym == TokenType.BREAKTK) {
            Break breakToken = new Break(currentToken().getTokenLineNumber());
            Semicolon semicolon = null;
            pushResult(currentToken());

            nextToken();
            if (sym == TokenType.SEMICN) {
                pushResult(currentToken());
                semicolon = new Semicolon(currentToken().getTokenLineNumber());
                nextToken();
                return new BreakStmt(num, breakToken, semicolon);
            } else {
                semicolon = dealWithErrorI();
                return new BreakStmt(num, breakToken, semicolon);
            }
        } else if (sym == TokenType.CONTINUETK) {
            Continue continueToken = new Continue(currentToken().getTokenLineNumber());
            Semicolon semicolon = null;
            pushResult(currentToken());

            nextToken();
            if (sym == TokenType.SEMICN) {
                semicolon = new Semicolon(currentToken().getTokenLineNumber());
                pushResult(currentToken());
                nextToken();
                return new ContinueStmt(num, continueToken, semicolon);
            } else {
                semicolon = dealWithErrorI();
                return new ContinueStmt(num, continueToken, semicolon);
            }
        } else if (sym == TokenType.RETURNTK) {
            Return returnToken = new Return(currentToken().getTokenLineNumber());
            Semicolon semicolon = null;
            Exp exp = null;
            pushResult(currentToken());

            nextToken();
            if (sym == TokenType.SEMICN) {
                pushResult(currentToken());
                semicolon = new Semicolon(currentToken().getTokenLineNumber());
                nextToken();
            } else if (sym == TokenType.RBRACE) {
                semicolon = dealWithErrorI();
            } else {
                exp = parseExp();
                if (sym == TokenType.SEMICN) {
                    pushResult(currentToken());
                    semicolon = new Semicolon(currentToken().getTokenLineNumber());
                    nextToken();
                } else {
                    semicolon = dealWithErrorI();
                }
            }
            return new ReturnStmt(num, returnToken, exp, semicolon);
        } else if (sym == TokenType.PRINTFTK) {
            Printf printToken = new Printf(currentToken().getTokenLineNumber());
            LeftParent leftParent = null;
            FormatString formatString = null;
            List<Comma> commas = new ArrayList<>();
            List<Exp> exps = new ArrayList<>();
            RightParent rightParent = null;
            Semicolon semicolon = null;

            pushResult(currentToken());

            nextToken();
            if (sym == TokenType.LPARENT) {
                leftParent = new LeftParent(currentToken().getTokenLineNumber());
                pushResult(currentToken());

                nextToken();
                formatString = parseFormatString();

                while (sym == TokenType.COMMA) {
                    commas.add(new Comma(currentToken().getTokenLineNumber()));
                    pushResult(currentToken());

                    nextToken();
                    exps.add(parseExp());
                }

                if (sym == TokenType.RPARENT) {
                    rightParent = new RightParent(currentToken().getTokenLineNumber());
                    pushResult(currentToken());

                    nextToken();
                } else {
                    rightParent = dealWithErrorJ();
                }
                if (sym == TokenType.SEMICN) {
                    semicolon = new Semicolon(currentToken().getTokenLineNumber());
                    pushResult(currentToken());
                    nextToken();
                    return new PrintfStmt(num, printToken, leftParent, formatString, commas, exps, rightParent,
                            semicolon);
                } else {
                    semicolon = dealWithErrorI();
                    return new PrintfStmt(num, printToken, leftParent, formatString, commas, exps, rightParent,
                            semicolon);
                }
            } else {
                throw new ParseError();
            }
        } else if (sym == TokenType.LBRACE) {
            Block block = parseBlock();
            return new BlockStmt(num, block);
        } else {
            // three hardest cases
            if (hasLVal()) {
                int savedIdx = idx;
                int savedSize = res.size();

                // parse LVal, but don't add it to res
                // warning!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                parseLVal();
                if (sym == TokenType.ASSIGN) {
                    nextToken();
                    if (sym == TokenType.GETINTTK) {
                        // retreat
                        idx = savedIdx - 1;
                        nextToken();
                        while (res.size() != savedSize) {
                            popResult();
                        }

                        // I'm sure we should parse getint case now
                        LVal lVal = parseLVal();
                        Assign assignToken = null;
                        Getint getint = null;
                        LeftParent leftParent = null;
                        RightParent rightParent = null;
                        Semicolon semicolon = null;
                        if (sym == TokenType.ASSIGN) {
                            pushResult(currentToken());
                            assignToken = new Assign(currentToken().getTokenLineNumber());

                            nextToken();
                            if (sym == TokenType.GETINTTK) {

                                pushResult(currentToken());
                                getint = new Getint(currentToken().getTokenLineNumber());
                                nextToken();
                                if (sym == TokenType.LPARENT) {
                                    pushResult(currentToken());
                                    leftParent = new LeftParent(currentToken().getTokenLineNumber());

                                    nextToken();
                                    if (sym == TokenType.RPARENT) {
                                        rightParent = new RightParent(currentToken().getTokenLineNumber());
                                        pushResult(currentToken());

                                        nextToken();
                                    } else {
                                        rightParent = dealWithErrorJ();
                                    }
                                    if (sym == TokenType.SEMICN) {
                                        semicolon = new Semicolon(currentToken().getTokenLineNumber());
                                        pushResult(currentToken());
                                        nextToken();
                                    } else {
                                        semicolon = dealWithErrorI();
                                    }
                                } else {
                                    throw new ParseError();
                                }
                            } else {
                                throw new ParseError();
                            }
                        } else {
                            throw new ParseError();
                        }
                        return new ReadValueStmt(num, lVal, assignToken, getint, leftParent, rightParent, semicolon);
                    } else {
                        idx = savedIdx - 1;
                        nextToken();
                        while (res.size() != savedSize) {
                            popResult();
                        }

                        // I'm sure we should parse LVal = exp;
                        LVal lVal = parseLVal();
                        Assign assign = null;
                        Semicolon semicolon = null;
                        Exp exp = null;

                        if (sym == TokenType.ASSIGN) {
                            assign = new Assign(currentToken().getTokenLineNumber());
                            pushResult(currentToken());

                            nextToken();
                            exp = parseExp();

                            if (sym == TokenType.SEMICN) {
                                semicolon = new Semicolon(currentToken().getTokenLineNumber());
                                pushResult(currentToken());
                                nextToken();
                            } else {
                                semicolon = dealWithErrorI();
                            }
                        } else {
                            throw new ParseError();
                        }
                        return new AssignValueStmt(num, lVal, assign, exp, semicolon);
                    }
                } else {
                    // return to savedIdx
                    idx = savedIdx - 1;
                    nextToken();
                    while (res.size() != savedSize) {
                        popResult();
                    }

                    // it must be [exp], and the exp is LVal
                    Semicolon semicolon = null;
                    Exp exp = null;
                    if (sym != TokenType.SEMICN) {
                        exp = parseExp();
                        if (sym == TokenType.SEMICN) {
                            pushResult(currentToken());
                            semicolon = new Semicolon(currentToken().getTokenLineNumber());
                            nextToken();
                        } else {
                            semicolon = dealWithErrorI();
                        }
                    } else {
                        semicolon = new Semicolon(currentToken().getTokenLineNumber());
                        pushResult(currentToken());
                        nextToken();
                    }
                    return new ExpStmt(num, exp, semicolon);
                }
            } else {
                // must be [exp] ;, and the exp is not LVal
                Semicolon semicolon = null;
                Exp exp = null;
                if (sym != TokenType.SEMICN) {
                    exp = parseExp();
                    if (sym == TokenType.SEMICN) {
                        pushResult(currentToken());
                        semicolon = new Semicolon(currentToken().getTokenLineNumber());
                        nextToken();
                    } else {
                        semicolon = dealWithErrorI();
                    }
                } else {
                    semicolon = new Semicolon(currentToken().getTokenLineNumber());
                    pushResult(currentToken());
                    nextToken();
                }
                return new ExpStmt(num, exp, semicolon);
            }
        }

        // put Stmt into res
//        pushResult(new Token(TokenType.Stmt, 0, "<Stmt>"));
    }

    public Cond parseCond() throws ParseOutOfBound, ParseError {
        int num = currentToken().getTokenLineNumber();
        LOrExp lOrExp = parseLOrExp();

        // put Cond into res
//        pushResult(new Token(TokenType.Cond, 0, "<Cond>"));
        return new Cond(num, lOrExp);
    }

    public LOrExp parseLOrExp() throws ParseError, ParseOutOfBound {
        int num = currentToken().getTokenLineNumber();
        List<LAndExp> lAndExps = new ArrayList<>();
        List<Operator> operators = new ArrayList<>();
        lAndExps.add(parseLAndExp());
        pushResult(new Token(TokenType.LOrExp, 0, "<LOrExp>"));

        while (sym == TokenType.OR) {
            pushResult(currentToken());
            operators.add(new Operator(currentToken().getTokenLineNumber(), OperatorType.OR));

            nextToken();
            lAndExps.add(parseLAndExp());
            pushResult(new Token(TokenType.LOrExp, 0, "<LOrExp>"));
        }

        return new LOrExp(num, lAndExps, operators);
    }

    public LAndExp parseLAndExp() throws ParseOutOfBound, ParseError {
        int num = currentToken().getTokenLineNumber();
        List<EqExp> eqExps = new ArrayList<>();
        List<Operator> operators = new ArrayList<>();
        eqExps.add(parseEqExp());
        pushResult(new Token(TokenType.LAndExp, 0, "<LAndExp>"));

        while (sym == TokenType.AND) {
            pushResult(currentToken());
            operators.add(new Operator(currentToken().getTokenLineNumber(), OperatorType.AND));

            nextToken();
            eqExps.add(parseEqExp());
            pushResult(new Token(TokenType.LAndExp, 0, "<LAndExp>"));
        }

        return new LAndExp(num, eqExps, operators);
    }

    public EqExp parseEqExp() throws ParseError, ParseOutOfBound {
        int num = currentToken().getTokenLineNumber();
        List<RelExp> relExps = new ArrayList<>();
        List<Operator> operators = new ArrayList<>();

        relExps.add(parseRelExp());
        pushResult(new Token(TokenType.EqExp, 0, "<EqExp>"));

        while ((sym == TokenType.EQL) || (sym == TokenType.NEQ)) {
            pushResult(currentToken());
            if (sym == TokenType.EQL) {
                operators.add(new Operator(currentToken().getTokenLineNumber(), OperatorType.EQ));
            } else {
                operators.add(new Operator(currentToken().getTokenLineNumber(), OperatorType.NEQ));
            }

            nextToken();
            relExps.add(parseRelExp());
            pushResult(new Token(TokenType.EqExp, 0, "<EqExp>"));
        }

        return new EqExp(num, relExps, operators);
    }

    public RelExp parseRelExp() throws ParseOutOfBound, ParseError {
        int num = currentToken().getTokenLineNumber();
        List<AddExp> addExps = new ArrayList<>();
        List<Operator> operators = new ArrayList<>();

        addExps.add(parseAddExp());
        pushResult(new Token(TokenType.RelExp, 0, "<RelExp>"));

        while ((sym == TokenType.GRE)
                || (sym == TokenType.LSS)
                || (sym == TokenType.GEQ)
                || (sym == TokenType.LEQ)) {
            pushResult(currentToken());
            if (sym == TokenType.GRE) {
                operators.add(new Operator(currentToken().getTokenLineNumber(), OperatorType.GRE));
            } else if (sym == TokenType.LSS) {
                operators.add(new Operator(currentToken().getTokenLineNumber(), OperatorType.LSS));
            } else if (sym == TokenType.GEQ) {
                operators.add(new Operator(currentToken().getTokenLineNumber(), OperatorType.GEQ));
            } else {
                operators.add(new Operator(currentToken().getTokenLineNumber(), OperatorType.LEQ));
            }

            nextToken();
            addExps.add(parseAddExp());
            pushResult(new Token(TokenType.RelExp, 0, "<RelExp>"));
        }

        return new RelExp(num, addExps, operators);
    }

    public FormatString parseFormatString() throws ParseError, ParseOutOfBound {
        if (sym == TokenType.STRCON) {
            pushResult(currentToken());
            FormatString formatString = new FormatString(currentToken().getTokenLineNumber(),
                    currentToken().getTokenValue());
            nextToken();
            return formatString;
        } else {
            throw new ParseError();
        }
    }
}