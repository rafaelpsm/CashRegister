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
public class InexistentCommandTest extends TestCase {
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
    public void testInexistentCommand() {
        outContent.reset();
        register.executeCommand("ops");
        assertEquals("invalid command\n", outContent.toString());

        outContent.reset();
        register.executeCommand("test a 7 8 0");
        assertEquals("invalid command\n", outContent.toString());

        outContent.reset();
        register.executeCommand("teke 1 2 3 4 5");
        assertEquals("invalid command\n", outContent.toString());
    }

}