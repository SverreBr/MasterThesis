package view;

import model.player.PlayerLying;
import utilities.Settings;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.text.DecimalFormat;

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
            addStylesToDoc(locationBeliefsPane, "Location beliefs agent (order=" + orderToM + "): ");
            mainPanel.add(locationBeliefsPane);

            locationBeliefsPaneValues = new JTextPane();
            double[] locationBeliefs = agent.getLocationBeliefs(orderToM);
            StringBuilder text = new StringBuilder("[" + df.format(locationBeliefs[0]));
            for (int i = 1; i < locationBeliefs.length; i++) {
                text.append("; ").append(df.format(locationBeliefs[i]));
            }
            text.append("]");
            addStylesToDoc(locationBeliefsPaneValues, String.valueOf(text));
            mainPanel.add(locationBeliefsPaneValues);
            orderToM--;
        }
    }

    private void addLocationBeliefsPartner() {
        JTextPane locationBeliefsPane, locationBeliefsPaneValues;
        int orderToM = agent.getOrderToM();

        while (orderToM > 1) {
            locationBeliefsPane = new JTextPane();
            addStylesToDoc(locationBeliefsPane, "Modelled location beliefs trading partner (order=" + (orderToM-1) + "): ");
            mainPanel.add(locationBeliefsPane);

            locationBeliefsPaneValues = new JTextPane();
            double[] locationBeliefs = agent.getPartnerModelLocationBeliefs(orderToM);
            StringBuilder text = new StringBuilder("[" + df.format(locationBeliefs[0]));
            for (int i = 1; i < locationBeliefs.length; i++) {
                text.append("; ").append(df.format(locationBeliefs[i]));
            }
            text.append("]");
            addStylesToDoc(locationBeliefsPaneValues, String.valueOf(text));
            mainPanel.add(locationBeliefsPaneValues);
            orderToM--;
        }
    }

    private void addStylesToDoc(JTextPane pane, String text) {
        StyledDocument doc = pane.getStyledDocument();
        Settings.addStylesToDocument(doc);
        try {
            doc.remove(0, doc.getLength());
            doc.insertString(doc.getLength(), text, doc.getStyle("regular"));
        } catch (BadLocationException ble) {
            System.err.println("Couldn't insert text into text pane.");
        }
        pane.setEditable(false);
        pane.setOpaque(false);
    }

}
