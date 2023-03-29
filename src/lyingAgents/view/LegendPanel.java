package lyingAgents.view;

import lyingAgents.controller.BoardSettingsAction;
import lyingAgents.controller.SettingsAction;
import lyingAgents.model.GameListener;
import lyingAgents.model.Game;
import lyingAgents.utilities.MiscFunc;
import lyingAgents.utilities.Settings;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.io.File;
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
     * Settings button
     */
    private final JButton settingsButton;

    private final JButton boardSettingsButton;

    /**
     * Panel for the settings button
     */
    private final JPanel buttonPanel;

    /**
     * Field for the width of the settings button
     */
    private final int SETTINGS_BUTTON_SIZE = 40;

    /**
     * The main frame of the visuals
     */
    private final JFrame mainFrame;

    /**
     * Constructor for the legend panel
     * @param game The game model
     */
    public LegendPanel(Game game, JFrame mainFrame) {
        this.game = game;
        this.mainFrame = mainFrame;

        this.info = new JTextPane();
        createTextPane();
        updateLegendText();

        this.settingsButton = new JButton();
        this.boardSettingsButton = new JButton();
        buttonPanel = new JPanel();
        createButtonPanel();


        this.setLayout(new BorderLayout());
        this.add(info, BorderLayout.CENTER);
        this.add(buttonPanel, BorderLayout.EAST);

        this.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(10, 10, 10, 10),
                BorderFactory.createCompoundBorder(
                        BorderFactory.createEtchedBorder(EtchedBorder.RAISED),
                        BorderFactory.createEmptyBorder(5, 5, 5, 5))));

        changeBackgrounds();
        game.addListener(this);
    }

    /**
     * Change background of the legend panel
     */
    private void changeBackgrounds() {
        setBackground(Settings.getBackGroundColor());
        info.setBackground(Settings.getBackGroundColor());
        settingsButton.setBackground(Settings.getBackGroundColor());
        boardSettingsButton.setBackground(Settings.getBackGroundColor());
        buttonPanel.setBackground(Settings.getBackGroundColor());
    }

    /**
     * Creates the text pane for the legend panel
     */
    private void createTextPane() {
        info.setPreferredSize(new Dimension(Settings.BUTTON_PANEL_WIDTH - SETTINGS_BUTTON_SIZE,
                Settings.LEGEND_PANEL_HEIGHT - 30));
        info.setEditable(false);
        info.setOpaque(false);
        StyledDocument doc = info.getStyledDocument();
        MiscFunc.createsStyledDocument(doc);
    }

    private void createButtonPanel() {
        createSettingsButton();
        createBoardSettingsButton();

        buttonPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.NORTH;
        buttonPanel.add(settingsButton, gbc);
        gbc.gridy++;
        buttonPanel.add(boardSettingsButton, gbc);
        gbc.gridy++;
        gbc.weighty = 1.0;
//        gbc.ipady = 40;
        buttonPanel.add(Box.createRigidArea(new Dimension(SETTINGS_BUTTON_SIZE, 0)), gbc);
    }

    /**
     * Creates settings button
     */
    private void createSettingsButton() {
        settingsButton.setAction(new SettingsAction("", game, mainFrame));
        try {
            Image img = ImageIO.read(new File("fig/settingsIcon.png"));
            img = img.getScaledInstance(SETTINGS_BUTTON_SIZE, SETTINGS_BUTTON_SIZE, Image.SCALE_DEFAULT);
            settingsButton.setIcon(new ImageIcon(img));
        } catch (Exception ex) {
            System.err.println("Image file not found!");
        }

        settingsButton.setPreferredSize(new Dimension(SETTINGS_BUTTON_SIZE, SETTINGS_BUTTON_SIZE + 5));
        settingsButton.setBorder(BorderFactory.createEmptyBorder(0,0,5,0));
        settingsButton.setToolTipText("Click here to change the settings of the agents.");
    }

    private void createBoardSettingsButton() {
        boardSettingsButton.setAction(new BoardSettingsAction("", game, mainFrame));
        try {
            Image img = ImageIO.read(new File("fig/boardSettingsIcon.png"));
            img = img.getScaledInstance(SETTINGS_BUTTON_SIZE, SETTINGS_BUTTON_SIZE, Image.SCALE_DEFAULT);
            boardSettingsButton.setIcon(new ImageIcon(img));
        } catch (Exception ex) {
            System.err.println("Image file not found!");
        }

        boardSettingsButton.setPreferredSize(new Dimension(SETTINGS_BUTTON_SIZE, SETTINGS_BUTTON_SIZE + 10));
        boardSettingsButton.setBorder(BorderFactory.createEmptyBorder(5,0,5,0));
        boardSettingsButton.setToolTipText("Click here to change the settings of the board.");
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
        int offset = 41;
        int betweenSpace = 11;
        int height = 85;

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
        String spaces = "        ";
        for (int i = 0; i < Settings.CHIP_DIVERSITY; i++) {
            chipsNumbering.append(spaces);
            chipsNumbering.append(i);
        }
        content.add(chipsNumbering.toString());
        style.add("regular");

        for (int i = 0; i < 2; i++) {
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
