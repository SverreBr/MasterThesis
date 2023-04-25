package lyingAgents.view.changeSettings;

import lyingAgents.model.Board;
import lyingAgents.model.Game;
import lyingAgents.utilities.Chips;
import lyingAgents.utilities.MiscFunc;
import lyingAgents.view.Popups;
import lyingAgents.utilities.Settings;

import javax.swing.*;
import java.awt.*;

/**
 * BoardSettingsDialog class: The class that deals with creating a dialog where the user can enter a new board setting
 */
public class GameSettingsDialog extends JDialog {

    /**
     * The board where the user can enter values that correspond to the colors of the board
     */
    private final GameSettingsBoard gameSettingsBoard;

    /**
     * The chips of the initiator where the user can enter values that correspond to the colors of the chips
     */
    private final GameSettingsChips gameSettingsChipsInitiator;

    /**
     * The chips of the responder where the user can enter values that correspond to the colors of the chips
     */
    private final GameSettingsChips gameSettingsChipsResponder;

    /**
     * Panel with settings of the agent that the user can change
     */
    private final JPanel agentSettingsPanel;

    /**
     * Panel with buttons where the user can either accept or cancel the operation
     */
    private final JPanel btnPanel;

    /**
     * Boolean value to indicate whether the user wants to change the game setting or not
     */
    private boolean gameHasChanged;

    /**
     * Text field containing the goal position of the initiator
     */
    private JTextField initiatorGPField;

    /**
     * Value of the text field that contains the goal position of the initiator
     */
    private int initiatorGPFieldValue;

    /**
     * Text field containing the goal position of the responder
     */
    private JTextField responderGPField;

    /**
     * Value of the text field that contains the goal position of the responder
     */
    private int responderGPFieldValue;

    /**
     * Constructor
     *
     * @param owner Frame that this dialog is put on top
     * @param title Title of this dialog
     */
    public GameSettingsDialog(JFrame owner, String title, Game game) {
        super(owner, title, true);

        gameSettingsBoard = new GameSettingsBoard(game.getBoard());
        gameSettingsChipsInitiator = new GameSettingsChips(Chips.getBins(game.getInitiator().getInitialChips(), game.getBinMaxChips()));
        gameSettingsChipsResponder = new GameSettingsChips(Chips.getBins(game.getResponder().getInitialChips(), game.getBinMaxChips()));
        agentSettingsPanel = new JPanel();
        createAgentSettingsPanel(game);

        btnPanel = new JPanel();
        createButtonPanel();

        getContentPane().add(gameSettingsBoard, BorderLayout.WEST);
        getContentPane().add(agentSettingsPanel, BorderLayout.EAST);
        getContentPane().add(btnPanel, BorderLayout.SOUTH);
        setResizable(false);
        pack();
        this.setLocationRelativeTo(null);
    }

    /**
     * Creates the agent settings panel
     */
    private void createAgentSettingsPanel(Game game) {
        agentSettingsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        agentSettingsPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        JTextPane someText;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.LINE_START;

        someText = new JTextPane();
        MiscFunc.addStylesToDoc(someText, "Leave a field empty for random insertion.", "boldalic");
        agentSettingsPanel.add(someText, gbc);
        gbc.gridy++;
        gbc.gridwidth = 1;

        someText = new JTextPane();
        MiscFunc.addStylesToDoc(someText, "- Initiator goal position: ", "regular");
        agentSettingsPanel.add(someText, gbc);
        gbc.gridx++;
        initiatorGPField = new JTextField(String.valueOf(game.getGoalPositionPlayer(Settings.INITIATOR_NAME)), 2);
        agentSettingsPanel.add(initiatorGPField, gbc);
        gbc.gridy++;
        gbc.gridx = 0;

        someText = new JTextPane();
        MiscFunc.addStylesToDoc(someText, "- Responder goal position: ", "regular");
        agentSettingsPanel.add(someText, gbc);
        gbc.gridx++;
        responderGPField = new JTextField(String.valueOf(game.getGoalPositionPlayer(Settings.RESPONDER_NAME)), 2);
        agentSettingsPanel.add(responderGPField, gbc);
        gbc.gridy++;
        gbc.gridx = 0;

        someText = new JTextPane();
        MiscFunc.addStylesToDoc(someText, "- Initiator chips: ", "regular");
        agentSettingsPanel.add(someText, gbc);
        gbc.gridx++;
        agentSettingsPanel.add(gameSettingsChipsInitiator, gbc);
        gbc.gridy++;
        gbc.gridx = 0;

        someText = new JTextPane();
        MiscFunc.addStylesToDoc(someText, "- Responder chips: ", "regular");
        agentSettingsPanel.add(someText, gbc);
        gbc.gridx++;
        agentSettingsPanel.add(gameSettingsChipsResponder, gbc);
    }

    /**
     * Creates the button panel
     */
    private void createButtonPanel() {
        btnPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;

        JButton removeAllBtn = new JButton("Remove all values");
        removeAllBtn.addActionListener(e -> removeAllValues());
        btnPanel.add(removeAllBtn, gbc);

        gbc.gridy++; gbc.gridwidth = 1;
        JButton okBtn = new JButton("Accept");
        okBtn.addActionListener(e -> okButton());
        btnPanel.add(okBtn, gbc);

        gbc.gridx++;
        JButton noBtn = new JButton("Cancel");
        noBtn.addActionListener(e -> noButton());
        btnPanel.add(noBtn, gbc);
    }

    private void removeAllValues() {
        gameSettingsBoard.removeAllValues();
        gameSettingsChipsInitiator.removeAllValues();
        gameSettingsChipsResponder.removeAllValues();
        initiatorGPField.setText("");
        responderGPField.setText("");
    }

    /**
     * Method that is called when the user clicks the ok button
     */
    private void okButton() {
        this.gameHasChanged = true;
        int numGoalPos = MiscFunc.getNumberOfGoalPositions();

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

    /**
     * Method that is called when the user clicks the cancel button
     */
    private void noButton() {
        this.gameHasChanged = false;
        setVisible(false);
    }

    /**
     * Checks whether the game is changed
     *
     * @return True when the game has changed, false otherwise
     */
    public boolean isGameHasChanged() {
        return gameHasChanged;
    }

    /**
     * Gets the color (integer) of a particular tile of the board
     *
     * @param row The row in which the tile of the board is placed
     * @param col The column in which the tile of the board is placed
     * @return Integer corresponding to the color of the tile of the board with specified row and column
     */
    public int getIntColorTile(int row, int col) {
        return gameSettingsBoard.getIntColorTile(row, col);
    }

    /**
     * Gets the goal positions of the players
     *
     * @return An array with goal positions of the initiator and the responder
     */
    public int[] getGoalPositions() {
        int[] goalPos = new int[2];
        goalPos[0] = initiatorGPFieldValue;
        goalPos[1] = responderGPFieldValue;
        return goalPos;
    }

    /**
     * Gets a chip of the initiator
     *
     * @param idx Index of the chip
     * @return Integer corresponding to the color of the chips asked for
     */
    public int getChipInitiator(int idx) {
        return gameSettingsChipsInitiator.getChip(idx);
    }

    /**
     * Gets a chip of the responder
     *
     * @param idx Index of the chip
     * @return Integer corresponding to the color of the chips asked for
     */
    public int getChipResponder(int idx) {
        return gameSettingsChipsResponder.getChip(idx);
    }
}
