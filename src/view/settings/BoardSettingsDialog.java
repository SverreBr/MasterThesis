package view.settings;

import view.Popups;
import utilities.Settings;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;
import java.awt.*;

public class BoardSettingsDialog extends JDialog {

    private final BoardSettingsBoard boardSettingsBoard;
    private BoardSettingsChips boardSettingsChipsInitiator;
    private BoardSettingsChips boardSettingsChipsResponder;
    private final JPanel agentSettingsPanel;

    private final JPanel btnPanel;

    private boolean gameHasChanged;

    private JTextField initiatorGPField;
    private int initiatorGPFieldValue;
    private JTextField responderGPField;
    private int responderGPFieldValue;

    public BoardSettingsDialog(JFrame owner, String title) {
        super(owner, title, true);

        boardSettingsBoard = new BoardSettingsBoard();
        boardSettingsChipsInitiator = new BoardSettingsChips();
        boardSettingsChipsResponder = new BoardSettingsChips();
        agentSettingsPanel = new JPanel();
        createAgentSettingsPanel();

        btnPanel = new JPanel();
        createButtonPanel();

        getContentPane().add(boardSettingsBoard, BorderLayout.WEST);
        getContentPane().add(agentSettingsPanel, BorderLayout.EAST);
        getContentPane().add(btnPanel, BorderLayout.SOUTH);
        setResizable(false);
        pack();
        this.setLocationRelativeTo(null);
    }

    private void createAgentSettingsPanel() {
        agentSettingsPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        agentSettingsPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        JTextPane someText;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.LINE_START;

        someText = new JTextPane();
        addStylesToDoc(someText, "Leave a field empty for random insertion.", "boldalic");
        agentSettingsPanel.add(someText, gbc);
        gbc.gridy++;
        gbc.gridwidth = 1;

        someText = new JTextPane();
        addStylesToDoc(someText, "- Initiator goal position: ", "regular");
        agentSettingsPanel.add(someText, gbc);
        gbc.gridx++;
        initiatorGPField = new JTextField("", 2);
        agentSettingsPanel.add(initiatorGPField, gbc);
        gbc.gridy++;
        gbc.gridx = 0;

        someText = new JTextPane();
        addStylesToDoc(someText, "- Responder goal position: ", "regular");
        agentSettingsPanel.add(someText, gbc);
        gbc.gridx++;
        responderGPField = new JTextField("", 2);
        agentSettingsPanel.add(responderGPField, gbc);
        gbc.gridy++;
        gbc.gridx = 0;

        someText = new JTextPane();
        addStylesToDoc(someText, "- Initiator chips: ", "regular");
        agentSettingsPanel.add(someText, gbc);
        gbc.gridx++;
        agentSettingsPanel.add(boardSettingsChipsInitiator, gbc);
        gbc.gridy++;
        gbc.gridx = 0;

        someText = new JTextPane();
        addStylesToDoc(someText, "- Responder chips: ", "regular");
        agentSettingsPanel.add(someText, gbc);
        gbc.gridx++;
        agentSettingsPanel.add(boardSettingsChipsResponder, gbc);
    }

    /**
     * Adds the styles to the document
     *
     * @param pane the pane to be styled
     * @param text the text to add to the pane
     */
    private void addStylesToDoc(JTextPane pane, String text, String style) {
        StyledDocument doc = pane.getStyledDocument();
        Settings.addStylesToDocument(doc);
        try {
            doc.remove(0, doc.getLength());
            doc.insertString(doc.getLength(), text, doc.getStyle(style));
        } catch (BadLocationException ble) {
            System.err.println("Couldn't insert text into text pane.");
        }
        pane.setEditable(false);
        pane.setOpaque(false);
    }

    private void createButtonPanel() {
        JButton okBtn = new JButton("Accept");
        okBtn.addActionListener(e -> okButton());
        btnPanel.add(okBtn);

        JButton noBtn = new JButton("Cancel");
        noBtn.addActionListener(e -> noButton());
        btnPanel.add(noBtn);
    }

    private void okButton() {
        this.gameHasChanged = true;
        int numGoalPos = Settings.getNumberOfGoalPositions();

        try {
            initiatorGPFieldValue = Integer.parseInt(initiatorGPField.getText());
            if (initiatorGPFieldValue < 0 || initiatorGPFieldValue >= numGoalPos) {
                Popups.showInvalidGoalPosition(Settings.INITIATOR_NAME, numGoalPos);
                this.gameHasChanged = false;
            }
        } catch (NumberFormatException e) {
            if (!initiatorGPField.getText().equals("")) {
                Popups.showInvalidGoalPosition(Settings.INITIATOR_NAME, numGoalPos);
                this.gameHasChanged = false;
            } else {
                initiatorGPFieldValue = (int) (Math.random() * numGoalPos);
            }
        }

        try {
            responderGPFieldValue = Integer.parseInt(responderGPField.getText());
            if (responderGPFieldValue < 0 || responderGPFieldValue >= numGoalPos) {
                Popups.showInvalidGoalPosition(Settings.RESPONDER_NAME, numGoalPos);
                this.gameHasChanged = false;
            }
        } catch (NumberFormatException e) {
            if (!responderGPField.getText().equals("")) {
                Popups.showInvalidGoalPosition(Settings.RESPONDER_NAME, numGoalPos);
                this.gameHasChanged = false;
            } else {
                responderGPFieldValue = (int) (Math.random() * numGoalPos);
            }
        }

        if (this.gameHasChanged)
            setVisible(false);
    }

    private void noButton() {
        this.gameHasChanged = false;
        setVisible(false);
    }

    public boolean isGameHasChanged() {
        return gameHasChanged;
    }

    public int getIntColorTile(int row, int col) {
        return boardSettingsBoard.getIntColorTile(row, col);
    }

    public int[] getGoalPositions() {
        int[] goalPos = new int[2];
        goalPos[0] = initiatorGPFieldValue;
        goalPos[1] = responderGPFieldValue;
        return goalPos;
    }

    public int getChipInitiator(int idx) {
        return boardSettingsChipsInitiator.getChip(idx);
    }

    public int getChipResponder(int idx) {
        return boardSettingsChipsResponder.getChip(idx);
    }
}
