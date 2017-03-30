package tests;

import com.rafael.CashRegister;
import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 * Created by Rafael on 3/29/17.
 */
public class TakeCommandTest extends TestCase {
    protected CashRegister register;

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    @Before
    public void setUp() {
        System.setOut(new PrintStream(outContent));
        register = new CashRegister();
    }

    @After
    public void cleanUpStreams() {
        register = null;
        System.setOut(null);
    }

    @Test
    public void testTakeNoArguments() {
        outContent.reset();
        register.executeCommand("take");
        assertEquals("invalid command\n", outContent.toString());
    }

    @Test
    public void testTakeIncorrectTypeOfArguments() {
        outContent.reset();
        register.executeCommand("take 1 a 3 4 5");
        assertEquals("invalid command\n", outContent.toString());

        outContent.reset();
        register.executeCommand("take 1 2 3 $ 5");
        assertEquals("invalid command\n", outContent.toString());

        outContent.reset();
        register.executeCommand("take 1 2 3 4 %");
        assertEquals("invalid command\n", outContent.toString());
    }

    @Test
    public void testTakeIncorrectNumberOfArguments() {
        outContent.reset();
        register.executeCommand("take 1 2 3 4");
        assertEquals("invalid command\n", outContent.toString());

        outContent.reset();
        register.executeCommand("take 1 2 3 4 5 6");
        assertEquals("invalid command\n", outContent.toString());
    }

    @Test
    public void testTakeFirstTime() {
        outContent.reset();
        register.executeCommand("put 1 2 3 4 5");
        assertEquals("$68 1 2 3 4 5\n", outContent.toString());

        outContent.reset();
        register.executeCommand("take 1 0 1 2 1");
        assertEquals("$38 0 2 2 2 4\n", outContent.toString());
    }

    @Test
    public void testTakeSecondTime() {
        outContent.reset();
        register.executeCommand("put 1 2 3 4 5");
        assertEquals("$68 1 2 3 4 5\n", outContent.toString());

        outContent.reset();
        register.executeCommand("take 1 1 1 1 1");
        assertEquals("$30 0 1 2 3 4\n", outContent.toString());

        outContent.reset();
        register.executeCommand("take 0 0 1 2 2");
        assertEquals("$19 0 1 1 1 2\n", outContent.toString());
    }

    @Test
    public void testTakeMultipleTimes() {
        outContent.reset();
        register.executeCommand("put 5 5 5 5 5");
        assertEquals("$190 5 5 5 5 5\n", outContent.toString());

        outContent.reset();
        register.executeCommand("take 0 0 0 4 0");
        assertEquals("$182 5 5 5 1 5\n", outContent.toString());

        outContent.reset();
        register.executeCommand("take 1 3 0 1 2");
        assertEquals("$128 4 2 5 0 3\n", outContent.toString());

        outContent.reset();
        register.executeCommand("take 2 2 4 0 1");
        assertEquals("$47 2 0 1 0 2\n", outContent.toString());

        outContent.reset();
        register.executeCommand("take 1 0 1 0 1");
        assertEquals("$21 1 0 0 0 1\n", outContent.toString());
    }

    @Test
    public void testTakeNotEnoughCashBack() {
        outContent.reset();
        register.executeCommand("put 1 2 3 4 5");
        assertEquals("$68 1 2 3 4 5\n", outContent.toString());

        outContent.reset();
        register.executeCommand("take 2 0 0 4 0");
        assertEquals("sorry\n", outContent.toString());

    }

}