package controller;

import utilities.Game;

import java.awt.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import javax.swing.*;

public class ForwardAction implements ForwardListener {

    private final JPanel pane;

    private JProgressBar pbProgress;
    private ProgressWorker pw;

    private final Game game;
    private final int rounds;

    private final JFrame frame;

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

    private void makeProgressWorker() {
        pw = new ProgressWorker(game, rounds);
        pw.addPropertyChangeListener(evt -> {
            String name = evt.getPropertyName();
            if (name.equals("progress")) {
                int progress = (int) evt.getNewValue();
                pbProgress.setValue(progress);
//                pbProgress.setString(progress + " / " + pbProgress.getMaximum());
                pane.repaint();
            } else if (name.equals("state")) {
                SwingWorker.StateValue state = (SwingWorker.StateValue) evt.getNewValue();
                if (Objects.requireNonNull(state) == SwingWorker.StateValue.DONE) {
                    System.out.println("Done");
                }
            }
        });
    }

    public void execute() {
        pw.startExecution();
        pw.execute();
    }

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

class ProgressWorker extends SwingWorker<Object, Object> {

    private final int rounds;
    private final Game game;
    private final Set<ForwardListener> listeners;

    private boolean cancel = false;

    public ProgressWorker(Game game, int rounds) {
        this.rounds = rounds;
        this.game = game;
        this.listeners = new HashSet<>();
    }

    public void addListener(ForwardListener listener) {
        listeners.add(listener);
    }

    public void startExecution() {
        for (ForwardListener listener : listeners) {
            listener.executionStarted();
        }
    }

    public void cancelExecution() {
        this.cancel = true;
    }

    @Override
    protected Object doInBackground() {
        int progress;
        for (int i = 0; i < rounds; i++) {
            if (this.cancel) {
                break;
            }
            game.playTillEnd();
            game.newRound();
            progress = (int) ((i + 1.0) / rounds * 100);
            setProgress(progress);
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