package lyingAgents.controller.gamePlayActions;

import lyingAgents.model.Game;
import lyingAgents.utilities.MiscFunc;

import java.awt.*;
import java.util.Objects;
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
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
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
        JTextPane textPane = new JTextPane();

        textPane.setEditable(false);
        textPane.setOpaque(false);
        MiscFunc.createsStyledDocument(textPane.getStyledDocument());

        String style = "boldalic";
        String content = "Click on cancel to cancel execution after the current step is done.";
        MiscFunc.addStylesToDoc(textPane, content, style);

        pbProgress = new JProgressBar();
        pbProgress.setPreferredSize(new Dimension(300, 50));
        pbProgress.setStringPainted(true);

        JButton cancel = new JButton("Cancel");
        cancel.addActionListener(e -> pw.cancelExecution());

        pane.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0; gbc.gridx = 0;
        gbc.insets = new Insets(10, 10, 10, 10);

        pane.add(textPane, gbc);
        gbc.gridy++;
        pane.add(pbProgress, gbc);
        gbc.gridy++;
        pane.add(cancel, gbc);
    }

    /**
     * Progress worker to make the progress bar update and repaint.
     */
    private void makeProgressWorker() {
        pw = new ProgressWorker(game, GamePlaySettings.FORWARD_ACTION, rounds);
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
        pw.addForwardListener(listener);
    }

    @Override
    public void forwardExecutionStarted() {
    }

    @Override
    public void forwardActionDone(int rounds) {
        frame.setVisible(false);
        frame.dispose();
    }

    @Override
    public void forwardExecutionAborted() {
        frame.setVisible(false);
        frame.dispose();
    }
}