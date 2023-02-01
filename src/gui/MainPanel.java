package gui;

import utilities.Game;
import utilities.Settings;

import javax.swing.*;
import java.awt.*;

import static utilities.Settings.*;

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

        JPanel leftBody = new JPanel();
        leftBody.setLayout(new BorderLayout());
        leftBody.setPreferredSize(new Dimension(BUTTON_PANEL_WIDTH, MAIN_PANEL_SIZE));

        AgentPanel agentPanelInit = new AgentPanel(game.initiator);
        agentPanelInit.setPreferredSize(new Dimension(BUTTON_PANEL_WIDTH, AGENT_PANEL_HEIGHT));

        LegendPanel legendPanel = new LegendPanel(game);

        leftBody.add(agentPanelInit, BorderLayout.NORTH);
        leftBody.add(legendPanel, BorderLayout.SOUTH);
        leftBody.setBackground(Settings.getBackGroundColor());

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
        rightBody.setBackground(Settings.getBackGroundColor());

        getContentPane().setLayout(new FlowLayout());
        getContentPane().add(leftBody);
        getContentPane().add(boardPanel);
        getContentPane().add(rightBody);
        getContentPane().setBackground(Settings.getBackGroundColor());
        pack();
    }
}