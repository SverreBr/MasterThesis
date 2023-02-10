package gui;

import controller.GameListener;
import utilities.Game;
import utilities.Settings;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * LegendPanel: creates a legend for the view
 */
public class LegendPanel extends JPanel implements GameListener {

    /**
     * Information text pane
     */
    private final JTextPane info;

    /**
     * The game model
     */
    private final Game game;

    /**
     * Constructor for the legend panel
     * @param game The game model
     */
    public LegendPanel(Game game) {
        this.game = game;

        this.info = new JTextPane();
        createTextPane();
        updateLegendText();

        changeBackgrounds();
        this.add(info, BorderLayout.NORTH);
        game.addListener(this);
    }

    /**
     * Change background of the legend panel
     */
    private void changeBackgrounds() {
        setBackground(Settings.getBackGroundColor());
        info.setBackground(Settings.getBackGroundColor());
    }

    /**
     * Creates the text pane for the legend panel
     */
    private void createTextPane() {
        info.setPreferredSize(new Dimension(Settings.BUTTON_PANEL_WIDTH - 20, Settings.MAIN_PANEL_SIZE - Settings.AGENT_PANEL_HEIGHT));
        info.setEditable(false);
        info.setOpaque(false);
        info.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0), BorderFactory.createCompoundBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED), BorderFactory.createEmptyBorder(5, 5, 5, 5))));
        StyledDocument doc = info.getStyledDocument();
        Settings.addStylesToDocument(doc);
    }

    /**
     * Paint the panel
     * @param g the <code>Graphics</code> object to protect
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g.create();
        paintChipsLegend(g2);
        g2.dispose();
    }

    /**
     * Paint the chips on the legend panel
     *
     * @param g2 graphics
     */
    private void paintChipsLegend(Graphics2D g2) {
        int tokenSize = 30;
        int offset = 38;
        int betweenSpace = 11;
        int height = 75;

        int numDiffTokens = Settings.CHIP_DIVERSITY;

        g2.setColor(Settings.getColor(0));
        g2.fillOval(offset, height, tokenSize, tokenSize);
        for (int i = 1; i < numDiffTokens; i++) {
            g2.setColor(Settings.getColor(i));
            g2.fillOval(offset + betweenSpace * i + i * tokenSize, height, tokenSize, tokenSize);
        }
    }

    /**
     * Updates the text in the legend
     */
    private void updateLegendText() {
        List<String> content = new ArrayList<>();
        List<String> style = new ArrayList<>();

        content.add("Legend");
        style.add("bold");

        content.add("list index of chips:");
        style.add("italic");

        StringBuilder chipsNumbering = new StringBuilder();
        String spaces = "    ";
        chipsNumbering.append(spaces);
        for (int i = 0; i < Settings.CHIP_DIVERSITY; i++) {
            chipsNumbering.append(spaces);
            chipsNumbering.append(i);
            chipsNumbering.append(spaces);
        }
        content.add(chipsNumbering.toString());
        style.add("regular");

        for (int i = 0; i < 3; i++) {
            content.add("");
            style.add("regular");
        }

        Point init, resp;
        init = game.getGoalPositionPointPlayer(Settings.INITIATOR_NAME);
        resp = game.getGoalPositionPointPlayer(Settings.RESPONDER_NAME);
        if (init.equals(resp)) {
            content.add("* " + Settings.GOAL_LOCATION_SYMBOL + ": Goal location both agents.");
        } else {
            content.add("* " + Settings.GOAL_LOCATION_SYMBOL_INITIATOR + ": Goal location initiator.");
            content.add("* " + Settings.GOAL_LOCATION_SYMBOL_RESPONDER + ": Goal location responder.");
            style.add("regular");
        }
        style.add("regular");

        content.add("* " + Settings.START_LOCATION_SYMBOL + ": Start location both agents.");
        style.add("regular");

        // Generate agent info panel
        StyledDocument doc = info.getStyledDocument();
        try {
            doc.remove(0, doc.getLength());
            for (int i = 0; i < content.size(); i++) {
                doc.insertString(doc.getLength(), content.get(i) + "\n", doc.getStyle(style.get(i)));
            }
        } catch (BadLocationException ble) {
            System.err.println("Couldn't insert text into text pane.");
        }
    }

    /**
     * Updates the legend text and repaints the panel
     */
    @Override
    public void newGame() {
        updateLegendText();
        this.repaint();
    }

    @Override
    public void gameChanged() {}
}
