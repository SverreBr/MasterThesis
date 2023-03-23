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
                JOptionPane.WARNING_MESSAGE);
    }

    public static void showAgentInformationButtonNotAccessible() {
        showMessageDialog(null,
                "The simulation is running. Please try later again.", "Cannot view agent information",
                JOptionPane.WARNING_MESSAGE);
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
                "Invalid Goal Position.", JOptionPane.ERROR_MESSAGE);
    }

    public static void showSuccessfulLoading() {
        showMessageDialog(null, "Loading the game setting was successful.",
                "Successful Loading", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void showCancelledLoading() {
        showMessageDialog(null, "Loading a game setting was cancelled.",
                "Cancelled Loading", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void showIOExceptionLoading() {
        showMessageDialog(null,
                "Something went wrong loading the game. Please select only files with .ser extension.",
                "Unsuccessful Loading", JOptionPane.ERROR_MESSAGE);
    }

    public static void showGeneralException() {
        showMessageDialog(null, "Hmm... I don't know what went wrong, but don't do that again please.",
                "Some Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void showSuccessfulSaving() {
        showMessageDialog(null, "Saving the game setting was successful.",
                "Successful Saving", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void showCancelledSaving() {
        showMessageDialog(null, "Saving the game setting was cancelled.",
                "Cancelled Saving", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void showErrorSaving(String description) {
        showMessageDialog(null, description, "Error Saving Game Setting",
                JOptionPane.ERROR_MESSAGE);
    }

    public static boolean showFileNameAlreadyExistsError(String description) {
        int returnVal = showOptionDialog(null, description, "Filename Already Exists Error",
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null,null, null);
        return returnVal == JOptionPane.YES_OPTION;
    }
}
