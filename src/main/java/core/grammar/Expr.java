package core.grammar;

import core.Token;
import core.types.Types;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static core.TokenType.FALSE;
import static core.TokenType.TRUE;

public abstract class Expr {

    interface ExprVisitor<R> {

        R visit(BinaryExpr expr);

        R visit(UnaryExpr expr);

        R visit(LiteralExpr expr);

        R visit(VariableExpr expr);

        R visit(AssignExpr assignExpr);

        R visit(LogicalExpr logicalExpr);

        R visit(BooleanExpr booleanExpr);

        R visit(EqualityExpr equalityExpr);

        R visit(CallExpr callExpr);

    }


    public static class BinaryExpr extends Expr {
        public final Expr left;

        public final Token operator;

        public final Expr right;

        public BinaryExpr(Expr left, Token operator, Expr right) {
            this.left = left;
            this.operator = operator;
            this.right = right;
        }

        @Override
        <R> R accept(ExprVisitor<R> visitor) {
            return visitor.visit(this);
        }
    }

    public static class EqualityExpr extends Expr {
        public final Expr left;

        public final Token operator;

        public final Expr right;

        public EqualityExpr(Expr left, Token operator, Expr right) {
            this.left = left;
            this.operator = operator;
            this.right = right;
        }

        @Override
        <R> R accept(ExprVisitor<R> visitor) {
            return visitor.visit(this);
        }
    }



    public static class UnaryExpr extends Expr {

        public final Token operator;

        public final Expr right;

        public UnaryExpr(Token operator, Expr right) {
            this.operator = operator;
            this.right = right;
        }

        @Override
        <R> R accept(ExprVisitor<R> visitor) {
            return visitor.visit(this);
        }
    }

    public static class LiteralExpr extends Expr {

        public final Token token;

        public LiteralExpr(Token token) {
            this.token = token;
        }

        @Override
        <R> R accept(ExprVisitor<R> visitor) {
            return visitor.visit(this);
        }
    }


    @RequiredArgsConstructor
    public static class VariableExpr extends Expr {
        public final Token name;

        @Override
        <R> R accept(ExprVisitor<R> visitor) {
            return visitor.visit(this);
        }
    }


    @RequiredArgsConstructor
    public static class AssignExpr extends Expr {
        public final Token token;
        public final Expr right;
        @Override
        <R> R accept(ExprVisitor<R> visitor) {
            return visitor.visit(this);
        }
    }



    @RequiredArgsConstructor
    public static class LogicalExpr extends Expr {
        public final Expr left;
        public final Token operator;
        public final Expr right;
        @Override
        <R> R accept(ExprVisitor<R> visitor) {
            return visitor.visit(this);
        }
    }



    abstract <R> R accept(ExprVisitor<R> visitor);


    @RequiredArgsConstructor
    public static class BooleanExpr extends Expr {
        public final Token token;

        @Override
        <R> R accept(ExprVisitor<R> visitor) {
            return visitor.visit(this);
        }

        public Types.AgBoolean eval() {
            if ( token.type == TRUE ) {
                return new Types.AgBoolean(true);
            }
            if ( token.type == FALSE ) {
                return new Types.AgBoolean(false);
            }
            return null;
        }
    }



    @RequiredArgsConstructor
    public static class CallExpr extends Expr {

        public final Expr callee;

        public final Token rightBracket;

        public final List<Expr> arguments;

        @Override
        <R> R accept(ExprVisitor<R> visitor) {
            return visitor.visit(this);
        }

    }

}
