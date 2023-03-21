package gui.settings;

import utilities.Settings;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;
import java.awt.*;

public class BoardSettingsDialog extends JDialog {

    private final BoardSettingsBoard boardPanel;
    private final JPanel agentSettingsPanel;

    private final JPanel btnPanel;

    private boolean gameHasChanged;

    private JTextField initiatorGPField;
    private int initiatorGPFieldValue;
    private JTextField responderGPField;
    private int responderGPFieldValue;

    public BoardSettingsDialog(JFrame owner, String title) {
        super(owner, title, true);

        boardPanel = new BoardSettingsBoard();
        agentSettingsPanel = new JPanel();
        createAgentSettingsPanel();

        btnPanel = new JPanel();
        createButtonPanel();

        getContentPane().add(boardPanel, BorderLayout.WEST);
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

        JTextPane initiatorGPText = new JTextPane();
        addStylesToDoc(initiatorGPText, "Initiator goal position: ");
        initiatorGPField = new JTextField("0", 5);

        JTextPane responderGPText = new JTextPane();
        addStylesToDoc(responderGPText, "Responder goal position: ");
        responderGPField = new JTextField("0", 5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.LINE_START;
        agentSettingsPanel.add(initiatorGPText, gbc);
        gbc.gridx++;
        agentSettingsPanel.add(initiatorGPField, gbc);
        gbc.gridy++;
        gbc.gridx = 0;
        agentSettingsPanel.add(responderGPText, gbc);
        gbc.gridx++;
        agentSettingsPanel.add(responderGPField, gbc);
        // TODO: add chips.
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

        try {
            initiatorGPFieldValue = Integer.parseInt(initiatorGPField.getText());
        } catch (NumberFormatException e) {
            initiatorGPFieldValue = -1;
        }
        if (initiatorGPFieldValue < 0 || initiatorGPFieldValue > 12) { // TODO: Change this to number of goal positions...
            this.gameHasChanged = false;
        }

        try {
            responderGPFieldValue = Integer.parseInt(responderGPField.getText());
        } catch (NumberFormatException e) {
            responderGPFieldValue = -1;
        }
        if (responderGPFieldValue < 0 || responderGPFieldValue > 12) { // TODO: Change this to number of goal positions...
            this.gameHasChanged = false;
        }

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
        return boardPanel.getIntColorTile(row, col);
    }

    public int[] getGoalPositions() {
        int[] goalPos = new int[2];
        goalPos[0] = initiatorGPFieldValue;
        goalPos[1] = responderGPFieldValue;
        return goalPos;
    }
}
