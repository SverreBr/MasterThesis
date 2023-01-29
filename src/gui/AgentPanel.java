package gui;

import alternatingOffers.PlayerToM;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.text.*;
import java.awt.*;

public class AgentPanel extends JPanel {

    private final JTextPane info;
    private final String[] content = new String[8];
    private final String[] style = {"bold", "regular"};
    private final PlayerToM agent;

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

        this.add(info, BorderLayout.NORTH);

    }

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

    private void updateInfo() {
        content[0] = agent.name;
        content[1] = "chips:";

        for (int i = 2; i <= 5; i++) {
            content[i] = "";
        }
//        content[6] = "points:" + agent.calculateCurrentPoints();


        // Generate agent info panel
        StyledDocument doc = info.getStyledDocument();
        try {
            doc.remove(0, doc.getLength());
            for (int i = 0; i < content.length; i++) {
                doc.insertString(doc.getLength(), content[i] + "\n", doc.getStyle(style[i]));
            }
        }
        catch (BadLocationException ble) {
            System.err.println("Couldn't insert text into text pane.");
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        paintChips(g2);

        updateInfo();

        g2.dispose();
    }

    private void paintChips(Graphics2D g2) {
        int tokenSize = 25;
        int offset = 20;
        int height = 70;

        int[] tokens = agent.getTokens();
        for (int i = 0; i < tokens.length; i++) {
            g2.setColor(agent.settings.getColor(tokens[i]));
            g2.fillOval(offset + i * tokenSize, height, tokenSize, tokenSize);
        }
    }
}
