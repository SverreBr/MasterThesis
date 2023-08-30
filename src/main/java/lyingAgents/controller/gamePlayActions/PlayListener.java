package lyingAgents.controller.gamePlayActions;

/**
 * PlayListener class: listens to the PlayAction when negotiation round is played
 */
public interface PlayListener {

    /**
     * Notified that execution of forwarding rounds has begun
     */
    void playExecutionStarted();

    /**
     * Notified that play action is done
     */
    void playActionDone();

    /**
     * Notified that execution for playing current negotiation round is aborted
     */
    void playExecutionAborted();
}
