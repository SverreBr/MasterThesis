package lyingAgents.controller;

import lyingAgents.view.AgentInformationDialog;
import lyingAgents.view.Popups;
import lyingAgents.model.Game;
import lyingAgents.model.GameListener;
import lyingAgents.model.player.PlayerLying;
import lyingAgents.utilities.Settings;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * AgentInformationAction class: Action performed when user asks for additional information on agent.
 */
public class AgentInformationAction extends AbstractAction implements GameListener {

    /**
     * The agent corresponding to the information shown with the dialog of this action.
     */
    private PlayerLying agent;

    /**
     * The name of the agent of which this dialog will be given.
     */
    private final String agentName;

    /**
     * Game model of the simulation
     */
    private final Game game;

    /**
     * The mainframe of the simulation
     */
    private final JFrame mainFrame;

    /**
     * Constructor
     *
     * @param agentName The name of the agent
     * @param game      The game model of the simulation
     * @param mainFrame The mainframe of the simulation.
     */
    public AgentInformationAction(String agentName, Game game, JFrame mainFrame) {
        super(agentName + " information");
        this.agentName = agentName;
        this.game = game;
        this.game.addListener(this);
        setAgent();
        this.mainFrame = mainFrame;
    }

    /**
     * Sets the agent corresponding to the agentName of this action. Method is called when the agent is changed in the game.
     */
    private void setAgent() {
        this.agent = agentName.equals(Settings.INITIATOR_NAME) ? game.getInitiator() : game.getResponder();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (game.isSimulationOff()) {
            Popups.showAgentInformationButtonNotAccessible();
            return;
        }

        AgentInformationDialog aid = new AgentInformationDialog(mainFrame, agentName + " information", agent);
        aid.setVisible(true);
        aid.dispose();
    }

    @Override
    public void gameChanged() {

    }

    @Override
    public void newGame() {
        setAgent();
    }
}
