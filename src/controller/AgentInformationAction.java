package controller;

import view.AgentInformationDialog;
import view.Popups;
import model.Game;
import model.GameListener;
import model.player.PlayerLying;
import utilities.Settings;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class AgentInformationAction extends AbstractAction implements GameListener {

    private PlayerLying agent;

    private final String agentName;
    private final Game game;

    private final JFrame mainFrame;

    public AgentInformationAction(String agentName, Game game, JFrame mainFrame) {
        super(agentName + " information");
        this.agentName = agentName;
        this.game = game;
        this.game.addListener(this);
        setAgent();
        this.mainFrame = mainFrame;
    }

    private void setAgent() {
        if (agentName.equals(Settings.INITIATOR_NAME)) {
            this.agent = game.getInitiator();
        } else {
            this.agent = game.getResponder();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (game.isSimulationOff()) {
            Popups.showAgentInformationButtonNotAccessible();
            return;
        }

        AgentInformationDialog aid = new AgentInformationDialog(mainFrame,agentName + " information", agent);
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
