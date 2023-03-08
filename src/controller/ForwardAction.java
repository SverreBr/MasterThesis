package controller;

import model.Game;

import java.awt.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import javax.swing.*;

/**
 * ForwardAction class: action to forward a number of steps with progress bar.
 */
public class ForwardAction implements ForwardListener {

    /**
     * A new frame to draw on.
     */
    private final JFrame frame;

    /**
     * The main panel with progress bar
     */
    private final JPanel pane;

    /**
     * The progress bar
     */
    private JProgressBar pbProgress;

    /**
     * Progress worker to make the progress bar work
     */
    private ProgressWorker pw;

    /**
     * Model of the Colored Trails game
     */
    private final Game game;

    /**
     * Number of rounds to forward
     */
    private final int rounds;

    /**
     * Constructor
     *
     * @param game   Game model
     * @param rounds Number of rounds to forward
     */
    public ForwardAction(Game game, int rounds) {
        this.game = game;
        this.rounds = rounds;
        pane = new JPanel();

        frame = new JFrame("Forwarding rounds");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());

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
        pane.setPreferredSize(new Dimension(300, 100));
        pbProgress = new JProgressBar();
        pbProgress.setPreferredSize(new Dimension(300, 50));
        pbProgress.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        pbProgress.setStringPainted(true);
        pane.add(pbProgress, BorderLayout.NORTH);

        JButton cancel = new JButton("Cancel");
        cancel.addActionListener(e -> pw.cancelExecution());
        pane.add(cancel, BorderLayout.CENTER);
    }

    /**
     * Progress worker to make the progress bar update and repaint.
     */
    private void makeProgressWorker() {
        pw = new ProgressWorker(game, rounds);
        pw.addPropertyChangeListener(evt -> {
            String name = evt.getPropertyName();
            if (name.equals("progress")) {
                int progress = (int) evt.getNewValue();
                pbProgress.setValue(progress);
                pane.repaint();
            } else if (name.equals("state")) {
                SwingWorker.StateValue state = (SwingWorker.StateValue) evt.getNewValue();
                if (Objects.requireNonNull(state) == SwingWorker.StateValue.DONE) {
                    System.out.println("Done");
                }
            }
        });
    }

    /**
     * Executes the forwarding of rounds.
     */
    public void execute() {
        pw.startExecution();
        pw.execute();
    }

    /**
     * Adds a listener to the progress worker.
     *
     * @param listener the ForwardListener to be added to the progress worker
     */
    public void addListenerToProgressWorker(ForwardListener listener) {
        pw.addListener(listener);
    }

    @Override
    public void executionStarted() {
    }

    @Override
    public void forwardActionDone(int rounds) {
        frame.setVisible(false);
        frame.dispose();
    }

    @Override
    public void executionAborted() {
        frame.setVisible(false);
        frame.dispose();
    }
}

/**
 * Class ProgressWorker: class to progress the work that has to be done: forwarding rounds in the game
 */
class ProgressWorker extends SwingWorker<Object, Object> {

    /**
     * The number of rounds to be forwarded
     */
    private final int rounds;

    /**
     * The game model
     */
    private final Game game;

    /**
     * The set of listeners that listen to changes in the progress worker
     */
    private final Set<ForwardListener> listeners;

    /**
     * Boolean check for when the execution has to be canceled
     */
    private boolean cancel = false;

    /**
     * Constructor
     *
     * @param game   The game model
     * @param rounds The number of rounds to be forwarded
     */
    public ProgressWorker(Game game, int rounds) {
        this.rounds = rounds;
        this.game = game;
        this.listeners = new HashSet<>();
    }

    /**
     * Adds a listener to the set of listeners
     *
     * @param listener The listener to be added
     */
    public void addListener(ForwardListener listener) {
        listeners.add(listener);
    }

    /**
     * Notifies all listeners that execution starts
     */
    public void startExecution() {
        for (ForwardListener listener : listeners) {
            listener.executionStarted();
        }
    }

    /**
     * Sets boolean cancel to true and cancels the execution before the next round.
     */
    public void cancelExecution() {
        this.cancel = true;
    }

    @Override
    protected Object doInBackground() {
        for (int i = 0; i < rounds; i++) {
            if (this.cancel) {
                break;
            }
            game.playTillEnd();
            game.newRound();
            this.setProgress((int) ((i + 1.0) / rounds * 100));
        }
        return null;
    }

    @Override
    protected void done() {
        super.done();
        try {
            get();
        } catch (InterruptedException | ExecutionException ex) {
            ex.printStackTrace();
            System.out.println("Bad: " + ex.getMessage());
        }
        if (this.cancel) {
            for (ForwardListener listener : listeners) {
                listener.executionAborted();
            }
        } else {
            for (ForwardListener listener : listeners) {
                listener.forwardActionDone(rounds);
            }
        }
    }
}