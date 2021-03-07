package core;


import com.google.inject.Inject;
import core.grammar.Expr;
import core.grammar.Stmt;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

import static core.TokenType.*;

public class Parser extends Iterator<Token> {

    @Inject
    private ErrorTable errorTable;


    public Parser(List<Token> tokens) {
        super(tokens);
    }


    public List<Stmt> parse() throws Exception {

        List<Stmt> stmts = new Stack<Stmt>();

        while (notAtEnd()) {
            Stmt stmt = declaration();
            stmts.add(stmt);
        }

        return stmts;
    }


    private Stmt declaration() throws Exception {
        if ( match(TokenType.VAR)){
            return varDeclaration();
        }
        if ( match(TokenType.FUNCTION) ) {
            return functionDeclaration();
        }
        return statement();
    }

    private Stmt functionDeclaration() throws Exception {
        Token name = consume("ஒரு செயல்பாட்டு பெயர் இருக்க வேண்டும்", IDENTIFIER);
        consume("செயல்பாட்டு அறிவிப்பு என்றால் திறந்த அடைப்புக்குறி `(` இருக்க வேண்டும்", OPEN_BRACKET);
        List<Token> parameters = new ArrayList<>();
        if (!check(CLOSE_BRACKET)) {
            do {
                if (parameters.size() >= 255) {
                    throw new Exception("255 க்கும் மேற்பட்ட வாதங்களைக் கொண்டிருக்க முடியாது.");
                }
                parameters.add(
                        consume("Expect parameter name.", IDENTIFIER));
            } while (match(COMMA));
        }
        consume("செயல்பாட்டு அறிவிப்புக்கு ஒரு மூடிய அடைப்புக்குறி `)` இருக்க வேண்டும்", CLOSE_BRACKET);
        consume("", LEFT_BRACE);
        List<Stmt> body = block();
        return new Stmt.FunctionStmt(name, parameters, body);

    }

    private Stmt varDeclaration() throws Exception {
        Token token = consume("", TokenType.IDENTIFIER);
        Expr init = null;
        if ( match(TokenType.EQUAL)) {
            init = expression();
        }
        consume("மாறி அறிவிப்பின் முடிவில் ஒரு அரைப்புள்ளி இருக்க வேண்டும் `;`", SEMICOLON);

        return new Stmt.VarStmt(token, init);
    }


    private Stmt statement() throws Exception {
        if ( match(TokenType.PRINT) ) {
            return printStatement();
        }

        if ( match(RETURN)) {
            Token token = prev();
            Expr expr = null;
            if ( ! check(SEMICOLON) ){
                expr = expression();
            }
            return new Stmt.ReturnStmt(token, expr);
        }


        if ( match(IF) ) {
            consume("ஒருவேளை லூப் அறிவிப்பு என்றால் திறந்த அடைப்புக்குறி `(` இருக்க வேண்டும்", OPEN_BRACKET);
            Expr condition = expression();
            consume("ஒருவேளை லூப் அறிவிப்புக்கு ஒரு மூடிய அடைப்புக்குறி `)` இருக்க வேண்டும்", CLOSE_BRACKET);
            Stmt thenBranch = statement();
            Stmt elseBranch = null;
            if ( match(ELSE) ) {
                 elseBranch = statement();
            }
            return new Stmt.IfStmt(condition, thenBranch, elseBranch);
        }
        if ( match(WHILE) ) {
            consume("இருப்பின் லூப் அறிவிப்பு என்றால் திறந்த அடைப்புக்குறி `(` இருக்க வேண்டும்", OPEN_BRACKET);
            Expr condition = expression();
            consume("இருப்பின் லூப் அறிவிப்புக்கு ஒரு மூடிய அடைப்புக்குறி `)` இருக்க வேண்டும்", CLOSE_BRACKET);
            Stmt thenBranch = statement();
            return new Stmt.WhileStmt(condition, thenBranch);
        }
        if ( match(FOR) ) {
            return forStmt();
        }
        if (match(LEFT_BRACE)) {
            return new Stmt.BlockStmt(block());
        }
        else {
            return expressionStatement();
        }
    }

    private Stmt forStmt() throws Exception {
        consume("ஒருவேளை லூப் அறிவிப்பு என்றால் திறந்த அடைப்புக்குறி `(` இருக்க வேண்டும்", OPEN_BRACKET);
        Stmt initializer;
        if (match(SEMICOLON)) {
            initializer = null;
        } else if (match(VAR)) {
            initializer = varDeclaration();
        } else {
            initializer = expressionStatement();
        }

        Expr condition = null;
        if (!check(SEMICOLON)) {
            condition = expression();
        }
        consume("மாறி அறிவிப்பின் முடிவில் ஒரு அரைப்புள்ளி இருக்க வேண்டும் `;`", SEMICOLON);

        Expr increment = null;
        if (!check(CLOSE_BRACKET)) {
            increment = expression();
        }
        consume("இருப்பின் லூப் அறிவிப்புக்கு ஒரு மூடிய அடைப்புக்குறி `)` இருக்க வேண்டும்", CLOSE_BRACKET);

        Stmt body = statement();

        if (increment != null) {
            body = new Stmt.BlockStmt(
                    Arrays.asList(
                            body,
                            new Stmt.ExprStmt(increment)));
        }

        if (condition == null) {
            Token token = new Token();
            token.type = TRUE;
            condition = new Expr.BooleanExpr(token);
        }
        body = new Stmt.WhileStmt(condition, body);

        if (initializer != null) {
            body = new Stmt.BlockStmt(Arrays.asList(initializer, body));
        }


        return body;
    }

