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
public class QuitCommandTest extends TestCase {
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
    public void testQuitWithArguments() {
        outContent.reset();
        register.executeCommand("quit 1");
        assertEquals("invalid command\n", outContent.toString());
    }

    @Test
    public void testQuit() {
        outContent.reset();
        boolean result = register.executeCommand("quit");
        assertEquals(false, result);
    }

    @Test
    public void testQuitAfterPut() {
        outContent.reset();
        register.executeCommand("put 1 2 3 4 5");
        assertEquals("$68 1 2 3 4 5\n", outContent.toString());

        outContent.reset();
        boolean result = register.executeCommand("quit");
        assertEquals(false, result);
    }

    @Test
    public void testQuitAfterPutAndTake() {
        outContent.reset();
        register.executeCommand("put 1 2 3 4 5");
        assertEquals("$68 1 2 3 4 5\n", outContent.toString());

        outContent.reset();
        register.executeCommand("take 1 1 1 1 1");
        assertEquals("$30 0 1 2 3 4\n", outContent.toString());

        outContent.reset();
        boolean result = register.executeCommand("quit");
        assertEquals(false, result);
    }

}