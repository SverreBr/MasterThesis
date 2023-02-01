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

    private JPanel southButtons;

    private JPanel body;

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

        southButtons.add(Box.createHorizontalGlue());
        southButtons.add(restart);
        southButtons.add(Box.createRigidArea(new Dimension(5, 0)));
        southButtons.add(exit);

        body = new JPanel();
        body.setLayout(new GridLayout(0, 1));
        body.setBorder(BorderFactory.createEmptyBorder(5,0,5,0));

        JButton step = new JButton("Step");
        step.setActionCommand("step");
        step.addActionListener(this);

        body.add(Box.createHorizontalGlue());
        body.add(step);

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

    /**
     * Action corresponding with a button event
     *
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if ("restart".equals(e.getActionCommand())) {
//            stop();
            game.reset();
//            update();
        }

        if ("exit".equals(e.getActionCommand())) {
            System.exit(0);
        }

        if ("step".equals(e.getActionCommand())) {
            game.step();
        }
    }
}