    private List<Stmt> block() throws Exception {
        List<Stmt> statements = new ArrayList<>();
        while (!check(RIGHT_BRACE) && notAtEnd()) {
            statements.add(declaration());
        }
        consume("", RIGHT_BRACE);
        return statements;
    }

    private Stmt expressionStatement() throws Exception {
        Expr expr = expression();
        consume("கூற்று முடிவில் ஒரு அரைப்புள்ளி இருக்க வேண்டும்`;`", SEMICOLON);
        return new Stmt.ExprStmt(expr);
    }


    private Stmt printStatement() throws Exception {
        Expr expr = expression();
        consume("எழுது கூற்று முடிவில் ஒரு அரைப்புள்ளி இருக்க வேண்டும்`;`", SEMICOLON);
        return new Stmt.PrintStmt(expr);
    }


    public Expr expression() throws Exception {

            return assignment();

    }


    public Expr assignment() throws Exception {
        Expr expr = or();
        if ( match(EQUAL) ) {
            // this is an assignment, we should not eval the left hand val.
            Expr value = assignment();
            if (expr instanceof Expr.VariableExpr) {
                Token name = ((Expr.VariableExpr) expr).name;
                return new Expr.AssignExpr(name, value);
            }
            throw new Exception("Invalid assignment target ");
        }
        return expr;
    }

    private Expr or() throws Exception {
        Expr expr = and();
        while ( match(LOGICAL_OR) ) {
            expr = new Expr.LogicalExpr(expr, prev(), and());
        }
        return expr;
    }

    private Expr and() throws Exception {
        Expr expr = equality();
        while ( match(LOGICAL_AND) ) {
            expr = new Expr.LogicalExpr(expr, prev(), and());

        }
        return expr;
    }


    private boolean match(TokenType... types) {
        for (TokenType type : types) {
            if (check(type)) {
                advance();
                return true;
            }
        }

        return false;
    }


    private boolean check( TokenType type ) {
        if ( ! notAtEnd() ) {
            return false;
        }
        return peek().type == type;
    }




    private Token consume(String errorMessage, TokenType type) throws Exception {
       if (check(type) ) {
            advance();
            return prev();
       }

       Token token = getCurrentItem();
       if (! errorMessage.isEmpty() ) {
           throw new Exception(errorMessage + " \n வரி எண் :  " + token.lineNumber);
       }
       else {
           throw new Exception("எதிர்பாராத டோக்கன் `" + token.value + "` \n வரி எண் :  " + token.lineNumber);
       }
    }


    private Expr factor() throws Exception {
        Expr expr = term();
        while (  match(STAR, SLASH, MODULO) ) {
            expr = new Expr.BinaryExpr(expr, prev(), unary());
        }
        return expr;
    }

    private Expr term() throws Exception {
        Expr expr = unary();
        while ( match(TokenType.PLUS, TokenType.MINUS)) {
            expr = new Expr.BinaryExpr(expr, prev(), unary());
        }
        return expr;
    }

    @Override
    boolean notAtEnd() {
        return peek().type != TokenType.EOF;
    }

    private Expr unary() throws Exception {
        if ( match(MINUS) ) {
            return new Expr.UnaryExpr(prev(), unary());
        }
        return call();
    }

    private Expr call() throws Exception {
        Expr expr = primary();
        while (true) {
            if (match(OPEN_BRACKET)) {
                expr = finishCall(expr);
            } else {
                break;
            }
        }

        return expr;

    }

    private Expr finishCall(Expr callee) throws Exception {
        List<Expr> arguments = new ArrayList<>();
        if (!check(CLOSE_BRACKET)) {
            do {
                arguments.add(expression());
                if (arguments.size() >= 255) {
                   throw new Exception("255 க்கும் மேற்பட்ட வாதங்களைக் கொண்டிருக்க முடியாது.");
                }
            } while (match(COMMA));
        }

        Token paren = consume("ஒரு மூடிய அடைப்புக்குறி `)` இருக்க வேண்டும் ", CLOSE_BRACKET);

        return new Expr.CallExpr(callee, paren, arguments);
    }

    public Expr equality() throws Exception {
        Expr expr = factor();
        while ( match(GREATER, GREATER_OR_EQUAL_TO, LESSER, LESSER_OR_EQUAL_TO, EQUAL_EQUAL)) {
            expr = new Expr.EqualityExpr(expr, prev(), factor());
        }
        return expr;
    }

    private Expr primary() throws Exception {
        if (match(TRUE, FALSE)) {
            return new Expr.BooleanExpr(prev());
        }
        if (match(NUMBER, STRING)) {
            return new Expr.LiteralExpr(prev());
        }
        if ( match(OPEN_BRACKET) ) {
            Expr expr = expression();
            consume("", CLOSE_BRACKET);
            return expr;
        }
        if ( match(IDENTIFIER) ) {
            return new Expr.VariableExpr(prev());
        }
        return null;
    }



}
