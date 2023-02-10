package gui;

import utilities.Game;
import utilities.Settings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * ButtonPanel: the panel with buttons
 */
public class ButtonPanel extends JPanel implements ActionListener {

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
     * Constructor: creates the button panel
     *
     * @param game the game model
     */
    public ButtonPanel(Game game) {
        this.game = game;
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
        restart.addActionListener(this);

        JButton exit = new JButton("Exit");
        exit.setActionCommand("exit");
        exit.addActionListener(this);

        southButtons.setLayout(new GridLayout(1,0,5,0));
        southButtons.add(restart);
        southButtons.add(exit);

        body = new JPanel();
        body.setLayout(new GridLayout(0, 1, 0, 5));
        body.setBorder(BorderFactory.createEmptyBorder(5,0,20,0));

        JButton step = new JButton("Step");
        step.setActionCommand("step");
        step.addActionListener(this);

        JButton play = new JButton("Play");
        play.setActionCommand("play");
        play.addActionListener(this);

        JButton newNeg = new JButton("New round");
        newNeg.setActionCommand("new round");
        newNeg.addActionListener(this);

        forwardPlays = new JTextField("100");
//        forwardPlays.setMaximumSize(new Dimension(80, 25));

        JButton forward = new JButton("Rounds forward");
        forward.setActionCommand("forward");
        forward.addActionListener(this);

        JPanel forwardPanel = new JPanel();
        forwardPanel.setLayout(new GridLayout(1,0,5,0));
        forwardPanel.add(forwardPlays);
        forwardPanel.add(forward);

        body.add(step);
        body.add(play);
        body.add(newNeg);
        body.add(Box.createRigidArea(new Dimension(1,1)));
        body.add(forwardPanel);

        changeBackgrounds();

//        this.add(info, BorderLayout.NORTH);
        this.add(body, BorderLayout.CENTER);
        this.add(southButtons, BorderLayout.SOUTH);
    }

    private void changeBackgrounds() {
        setBackground(Settings.getBackGroundColor());
        southButtons.setBackground(Settings.getBackGroundColor());
        body.setBackground(Settings.getBackGroundColor());
    }

    private void forward() {
        int rounds;
        Popups popup = new Popups();

        try {
            rounds = Integer.parseInt(forwardPlays.getText());
        } catch (NumberFormatException nfe) {
            popup.showInvalidTextError();
            return;
        }

        if (rounds < 1)
            popup.showInvalidTextError();

        game.setSimulationOff();

        for (int i = 0; i < rounds; i++) {
            game.playTillEnd();
            game.newRound();
        }

        game.setSimulationOn();
        game.notifyListenersNewGame();
    }

    /**
     * Action corresponding with a button event
     *
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "restart" -> game.reset();
            case "new round" -> game.newRound();
            case "exit" -> System.exit(0);
            case "step" -> game.step();
            case "play" -> game.playTillEnd();
            case "forward" -> forward();
        }
//        if ("restart".equals(e.getActionCommand())) {
////            stop();
//            game.reset();
////            update();
//        }
//
//        if ("new round".equals(e.getActionCommand())) {
//            game.newRound();
//        }
//
//        if ("exit".equals(e.getActionCommand())) {
//            System.exit(0);
//        }
//
//        if ("step".equals(e.getActionCommand())) {
//            game.step();
//        }

    }
}
