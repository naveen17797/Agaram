package core;

import core.grammar.ExprVisitor;
import core.grammar.Stmt;
import core.grammar.StmtVisitor;
import core.memory.Environment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class Interpreter {

    public static final Environment environment = new Environment();

    public static void main(String[] args) throws IOException {
            runPrompt();
    }


    private static void runPrompt() throws IOException {
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);
        System.out.println("--- அகரம் v 0.0.1 ---");
        System.out.println("--- மென்பொருள் எழுத்தாளர்: நவீன் ---");
        for (;;) {
            System.out.print("அகரம் v0.0.1 > ");
            String line = reader.readLine();
            if ( line.equals("நிறுத்து;") ) {
                break;
            }
            Interpreter.interpret(line);
        }
    }



    public static void interpret( String code) {
        try {
            Tokenizer tokenizer = new Tokenizer(code);
            interpretTokens(tokenizer.getTokens());
        }
        catch (Exception e) {
            System.out.println(e.getMessage() + "\n");
        }
    }

    private static void interpretTokens(List<Token> tokens) {
        Parser parser = new Parser(tokens);
        try {
            List<Stmt> stmts = parser.parse();

            ExprVisitor exprVisitor = new ExprVisitor(null, environment);
            StmtVisitor visitor = new StmtVisitor(environment, exprVisitor);
            exprVisitor.setStmtVisitor(visitor);

            for ( Stmt stmt: stmts ) {
                visitor.execute(stmt);
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage() + "\n");
            //System.exit(1);
        }


    }




}
