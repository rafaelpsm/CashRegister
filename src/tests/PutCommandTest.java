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
public class PutCommandTest extends TestCase {
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
    public void testPutNoArguments() {
        outContent.reset();
        register.executeCommand("put");
        assertEquals("invalid command\n", outContent.toString());
    }

    @Test
    public void testPutIncorrectTypeOfArguments() {
        outContent.reset();
        register.executeCommand("put 1 a 3 4 5");
        assertEquals("invalid command\n", outContent.toString());

        outContent.reset();
        register.executeCommand("put 1 2 3 $ 5");
        assertEquals("invalid command\n", outContent.toString());

        outContent.reset();
        register.executeCommand("put 1 2 3 4 %");
        assertEquals("invalid command\n", outContent.toString());
    }

    @Test
    public void testPutIncorrectNumberOfArguments() {
        outContent.reset();
        register.executeCommand("put 1 2 3 4");
        assertEquals("invalid command\n", outContent.toString());

        outContent.reset();
        register.executeCommand("put 1 2 3 4 5 6");
        assertEquals("invalid command\n", outContent.toString());
    }

    @Test
    public void testPutFirstTime() {
        outContent.reset();
        register.executeCommand("put 1 2 3 4 5");
        assertEquals("$68 1 2 3 4 5\n", outContent.toString());
    }

    @Test
    public void testPutSecondTime() {
        outContent.reset();
        register.executeCommand("put 1 2 3 4 5");
        assertEquals("$68 1 2 3 4 5\n", outContent.toString());

        outContent.reset();
        register.executeCommand("put 1 1 1 1 1");
        assertEquals("$106 2 3 4 5 6\n", outContent.toString());
    }

    @Test
    public void testPutMultipleTimes() {
        outContent.reset();
        register.executeCommand("put 0 0 0 4 0");
        assertEquals("$8 0 0 0 4 0\n", outContent.toString());

        outContent.reset();
        register.executeCommand("put 1 3 0 1 2");
        assertEquals("$62 1 3 0 5 2\n", outContent.toString());

        outContent.reset();
        register.executeCommand("put 0 2 4 0 5");
        assertEquals("$107 1 5 4 5 7\n", outContent.toString());

        outContent.reset();
        register.executeCommand("put 1 1 1 1 1");
        assertEquals("$145 2 6 5 6 8\n", outContent.toString());
    }

}