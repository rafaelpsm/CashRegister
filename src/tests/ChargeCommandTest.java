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
public class ChargeCommandTest extends TestCase {
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
    public void testChargeNoArguments() {
        outContent.reset();
        register.executeCommand("charge");
        assertEquals("invalid command\n", outContent.toString());
    }

    @Test
    public void testChargeIncorrectTypeOfArguments() {
        outContent.reset();
        register.executeCommand("charge a 1 1 0 0 0");
        assertEquals("invalid command\n", outContent.toString());
    }

    @Test
    public void testChargeIncorrectNumberOfArguments() {
        outContent.reset();
        register.executeCommand("charge 1 2 3 4 5");
        assertEquals("invalid command\n", outContent.toString());

        outContent.reset();
        register.executeCommand("charge 1 2 3 4 5 6 7");
        assertEquals("invalid command\n", outContent.toString());
    }

    @Test
    public void testChargeFirstTime() {
        outContent.reset();
        register.executeCommand("put 1 2 3 4 5");
        assertEquals("$68 1 2 3 4 5\n", outContent.toString());

        outContent.reset();
        register.executeCommand("charge 25 1 1 0 0 0");
        assertEquals("0 0 1 0 0\n", outContent.toString());
    }

    @Test
    public void testChargeMultipleTimes() {
        outContent.reset();
        register.executeCommand("put 1 5 5 5 5");
        assertEquals("$110 1 5 5 5 5\n", outContent.toString());

        outContent.reset();
        register.executeCommand("charge 37 2 0 0 0 0");
        assertEquals("0 0 0 1 1\n", outContent.toString());
        outContent.reset();
        register.executeCommand("show");
        assertEquals("$147 3 5 5 4 4\n", outContent.toString());

        outContent.reset();
        register.executeCommand("charge 17 0 1 2 0 0");
        assertEquals("0 0 0 1 1\n", outContent.toString());
        outContent.reset();
        register.executeCommand("show");
        assertEquals("$164 3 6 7 3 3\n", outContent.toString());
    }

    @Test
    public void testChargeNotEnoughCashReceive() {
        outContent.reset();
        register.executeCommand("put 1 2 3 1 0");
        assertEquals("$57 1 2 3 1 0\n", outContent.toString());

        outContent.reset();
        register.executeCommand("charge 7 0 0 1 0 0");
        assertEquals("not enough money to charge\n", outContent.toString());

    }

    @Test
    public void testChargeNotEnoughCashBack() {
        outContent.reset();
        register.executeCommand("put 1 2 3 1 0");
        assertEquals("$57 1 2 3 1 0\n", outContent.toString());

        outContent.reset();
        register.executeCommand("charge 7 0 1 0 0 0");
        assertEquals("sorry\n", outContent.toString());

    }

}