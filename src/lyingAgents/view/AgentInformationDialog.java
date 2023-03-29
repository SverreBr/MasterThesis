package lyingAgents.view;

import lyingAgents.model.player.PlayerLying;
import lyingAgents.utilities.MiscFunc;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;

//TODO: comments
public class AgentInformationDialog extends JDialog {
    private final JPanel mainPanel;
    private final PlayerLying agent;

    private final DecimalFormat df = new DecimalFormat("####0.00");

    public AgentInformationDialog(Frame owner, String title, PlayerLying agent) {
        super(owner, title, true);
        this.agent = agent;

        this.mainPanel = new JPanel();
        createMainPanel();

        getContentPane().add(mainPanel);
        pack();
        this.setLocationRelativeTo(null);
    }

    private void createMainPanel() {
        mainPanel.setLayout(new GridLayout(0, 2));

        if (agent.getOrderToM() > 0)
            addLocationBeliefs();
        if (agent.getOrderToM() > 1)
            addLocationBeliefsPartner();
    }

    private void addLocationBeliefs() {
        JTextPane locationBeliefsPane, locationBeliefsPaneValues;
        int orderToM = agent.getOrderToM();

        while (orderToM > 0) {
            locationBeliefsPane = new JTextPane();
            MiscFunc.addStylesToDoc(locationBeliefsPane, "Location beliefs agent (order=" + orderToM + "): ", "regular");
            mainPanel.add(locationBeliefsPane);

            locationBeliefsPaneValues = new JTextPane();
            double[] locationBeliefs = agent.getLocationBeliefs(orderToM);
            StringBuilder text = new StringBuilder("[" + df.format(locationBeliefs[0]));
            for (int i = 1; i < locationBeliefs.length; i++) {
                text.append("; ").append(df.format(locationBeliefs[i]));
            }
            text.append("]");
            MiscFunc.addStylesToDoc(locationBeliefsPaneValues, String.valueOf(text), "regular");
            mainPanel.add(locationBeliefsPaneValues);

            /////////////////////////////
            locationBeliefsPane = new JTextPane();
            MiscFunc.addStylesToDoc(locationBeliefsPane, "Saved location beliefs agent (order=" + orderToM + "): ", "regular");
            mainPanel.add(locationBeliefsPane);

            locationBeliefsPaneValues = new JTextPane();
            locationBeliefs = agent.getLocationBeliefsWithoutMessage(orderToM);
            text = new StringBuilder("[" + df.format(locationBeliefs[0]));
            for (int i = 1; i < locationBeliefs.length; i++) {
                text.append("; ").append(df.format(locationBeliefs[i]));
            }
            text.append("]");
            MiscFunc.addStylesToDoc(locationBeliefsPaneValues, String.valueOf(text), "regular");
            mainPanel.add(locationBeliefsPaneValues);

            orderToM--;
        }
    }

    private void addLocationBeliefsPartner() {
        JTextPane locationBeliefsPane, locationBeliefsPaneValues;
        int orderToM = agent.getOrderToM();

        while (orderToM > 1) {
            locationBeliefsPane = new JTextPane();
            MiscFunc.addStylesToDoc(locationBeliefsPane, "Modelled location beliefs trading partner (order=" + (orderToM-1) + "): ", "regular");
            mainPanel.add(locationBeliefsPane);

            locationBeliefsPaneValues = new JTextPane();
            double[] locationBeliefs = agent.getPartnerModelLocationBeliefs(orderToM);
            StringBuilder text = new StringBuilder("[" + df.format(locationBeliefs[0]));
            for (int i = 1; i < locationBeliefs.length; i++) {
                text.append("; ").append(df.format(locationBeliefs[i]));
            }
            text.append("]");
            MiscFunc.addStylesToDoc(locationBeliefsPaneValues, String.valueOf(text), "regular");
            mainPanel.add(locationBeliefsPaneValues);

            //////////////////////////////////////
            locationBeliefsPane = new JTextPane();
            MiscFunc.addStylesToDoc(locationBeliefsPane, "Modelled saved location beliefs trading partner (order=" + (orderToM-1) + "): ", "regular");
            mainPanel.add(locationBeliefsPane);

            locationBeliefsPaneValues = new JTextPane();
            locationBeliefs = agent.getPartnerModelLocationBeliefsWithoutMessage(orderToM);
            text = new StringBuilder("[" + df.format(locationBeliefs[0]));
            for (int i = 1; i < locationBeliefs.length; i++) {
                text.append("; ").append(df.format(locationBeliefs[i]));
            }
            text.append("]");
            MiscFunc.addStylesToDoc(locationBeliefsPaneValues, String.valueOf(text), "regular");
            mainPanel.add(locationBeliefsPaneValues);
            orderToM--;
        }
    }
}
