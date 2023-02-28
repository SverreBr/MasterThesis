package gui;

import utilities.Settings;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;
import java.awt.*;

public class SettingsDialog extends JDialog {

    private final JPanel optionPanel;

    private JTextField initiatorToMField;
    private int initiatorToMFieldValue;
    private JTextField initiatorLRField;
    private double initiatorLRFieldValue;

    private JTextField responderToMField;
    private int responderToMFieldValue;
    private JTextField responderLRField;
    private double responderLRFieldValue;

    public boolean gameHasChanged = false;

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
        pack();
    }

    private void createOptionPanel() {
        optionPanel.setLayout(new GridLayout(0,2));

        JTextPane initiatorToMText = new JTextPane();
        addStylesToDoc(initiatorToMText, "Initiator ToM: ");
        initiatorToMField = new JTextField("0",0);

        JTextPane initiatorLRText = new JTextPane();
        addStylesToDoc(initiatorLRText, "Initiator learning rate: ");
        initiatorLRField = new JTextField("0.1",0);

        JTextPane responderToMText = new JTextPane();
        addStylesToDoc(responderToMText, "Responder ToM: ");
        responderToMField = new JTextField("0",0);

        JTextPane responderLRText = new JTextPane();
        addStylesToDoc(responderLRText, "Responder learning rate: ");
        responderLRField = new JTextField("0.1",0);

        optionPanel.add(initiatorToMText);
        optionPanel.add(initiatorToMField);
        optionPanel.add(initiatorLRText);
        optionPanel.add(initiatorLRField);

        optionPanel.add(responderToMText);
        optionPanel.add(responderToMField);
        optionPanel.add(responderLRText);
        optionPanel.add(responderLRField);
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

        setVisible(false);
    }

    public int getInitiatorToMFieldValue() {
        return this.initiatorToMFieldValue;
    }

    public double getInitiatorLRFieldValue() {
        return this.initiatorLRFieldValue;
    }

    public int getResponderToMFieldValue() {
        return this.responderToMFieldValue;
    }

    public double getResponderLRFieldValue() {
        return this.responderLRFieldValue;
    }

    private void noButton() {
        initiatorToMFieldValue = -1;
        responderToMFieldValue = -1;

        setVisible(false);
    }
}
