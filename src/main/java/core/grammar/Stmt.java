package core.grammar;

import core.Token;
import lombok.RequiredArgsConstructor;

import java.util.List;

public abstract class Stmt {

    interface StmtVisitor<R> {

        R visit(PrintStmt stmt);

        R visit(ExprStmt stmt);

        R visit(VarStmt stmt);

        R visit(BlockStmt blockStmt);

        R visit(IfStmt ifStmt);

        R visit(WhileStmt whileStmt);

        R visit(FunctionStmt functionStmt);

        R visit(ReturnStmt returnStmt);
    }


    @RequiredArgsConstructor
    public static class PrintStmt extends Stmt {

        public final Expr expr;

        @Override
        <R> R accept(StmtVisitor<R> visitor) {
            return visitor.visit(this);
        }
    }



    @RequiredArgsConstructor
    public static class ExprStmt extends Stmt {
        public final Expr expr;
        @Override
        <R> R accept(StmtVisitor<R> visitor) {
            return visitor.visit(this);
        }
    }

    @RequiredArgsConstructor
    public static class VarStmt extends Stmt {
        public final Token variableName;
        public final Expr expr;
        @Override
        <R> R accept(StmtVisitor<R> visitor) {
            return visitor.visit(this);
        }
    }

    @RequiredArgsConstructor
    public static class BlockStmt extends Stmt {
        public final List<Stmt> statements;
        @Override
        <R> R accept(StmtVisitor<R> visitor) {
            return visitor.visit(this);
        }
    }



    @RequiredArgsConstructor
    public static class IfStmt extends Stmt {

        public final Expr condition;

        public final Stmt thenBranch;

        public final Stmt elseBranch;
        @Override
        <R> R accept(StmtVisitor<R> visitor) {
            return visitor.visit(this);
        }
    }


    @RequiredArgsConstructor
    public static class WhileStmt extends Stmt {

        public final Expr condition;

        public final Stmt thenBranch;
        
        @Override
        <R> R accept(StmtVisitor<R> visitor) {
            return visitor.visit(this);
        }
    }


    @RequiredArgsConstructor
    public static class FunctionStmt extends Stmt {

        public final Token name;
        public final List<Token> parameters;
        public final List<Stmt> body;

        @Override
        <R> R accept(StmtVisitor<R> visitor) {
            return visitor.visit(this);
        }
    }



    @RequiredArgsConstructor
    public static class ReturnStmt extends Stmt {

        public final Token token;

        public final Expr expr;

        @Override
        <R> R accept(StmtVisitor<R> visitor) {
            return visitor.visit(this);
        }

    }




    abstract <R> R accept(StmtVisitor<R> visitor);
}
