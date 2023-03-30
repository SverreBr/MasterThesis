package lyingAgents.view;

import lyingAgents.model.player.PlayerLying;
import lyingAgents.utilities.MiscFunc;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;

/**
 * AgentInformationDialog class: Shows some information about the beliefs of an agent
 */
public class AgentInformationDialog extends JDialog {

    /**
     * Main panel of the dialog
     */
    private final JPanel mainPanel;

    /**
     * Agent model
     */
    private final PlayerLying agent;

    /**
     * Formatter for double values
     */
    private final DecimalFormat df = new DecimalFormat("####0.00");

    /**
     * Constructor
     *
     * @param owner Main frame where the dialog is placed on top
     * @param title Title of the dialog
     * @param agent Agent model
     */
    public AgentInformationDialog(Frame owner, String title, PlayerLying agent) {
        super(owner, title, true);
        this.agent = agent;

        this.mainPanel = new JPanel();
        createMainPanel();

        getContentPane().add(mainPanel);
        pack();
        this.setLocationRelativeTo(null);
    }

    /**
     * Creates the main panel of the dialog. Adds location beliefs to the dialog.
     */
    private void createMainPanel() {
        mainPanel.setLayout(new GridLayout(0, 2));
        addInformation();

        if (agent.getOrderToM() > 0) addLocationBeliefs();
        if (agent.getOrderToM() > 1) addLocationBeliefsPartner();
    }

    /**
     * Method to add some basic information about the agent to the dialog
     */
    private void addInformation() {
        JTextPane textPane;

        textPane = new JTextPane();
        MiscFunc.addStylesToDoc(textPane, "Agent has sent a message: ", "regular");
        mainPanel.add(textPane);

        textPane = new JTextPane();
        MiscFunc.addStylesToDoc(textPane, Boolean.toString(agent.getHasSentMessage()), "regular");
        mainPanel.add(textPane);

        textPane = new JTextPane();
        MiscFunc.addStylesToDoc(textPane, "Agent has received a message: ", "regular");
        mainPanel.add(textPane);

        textPane = new JTextPane();
        MiscFunc.addStylesToDoc(textPane, Boolean.toString(agent.getHasReceivedMessage()), "regular");
        mainPanel.add(textPane);
    }

    /**
     * Method to add location beliefs to the panel
     */
    private void addLocationBeliefs() {
        double[] locationBeliefs;
        JTextPane locationBeliefsPane;
        int orderToM = agent.getOrderToM();

        while (orderToM > 0) {
            locationBeliefsPane = new JTextPane();
            MiscFunc.addStylesToDoc(locationBeliefsPane, "Location beliefs agent (order=" + orderToM + "): ", "regular");
            mainPanel.add(locationBeliefsPane);
            locationBeliefs = agent.getLocationBeliefs(orderToM);
            addArrayToPanel(locationBeliefs);

            locationBeliefsPane = new JTextPane();
            MiscFunc.addStylesToDoc(locationBeliefsPane, "Saved location beliefs agent (order=" + orderToM + "): ", "regular");
            mainPanel.add(locationBeliefsPane);
            locationBeliefs = agent.getLocationBeliefsWithoutMessage(orderToM);
            addArrayToPanel(locationBeliefs);

            orderToM--;
        }
    }

    /**
     * Helper method to add an array of location beliefs to the panel
     *
     * @param arr The array to be added to the panel
     */
    private void addArrayToPanel(double[] arr) {
        JTextPane values = new JTextPane();
        StringBuilder text = new StringBuilder("[" + df.format(arr[0]));
        for (int i = 1; i < arr.length; i++) {
            text.append("; ").append(df.format(arr[i]));
        }
        text.append("]");
        MiscFunc.addStylesToDoc(values, String.valueOf(text), "regular");
        mainPanel.add(values);
    }

    /**
     * Adds the location beliefs of the partner model
     */
    private void addLocationBeliefsPartner() {
        JTextPane locationBeliefsPane;
        double[] locationBeliefs;
        int orderToM = agent.getOrderToM();

        while (orderToM > 1) {
            locationBeliefsPane = new JTextPane();
            MiscFunc.addStylesToDoc(locationBeliefsPane, "Modelled location beliefs trading partner (order=" + (orderToM - 1) + "): ", "regular");
            mainPanel.add(locationBeliefsPane);
            locationBeliefs = agent.getPartnerModelLocationBeliefs(orderToM);
            addArrayToPanel(locationBeliefs);

            locationBeliefsPane = new JTextPane();
            MiscFunc.addStylesToDoc(locationBeliefsPane, "Modelled saved location beliefs trading partner (order=" + (orderToM - 1) + "): ", "regular");
            mainPanel.add(locationBeliefsPane);
            locationBeliefs = agent.getPartnerModelLocationBeliefsWithoutMessage(orderToM);
            addArrayToPanel(locationBeliefs);

            orderToM--;
        }
    }
}
