package lyingAgents.view.settings;

import lyingAgents.utilities.MiscFunc;
import lyingAgents.view.Popups;
import lyingAgents.utilities.Settings;

import javax.swing.*;
import java.awt.*;

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
    private JTextField initiatorToMField;

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
    private JTextField initiatorLieField;

    /**
     * Value of the text field containing whether the initiator can lie
     */
    private boolean initiatorLieFieldValue;

    /**
     * Text field containing the ToM of the responder
     */
    private JTextField responderToMField;

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
    private JTextField responderLieField;

    /**
     * Value of the text field containing whether the responder can lie
     */
    private boolean responderLieFieldValue;

    /**
     * Boolean to check if the game has changed
     */
    public boolean gameHasChanged = false;

    /**
     * Constructor
     *
     * @param mainFrame Mainframe of the visuals
     * @param title     Title given to this dialog
     */
    public AgentSettingsDialog(JFrame mainFrame, String title) {
        super(mainFrame, title, true);
        JPanel btnPanel = new JPanel();

        JButton okBtn = new JButton("Accept");
        okBtn.addActionListener(e -> okButton());
        btnPanel.add(okBtn);

        JButton noBtn = new JButton("Cancel");
        noBtn.addActionListener(e -> noButton());
        btnPanel.add(noBtn);

        optionPanel = new JPanel();
        createOptionPanel();

        getContentPane().add(optionPanel);
        getContentPane().add(btnPanel, BorderLayout.SOUTH);
        setResizable(false);
        pack();
        this.setLocationRelativeTo(null);
    }

    /**
     * Creates the options panel
     */
    private void createOptionPanel() {
        optionPanel.setLayout(new GridLayout(0, 2));

        JTextPane initiatorToMText = new JTextPane();
        MiscFunc.addStylesToDoc(initiatorToMText, "Initiator ToM: ", "regular");
        initiatorToMField = new JTextField("0", 0);

        JTextPane initiatorLRText = new JTextPane();
        MiscFunc.addStylesToDoc(initiatorLRText, "Initiator learning rate: ", "regular");
        initiatorLRField = new JTextField("0.5", 0);

        JTextPane initiatorLieText = new JTextPane();
        MiscFunc.addStylesToDoc(initiatorLieText, "Initiator can lie: ", "regular");
        initiatorLieField = new JTextField("false", 0);

        JTextPane responderToMText = new JTextPane();
        MiscFunc.addStylesToDoc(responderToMText, "Responder ToM: ", "regular");
        responderToMField = new JTextField("0", 0);

        JTextPane responderLRText = new JTextPane();
        MiscFunc.addStylesToDoc(responderLRText, "Responder learning rate: ", "regular");
        responderLRField = new JTextField("0.5", 0);

        JTextPane responderLieText = new JTextPane();
        MiscFunc.addStylesToDoc(responderLieText, "Responder can lie: ", "regular");
        responderLieField = new JTextField("false", 0);

        optionPanel.add(initiatorToMText);
        optionPanel.add(initiatorToMField);
        optionPanel.add(initiatorLRText);
        optionPanel.add(initiatorLRField);
        optionPanel.add(initiatorLieText);
        optionPanel.add(initiatorLieField);

        optionPanel.add(responderToMText);
        optionPanel.add(responderToMField);
        optionPanel.add(responderLRText);
        optionPanel.add(responderLRField);
        optionPanel.add(responderLieText);
        optionPanel.add(responderLieField);
    }

    /**
     * Action performed when clicked on OK
     */
    private void okButton() {
        this.gameHasChanged = true;
        try {
            initiatorToMFieldValue = Integer.parseInt(initiatorToMField.getText());
        } catch (NumberFormatException ex) {
            initiatorToMFieldValue = -1;
        }
        if (initiatorToMFieldValue < 0 || initiatorToMFieldValue > 2) {
            Popups.showInvalidOrderToM(Settings.INITIATOR_NAME);
            this.gameHasChanged = false;
        }

        try {
            initiatorLRFieldValue = Double.parseDouble(initiatorLRField.getText());
        } catch (NumberFormatException ex) {
            initiatorLRFieldValue = -1;
        }
        if (initiatorLRFieldValue < 0 || initiatorLRFieldValue > 1) {
            Popups.showInvalidLR(Settings.INITIATOR_NAME);
            this.gameHasChanged = false;
        }


        initiatorLieFieldValue = Boolean.parseBoolean(initiatorLieField.getText());
        if (this.gameHasChanged && initiatorLieFieldValue && (initiatorToMFieldValue < 2)) {
            Popups.showInvalidCanLieToM(Settings.INITIATOR_NAME, initiatorToMFieldValue);
            this.gameHasChanged = false;
        }

        try {
            responderToMFieldValue = Integer.parseInt(responderToMField.getText());
        } catch (NumberFormatException ex) {
            responderToMFieldValue = -1;
        }
        if (responderToMFieldValue < 0 || responderToMFieldValue > 2) {
            Popups.showInvalidOrderToM(Settings.RESPONDER_NAME);
            this.gameHasChanged = false;
        }

        try {
            responderLRFieldValue = Double.parseDouble(responderLRField.getText());
        } catch (NumberFormatException ex) {
            responderLRFieldValue = -1;
        }
        if (responderLRFieldValue < 0 || responderLRFieldValue > 1) {
            Popups.showInvalidLR(Settings.RESPONDER_NAME);
            this.gameHasChanged = false;
        }

        responderLieFieldValue = Boolean.parseBoolean(responderLieField.getText());
        if (this.gameHasChanged && responderLieFieldValue && (responderToMFieldValue < 2)) {
            Popups.showInvalidCanLieToM(Settings.RESPONDER_NAME, responderToMFieldValue);
            this.gameHasChanged = false;
        }

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
    public boolean isInitiatorLieFieldValue() {
        return initiatorLieFieldValue;
    }

    /**
     * Getter for whether the responder can lie
     *
     * @return True if the responder can lie, false otherwise
     */
    public boolean isResponderLieFieldValue() {
        return responderLieFieldValue;
    }
}
