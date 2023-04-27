package lyingAgents.view.changeSettings;

import lyingAgents.model.Game;
import lyingAgents.utilities.MiscFunc;
import lyingAgents.view.Popups;
import lyingAgents.utilities.Settings;
import lyingAgents.view.ViewSettings;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

/**
 * SettingsDialog class: shown when changing the settings of the agents
 */
public class AgentSettingsDialog extends JDialog {

    /**
     * Panel to choose the options
     */
    private final JPanel optionPanel;

    /**
     * Text field containing the ToM of the initiator
     */
    private JComboBox<String> initiatorToMField;

    /**
     * Value of the text field containing the ToM of the initiator
     */
    private int initiatorToMFieldValue;

    /**
     * Text field containing the learning speed of the initiator
     */
    private JTextField initiatorLRField;

    /**
     * Value of the text field containing the learning speed of the initiator
     */
    private double initiatorLRFieldValue;

    /**
     * Text field containing whether the initiator can lie
     */
    private JComboBox<String> initiatorCanLieField;

    /**
     * Value of the text field containing whether the initiator can lie
     */
    private boolean initiatorCanLieFieldValue;

    private JComboBox<String> initiatorCanSendMessagesField;
    private boolean initiatorCanSendMessagesValue;

    /**
     * Text field containing the ToM of the responder
     */
    private JComboBox<String> responderToMField;

    /**
     * Value of the text field containing the ToM of the responder
     */
    private int responderToMFieldValue;

    /**
     * Text field containing the learning speed of the responder
     */
    private JTextField responderLRField;

    /**
     * Value of the text field containing the learning speed of the responder
     */
    private double responderLRFieldValue;

    /**
     * Text field containing whether the responder can lie
     */
    private JComboBox<String> responderCanLieField;

    /**
     * Value of the text field containing whether the responder can lie
     */
    private boolean responderCanLieFieldValue;

    private JComboBox<String> responderCanSendMessagesField;
    private boolean responderCanSendMessagesValue;

    /**
     * Boolean to check if the game has changed
     */
    public boolean gameHasChanged = false;

    private final JPanel btnPanel;

    /**
     * Constructor
     *
     * @param mainFrame Mainframe of the visuals
     * @param title     Title given to this dialog
     */
    public AgentSettingsDialog(JFrame mainFrame, String title, Game game) {
        super(mainFrame, title, true);
        btnPanel = new JPanel();

        JButton okBtn = new JButton("Accept");
        okBtn.addActionListener(e -> okButton());
        btnPanel.add(okBtn);

        JButton noBtn = new JButton("Cancel");
        noBtn.addActionListener(e -> noButton());
        btnPanel.add(noBtn);

        optionPanel = new JPanel();
        createOptionPanel(game);

        getContentPane().add(optionPanel);
        getContentPane().add(btnPanel, BorderLayout.SOUTH);
        setBackgrounds();
        setResizable(false);
        pack();
        this.setLocationRelativeTo(null);
    }

    private void setBackgrounds() {
        Color bgColor = ViewSettings.getBackGroundColor();
        this.setBackground(bgColor);
        optionPanel.setBackground(bgColor);
        btnPanel.setBackground(bgColor);
        initiatorToMField.setBackground(bgColor);
        initiatorLRField.setBackground(bgColor);
        initiatorCanSendMessagesField.setBackground(bgColor);
        initiatorCanLieField.setBackground(bgColor);
        responderToMField.setBackground(bgColor);
        responderLRField.setBackground(bgColor);
        responderCanLieField.setBackground(bgColor);
        responderCanSendMessagesField.setBackground(bgColor);
    }

    /**
     * Creates the options panel
     */
    private void createOptionPanel(Game game) {
        optionPanel.setLayout(new GridLayout(0, 2));

        JTextPane initiatorToMText = new JTextPane();
        MiscFunc.addStylesToDoc(initiatorToMText, "Initiator ToM: ", "regular");
        initiatorToMField = new JComboBox<>(new String[]{"0", "1", "2"});
        initiatorToMField.setSelectedItem(String.valueOf(game.getInitiator().getOrderToM()));

        JTextPane initiatorLRText = new JTextPane();
        MiscFunc.addStylesToDoc(initiatorLRText, "Initiator learning rate: ", "regular");
        initiatorLRField = new JTextField(String.valueOf(game.getInitiator().getLearningSpeed()), 0);

        JTextPane initiatorMessagesText = new JTextPane();
        MiscFunc.addStylesToDoc(initiatorMessagesText, "Initiator can send messages: ", "regular");
        initiatorCanSendMessagesField = new JComboBox<>(new String[]{"false", "true"});
        initiatorCanSendMessagesField.setSelectedItem(String.valueOf(game.getInitiator().isCanSendMessages()));

        JTextPane initiatorLieText = new JTextPane();
        MiscFunc.addStylesToDoc(initiatorLieText, "Initiator can lie: ", "regular");
        initiatorCanLieField = new JComboBox<>(new String[]{"false", "true"});
        initiatorCanLieField.setSelectedItem(String.valueOf(game.getInitiator().isCanLie()));


        JTextPane responderToMText = new JTextPane();
        MiscFunc.addStylesToDoc(responderToMText, "Responder ToM: ", "regular");
        responderToMField = new JComboBox<>(new String[]{"0", "1", "2"});
        responderToMField.setSelectedItem(String.valueOf(game.getResponder().getOrderToM()));

        JTextPane responderLRText = new JTextPane();
        MiscFunc.addStylesToDoc(responderLRText, "Responder learning rate: ", "regular");
        responderLRField = new JTextField(String.valueOf(game.getResponder().getLearningSpeed()), 0);

        JTextPane responderMessagesText = new JTextPane();
        MiscFunc.addStylesToDoc(responderMessagesText, "Responder can send messages: ", "regular");
        responderCanSendMessagesField = new JComboBox<>(new String[]{"false", "true"});
        responderCanSendMessagesField.setSelectedItem(String.valueOf(game.getResponder().isCanSendMessages()));

        JTextPane responderLieText = new JTextPane();
        MiscFunc.addStylesToDoc(responderLieText, "Responder can lie: ", "regular");
        responderCanLieField = new JComboBox<>(new String[]{"false", "true"});
        responderCanLieField.setSelectedItem(String.valueOf(game.getResponder().isCanLie()));

        addFieldsToPanel(initiatorToMText, initiatorLRText, initiatorMessagesText, initiatorLieText, initiatorToMField, initiatorLRField, initiatorCanSendMessagesField, initiatorCanLieField);

        addFieldsToPanel(responderToMText, responderLRText, responderMessagesText, responderLieText, responderToMField, responderLRField, responderCanSendMessagesField, responderCanLieField);
    }

