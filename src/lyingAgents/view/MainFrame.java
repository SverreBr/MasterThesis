package lyingAgents.view;

import lyingAgents.controller.saveload.LoadAction;
import lyingAgents.controller.saveload.SaveAction;
import lyingAgents.model.Game;
import lyingAgents.utilities.Settings;

import javax.swing.*;
import java.awt.*;


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
        leftBody.setPreferredSize(new Dimension(ViewSettings.BUTTON_PANEL_WIDTH, ViewSettings.AGENT_PANEL_HEIGHT + ViewSettings.LEGEND_PANEL_HEIGHT));

        AgentPanel agentPanelInit = new AgentPanel(game, Settings.INITIATOR_NAME, this);
        agentPanelInit.setPreferredSize(new Dimension(ViewSettings.BUTTON_PANEL_WIDTH, ViewSettings.AGENT_PANEL_HEIGHT));

        LegendPanel legendPanel = new LegendPanel(game, this);

        leftBody.add(agentPanelInit, BorderLayout.NORTH);
        leftBody.add(legendPanel, BorderLayout.SOUTH);
        leftBody.setBackground(ViewSettings.getBackGroundColor());

        CenterPanel centerPanel = new CenterPanel(game);

        JPanel rightBody = new JPanel();
        rightBody.setLayout(new BorderLayout());
        rightBody.setPreferredSize(new Dimension(ViewSettings.BUTTON_PANEL_WIDTH, ViewSettings.AGENT_PANEL_HEIGHT + ViewSettings.LEGEND_PANEL_HEIGHT));

        AgentPanel agentPanelResp = new AgentPanel(game, Settings.RESPONDER_NAME, this);
        agentPanelResp.setPreferredSize(new Dimension(ViewSettings.BUTTON_PANEL_WIDTH, ViewSettings.AGENT_PANEL_HEIGHT));
        ButtonPanel buttonPanel = new ButtonPanel(game);

        rightBody.add(agentPanelResp, BorderLayout.NORTH);
        rightBody.add(buttonPanel, BorderLayout.SOUTH);
        rightBody.setBackground(ViewSettings.getBackGroundColor());

        addMenuBar(game);

        getContentPane().setLayout(new FlowLayout());
        getContentPane().add(leftBody);
        getContentPane().add(centerPanel);
        getContentPane().add(rightBody);
        getContentPane().setBackground(ViewSettings.getBackGroundColor());
        pack();
    }

    /**
     * Adds a menubar to the frame
     *
     * @param game The game model
     */
    private void addMenuBar(Game game) {
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem saveMenuItem = new JMenuItem(new SaveAction("Save game setting", this, game));
        fileMenu.add(saveMenuItem);

        JMenuItem loadMenuItem = new JMenuItem(new LoadAction("Load game setting", this, game));
        fileMenu.add(loadMenuItem);

        menuBar.add(fileMenu);
        this.setJMenuBar(menuBar);
    }
}