package gui.settings;

import gui.Popups;
import utilities.Settings;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;
import java.awt.*;

/**
 * SettingsDialog class: shown when changing the settings
 */
public class SettingsDialog extends JDialog {

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

    private JTextField initiatorLieField;
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

    private JTextField responderLieField;
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
    public SettingsDialog(JFrame mainFrame, String title) {
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
        addStylesToDoc(initiatorToMText, "Initiator ToM: ");
        initiatorToMField = new JTextField("0", 0);

        JTextPane initiatorLRText = new JTextPane();
        addStylesToDoc(initiatorLRText, "Initiator learning rate: ");
        initiatorLRField = new JTextField("0.5", 0);

        JTextPane initiatorLieText = new JTextPane();
        addStylesToDoc(initiatorLieText, "Initiator can lie: ");
        initiatorLieField = new JTextField("false", 0);

        JTextPane responderToMText = new JTextPane();
        addStylesToDoc(responderToMText, "Responder ToM: ");
        responderToMField = new JTextField("0", 0);

        JTextPane responderLRText = new JTextPane();
        addStylesToDoc(responderLRText, "Responder learning rate: ");
        responderLRField = new JTextField("0.5", 0);

        JTextPane responderLieText = new JTextPane();
        addStylesToDoc(responderLieText, "Responder can lie: ");
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
     * Adds the styles to the document
     *
     * @param pane the pane to be styled
     * @param text the text to add to the pane
     */
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

    public boolean isInitiatorLieFieldValue() {
        return initiatorLieFieldValue;
    }

    public boolean isResponderLieFieldValue() {
        return responderLieFieldValue;
    }
}
