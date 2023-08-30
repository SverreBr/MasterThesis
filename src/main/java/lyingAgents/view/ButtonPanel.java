package lyingAgents.view;

import lyingAgents.controller.gamePlayActions.ForwardAction;
import lyingAgents.controller.gamePlayActions.ForwardListener;
import lyingAgents.controller.gamePlayActions.PlayAction;
import lyingAgents.controller.gamePlayActions.PlayListener;
import lyingAgents.model.Game;
import lyingAgents.model.GameListener;
import lyingAgents.model.player.PlayerLying;
import lyingAgents.utilities.GameSetting;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * ButtonPanel: the panel with buttons
 */
public class ButtonPanel extends JPanel implements ActionListener, GameListener, ForwardListener, PlayListener {

    /**
     * The game model
     */
    private final Game game;

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

    private JButton newNegPlay;

    /**
     * Button to forward a specified number of rounds
     */
    private JButton forward;

    private JButton exit;

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
        restart = new JButton("Full restart");
        restart.setActionCommand("restart");
        restart.setToolTipText("Click to reset the beliefs of the agents completely.");
        restart.addActionListener(this);

        exit = new JButton("Exit");
        exit.setActionCommand("exit");
        exit.setToolTipText("Click to exit the game.");
        exit.addActionListener(this);

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

        newNegPlay = new JButton("Play new round");
        newNegPlay.setActionCommand("play new round");
        newNegPlay.setToolTipText("Click to start a new round in the negotiation and play.");
        newNegPlay.addActionListener(this);

        forwardPlays = new JTextField("100");

        forward = new JButton("Rounds forward");
        forward.setActionCommand("forward");
        forward.setToolTipText("Click to forward the number of rounds provided in the text field.");
        forward.addActionListener(this);

        addButtonsToPanel();
        changeBackgrounds();
    }

    private void addButtonsToPanel() {
        this.setLayout(new GridBagLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(2, 2, 2, 2);
        gbc.gridwidth = 2;
        gbc.weightx = 0.5;
        gbc.gridy = 0;
        gbc.gridx = 0;

        add(step, gbc);
        gbc.gridy++;

        gbc.insets = new Insets(2, 2, 12, 2);
        add(play, gbc);
        gbc.gridy++;
        gbc.insets = new Insets(2, 2, 2, 2);

        gbc.gridwidth = 1;
        add(newNeg, gbc);
        gbc.gridx++;
        add(newNegPlay, gbc);
        gbc.gridy++;
        gbc.gridx = 0;

        add(forwardPlays, gbc);
        gbc.gridx++;
        add(forward, gbc);
        gbc.gridy++;
        gbc.gridx = 0;

        gbc.insets = new Insets(12, 2, 0, 2);

        add(restart, gbc);
        gbc.gridx++;
        add(exit, gbc);
    }

    /**
     * Change backgrounds
     */
    private void changeBackgrounds() {
        Color color = ViewSettings.getBackGroundColor();
        setBackground(color);
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

    private void play() {
        PlayAction playAction = new PlayAction(game);
        playAction.addListenerToProgressWorker(this);
        playAction.execute();
    }

    /**
     * Restarts the game with new agents
     */
    private void restart() {
        PlayerLying init = game.getInitiator();
        PlayerLying resp = game.getResponder();
        GameSetting gameSetting = game.getGameSetting();
        game.initFullyNewGame(init.getOrderToM(),
                resp.getOrderToM(),
                init.getLearningSpeed(),
                resp.getLearningSpeed(),
                init.isCanMakeFalseStatements(),
                resp.isCanMakeFalseStatements(),
                init.isCanSendMessages(),
                resp.isCanSendMessages());
        game.newGameSettings(gameSetting);
    }

    private void newRoundAndPlay() {
        game.newRound();
        play();
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
            case "play new round" -> newRoundAndPlay();
            case "exit" -> System.exit(0);
            case "step" -> this.game.step();
            case "play" -> play();
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

    @Override
    public void inGameChanged() {
        boolean setEnabled = !(this.game.isGameFinished());
        play.setEnabled(setEnabled);
        step.setEnabled(setEnabled);
    }

    private void setButtonBooleans(boolean truthVal) {
        play.setEnabled(truthVal);
        step.setEnabled(truthVal);
        restart.setEnabled(truthVal);
        forward.setEnabled(truthVal);
        newNeg.setEnabled(truthVal);
        newNegPlay.setEnabled(truthVal);
    }

    @Override
    public void forwardExecutionStarted() {
        setButtonBooleans(false);
    }

    @Override
    public void playExecutionStarted() {
        setButtonBooleans(false);
    }

    @Override
    public void forwardActionDone(int rounds) {
        setButtonBooleans(true);

        game.setSimulationOn();
        game.notifyListenersNewGame();
        Popups.showForwardSuccess(rounds);
    }

    @Override
    public void playActionDone() {
        setButtonBooleans(true);
        boolean setEnabled = !(this.game.isGameFinished());
        play.setEnabled(setEnabled);
        step.setEnabled(setEnabled);
    }

    @Override
    public void forwardExecutionAborted() {
        setButtonBooleans(true);
        game.setSimulationOn();
        game.notifyListenersNewGame();
        Popups.showForwardingCancelled();
    }

    @Override
    public void playExecutionAborted() {
        setButtonBooleans(true);
        boolean setEnabled = !(this.game.isGameFinished());
        play.setEnabled(setEnabled);
        step.setEnabled(setEnabled);
    }
}
