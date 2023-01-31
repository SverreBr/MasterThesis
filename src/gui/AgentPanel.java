package gui;

import alternatingOffers.PlayerToM;
import controller.GameListener;
import utilities.Settings;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.text.*;
import java.awt.*;
import java.util.List;

/**
 * AgentPanel: makes the agent panel
 */
public class AgentPanel extends JComponent implements GameListener {

    /**
     * Text pane with info about the agent
     */
    private final JTextPane info;

    /**
     * Content of the info text pane
     */
    private final String[] content = new String[7];

    /**
     * The style of the content of the text pane
     */
    private final String[] style = {
            "bold", "regular", "regular", "regular", "regular",
            "regular", "regular"
    };

    /**
     * The agent model of this panel
     */
    private final PlayerToM agent;

    /**
     * Constructor of the agent panel
     *
     * @param agent the model of the agent
     */
    public AgentPanel(PlayerToM agent) {
        this.agent = agent;

        this.setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        info = new JTextPane();
        info.setPreferredSize(new Dimension(270, 400));
        info.setMaximumSize(new Dimension(270, 400));
        info.setEditable(false);
        info.setOpaque(false);
        info.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0), BorderFactory.createCompoundBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED), BorderFactory.createEmptyBorder(5, 5, 5, 5))));
        StyledDocument doc = info.getStyledDocument();
        addStylesToDocument(doc);

        updateInfo();
        this.add(info, BorderLayout.NORTH);

        this.agent.game.addListener(this);
    }

    /**
     * Adds some style to the text pane of the panel
     *
     * @param doc the document
     */
    private void addStylesToDocument(StyledDocument doc) {
        // Initialize some styles.
        Style def = StyleContext.getDefaultStyleContext().getStyle(StyleContext.DEFAULT_STYLE);

        Style regular = doc.addStyle("regular", def);
        StyleConstants.setFontFamily(def, "SansSerif");
        StyleConstants.setFontSize(regular, 16);

        Style s = doc.addStyle("italic", regular);
        StyleConstants.setItalic(s, true);

        s = doc.addStyle("bold", regular);
        StyleConstants.setBold(s, true);
    }

    /**
     * Method to update the information on the info text panel
     */
    private void updateInfo() {
        content[0] = agent.name;
        content[1] = "chips:";

        for (int i = 2; i <= 5; i++) {
            content[i] = "";
        }
        content[6] = "points: " + agent.calculateScore(agent.startingPosition, agent.getTokens(), agent.goalPosition);

        // Generate agent info panel
        StyledDocument doc = info.getStyledDocument();
        try {
            doc.remove(0, doc.getLength());
            for (int i = 0; i < content.length; i++) {
                doc.insertString(doc.getLength(), content[i] + "\n", doc.getStyle(style[i]));
            }
        } catch (BadLocationException ble) {
            System.err.println("Couldn't insert text into text pane.");
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        paintChips(g2);
        g2.dispose();
    }

    /**
     * Paint the chips of the agent on the info text panel
     *
     * @param g2 graphics
     */
    private void paintChips(Graphics2D g2) {
        int tokenSize = 40;
        int offset = 30;
        int height = 70;

        List<Integer> tokens = agent.getTokens();
        for (int i = 0; i < tokens.size(); i++) {
            g2.setColor(Settings.getColor(tokens.get(i)));
            g2.fillOval(offset + i * tokenSize, height, tokenSize, tokenSize);
        }
    }

    @Override
    public void gameChanged() {
        updateInfo();
        this.repaint();
    }
}
