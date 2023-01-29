package gui;

import utilities.Game;

import javax.swing.*;
import java.awt.*;

/**
 * The application window with all the panels.
 */
public class MainPanel extends JFrame {

    public MainPanel(Game game) {
        super("Colored Trails");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        int BUTTON_PANEL_WIDTH = 330;
        int MAIN_PANEL_SIZE = 651;

        AgentPanel AgentPanelInit = new AgentPanel(game.initiator);
        AgentPanelInit.setPreferredSize(new Dimension(BUTTON_PANEL_WIDTH, MAIN_PANEL_SIZE));

        BoardPanel boardPanel = new BoardPanel(game);
        boardPanel.setPreferredSize(new Dimension(MAIN_PANEL_SIZE, MAIN_PANEL_SIZE));

        AgentPanel AgentPanelResp = new AgentPanel(game.responder);
        AgentPanelResp.setPreferredSize(new Dimension(BUTTON_PANEL_WIDTH, MAIN_PANEL_SIZE));

        getContentPane().setLayout(new FlowLayout());

        getContentPane().add(AgentPanelInit);
        getContentPane().add(boardPanel);
        getContentPane().add(AgentPanelResp);
        pack();
    }
}