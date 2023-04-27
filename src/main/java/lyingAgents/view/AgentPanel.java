package lyingAgents.view;

import lyingAgents.controller.AgentInformationAction;
import lyingAgents.model.Game;
import lyingAgents.model.GameListener;
import lyingAgents.model.player.PlayerLying;
import lyingAgents.utilities.MiscFunc;
import lyingAgents.utilities.Settings;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.text.*;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * AgentPanel: makes the agent panel
 */
public class AgentPanel extends JComponent implements GameListener {

    /**
     * Formatter for double values
     */
    private static final DecimalFormat df = new DecimalFormat("0.00");

    /**
     * Text pane with info about the agent
     */
    private final JTextPane info;

    /**
     * Pane with messages of the agent
     */
    private final JTextPane messagePane;

    /**
     * Scroll pane with the messages of the agent
     */
    private final JScrollPane messageScroll;

    /**
     * Contains the buttons on this agent panel
     */
    private final JPanel buttonPanel;

    /**
     * The main frame where this panel is built upon
     */
    private final JFrame mainFrame;

    /**
     * Size of the button to get information on the agent
     */
    private static final int INFORMATION_BUTTON_SIZE = 40;

    /**
     * Content of the info text pane
     */
    private final String[] content = new String[13];
    private final String[] style = new String[13];

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
    private PlayerLying agent;

    /**
     * The name of the agent
     */
    private final String agentName;

    /**
     * The game model
     */
    private final Game game;

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
     */
    public AgentPanel(Game game, String agentName, JFrame mainFrame) {
        this.game = game;
        this.agentName = agentName;
        setAgent();
        this.mainFrame = mainFrame;

        this.initialChips = agent.getChipsBin();
        this.initialPoints = agent.getUtilityValue();
        this.setForeground(Color.WHITE);

        this.setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        this.info = new JTextPane();
        createTextPane();
        updateInfo();

        this.buttonPanel = new JPanel();
        createInformationButton();

        this.messagePane = new JTextPane();
        this.messageScroll = new JScrollPane(this.messagePane);
        createScrollMessagePane();
        createMessages();
        updateMessages();

        changeBackgrounds();

        this.add(info, BorderLayout.NORTH);
        this.add(buttonPanel, BorderLayout.CENTER);
        this.add(messageScroll, BorderLayout.SOUTH);

        this.agent.getGame().addListener(this);
    }

    private void createInformationButton() {
        JButton informationButton = new JButton();
        informationButton.setAction(new AgentInformationAction(agentName, game, mainFrame));

        informationButton.setPreferredSize(new Dimension(INFORMATION_BUTTON_SIZE, 0));
        informationButton.setToolTipText("Click here to see information about the " + this.agentName + ".");

        buttonPanel.setLayout(new BorderLayout());
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        buttonPanel.add(informationButton, BorderLayout.CENTER);
    }

    /**
     * Sets the agent with the corresponding name to this panel
     */
    private void setAgent() {
        this.agent = agentName.equals(Settings.INITIATOR_NAME) ? game.getInitiator() : game.getResponder();
    }

    /**
     * Changes the backgrounds of this panel
     */
    private void changeBackgrounds() {
        Color color = ViewSettings.getBackGroundColor();
        this.info.setBackground(color);
        this.buttonPanel.setBackground(color);
        this.messageScroll.setBackground(color);
        this.messagePane.setBackground(color);
        this.setBackground(color);
    }

    /**
     * Creates text panel for this agent panel
     */
    private void createTextPane() {
        info.setPreferredSize(new Dimension(ViewSettings.BUTTON_PANEL_WIDTH - 20, ViewSettings.AGENT_TEXT_HEIGHT));
        info.setMaximumSize(new Dimension(ViewSettings.BUTTON_PANEL_WIDTH, ViewSettings.AGENT_TEXT_HEIGHT));
        info.setEditable(false);
        info.setOpaque(false);
        info.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0),
                BorderFactory.createCompoundBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED),
                        BorderFactory.createEmptyBorder(5, 5, 5, 5))));
        StyledDocument doc = info.getStyledDocument();
        MiscFunc.createsStyledDocument(doc);
    }

    /**
     * Creates a scroll pane for the messages of the agent
     */
    private void createScrollMessagePane() {
        messageScroll.setPreferredSize(new Dimension(ViewSettings.BUTTON_PANEL_WIDTH,
                ViewSettings.AGENT_PANEL_HEIGHT - ViewSettings.AGENT_TEXT_HEIGHT - INFORMATION_BUTTON_SIZE - 20));
        messageScroll.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(5, 0, 0, 0),
                BorderFactory.createCompoundBorder(
                        BorderFactory.createEtchedBorder(EtchedBorder.RAISED),
                        BorderFactory.createEmptyBorder(5, 5, 5, 5))));
        messageScroll.setOpaque(false);

        messagePane.setEditable(false);
        messagePane.setOpaque(false);

        MiscFunc.createsStyledDocument(messagePane.getStyledDocument());
    }

    /**
     * Adds an agent message to the messages panel
     */
    public void addAgentMessages() {
        List<String> messages = agent.getMessages();
        for (String message : messages) {
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
        int idx = 0;
        style[idx] = "bold";
        content[idx++] = agent.getName() + " (ToM=" + agent.getOrderToM() + ", lr=" + df.format(agent.getLearningSpeed()) + ", lie=" + agent.isCanMakeFalseStatements() + ")";
        content[idx++] = "initial chips: " + Arrays.toString(this.initialChips);
        content[idx++] = "";
        content[idx++] = "";
        content[idx++] = "initial points: " + this.initialPoints;

        for (int i = 1; i < idx; i++) {
            style[i] = "regular";
        }

        if (agent.getGame().isGameFinished()) {
            style[idx] = "regular";
            content[idx++] = "---";
            style[idx] = "regular";
            content[idx++] = "final distribution chips: " + Arrays.toString(agent.getChipsBin());
            style[idx] = "regular";
            content[idx++] = "";
            style[idx] = "regular";
            content[idx++] = "";
            style[idx] = "regular";
            content[idx++] = "total nr. offers: " + game.getTotalNrOffersMade();
            style[idx] = "italic";
            content[idx++] = "final (total) points: " + agent.getUtilityValue() + " (" + agent.getFinalPoints() + ")";
        }

        while (idx < content.length) {
            style[idx] = "regular";
            content[idx++] = "";
        }
        MiscFunc.addStylesToDocMulti(info, content, style);
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
        int tokenSize = 30;
        int offset = 30;
        int height = 65;

        int trackNumChips = 0;
        for (int color = 0; color < Settings.CHIP_DIVERSITY; color++) {
            for (int num = 0; num < this.initialChips[color]; num++) {
                g2.setColor(Settings.getColor(color));
                g2.fillOval(offset + trackNumChips * tokenSize, height, tokenSize, tokenSize);
                trackNumChips += 1;
            }
        }

        if (agent.getGame().isGameFinished()) {
            trackNumChips = 0;
            int[] chips = agent.getChipsBin();
            height = 170;
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
        setAgent();
        this.initialChips = agent.getChipsBin();
        this.initialPoints = agent.getUtilityValue();
        gameChanged();
    }
}
