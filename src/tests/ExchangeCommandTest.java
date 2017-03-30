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
public class ExchangeCommandTest extends TestCase {
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
    public void testExchangeNoArguments() {
        outContent.reset();
        register.executeCommand("exchange");
        assertEquals("invalid command\n", outContent.toString());
    }

    @Test
    public void testExchangeIncorrectTypeOfArguments() {
        outContent.reset();
        register.executeCommand("exchange aa");
        assertEquals("invalid command\n", outContent.toString());
    }

    @Test
    public void testExchangeIncorrectNumberOfArguments() {
        outContent.reset();
        register.executeCommand("exchange 1 0 0 0");
        assertEquals("invalid command\n", outContent.toString());

        outContent.reset();
        register.executeCommand("exchange 1 0 0 0 0 0");
        assertEquals("invalid command\n", outContent.toString());
    }

    @Test
    public void testExchangeIncorrectNumberOfBills() {
        outContent.reset();
        register.executeCommand("exchange 1 1 0 0 0");
        assertEquals("invalid command\n", outContent.toString());

        outContent.reset();
        register.executeCommand("exchange 0 2 0 0 0");
        assertEquals("invalid command\n", outContent.toString());
    }

    @Test
    public void testExchangeFirstTime() {
        outContent.reset();
        register.executeCommand("put 1 2 3 4 5");
        assertEquals("$68 1 2 3 4 5\n", outContent.toString());

        outContent.reset();
        register.executeCommand("exchange 1 0 0 0 0");
        assertEquals("0 2 0 0 0\n", outContent.toString());
    }

    @Test
    public void testExchangeMultipleTimes() {
        outContent.reset();
        register.executeCommand("put 1 2 3 4 5");
        assertEquals("$68 1 2 3 4 5\n", outContent.toString());

        outContent.reset();
        register.executeCommand("exchange 1 0 0 0 0");
        assertEquals("0 2 0 0 0\n", outContent.toString());
        outContent.reset();
        register.executeCommand("show");
        assertEquals("$68 2 0 3 4 5\n", outContent.toString());

        outContent.reset();
        register.executeCommand("exchange 0 1 0 0 0");
        assertEquals("0 0 2 0 0\n", outContent.toString());
        outContent.reset();
        register.executeCommand("show");
        assertEquals("$68 2 1 1 4 5\n", outContent.toString());
    }

    @Test
    public void testExchangeNotEnoughCashBack() {
        outContent.reset();
        register.executeCommand("put 1 0 3 4 0");
        assertEquals("$43 1 0 3 4 0\n", outContent.toString());

        outContent.reset();
        register.executeCommand("exchange 0 0 1 0 0");
        assertEquals("sorry\n", outContent.toString());

    }

}