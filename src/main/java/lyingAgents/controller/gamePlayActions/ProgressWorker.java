package lyingAgents.controller.gamePlayActions;

import lyingAgents.model.Game;
import lyingAgents.utilities.Settings;

import javax.swing.*;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutionException;

/**
 * Class ProgressWorker: class to progress the work that has to be done: forwarding rounds in the game
 */
class ProgressWorker extends SwingWorker<Object, Object> {

    /**
     * The number of rounds to be forwarded
     */
    private int rounds;

    /**
     * The game model
     */
    private final Game game;

    /**
     * The set of listeners that listen to changes in the progress worker
     */
    private Set<PlayListener> playListeners;
    private Set<ForwardListener> forwardListeners;

    /**
     * Boolean check for when the execution has to be canceled
     */
    private boolean cancel = false;

    /**
     * Description of which action to perform (play or forward)
     */
    private final String description;

    /**
     * Constructor
     *
     * @param game   The game model
     * @param rounds The number of rounds to be forwarded
     */
    public ProgressWorker(Game game, String description, int rounds) {
        this.rounds = rounds;
        this.description = description;
        this.game = game;
        createListenerSet();
    }

    /**
     * Constructor
     *
     * @param game        The game model
     * @param description Description of which action to perform
     */
    public ProgressWorker(Game game, String description) {
        this.game = game;
        this.description = description;
        createListenerSet();
    }

    /**
     * Method to create a listeners set
     */
    private void createListenerSet() {
        if (description.equals(GamePlaySettings.FORWARD_ACTION)) {
            this.forwardListeners = new HashSet<>();
        } else if (description.equals(GamePlaySettings.PLAY_ACTION)) {
            this.playListeners = new HashSet<>();
        }
    }

    /**
     * Adds a listener to the set of listeners
     *
     * @param listener The listener to be added
     */
    public void addForwardListener(ForwardListener listener) {
        forwardListeners.add(listener);
    }

    /**
     * Adds a listener to the set of listeners
     *
     * @param listener The listener to be added
     */
    public void addPlayListener(PlayListener listener) {
        playListeners.add(listener);
    }

    /**
     * Notifies all listeners that execution starts
     */
    public void startExecution() {
        if (description.equals(GamePlaySettings.FORWARD_ACTION)) {
            for (ForwardListener listener : forwardListeners) {
                listener.forwardExecutionStarted();
            }
        } else if (description.equals(GamePlaySettings.PLAY_ACTION)) {
            for (PlayListener listener : playListeners) {
                listener.playExecutionStarted();
            }
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
        if (description.equals(GamePlaySettings.FORWARD_ACTION)) {
            doInBackgroundForwardAction();
        } else if (description.equals(GamePlaySettings.PLAY_ACTION)) {
            doInBackgroundPlayAction();
        }

        return null;
    }

    private void doInBackgroundForwardAction() {
        for (int i = 0; i < rounds; i++) {
            if (this.cancel) {
                break;
            }
            game.playTillEnd();
            game.newRound();
            this.setProgress((int) ((i + 1.0) / rounds * 100));
        }
    }

    private void doInBackgroundPlayAction() {
        int i = 0;
        while (i < Settings.MAX_NUMBER_OFFERS && !game.isGameFinished()) {
            if (this.cancel) break;
            game.step();
            this.setProgress(i + 1);
            i++;
        }
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
            if (description.equals(GamePlaySettings.FORWARD_ACTION)) {
                for (ForwardListener listener : forwardListeners) {
                    listener.forwardExecutionAborted();
                }
            } else if (description.equals(GamePlaySettings.PLAY_ACTION)) {
                for (PlayListener listener : playListeners) {
                    listener.playExecutionAborted();
                }
            }
        } else {
            if (description.equals(GamePlaySettings.FORWARD_ACTION)) {
                for (ForwardListener listener : forwardListeners) {
                    listener.forwardActionDone(rounds);
                }
            } else if (description.equals(GamePlaySettings.PLAY_ACTION)) {
                for (PlayListener listener : playListeners) {
                    listener.playActionDone();
                }
            }
        }
    }
}
