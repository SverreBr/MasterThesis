package view.settings;

import utilities.Settings;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class BoardSettingsBoard extends JComponent {

    private static final int OFFSET = 1;

    private static final int BORDER_OFFSET = 10;

    private Map<Integer, JTextField> boardTextMap;


    /**
     * Constructor of the board panel
     */
    public BoardSettingsBoard() {
        this.setBackground(Settings.getBackGroundColor());
        setPreferredSize(new Dimension(300, 300));

        addTextFields();
    }

    private void addTextFields() {
        JTextField textField;

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        boardTextMap = new HashMap<>();

        for (int row = 0; row < Settings.BOARD_WIDTH; row++) {
            for (int column = 0; column < Settings.BOARD_HEIGHT; column++) {
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
                boardTextMap.put(row * Settings.BOARD_WIDTH + column, textField);
            }
        }

        gbc.gridx = 0;
        gbc.ipadx = 10;

        int offsetWidth = 21;
        int offsetHeight = 18;
        gbc.insets = new Insets(offsetHeight, offsetWidth, offsetHeight, offsetWidth);

        for (int row = 0; row < Settings.BOARD_WIDTH; row++) {
            gbc.gridy = 0;
            for (int column = 0; column < Settings.BOARD_HEIGHT; column++) {
                add(boardTextMap.get(row * Settings.BOARD_WIDTH + column), gbc);
                gbc.gridy++;
            }
            gbc.gridx++;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g.create();
        paintGrid(g2);

        g2.dispose();
    }

    /**
     * Paints the grid of the board
     *
     * @param g2 graphics
     */
    private void paintGrid(Graphics2D g2) {
        JTextField textField;
        int textFieldValue;

        Dimension panelSize = getPanelSize();
        Dimension siteSize = getTileSize();
        Dimension simulationSize = new Dimension(Settings.BOARD_WIDTH, Settings.BOARD_HEIGHT);

        g2.setColor(Color.BLACK);
        for (int x = 0; x <= simulationSize.width; x++) {
            g2.drawLine(
                    x * siteSize.width + BORDER_OFFSET, BORDER_OFFSET,
                    x * siteSize.width + BORDER_OFFSET, panelSize.height + BORDER_OFFSET);
        }

        for (int y = 0; y <= simulationSize.height; y++) {
            g2.drawLine(
                    BORDER_OFFSET, y * siteSize.height + BORDER_OFFSET,
                    panelSize.width + BORDER_OFFSET, y * siteSize.height + BORDER_OFFSET);
        }

        for (int x = 0; x < simulationSize.width; x++) {
            for (int y = 0; y < simulationSize.height; y++) {
                textField = boardTextMap.get(x * Settings.BOARD_WIDTH + y);
                textFieldValue = Miscellaneous.parseIntTextField(textField, false);
                g2.setColor(Settings.getColor(textFieldValue));
                g2.fillRect(
                        x * siteSize.width + OFFSET + BORDER_OFFSET, y * siteSize.height + OFFSET + BORDER_OFFSET,
                        siteSize.width - OFFSET, siteSize.height - OFFSET);
            }
        }
    }

    /**
     * gets the size of each tile
     *
     * @return size of each tile
     */
    private Dimension getTileSize() {
        Dimension size = new Dimension(Settings.BOARD_WIDTH, Settings.BOARD_HEIGHT);
        Dimension panelSize = getPanelSize();
        return new Dimension(panelSize.width / size.width, panelSize.height / size.height);
    }

    private Dimension getPanelSize() {
        return new Dimension(getWidth() - BORDER_OFFSET * 2, getHeight() - BORDER_OFFSET * 2);
    }

    public int getIntColorTile(int row, int col) {
        JTextField textField = boardTextMap.get(row * Settings.BOARD_WIDTH + col);
        return Miscellaneous.parseIntTextField(textField, true);
    }
}
