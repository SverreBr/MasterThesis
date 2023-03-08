package gui;

import controller.ForwardAction;
import controller.ForwardListener;
import model.Game;
import model.GameListener;
import model.player.PlayerLying;
import utilities.Settings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * ButtonPanel: the panel with buttons
 */
public class ButtonPanel extends JPanel implements ActionListener, GameListener, ForwardListener {

    /**
     * The game model
     */
    private final Game game;

    /**
     * Panel with the buttons that are located south
     */
    private JPanel southButtons;

    /**
     * The main body of the button panel
     */
    private JPanel body;

    /**
     * The number of rounds forward
     */
    private JTextField forwardPlays;

    /**
     * Button to play until end of negotiation round
     */
    private JButton play;

    /**
     * Button to take one step in negotiation
     */
    private JButton step;

    /**
     * Button to restart the game completely with fresh agents
     */
    private JButton restart;

    /**
     * Button to begin new negotiation round
     */
    private JButton newNeg;

    /**
     * Button to forward a specified number of rounds
     */
    private JButton forward;

    /**
     * Constructor: creates the button panel
     *
     * @param game the game model
     */
    public ButtonPanel(Game game) {
        this.game = game;
        game.addListener(this);
        createButtonPanel();
    }

    /**
     * Creates the buttons on the button panel
     */
    private void createButtonPanel() {
        this.setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        southButtons = new JPanel();
        southButtons.setLayout(new BoxLayout(southButtons, BoxLayout.LINE_AXIS));
        southButtons.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

        restart = new JButton("Restart");
        restart.setActionCommand("restart");
        restart.setToolTipText("Click to reset the agents completely.");
        restart.addActionListener(this);

        JButton exit = new JButton("Exit");
        exit.setActionCommand("exit");
        exit.setToolTipText("Click to exit the game.");
        exit.addActionListener(this);

        southButtons.setLayout(new GridLayout(1, 0, 5, 0));
        southButtons.add(restart);
        southButtons.add(exit);

        body = new JPanel();
        body.setLayout(new GridLayout(0, 1, 0, 5));
        body.setBorder(BorderFactory.createEmptyBorder(5, 0, 20, 0));

        step = new JButton("Step");
        step.setActionCommand("step");
        step.setToolTipText("Click to take one step in the negotiation.");
        step.addActionListener(this);

        play = new JButton("Play");
        play.setActionCommand("play");
        play.setToolTipText("Click to play until the end of the negotiation.");
        play.addActionListener(this);

        newNeg = new JButton("New round");
        newNeg.setActionCommand("new round");
        newNeg.setToolTipText("Click to start a new round in the negotiation.");
        newNeg.addActionListener(this);

        forwardPlays = new JTextField("100");

        forward = new JButton("Rounds forward");
        forward.setActionCommand("forward");
        forward.setToolTipText("Click to forward the number of rounds provided in the text field.");
        forward.addActionListener(this);

        JPanel forwardPanel = new JPanel();
        forwardPanel.setLayout(new GridLayout(1, 0, 5, 0));
        forwardPanel.add(forwardPlays);
        forwardPanel.add(forward);

        body.add(step);
        body.add(play);
        body.add(newNeg);
//        body.add(Box.createRigidArea(new Dimension(1, 1)));
        body.add(forwardPanel);

        changeBackgrounds();

        this.add(body, BorderLayout.CENTER);
        this.add(southButtons, BorderLayout.SOUTH);
    }

    /**
     * Change backgrounds
     */
    private void changeBackgrounds() {
        setBackground(Settings.getBackGroundColor());
        southButtons.setBackground(Settings.getBackGroundColor());
        body.setBackground(Settings.getBackGroundColor());
    }

    /**
     * Forward the number of rounds that have been provided by the user.
     */
    private void forward() {
        int rounds;

        try {
            rounds = Integer.parseInt(forwardPlays.getText());
        } catch (NumberFormatException nfe) {
            rounds = -1;
        }

        if (rounds < 1) {
            Popups.showInvalidTextError();
            return;
        }

        game.setSimulationOff();
        ForwardAction forwardAction = new ForwardAction(game, rounds);
        forwardAction.addListenerToProgressWorker(this);
        forwardAction.execute();
    }

    /**
     * Restarts the game with new agents
     */
    private void restart() {
        PlayerLying init = game.getInitiator();
        PlayerLying resp = game.getResponder();
        game.reset(init.getOrderToM(),
                resp.getOrderToM(),
                init.getLearningSpeed(),
                resp.getLearningSpeed(),
                init.isCanLie(),
                resp.isCanLie());
    }


    /**
     * Action corresponding with a button event
     *
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "restart" -> restart();
            case "new round" -> this.game.newRound();
            case "exit" -> System.exit(0);
            case "step" -> this.game.step();
            case "play" -> this.game.playTillEnd();
            case "forward" -> forward();
        }
    }

    @Override
    public void gameChanged() {
    }

    @Override
    public void newGame() {
        boolean setEnabled = !(this.game.isGameFinished());
        play.setEnabled(setEnabled);
        step.setEnabled(setEnabled);
    }

    /**
     * Method called when field inGame is changed. Two buttons are disabled.
     */
    @Override
    public void inGameChanged() {
        boolean setEnabled = !(this.game.isGameFinished());
        play.setEnabled(setEnabled);
        step.setEnabled(setEnabled);
    }

    @Override
    public void executionStarted() {
        play.setEnabled(false);
        step.setEnabled(false);
        restart.setEnabled(false);
        forward.setEnabled(false);
        newNeg.setEnabled(false);
    }

    @Override
    public void forwardActionDone(int rounds) {
        play.setEnabled(true);
        step.setEnabled(true);
        restart.setEnabled(true);
        forward.setEnabled(true);
        newNeg.setEnabled(true);

        game.setSimulationOn();
        game.notifyListenersNewGame();
        Popups.showForwardSuccess(rounds);
    }

    @Override
    public void executionAborted() {
        play.setEnabled(true);
        step.setEnabled(true);
        restart.setEnabled(true);
        forward.setEnabled(true);
        newNeg.setEnabled(true);

        game.setSimulationOn();
        game.notifyListenersNewGame();
        Popups.showForwardingCancelled();
    }
}
