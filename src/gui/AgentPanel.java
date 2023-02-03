package gui;

import controller.GameListener;
import utilities.Player;
import utilities.Settings;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.text.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;


/**
 * AgentPanel: makes the agent panel
 */
public class AgentPanel extends JComponent implements GameListener {

    /**
     * Text pane with info about the agent
     */
    private final JTextPane info;

    private final JTextPane messagePane;

    private final JScrollPane messageScroll;

    /**
     * Content of the info text pane
     */
    private final String[] content = new String[12];

    /**
     * The style of the content of the text pane
     */
    private final String[] style = {
            "bold", "regular", "regular", "regular", "regular",
            "regular", "regular", "regular", "regular", "regular",
            "regular", "regular"
    };

    /**
     * Content of the messages text pane
     */
    private List<String> messages;

    /**
     * The style of the content of the messages pane
     */
    private List<String> styleMessages;

    /**
     * The agent model of this panel
     */
    private final Player agent;

    /**
     * Initial chips of this agent
     */
    private int[] initialChips;

    /**
     * Initial points of this agent
     */
    private int initialPoints;

    /**
     * Constructor of the agent panel
     *
     * @param agent the model of the agent
     */
    public AgentPanel(Player agent) {
        this.agent = agent;
        this.initialChips = agent.getChips().clone();
        this.initialPoints = agent.calculateCurrentScore();

        this.setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        this.info = new JTextPane();
        createTextPane();
        updateInfo();

        this.messagePane = new JTextPane();
        this.messageScroll = new JScrollPane(this.messagePane);
        createScrollMessagePane();
        createMessages();
        updateMessages();

        changeBackgrounds();

        this.add(info, BorderLayout.NORTH);
        this.add(messageScroll, BorderLayout.CENTER);

        this.agent.getGame().addListener(this);
    }

    /**
     * Changes the backgrounds of this panel
     */
    private void changeBackgrounds() {
        this.info.setBackground(Settings.getBackGroundColor());
        this.messageScroll.setBackground(Settings.getBackGroundColor());
        this.messagePane.setBackground(Settings.getBackGroundColor());
        this.setBackground(Settings.getBackGroundColor());
    }

    /**
     * Creates text panel for this agent panel
     */
    private void createTextPane() {
        info.setPreferredSize(new Dimension(Settings.BUTTON_PANEL_WIDTH - 20, Settings.AGENT_TEXT_HEIGHT));
        info.setMaximumSize(new Dimension(Settings.BUTTON_PANEL_WIDTH, Settings.AGENT_TEXT_HEIGHT));
        info.setEditable(false);
        info.setOpaque(false);
        info.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0), BorderFactory.createCompoundBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED), BorderFactory.createEmptyBorder(5, 5, 5, 5))));
        StyledDocument doc = info.getStyledDocument();
        Settings.addStylesToDocument(doc);
    }

    /**
     * Creates a scroll pane for the messages of the agent
     */
    private void createScrollMessagePane() {
        messageScroll.setPreferredSize(new Dimension(Settings.BUTTON_PANEL_WIDTH, Settings.AGENT_PANEL_HEIGHT - Settings.AGENT_TEXT_HEIGHT));
        messageScroll.setMaximumSize(new Dimension(Settings.BUTTON_PANEL_WIDTH, Settings.AGENT_PANEL_HEIGHT - Settings.AGENT_TEXT_HEIGHT));
        messageScroll.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(0, 0, 10, 0),
                BorderFactory.createCompoundBorder(
                        BorderFactory.createEtchedBorder(EtchedBorder.RAISED),
                        BorderFactory.createEmptyBorder(5, 5, 5, 5))));

        messagePane.setEditable(false);
        messagePane.setOpaque(false);

        StyledDocument doc = messagePane.getStyledDocument();
        Settings.addStylesToDocument(doc);
    }

    /**
     * Adds an agent message to the messages panel
     */
    public void addAgentMessages() {
        for (String message : agent.getMessages()) {
            this.messages.add(message);
            this.styleMessages.add("regular");
        }
    }

    /**
     * Creates new messages
     */
    private void createMessages() {
        messages = new ArrayList<>();
        styleMessages = new ArrayList<>();

        messages.add("Messages:");
        styleMessages.add("italic");
    }

    /**
     * Updates messages
     */
    private void updateMessages() {
        createMessages();
        addAgentMessages();
        // Generate agent info panel
        StyledDocument doc = messagePane.getStyledDocument();
        try {
            doc.remove(0, doc.getLength());
            for (int i = 0; i < messages.size(); i++) {
                doc.insertString(doc.getLength(), messages.get(i) + "\n", doc.getStyle(styleMessages.get(i)));
            }
        } catch (BadLocationException ble) {
            System.err.println("Couldn't insert text into message pane.");
        }
    }

    /**
     * Method to update the information on the info text panel
     */
    private void updateInfo() {
        content[0] = agent.getName();
        content[1] = "initial chips:";

        for (int i = 2; i <= 4; i++) {
            content[i] = "";
        }
        content[5] = "points: " + this.initialPoints;

        if (!agent.getGame().isGameEnabled()) {
            content[6] = "---";
            content[7] = "final distribution chips:";
            for (int i = 8; i <= 10; i++) {
                content[i] = "";
            }
            content[11] = "points: " + agent.getGame().getBoard().calculateScoreAgent(agent);
        } else {
            for (int i = 6; i <= 11; i++) {
                content[i] = "";
            }
        }
        generateAgentInfo();
    }

    private void generateAgentInfo() {
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

        int trackNumChips = 0;
        for (int color = 0; color < Settings.CHIP_DIVERSITY; color++) {
            for (int num = 0; num < this.initialChips[color]; num++) {
                g2.setColor(Settings.getColor(color));
                g2.fillOval(offset + trackNumChips * tokenSize, height, tokenSize, tokenSize);
                trackNumChips += 1;
            }

        }

        if (!agent.getGame().isGameEnabled()) {
            trackNumChips = 0;
            int[] chips = agent.getChips();
            height = 195;
            for (int color = 0; color < Settings.CHIP_DIVERSITY; color++) {
                for (int num = 0; num < chips[color]; num++) {
                    g2.setColor(Settings.getColor(color));
                    g2.fillOval(offset + trackNumChips * tokenSize, height, tokenSize, tokenSize);
                    trackNumChips += 1;
                }
            }
        }
    }

    @Override
    public void gameChanged() {
        updateInfo();
        updateMessages();
        this.repaint();
    }

    @Override
    public void newGame() {
        this.initialChips = agent.getChips().clone();
        this.initialPoints = agent.calculateCurrentScore();
        gameChanged();
    }
}
