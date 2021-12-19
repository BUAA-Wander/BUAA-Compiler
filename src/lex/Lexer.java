package lex;

import error.Error;
import exception.NoMatchToken;
import token.Token;
import token.TokenType;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexer {
    private Map<Pattern, TokenType> patterns;
    private int lineNumber;
    private String line;
    private List<Error> errors;

    public Lexer() {
        patterns = new HashMap<>();
        errors = new ArrayList<>();
        String normal = "[\\s!()*+,-./\\w:;<=>?@\\[\\]^_`{|}\\\\~]";
        String format = "%d";
        patterns.put(Pattern.compile("\"((" + normal + ")|(" + format + "))*\""), TokenType.STRCON);
        patterns.put(Pattern.compile("[a-zA-Z_]\\w*"), TokenType.IDENFR);
        patterns.put(Pattern.compile("&&"), TokenType.AND);
        patterns.put(Pattern.compile("="), TokenType.ASSIGN);
        patterns.put(Pattern.compile("break"), TokenType.BREAKTK);
        patterns.put(Pattern.compile(","), TokenType.COMMA);
        patterns.put(Pattern.compile("const"), TokenType.CONSTTK);
        patterns.put(Pattern.compile("continue"), TokenType.CONTINUETK);
        patterns.put(Pattern.compile("/"), TokenType.DIV);
        patterns.put(Pattern.compile("else"), TokenType.ELSETK);
        patterns.put(Pattern.compile("=="), TokenType.EQL);
        patterns.put(Pattern.compile("getint"), TokenType.GETINTTK);
        patterns.put(Pattern.compile(">="), TokenType.GEQ);
        patterns.put(Pattern.compile(">"), TokenType.GRE);
        patterns.put(Pattern.compile("if"), TokenType.IFTK);
        patterns.put(Pattern.compile("([1-9]\\d*)|(0)"), TokenType.INTCON);
        patterns.put(Pattern.compile("int"), TokenType.INTTK);
        patterns.put(Pattern.compile("\\{"), TokenType.LBRACE);
        patterns.put(Pattern.compile("\\["), TokenType.LBRACK);
        patterns.put(Pattern.compile("\\("), TokenType.LPARENT);
        patterns.put(Pattern.compile("<="), TokenType.LEQ);
        patterns.put(Pattern.compile("<"), TokenType.LSS);
        patterns.put(Pattern.compile("main"), TokenType.MAINTK);
        patterns.put(Pattern.compile("-"), TokenType.MINU);
        patterns.put(Pattern.compile("%"), TokenType.MOD);
        patterns.put(Pattern.compile("\\*"), TokenType.MULT);
        patterns.put(Pattern.compile("!="), TokenType.NEQ);
        patterns.put(Pattern.compile("!"), TokenType.NOT);
        patterns.put(Pattern.compile("\\|\\|"), TokenType.OR);
        patterns.put(Pattern.compile("\\+"), TokenType.PLUS);
        patterns.put(Pattern.compile("printf"), TokenType.PRINTFTK);
        patterns.put(Pattern.compile("return"), TokenType.RETURNTK);
        patterns.put(Pattern.compile("}"), TokenType.RBRACE);
        patterns.put(Pattern.compile("]"), TokenType.RBRACK);
        patterns.put(Pattern.compile("\\)"), TokenType.RPARENT);
        patterns.put(Pattern.compile(";"), TokenType.SEMICN);
        patterns.put(Pattern.compile("void"), TokenType.VOIDTK);
        patterns.put(Pattern.compile("while"), TokenType.WHILETK); //TODO 3 patterns

        File file = new File("error.txt");
        if (file.exists()) {
            file.delete();
        }
    }

    public List<Error> getErrors() {
        return errors;
    }

    public List<Token> tokenization() throws NoMatchToken, IOException {
        lineNumber = 1;
        List<Token> tokens = new ArrayList<>();
        line = getLine();
        while (line.length() != 0) {
            while (true) {
                boolean flag = deleteSpace();
                flag |= deleteAnnotation();
                if (!flag) {
                    break;
                }
            }
            if (line.length() == 0) {
                break;
            }
            List<String> values = new ArrayList<>();
            boolean flag = getMatchedTokens(values);
            if (!flag) {
                tokens.add(dealWithErrorA());
            } else {
                tokens.add(chooseLongestToken(values));
            }
        }
        return tokens;
    }

    private Token dealWithErrorA() throws IOException {
        errors.add(new Error(lineNumber, "a"));
        for (int i = 1; i < line.length(); i++) {
            if (line.charAt(i) == '"') {
                line = line.substring(i + 1);
                break;
            }
        }
        return new Token(TokenType.STRCON, lineNumber, "fuck");
    }

    private boolean getMatchedTokens(List<String> values) {
        boolean flag = false;
        for (Pattern p : patterns.keySet()) {
            Matcher matcher = p.matcher(line);
            if (matcher.lookingAt()) {
                String val = line.substring(matcher.start(), matcher.end());
                values.add(val);
                flag = true;
            }
        }
        return flag;
    }

    private Token chooseLongestToken(List<String> values) throws NoMatchToken {
        int maxlen = 0;
        String val = values.get(0);
        for (String str : values) {
            if (str.length() > maxlen) {
                maxlen = str.length();
                val = str;
            }
        }
        Token res = null;
        Matcher finalMatcher = null;
        for (Pattern p : patterns.keySet()) {
            Matcher m1 = p.matcher(val);
            if (m1.matches()) {
                Matcher matcher = p.matcher(line);
                if (matcher.lookingAt()) {
                    if (res != null) {
                        if (res.getTokenType() == TokenType.IDENFR) {
                            res = new Token(patterns.get(p), lineNumber, val);
                        }
                    } else {
                        res = new Token(patterns.get(p), lineNumber, val);
                        finalMatcher = matcher;
                    }
                }
            }
        }
        if (res == null) {
            throw new NoMatchToken();
        }
        if (finalMatcher.lookingAt()) {
            line = line.substring(finalMatcher.end());
        }
        return res;
    }

    private String getLine() throws FileNotFoundException {
        File src = new File("testfile.txt");
        Scanner sc = new Scanner(src, "UTF-8");
        StringBuilder builder = new StringBuilder();
        while (sc.hasNextLine()) {
            builder.append(sc.nextLine());
            builder.append("\n");
        }
        sc.close();
        return builder.toString();
    }

    private boolean deleteSpace() {
        if (line.length() == 0) {
            return false;
        }
        boolean flag = false;
        Pattern spacePattern = Pattern.compile("\\s");
        while (line.length() != 0) {
            Matcher spaceMatcher = spacePattern.matcher(line);
            if (spaceMatcher.lookingAt()) {
                flag = true;
                if (line.charAt(0) == '\n') {
                    lineNumber++;
                }
                line = line.substring(spaceMatcher.end());
            } else {
                break;
            }
        }
        return flag;
    }

    private boolean deleteAnnotation() {
        if (line.length() == 0) {
            return false;
        }
        boolean flag = false;
        Pattern annotationPattern = Pattern.compile("(//[^\n]*)|(/\\*(\\s|.)*?\\*/)");
        Matcher annotationMatcher = annotationPattern.matcher(line);
        if (annotationMatcher.lookingAt()) {
            flag = true;
            String val = line.substring(annotationMatcher.start(), annotationMatcher.end());
            for (int i = 0; i < val.length(); i++) {
                if (val.charAt(i) == '\n') {
                    lineNumber++;
                }
            }
            line = line.substring(annotationMatcher.end());
        }
        return flag;
    }
}