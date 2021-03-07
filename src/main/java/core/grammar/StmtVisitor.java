package core.grammar;

import core.functions.Return;
import core.memory.Environment;
import core.types.AgaramType;
import core.types.Types;
import lombok.AllArgsConstructor;
import lombok.val;

import java.util.List;

@AllArgsConstructor
public class StmtVisitor implements Stmt.StmtVisitor<Void> {


    public Environment environment;

    private ExprVisitor exprVisitor;

    @Override
    public Void visit(Stmt.PrintStmt stmt) {
        val expression = stmt.expr;
        AgaramType type = exprVisitor.evalExpr(expression);
        String result;
        if ( type instanceof Types.AgBoolean) {
            result = type.toString();
        }
        else {
            result = type.getValue().toString();
        }
        System.out.println(result);
        return null;
    }

    @Override
    public Void visit(Stmt.ExprStmt stmt) {
        exprVisitor.evalExpr(stmt.expr);
        return null;
    }

    @Override
    public Void visit(Stmt.VarStmt stmt) {
        if ( stmt.expr == null ) {
            environment.define(stmt.variableName.value, null);
        }
        else {
            environment.define(stmt.variableName.value, exprVisitor.evalExpr(stmt.expr));
        }
        return null;
    }

    @Override
    public Void visit(Stmt.BlockStmt blockStmt) {
        executeBlock(blockStmt.statements, new Environment(environment));
        
        return null;
    }

    @Override
    public Void visit(Stmt.IfStmt ifStmt) {

        val value = (Types.AgBoolean) exprVisitor.evalExpr(ifStmt.condition);

        if ( value.result ) {
            execute(ifStmt.thenBranch);
        }
        else {
            execute(ifStmt.elseBranch);
        }

        return null;
    }

    @Override
    public Void visit(Stmt.WhileStmt whileStmt) {
        Types.AgBoolean value = (Types.AgBoolean) exprVisitor.evalExpr(whileStmt.condition);
        while ( value.result ) {
            execute(whileStmt.thenBranch);
            // re compute the expr.
            value = (Types.AgBoolean) exprVisitor.evalExpr(whileStmt.condition);
        }
        return null;
    }

    @Override
    public Void visit(Stmt.FunctionStmt stmt) {
        Types.AgFunction function = new Types.AgFunction(stmt);
        environment.define(stmt.name.value, function);
        return null;
    }

    @Override
    public Void visit(Stmt.ReturnStmt returnStmt) {
        if ( returnStmt.expr != null ) {
            throw new Return(this.exprVisitor.evalExpr(returnStmt.expr));
        }
        else {
            throw new Return(null);
        }
    }

    public void executeBlock(List<Stmt> statements, Environment environment) {
        Environment previous = this.environment;
            this.environment = environment;
            exprVisitor.setEnvironment(environment);
            for (Stmt statement : statements) {
                execute(statement);
            }

            this.environment = previous;
            exprVisitor.setEnvironment(previous);
    }


    public void execute(Stmt stmt) {
        stmt.accept(this);
    }
}
