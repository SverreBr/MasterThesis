package view;

import javax.swing.*;

/**
 * Class Popups: provides popups for messages to the user
 */
public class Popups extends JOptionPane {

    /**
     * Popup to be shown when an invalid text entry has been entered.
     */
    public static void showInvalidTextError() {
        showMessageDialog(null, "The text you entered is probably not a positive integer.",
                "Invalid text", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Popup to be shown when forwarding was a success
     *
     * @param rounds The number of rounds forwarded
     */
    public static void showForwardSuccess(int rounds) {
        showMessageDialog(null, "Forwarding " + rounds + " rounds was a success!",
                "Forwarding rounds", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Popup to be shown when an invalid order of theory of mind has been chosen
     *
     * @param name Name of the agent for which an invalid order of ToM has been chosen
     */
    public static void showInvalidOrderToM(String name) {
        showMessageDialog(null,
                "Provided an invalid order for theory of mind for the " + name + ".\n" +
                        "Please insert a number from the set {0, 1, 2}.\n" +
                        "For now, no changes have been made to the game.",
                "Invalid input", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Popup to be shown when an invalid learning rate has been chosen
     *
     * @param name Name of the agent for which an invalid learning rate has been chosen
     */
    public static void showInvalidLR(String name) {
        showMessageDialog(null,
                "Provided an invalid learning rate for the " + name + ".\n" +
                        "Please insert a number in the range [0, 1].\n" +
                        "For now, no changes have been made to the game.",
                "Invalid input", JOptionPane.ERROR_MESSAGE);
    }

    public static void showInvalidCanLieToM(String name, int tom) {
        showMessageDialog(null,
                "Provided an invalid boolean value that the " + name + " can lie.\n" +
                        "Only an agent with a higher-order theory of mind can lie (2+), but you provided " + tom + ".\n" +
                        "For now, no changes have been made to the game.",
                "Invalid input", JOptionPane.ERROR_MESSAGE);
    }


    /**
     * Popup to be shown when settings cannot be changed (maybe during forwarding of rounds).
     */
    public static void showSettingsButtonNotAccessible() {
        showMessageDialog(null,
                "The simulation is running. Please try later again.", "Cannot change settings",
                JOptionPane.ERROR_MESSAGE);
    }

    public static void showAgentInformationButtonNotAccessible() {
        showMessageDialog(null,
                "The simulation is running. Please try later again.", "Cannot view agent information",
                JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Popup to be shown when forwarding has been canceled.
     */
    public static void showForwardingCancelled() {
        showMessageDialog(null,
                "The forwarding has been canceled. However, all changes that have been made up until now are still there.");
    }

    public static void showInvalidGoalPosition(String name, int numGoalPositions) {
        showMessageDialog(null,
                "An invalid goal position has been provided to the " + name + ".\n" +
                        "Please insert an integer in the set {0,...," + numGoalPositions + "}.",
                "Invalid Goal position.", JOptionPane.ERROR_MESSAGE);
    }
}