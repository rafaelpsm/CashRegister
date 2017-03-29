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
public class CashRegisterTest extends TestCase {
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
    public void testTurnOnRegister() {
        assertEquals("ready\n", outContent.toString());
    }

    @Test
    public void testTurnOffRegister() {
        outContent.reset();
        boolean result = register.executeCommand("quit");
        assertEquals(false, result);
    }

}