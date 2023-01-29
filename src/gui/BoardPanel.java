package gui;

import utilities.Game;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class BoardPanel extends JComponent {
    private final Game game;

    public BoardPanel(Game game) {
        this.game = game;
    }

    @Override
    public void paint(Graphics g) {
        paintComponent(g);

        Graphics2D g2 = (Graphics2D) g.create();
        Map<RenderingHints.Key, Object> rh = new HashMap<>();
        rh.put(RenderingHints.KEY_TEXT_ANTIALIASING,  RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        rh.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHints(rh);

//        Dimension dim = game.getBoardSize();
//        for (int y = 0; y < dim.height; y++) {
//            for (int x = 0; x < dim.width; x++) {
//                Site site = simulation.getSite(x, y);
//
//                if (site.getAgent() != null)
//                    paintAgent(g2, site, site.getAgent());
//            }
//        }

        paintGrid(g2);

        g2.setFont(new Font("SansSerif", Font.PLAIN, 36));
        paintStartLocation(g2);
        paintGoalLocation(g2);

//        if (selected != null)
//            paintSelection(g2);

        g2.dispose();
    }

    private void paintGrid(Graphics2D g2) {
        Dimension panelSize = getSize();
        Dimension siteSize = getSiteSize();
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
                g2.setColor(game.board.getSquareColor(x, y));
                g2.fillRect(
                        x * siteSize.width + offset, y * siteSize.height + offset,
                        siteSize.width - offset, siteSize.height - offset);
            }
        }

    }

    private Dimension getSiteSize() {
        Dimension size = game.getBoardSize();
        return new Dimension(getSize().width / size.width, getSize().height / size.height);
    }

    private void paintStartLocation(Graphics2D g2) {
        String symbol = "X";
        drawSymbol(symbol, g2, game.initiator.getStartingPosition());
        drawSymbol(symbol, g2, game.responder.getStartingPosition());
    }

    private void paintGoalLocation(Graphics2D g2) {
        String symbol = "Gi";
        drawSymbol(symbol, g2, game.initiator.getGoalPosition());

        symbol = "Gr";
        drawSymbol(symbol, g2, game.responder.getGoalPosition());
    }

    public void drawSymbol(String symbol, Graphics2D g2, Point position) {
        Dimension siteSize = getSiteSize();
        FontMetrics metrics = g2.getFontMetrics(g2.getFont());

        int x = (siteSize.width - metrics.stringWidth(symbol)) / 2;
        int y = ((siteSize.height - metrics.getHeight()) / 2) + metrics.getAscent();

        g2.setColor(Color.BLACK);
        g2.drawString(symbol,
                x + siteSize.width * position.x,
                y + siteSize.height * position.y);
    }
}
