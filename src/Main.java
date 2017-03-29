import com.rafael.CashRegister;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ResourceBundle;

/**
 * Created by Rafael on 3/28/17.
 */
public class Main {

    private static final ResourceBundle BUNDLE = ResourceBundle.getBundle("MessagesBundle");
    private static final BufferedReader CONSOLE = new BufferedReader(new InputStreamReader(System.in));

    public static void main(String[] args) {

        CashRegister register = new CashRegister();

	    String input = null;
	    boolean on = false;

        while(true) {

            try {
                input = readConsole().trim();

                on = register.executeCommand(input);

                if (!on) {

                    break;

                }

            } catch(Exception e) {
                e.printStackTrace();
            }

        }
    }

    private static String readConsole() {

        String input = null;

        try {

            System.out.print("> ");
            input = CONSOLE.readLine();

        } catch(IOException ex) {
            ex.printStackTrace();
        }

        return input;
    }
}
