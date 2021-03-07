package core.grammar;

import core.TokenType;
import core.errors.EvalError;
import core.memory.Environment;
import core.types.AgaramType;
import core.types.Types;
import lombok.AllArgsConstructor;
import lombok.Setter;
import lombok.val;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class ExprVisitor implements Expr.ExprVisitor<AgaramType> {


    @Setter
    private StmtVisitor stmtVisitor = null;

    @Setter
    private Environment environment;

    @Override
    public AgaramType visit(Expr.BinaryExpr expr) {

        Object leftVal = evalExpr(expr.left);
        Object rightVal = evalExpr(expr.right);
        TokenType type = expr.operator.type;


        if ( ! leftVal.getClass().equals(rightVal.getClass())) {
            throw new EvalError( "பிழை", expr.operator );
        }


        if ( leftVal instanceof Types.AgString &&  rightVal instanceof Types.AgString ){
            return new Types.AgString(((Types.AgString) leftVal).getValue().toString() + ((Types.AgString) rightVal).getValue().toString() );
        }

        Double left = (Double) evalExpr(expr.left).getValue();
        Double right  = (Double) evalExpr(expr.right).getValue();

        if ( type == TokenType.PLUS) {
            return new Types.AgDouble(left + right);
        }
        else if ( type == TokenType.MINUS) {
            return new Types.AgDouble(left - right);
        }
        else if ( type == TokenType.STAR ) {
            return new Types.AgDouble(left * right);
        }
        else if (type == TokenType.SLASH ) {
            return new Types.AgDouble(left / right);
        }
        else if ( type == TokenType.MODULO ) {
            return new Types.AgDouble(left % right);
        }


        return null;
    }

    @Override
    public AgaramType visit(Expr.UnaryExpr expr) {
        TokenType type = expr.operator.type;
        Expr right = expr.right;
        if (type == TokenType.MINUS) {
            return new Types.AgDouble(-((Double)evalExpr(right).getValue()));
        }
        return null;
    }

    public AgaramType evalExpr(Expr expr) {
        return expr.accept(this);
    }

    @Override
    public AgaramType visit(Expr.LiteralExpr expr) {
        if (expr.token.type == TokenType.NUMBER) {
            return new Types.AgDouble(Double.parseDouble(expr.token.value));
        }
        return new Types.AgString(expr.token.value);
    }

    @Override
    public AgaramType visit(Expr.VariableExpr expr) {

        return typeConvert( environment.getValue(expr.name.value) );
    }

    private AgaramType typeConvert(Object value) {
        if ( value instanceof Types.AgString ) {
            return (Types.AgString) value;
        }
        else if ( value instanceof Types.AgBoolean){
            return (Types.AgBoolean) value;
        }
        else if (value instanceof Types.AgDouble){
            return (Types.AgDouble) value;
        }
        else if ( value instanceof  Types.AgFunction ) {
            return (Types.AgFunction) value;
        }
        return null;
    }

    @Override
    public AgaramType visit(Expr.AssignExpr assignExpr) {
        String variableName = assignExpr.token.value;
        Object value = evalExpr(assignExpr.right);
        environment.assign(variableName, value);
        return null;
    }

    @Override
    public AgaramType visit(Expr.LogicalExpr logicalExpr) {
        val left = (Types.AgBoolean)evalExpr(logicalExpr.left);
        val right = (Types.AgBoolean) evalExpr(logicalExpr.right);
        if ( logicalExpr.operator.type == TokenType.LOGICAL_OR ) {
            return new Types.AgBoolean( left.result || right.result);
        }
        else if (logicalExpr.operator.type == TokenType.LOGICAL_AND) {
            return new Types.AgBoolean( left.result && right.result);
        }
        return null;
    }

    @Override
    public AgaramType visit(Expr.BooleanExpr booleanExpr) {
        return booleanExpr.eval();
    }

    @Override
    public AgaramType visit(Expr.EqualityExpr equalityExpr) {

        /**
         * When boolean is made to compare with another boolean,
         * process it differently, to perform equal operation we need to know
         * right / left is of same type, do the check and throw exception here.
         */
        Object leftVal = evalExpr(equalityExpr.left);
        Object rightVal = evalExpr(equalityExpr.right);
        TokenType type = equalityExpr.operator.type;


        if ( ! leftVal.getClass().equals(rightVal.getClass())) {
            throw new EvalError( "பிழை", equalityExpr.operator );
        }


        /**
         * If they belong to same type then perform operations
         */
        if ( leftVal instanceof Types.AgBoolean && rightVal instanceof Types.AgBoolean ) {
            if (type == TokenType.EQUAL_EQUAL) {
                return new Types.AgBoolean(((Types.AgBoolean) leftVal).getValue() == ((Types.AgBoolean) rightVal).getValue());
            }
            else {
                throw new EvalError( "பிழை", equalityExpr.operator );
            }
        }



        Double left = (Double) evalExpr(equalityExpr.left).getValue();
        Double right  = (Double) evalExpr(equalityExpr.right).getValue();


        if (type == TokenType.LESSER) {
            return new Types.AgBoolean(left < right);
        }
        else if (type == TokenType.LESSER_OR_EQUAL_TO) {
            return new Types.AgBoolean(left <= right);
        }
        else if (type == TokenType.GREATER) {
            return new Types.AgBoolean(left > right);
        }
        else if (type == TokenType.GREATER_OR_EQUAL_TO) {
            boolean result  =  ( left >= right );
            return new Types.AgBoolean(result);
        }
        else if ( type == TokenType.EQUAL_EQUAL) {
            boolean result = left.equals(right);
            return new Types.AgBoolean(result);
        }
        return null;
    }

    @Override
    public AgaramType visit(Expr.CallExpr expr) {
        Object callee = evalExpr(expr.callee);

        List<AgaramType> arguments = new ArrayList<>();
        for (Expr argument : expr.arguments) {
            arguments.add(evalExpr(argument));
        }
        // @todo: Add tamil exception message here.
        if (!(callee instanceof Types.AgFunction)) {
            throw new EvalError("", expr.rightBracket);
        }
        Types.AgFunction function = (Types.AgFunction) callee;
        return (AgaramType) function.call(this.stmtVisitor, arguments);
    }


}
