package gui;

import utilities.Game;
import utilities.GameListener;
import utilities.Settings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * ButtonPanel: the panel with buttons
 */
public class ButtonPanel extends JPanel implements ActionListener, GameListener {

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

    private final JFrame mainFrame;

    /**
     * Constructor: creates the button panel
     *
     * @param game the game model
     */
    public ButtonPanel(Game game, JFrame mainFrame) {
        this.game = game;
        this.mainFrame = mainFrame;
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

        JButton restart = new JButton("Restart");
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

        JButton newNeg = new JButton("New round");
        newNeg.setActionCommand("new round");
        newNeg.setToolTipText("Click to start a new round in the negotiation.");
        newNeg.addActionListener(this);

        forwardPlays = new JTextField("100");

        JButton forward = new JButton("Rounds forward");
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
        body.add(Box.createRigidArea(new Dimension(1, 1)));
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
//        ProgressMonitor progressMonitor;

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
//        progressMonitor = new ProgressMonitor(mainFrame, "Running a long task", "0 out of " + rounds + " rounds", 0, rounds);

        for (int i = 0; i < rounds; i++) {
            game.playTillEnd();
            game.newRound();
//            if (i % 100 == 0) {
//                progressMonitor.setProgress(i+1);
//                progressMonitor.setNote((i + 1) + " out of " + rounds + " rounds");
//            }
        }
//        progressMonitor.close();

        game.setSimulationOn();
        game.notifyListenersNewGame();
        Popups.showForwardSuccess(rounds);
    }


    /**
     * Action corresponding with a button event
     *
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "restart" -> game.reset(
                    game.getInitiator().getOrderToM(),
                    game.getResponder().getOrderToM(),
                    game.getInitiator().getLearningSpeed(),
                    game.getResponder().getLearningSpeed()); // TODO: (SELF) could be optimized.
            case "new round" -> game.newRound();
            case "exit" -> System.exit(0);
            case "step" -> game.step();
            case "play" -> game.playTillEnd();
            case "forward" -> forward();
        }
    }

    @Override
    public void gameChanged() {}

    @Override
    public void newGame() {
        boolean setEnabled = !(this.game.isGameDisabled());
        play.setEnabled(setEnabled);
        step.setEnabled(setEnabled);
    }

    /**
     * Method called when field inGame is changed. Two buttons are disabled.
     */
    @Override
    public void inGameChanged() {
        boolean setEnabled = !(this.game.isGameDisabled());
        play.setEnabled(setEnabled);
        step.setEnabled(setEnabled);
    }
}