    private void addFieldsToPanel(JTextPane tomText, JTextPane lrText, JTextPane messagesText, JTextPane lieText,
                                  JComboBox<String> toMField, JTextField lrField, JComboBox<String> canSendMessagesField, JComboBox<String> canLieField) {
        optionPanel.add(tomText);
        optionPanel.add(toMField);
        optionPanel.add(lrText);
        optionPanel.add(lrField);
        optionPanel.add(messagesText);
        optionPanel.add(canSendMessagesField);
        optionPanel.add(lieText);
        optionPanel.add(canLieField);
    }

    /**
     * Action performed when clicked on OK
     */
    private void okButton() {
        this.gameHasChanged = true;
        initiatorToMFieldValue = Integer.parseInt((String) Objects.requireNonNull(initiatorToMField.getSelectedItem()));

        try {
            initiatorLRFieldValue = Double.parseDouble(initiatorLRField.getText());
        } catch (NumberFormatException ex) {
            initiatorLRFieldValue = -1;
        } finally {
            if (initiatorLRFieldValue < 0 || initiatorLRFieldValue > 1) {
                Popups.showInvalidLR(Settings.INITIATOR_NAME);
                this.gameHasChanged = false;
            }
        }

        initiatorCanSendMessagesValue = Boolean.parseBoolean((String) initiatorCanSendMessagesField.getSelectedItem());

        initiatorCanLieFieldValue = Boolean.parseBoolean((String) initiatorCanLieField.getSelectedItem());
        if ((initiatorCanLieFieldValue && !initiatorCanSendMessagesValue) || (initiatorCanLieFieldValue && (initiatorToMFieldValue < 2))) {
            Popups.showInvalidCanLieToM(Settings.INITIATOR_NAME);
            this.gameHasChanged = false;
        }

        responderToMFieldValue = Integer.parseInt((String) Objects.requireNonNull(responderToMField.getSelectedItem()));

        try {
            responderLRFieldValue = Double.parseDouble(responderLRField.getText());
        } catch (NumberFormatException ex) {
            responderLRFieldValue = -1;
        } finally {
            if (responderLRFieldValue < 0 || responderLRFieldValue > 1) {
                Popups.showInvalidLR(Settings.RESPONDER_NAME);
                this.gameHasChanged = false;
            }
        }

        responderCanSendMessagesValue = Boolean.parseBoolean((String) responderCanSendMessagesField.getSelectedItem());

        responderCanLieFieldValue = Boolean.parseBoolean((String) responderCanLieField.getSelectedItem());
        if ((responderCanLieFieldValue && !responderCanSendMessagesValue) || (responderCanLieFieldValue && (responderToMFieldValue < 2))) {
            Popups.showInvalidCanLieToM(Settings.RESPONDER_NAME);
            this.gameHasChanged = false;
        }

        if (this.gameHasChanged)
            setVisible(false);
    }

    /**
     * Action performed when clicked on Cancel
     */
    private void noButton() {
        initiatorToMFieldValue = -1;
        responderToMFieldValue = -1;
        setVisible(false);
    }

    /**
     * Getter for initiator ToM
     *
     * @return ToM for the initiator
     */
    public int getInitiatorToMFieldValue() {
        return this.initiatorToMFieldValue;
    }

    /**
     * Getter for initiator learning speed
     *
     * @return learning speed for the initiator
     */
    public double getInitiatorLRFieldValue() {
        return this.initiatorLRFieldValue;
    }

    /**
     * Getter for responder ToM
     *
     * @return ToM for the responder
     */
    public int getResponderToMFieldValue() {
        return this.responderToMFieldValue;
    }

    /**
     * Getter for responder learning speed
     *
     * @return learning speed for the responder
     */
    public double getResponderLRFieldValue() {
        return this.responderLRFieldValue;
    }

    /**
     * Getter for whether the initiator can lie
     *
     * @return True if the initiator can lie, false otherwise
     */
    public boolean isInitiatorCanLieFieldValue() {
        return initiatorCanLieFieldValue;
    }

    /**
     * Getter for whether the responder can lie
     *
     * @return True if the responder can lie, false otherwise
     */
    public boolean isResponderCanLieFieldValue() {
        return responderCanLieFieldValue;
    }

    public boolean isResponderCanSendMessagesValue() {
        return responderCanSendMessagesValue;
    }

    public boolean isInitiatorCanSendMessagesValue() {
        return initiatorCanSendMessagesValue;
    }
}
