package gui;

import utilities.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ButtonPanel extends JPanel implements ActionListener {

    private final Game game;

    public ButtonPanel(Game game) {
        this.game = game;
        createButtonPanel();
    }

    private void createButtonPanel() {
        this.setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        JPanel buttons = new JPanel();
        buttons.setLayout(new BoxLayout(buttons, BoxLayout.LINE_AXIS));
        buttons.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        JButton restart = new JButton("Restart");
        restart.setActionCommand("restart");
        restart.addActionListener(this);

        JButton exit = new JButton("Exit");
        exit.setActionCommand("exit");
        exit.addActionListener(this);

        buttons.add(Box.createHorizontalGlue());
        buttons.add(restart);
        buttons.add(Box.createRigidArea(new Dimension(5, 0)));
        buttons.add(exit);

        JPanel body = new JPanel();
        body.setLayout(new GridLayout(0, 1));

        // body.add(buttons1);
        // body.add(forwardLabel);
//        body.add(buttons2);

//        this.add(info, BorderLayout.NORTH);
        this.add(body, BorderLayout.CENTER);
        this.add(buttons, BorderLayout.SOUTH);
    }

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

    }
}
