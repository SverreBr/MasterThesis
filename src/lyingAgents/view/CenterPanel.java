package lyingAgents.view;

import lyingAgents.model.Game;
import lyingAgents.model.GameListener;
import lyingAgents.utilities.MiscFunc;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;

public class CenterPanel extends JPanel implements GameListener {
    private final Game game;
    private final JTextPane informationPanel;

    private final String[] content = new String[2];
    private final String[] style = new String[2];

    public CenterPanel(Game game) {
        this.game = game;
        informationPanel = new JTextPane();

        this.setPreferredSize(new Dimension(ViewSettings.BOARD_PANEL_SIZE,
                ViewSettings.AGENT_PANEL_HEIGHT + ViewSettings.LEGEND_PANEL_HEIGHT));
        this.setBorder(BorderFactory.createEmptyBorder(10,0,10,0));
        this.setBackground(ViewSettings.getBackGroundColor());
        BoardPanel boardPanel = new BoardPanel(game);

        makeInfoPanel();
        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        this.add(boardPanel, gbc);
        gbc.insets = new Insets(10,0,0,0);
        gbc.gridy++;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        this.add(informationPanel, gbc);
    }

    private void makeInfoPanel() {
        informationPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(5, 0, 0, 0),
                BorderFactory.createCompoundBorder(
                        BorderFactory.createEtchedBorder(EtchedBorder.RAISED),
                        BorderFactory.createEmptyBorder(5, 5, 5, 5))));

        informationPanel.setEditable(false);

        MiscFunc.createsStyledDocument(informationPanel.getStyledDocument());
        updateInfo();
    }

    private void updateInfo() {
        int idx = 0;
        style[idx] = "bold";
        content[idx++] = "Additional Information";
        style[idx] = "regular";
        content[idx] = "Utility score = " + game.getInitiator().getUtilityValue();

        MiscFunc.addStylesToDocMulti(informationPanel, content, style);
    }


    @Override
    public void gameChanged() {

    }

    @Override
    public void newGame() {
        updateInfo();
    }
}
