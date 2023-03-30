package lyingAgents.view.settings;

import lyingAgents.utilities.MiscFunc;
import lyingAgents.utilities.Settings;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * BoardSettingsChips class: deals with determining the colors of the chips of the players in game settings
 */
public class GameSettingsChips extends JComponent {

    /**
     * Component width of the chips with text fields
     */
    private static final int COMPONENT_WIDTH = 200;

    /**
     * Component height of the chips with text fields
     */
    private static final int COMPONENT_HEIGHT = 40;

    /**
     * Maps integer of chip to a text field
     */
    private Map<Integer, JTextField> chipTextMap;

    /**
     * Constructor
     */
    public GameSettingsChips() {
        this.setBackground(Settings.getBackGroundColor());
        setPreferredSize(new Dimension(COMPONENT_WIDTH, COMPONENT_HEIGHT));

        addTextFields();
    }

    /**
     * Adds text fields to the chips
     */
    private void addTextFields() {
        JTextField textField;

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        chipTextMap = new HashMap<>();

        for (int chipNum = 0; chipNum < Settings.CHIPS_PER_PLAYER; chipNum++) {
            textField = new JTextField("", 1);

            // Listen for changes in the text
            textField.getDocument().addDocumentListener(new DocumentListener() {
                public void changedUpdate(DocumentEvent e) {
                    change();
                }

                public void removeUpdate(DocumentEvent e) {
                    change();
                }

                public void insertUpdate(DocumentEvent e) {
                    change();
                }

                public void change() {
                    repaint();
                }
            });
            chipTextMap.put(chipNum, textField);
        }

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.ipadx = 5;

        gbc.anchor = GridBagConstraints.LINE_START;

        int offsetWidth = 10;
        int offsetHeight = 0;
        gbc.insets = new Insets(offsetHeight, offsetWidth, offsetHeight, offsetWidth);

        for (int chip = 0; chip < Settings.CHIPS_PER_PLAYER - 1; chip++) {
            add(chipTextMap.get(chip), gbc);
            gbc.gridx++;
        }
        gbc.weightx = 1.0;
        add(chipTextMap.get(Settings.CHIPS_PER_PLAYER - 1), gbc);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g.create();
        paintChips(g2);

        g2.dispose();
    }

    /**
     * Paint the chips on the legend panel
     *
     * @param g2 graphics
     */
    private void paintChips(Graphics2D g2) {
        int tokenSize = COMPONENT_HEIGHT;
        int numDiffTokens = Settings.CHIPS_PER_PLAYER;
        JTextField textField;
        int textFieldValue;

        for (int i = 0; i < numDiffTokens; i++) {
            textField = chipTextMap.get(i);
            textFieldValue = MiscFunc.parseIntTextField(textField, false);
            g2.setColor(Settings.getColor(textFieldValue));
            g2.fillOval(i * tokenSize, 0, tokenSize, tokenSize);
        }
    }

    /**
     * Gets the chip number (color) of a specified chip index
     *
     * @param idx Index of the chip
     * @return Color of the chip as specified by the user
     */
    public int getChip(int idx) {
        JTextField textField = chipTextMap.get(idx);
        return MiscFunc.parseIntTextField(textField, true);
    }
}
