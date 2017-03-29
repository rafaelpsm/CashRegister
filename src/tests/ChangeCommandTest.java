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
public class ChangeCommandTest extends TestCase {
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
    public void testChangeNoArguments() {
        outContent.reset();
        register.executeCommand("change");
        assertEquals("invalid command\n", outContent.toString());
    }

    @Test
    public void testChangeIncorrectTypeOfArguments() {
        outContent.reset();
        register.executeCommand("change aa");
        assertEquals("invalid command\n", outContent.toString());
    }

    @Test
    public void testChangeIncorrectNumberOfArguments() {
        outContent.reset();
        register.executeCommand("change 1 2 3 4");
        assertEquals("invalid command\n", outContent.toString());

        outContent.reset();
        register.executeCommand("change 1 2");
        assertEquals("invalid command\n", outContent.toString());
    }

    @Test
    public void testChangeFirstTime() {
        outContent.reset();
        register.executeCommand("put 1 2 3 4 5");
        assertEquals("$68 1 2 3 4 5\n", outContent.toString());

        outContent.reset();
        register.executeCommand("change 25");
        assertEquals("1 0 1 0 0\n", outContent.toString());
    }

    @Test
    public void testChangeMultipleTimes() {
        outContent.reset();
        register.executeCommand("put 1 2 3 4 5");
        assertEquals("$68 1 2 3 4 5\n", outContent.toString());

        outContent.reset();
        register.executeCommand("change 12");
        assertEquals("0 1 0 1 0\n", outContent.toString());
        outContent.reset();
        register.executeCommand("show");
        assertEquals("$56 1 1 3 3 5\n", outContent.toString());

        outContent.reset();
        register.executeCommand("change 27");
        assertEquals("1 0 1 1 0\n", outContent.toString());
        outContent.reset();
        register.executeCommand("show");
        assertEquals("$29 0 1 2 2 5\n", outContent.toString());
    }

    @Test
    public void testChangeUnavailableAmount() {
        outContent.reset();
        register.executeCommand("put 1 0 3 4 0");
        assertEquals("$43 1 0 3 4 0\n", outContent.toString());

        outContent.reset();
        register.executeCommand("change 40");
        assertEquals("sorry\n", outContent.toString());

    }

    @Test
    public void testChangeUnavailableAmountAfterChangeSuccessfull() {
        outContent.reset();
        register.executeCommand("put 1 0 3 4 0");
        assertEquals("$43 1 0 3 4 0\n", outContent.toString());

        outContent.reset();
        register.executeCommand("change 11");
        assertEquals("0 0 1 3 0\n", outContent.toString());

        outContent.reset();
        register.executeCommand("change 14");
        assertEquals("sorry\n", outContent.toString());

    }

}