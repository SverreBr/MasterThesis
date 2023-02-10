package gui;

import controller.GameListener;
import utilities.Game;
import utilities.Settings;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * BoardPanel: the panel where the board is on
 */
public class BoardPanel extends JComponent implements GameListener {

    /**
     * the game model
     */
    private final Game game;

    /**
     * Constructor of the board panel
     *
     * @param game the game model
     */
    public BoardPanel(Game game) {
        this.game = game;
        this.setBackground(Settings.getBackGroundColor());
        this.game.addListener(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g.create();
        Map<RenderingHints.Key, Object> rh = new HashMap<>();
        rh.put(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        rh.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHints(rh);

        paintGrid(g2);

        g2.setFont(new Font("SansSerif", Font.PLAIN, 36));
        paintStartLocation(g2);
        paintGoalLocation(g2);

//        if (selected != null)
//            paintSelection(g2);

        g2.dispose();
    }

    /**
     * Paints the grid of the board
     *
     * @param g2 graphics
     */
    private void paintGrid(Graphics2D g2) {
        Dimension panelSize = getSize();
        Dimension siteSize = getTileSize();
        Dimension simulationSize = game.getBoardSize();

        g2.setColor(Color.BLACK);
        for (int x = 0; x <= simulationSize.width; x++) {
            g2.drawLine(
                    x * siteSize.width, 0,
                    x * siteSize.width, panelSize.height);
        }

        for (int y = 0; y <= simulationSize.height; y++) {
            g2.drawLine(
                    0, y * siteSize.height,
                    panelSize.width, y * siteSize.height);
        }

        int offset = 1;
        for (int x = 0; x < simulationSize.width; x++) {
            for (int y = 0; y < simulationSize.height; y++) {
                g2.setColor(game.getBoard().getTileColor(new Point(x, y)));
                g2.fillRect(
                        x * siteSize.width + offset, y * siteSize.height + offset,
                        siteSize.width - offset, siteSize.height - offset);
            }
        }

    }

    /**
     * gets the size of each tile
     *
     * @return size of each tile
     */
    private Dimension getTileSize() {
        Dimension size = game.getBoardSize();
        return new Dimension(getSize().width / size.width, getSize().height / size.height);
    }

    /**
     * Paints the start location of the initiator and the responder
     *
     * @param g2 graphics
     */
    private void paintStartLocation(Graphics2D g2) {
        Dimension siteSize = getTileSize();
        int offset = 1;

        Point start = Settings.STARTING_POSITION;

        g2.setColor(Color.WHITE);
        g2.fillRect(
                start.x * siteSize.width + offset, start.y * siteSize.height + offset,
                siteSize.width - offset, siteSize.height - offset);
        drawSymbol(Settings.START_LOCATION_SYMBOL, g2, start);
    }

    /**
     * Paints the goal location of the initiator and the responder
     *
     * @param g2 graphics
     */
    private void paintGoalLocation(Graphics2D g2) {
        Point goalInit, goalResp;

        goalInit = game.getGoalPositionPointPlayer(Settings.INITIATOR_NAME);
        goalResp = game.getGoalPositionPointPlayer(Settings.RESPONDER_NAME);

        if (goalInit.equals(goalResp)) {
            drawSymbol(Settings.GOAL_LOCATION_SYMBOL, g2, goalInit);
        } else {
            drawSymbol(Settings.GOAL_LOCATION_SYMBOL_INITIATOR, g2, goalInit);
            drawSymbol(Settings.GOAL_LOCATION_SYMBOL_RESPONDER, g2, goalResp);
        }
    }

    /**
     * Draws a symbol on a tile with position "position"
     *
     * @param symbol   the symbol to be drawn
     * @param g2       graphics
     * @param position the position of the symbol
     */
    public void drawSymbol(String symbol, Graphics2D g2, Point position) {
        Dimension siteSize = getTileSize();
        FontMetrics metrics = g2.getFontMetrics(g2.getFont());

        int x = (siteSize.width - metrics.stringWidth(symbol)) / 2;
        int y = ((siteSize.height - metrics.getHeight()) / 2) + metrics.getAscent();

        g2.setColor(Color.BLACK);
        g2.drawString(symbol,
                x + siteSize.width * position.x,
                y + siteSize.height * position.y);
    }

    @Override
    public void newGame() {
        this.repaint();
    }

    @Override
    public void gameChanged() {}
}
