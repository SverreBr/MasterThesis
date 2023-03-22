package controller.saveload;

import model.Game;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class LoadAction extends AbstractAction {

    private final JFrame frame;
    private final Game game;

    public LoadAction(String name, JFrame frame, Game game) {
        super(name);
        this.frame = frame;
        this.game = game;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("load");
    }
}
