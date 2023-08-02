package lyingAgents.controller.gamePlayActions;

import lyingAgents.model.Game;
import lyingAgents.utilities.MiscFunc;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;

public class PlayAction implements PlayListener {

    private final Game game;

    private final JPanel pane;

    private final JFrame frame;

    private ProgressWorker pw;

    private final JTextPane textPane;

    public PlayAction(Game game) {
        this.game = game;
        pane = new JPanel();

        frame = new JFrame("Play negotiation");
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        textPane = new JTextPane();
        makePane();
        frame.add(pane);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        makeProgressWorker();
        addListenerToProgressWorker(this);
    }

    /**
     * Makes the main panel of the progress bar
     */
    private void makePane() {
        textPane.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(0, 0, 0, 0),
                BorderFactory.createCompoundBorder(
                        BorderFactory.createEtchedBorder(EtchedBorder.RAISED),
                        BorderFactory.createEmptyBorder(5, 5, 5, 5))));

        textPane.setEditable(false);
        textPane.setOpaque(false);
        MiscFunc.createsStyledDocument(textPane.getStyledDocument());
        updatePane(0);

        JButton cancel = new JButton("Cancel");
        cancel.addActionListener(e -> pw.cancelExecution());

        pane.add(textPane, BorderLayout.NORTH);
        pane.add(cancel, BorderLayout.SOUTH);
    }

    private void updatePane(int numSteps) {
        String[] content = new String[3];
        String[] style = new String[3];

        style[0] = "regular";
        content[0] = "Number of negotiation steps executed: " + numSteps + ".";

        style[1] = "regular";
        content[1] = "";
        style[2] = "boldalic";
        content[2] = "Click on cancel to cancel execution after the current step is done.";

        MiscFunc.addStylesToDocMulti(textPane, content, style);
    }

    /**
     * Progress worker to make the progress bar update and repaint.
     */
    private void makeProgressWorker() {
        pw = new ProgressWorker(game, GamePlaySettings.PLAY_ACTION);
        pw.addPropertyChangeListener(evt -> {
            String name = evt.getPropertyName();
            if (name.equals("progress")) {
                updatePane(pw.getProgress());
                pane.repaint();
            }
        });
    }

    public void execute() {
        pw.startExecution();
        pw.execute();
    }

    /**
     * Adds a listener to the progress worker.
     *
     * @param listener the ForwardListener to be added to the progress worker
     */
    public void addListenerToProgressWorker(PlayListener listener) {
        pw.addPlayListener(listener);
    }

    @Override
    public void playExecutionStarted() {
    }

    @Override
    public void playActionDone() {
        frame.setVisible(false);
        frame.dispose();
    }

    @Override
    public void playExecutionAborted() {
        frame.setVisible(false);
        frame.dispose();
    }
}
