package tests;

import core.Interpreter;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.Assert.assertEquals;

public class InterpreterTest {


    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    public void interpretPrintExpression() {
        ByteArrayOutputStream baos = getByteOutputStream();
        Interpreter.interpret("எழுது '123';");
        assertEquals("123", getWithoutLines(baos));
    }


    private String getWithoutLines(ByteArrayOutputStream baos) {
        return baos.toString().replaceAll("\n", "");
    }

    @Test
    public void interpretAdditionExpr() {
        ByteArrayOutputStream baos = getByteOutputStream();
        Interpreter.interpret("எழுது 1 + 2;");
        assertEquals("3.0",getWithoutLines(baos));
    }


    @Test
    public void interpretSubtractionExpr() {
        ByteArrayOutputStream baos = getByteOutputStream();
        Interpreter.interpret("எழுது 1 - 2;");
        assertEquals("-1.0",getWithoutLines(baos));
    }

    @Test
    public void interpretMulExpr() {
        ByteArrayOutputStream baos = getByteOutputStream();
        Interpreter.interpret("எழுது 1 * 2;");
        assertEquals("2.0",getWithoutLines(baos));
    }


    @Test
    public void interpretDivExpr() {
        ByteArrayOutputStream baos = getByteOutputStream();
        Interpreter.interpret("எழுது 4/2;");
        assertEquals("2.0",getWithoutLines(baos));
    }


    @Test
    public void interpretGroupedAddExpr() {
        ByteArrayOutputStream baos = getByteOutputStream();
        Interpreter.interpret("எழுது (1 + 2) + (3-4);");
        assertEquals("2.0",getWithoutLines(baos));
    }

    @Test
    public void interpretGroupedMulExpr() {
        ByteArrayOutputStream baos = getByteOutputStream();
        Interpreter.interpret("எழுது (1 * 2) + (4 / 2);");
        assertEquals("4.0",getWithoutLines(baos));
    }


    @Test
    public void interpretModuloExpr() {
        ByteArrayOutputStream baos = getByteOutputStream();
        Interpreter.interpret("எழுது 10 % 9;");
        assertEquals("1.0",getWithoutLines(baos));
    }


    @Test
    public void interpretUnaryExpr() {
        ByteArrayOutputStream baos = getByteOutputStream();
        Interpreter.interpret("எழுது -10;");
        assertEquals("-10.0",getWithoutLines(baos));
    }



    @Test
    public void interpretGreaterThanSign() {
        ByteArrayOutputStream baos = getByteOutputStream();
        Interpreter.interpret("எழுது 10 > 20;");
        assertEquals("பொய்",getWithoutLines(baos));
    }


    @Test
    public void interpretGreaterThanEqualToSign() {
        ByteArrayOutputStream baos = getByteOutputStream();
        Interpreter.interpret("எழுது 10 >= 10;");
        assertEquals("உண்மை",getWithoutLines(baos));
    }


    @Test
    public void interpretLesserThanSign() {
        ByteArrayOutputStream baos = getByteOutputStream();
        Interpreter.interpret("எழுது 10 < 20;");
        assertEquals("உண்மை",getWithoutLines(baos));
    }

    @Test
    public void interpretLesserThanEqualToSign() {
        ByteArrayOutputStream baos = getByteOutputStream();
        Interpreter.interpret("எழுது 10 <= 10;");
        assertEquals("உண்மை",getWithoutLines(baos));
    }



    @Test
    public void interpretVariableAssignmentShouldWorkCorrectly() {
        ByteArrayOutputStream baos = getByteOutputStream();
        Interpreter.interpret("மாறி எ = 10; எழுது எ;");
        assertEquals("10.0",getWithoutLines(baos));
    }


    @Test
    public void interpretVariableAssignmentShouldWorkCorrectlyForString() {
        ByteArrayOutputStream baos = getByteOutputStream();
        Interpreter.interpret("மாறி எ = 10; எழுது எ;");
        assertEquals("10.0",getWithoutLines(baos));
    }


    @Test
    public void interpretVariableReassignShouldWorkCorrectly() {
        ByteArrayOutputStream baos = getByteOutputStream();
        Interpreter.interpret("மாறி எ = '10';" +
                "மாறி அ;" +
                "அ = எ;" +
                "எழுது அ;");
        assertEquals("10",getWithoutLines(baos));
    }



    @Test
    public void interpretVariableReassignInScopeShouldWorkCorrectly() {
        ByteArrayOutputStream baos = getByteOutputStream();
        Interpreter.interpret("மாறி எ = '10';" +
                "{ மாறி எ; எ = '30'; }" +
                "எழுது எ;");
        assertEquals("10",getWithoutLines(baos));

    }


    @Test
    public void interpretIfLoopCorrectly() {
        ByteArrayOutputStream baos = getByteOutputStream();
        Interpreter.interpret("மாறி எ = 10;"  + "ஒருவேளை ( எ == 10) {  எழுது எ; }");
        assertEquals("10.0",getWithoutLines(baos));
    }


