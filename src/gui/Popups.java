package gui;

import javax.swing.*;
import java.awt.*;

/**
 * Class Popups: provides popups for messages to the user
 */
public class Popups extends JOptionPane {

    /**
     * Popup to be shown when an invalid text entry has been entered.
     */
    public static void showInvalidTextError() {
        showMessageDialog((Component)null, "The text you entered is probably not a positive integer.",
                "Invalid text", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Popup to be shown when forwarding was a success
     * @param rounds The number of rounds forwarded
     */
    public static void showForwardSuccess(int rounds) {
        showMessageDialog((Component)null, "Forwarding " + rounds + " rounds was a success!",
                "Forwarding rounds", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void showInvalidOrderToM(String name) {
        showMessageDialog((Component) null,
                "Provided an invalid order for theory of mind for the " + name + ".\n" +
                "Please insert a number from the set {0, 1, 2}.\n" +
                "For now, no changes have been made to the game.",
                "Invalid input", JOptionPane.ERROR_MESSAGE);
    }

    public static void showInvalidLR(String name) {
        showMessageDialog((Component) null,
                "Provided an invalid learning rate for the " + name + ".\n" +
                        "Please insert a number in the range [0, 1].\n" +
                        "For now, no changes have been made to the game.",
                "Invalid input", JOptionPane.ERROR_MESSAGE);
    }
}