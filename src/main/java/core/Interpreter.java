package core;

import core.grammar.ExprVisitor;
import core.grammar.Stmt;
import core.grammar.StmtVisitor;
import core.memory.Environment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Interpreter {

    public static final Environment environment = new Environment();

    public static void main(String[] args) throws IOException {
        if (args.length > 1) {
            System.out.println("அகரம் : script_file_name.அ");
            System.exit(64);
        } else if (args.length == 1) {
            runFile(args[0]);
        } else {
            runPrompt();
        }
    }

    private static void runFile(String path) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(path));
        run(new String(bytes, Charset.defaultCharset()));
    }

    private static void run(String s) {
        interpret(s);
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