    @Test
    public void interpretIfElseLoopCorrectly() {
        ByteArrayOutputStream baos = getByteOutputStream();
        Interpreter.interpret("மாறி எ = 10;"  + "ஒருவேளை ( எ == 11) {  எழுது எ; } இல்லையென்றால் {  எழுது எ; } ");
        assertEquals("10.0",getWithoutLines(baos));
    }


    @Test
    public void interpretOrOperatorWorkingCorrectly() {
        ByteArrayOutputStream baos = getByteOutputStream();
        Interpreter.interpret("மாறி எ = 10;"  + "ஒருவேளை ( எ == 11 || எ == 10 ) {  எழுது எ; } ");
        assertEquals("10.0",getWithoutLines(baos));
    }

    @Test
    public void interpretOrOperatorKeywordWorkingCorrectly() {
        ByteArrayOutputStream baos = getByteOutputStream();
        Interpreter.interpret("மாறி எ = 10;"  + "ஒருவேளை ( எ == 11 அல்லது எ == 10 ) {  எழுது எ; } ");
        assertEquals("10.0",getWithoutLines(baos));
    }


    @Test
    public void interpretAndOperatorWorkingCorrectly() {
        ByteArrayOutputStream baos = getByteOutputStream();
        Interpreter.interpret("மாறி எ = 10;"  + "ஒருவேளை ( எ < 11 && எ == 10 ) {  எழுது எ; } ");
        assertEquals("10.0",getWithoutLines(baos));
    }


    @Test
    public void interpretAndOperatorKeywordWorkingCorrectly() {
        ByteArrayOutputStream baos = getByteOutputStream();
        Interpreter.interpret("மாறி எ = 10;"  + "ஒருவேளை ( எ < 11 மற்றும் எ == 10 ) {  எழுது எ; } ");
        assertEquals("10.0",getWithoutLines(baos));
    }


    @Test
    public void interpretWhileLoopCorrectly() {
        ByteArrayOutputStream baos = getByteOutputStream();
        Interpreter.interpret("மாறி எ = 10;"  + "இருப்பின்(எ > 0) { எழுது எ; எ = எ - 1; }");
        assertEquals("10.09.08.07.06.05.04.03.02.01.0",getWithoutLines(baos));
    }


    @Test
    public void interpretForLoopCorrectly() {
        ByteArrayOutputStream baos = getByteOutputStream();
        Interpreter.interpret( "ஆக(மாறி எ = 1; எ < 5; எ = எ + 1) { எழுது எ; }");
        assertEquals("1.02.03.04.0",getWithoutLines(baos));
    }



    @Test
    public void interpretDecimalCorrectly() {
        ByteArrayOutputStream baos = getByteOutputStream();
        Interpreter.interpret("மாறி எ = 10.01;"  + "எ = எ + 0.01; எழுது எ;");
        assertEquals("10.02",getWithoutLines(baos));
    }


    @Test
    public void interpretTrueKeywordWorkingCorrectly() {
        ByteArrayOutputStream baos = getByteOutputStream();
        Interpreter.interpret("மாறி எ = 10;"  + "ஒருவேளை ( எ == 10 == உண்மை ) {  எழுது எ; } ");
        assertEquals("10.0",getWithoutLines(baos));
    }

    @Test
    public void interpretFalseKeywordWorkingCorrectly() {
        ByteArrayOutputStream baos = getByteOutputStream();
        Interpreter.interpret("மாறி எ = 10;"  + "ஒருவேளை ( எ == 11 == பொய் ) {  எழுது எ; } ");
        assertEquals("10.0",getWithoutLines(baos));
    }



    @Test
    public void interpretFunctionShouldWorkCorrectly() {
        ByteArrayOutputStream baos = getByteOutputStream();
        Interpreter.interpret("செயல்பாடு  கூட்டு( அ, ஆ ) {  எழுது அ + ஆ; }    கூட்டு(1,2);  ");
        assertEquals("3.0",getWithoutLines(baos));
    }

    @Test
    public void interpretFunctionShouldReturnCorrectly() {
        ByteArrayOutputStream baos = getByteOutputStream();
        Interpreter.interpret("செயல்பாடு  கூட்டு( அ, ஆ ) {  திருப்பு அ + ஆ; }    எழுது கூட்டு(1,2);  ");
        assertEquals("3.0",getWithoutLines(baos));
    }

    @Test
    public void interpretSingleLineCommentCorrectly() {
        ByteArrayOutputStream baos = getByteOutputStream();
        Interpreter.interpret("// anything here should be ignored for single line ");
        assertEquals("",getWithoutLines(baos));
    }

    @Test
    public void interpretSingleLineCommentCorrectlyWithPrintStatement() {
        ByteArrayOutputStream baos = getByteOutputStream();
        Interpreter.interpret("// anything here should be ignored for single line \n " +
                "எழுது '123';");
        assertEquals("123",getWithoutLines(baos));
    }


    @Test
    public void interpretStringConcatCorrectly() {
        ByteArrayOutputStream baos = getByteOutputStream();
        Interpreter.interpret("எழுது '123' + '45';");
        assertEquals("12345",getWithoutLines(baos));
    }



    private ByteArrayOutputStream getByteOutputStream() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        System.setOut(new PrintStream(baos));
        return baos;
    }



}