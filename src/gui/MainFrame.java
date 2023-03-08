package gui;

import model.Game;
import utilities.Settings;

import javax.swing.*;
import java.awt.*;

import static utilities.Settings.*;

/**
 * The application window with all the panels.
 */
public class MainFrame extends JFrame {

    /**
     * Makes the main panel
     *
     * @param game the game model
     */
    public MainFrame(Game game) {
        super("Colored Trails");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        JPanel leftBody = new JPanel();
        leftBody.setLayout(new BorderLayout());
        leftBody.setPreferredSize(new Dimension(BUTTON_PANEL_WIDTH, AGENT_PANEL_HEIGHT + LEGEND_PANEL_HEIGHT));

        AgentPanel agentPanelInit = new AgentPanel(game, INITIATOR_NAME);
        agentPanelInit.setPreferredSize(new Dimension(BUTTON_PANEL_WIDTH, AGENT_PANEL_HEIGHT));

        LegendPanel legendPanel = new LegendPanel(game, this);

        leftBody.add(agentPanelInit, BorderLayout.NORTH);
        leftBody.add(legendPanel, BorderLayout.SOUTH);
        leftBody.setBackground(Settings.getBackGroundColor());

        BoardPanel boardPanel = new BoardPanel(game);
        boardPanel.setPreferredSize(new Dimension(BOARD_PANEL_SIZE, BOARD_PANEL_SIZE));

        JPanel rightBody = new JPanel();
        rightBody.setLayout(new BorderLayout());
        rightBody.setPreferredSize(new Dimension(BUTTON_PANEL_WIDTH, AGENT_PANEL_HEIGHT + LEGEND_PANEL_HEIGHT));

        AgentPanel agentPanelResp = new AgentPanel(game, RESPONDER_NAME);
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