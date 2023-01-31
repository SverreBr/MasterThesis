package gui;

import utilities.Game;

import javax.swing.*;
import java.awt.*;

/**
 * The application window with all the panels.
 */
public class MainPanel extends JFrame {

    /**
     * Makes the main panel
     *
     * @param game the game model
     */
    public MainPanel(Game game) {
        super("Colored Trails");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        int BUTTON_PANEL_WIDTH = 330;
        int AGENT_PANEL_HEIGHT = 450;
        int MAIN_PANEL_SIZE = 651;

        JPanel leftBody = new JPanel();
        leftBody.setLayout(new BorderLayout());
        leftBody.setPreferredSize(new Dimension(BUTTON_PANEL_WIDTH, MAIN_PANEL_SIZE));

        AgentPanel agentPanelInit = new AgentPanel(game.initiator);
        agentPanelInit.setPreferredSize(new Dimension(BUTTON_PANEL_WIDTH, AGENT_PANEL_HEIGHT));

        leftBody.add(agentPanelInit, BorderLayout.NORTH);

        BoardPanel boardPanel = new BoardPanel(game);
        boardPanel.setPreferredSize(new Dimension(MAIN_PANEL_SIZE, MAIN_PANEL_SIZE));

        JPanel rightBody = new JPanel();
        rightBody.setLayout(new BorderLayout());
        rightBody.setPreferredSize(new Dimension(BUTTON_PANEL_WIDTH, MAIN_PANEL_SIZE));

        AgentPanel agentPanelResp = new AgentPanel(game.responder);
        agentPanelResp.setPreferredSize(new Dimension(BUTTON_PANEL_WIDTH, AGENT_PANEL_HEIGHT));
        ButtonPanel buttonPanel = new ButtonPanel(game);

        rightBody.add(agentPanelResp, BorderLayout.NORTH);
        rightBody.add(buttonPanel, BorderLayout.SOUTH);

        getContentPane().setLayout(new FlowLayout());
        getContentPane().add(leftBody);
        getContentPane().add(boardPanel);
        getContentPane().add(rightBody);
        pack();
    }
}