package lyingAgents.controller.gamePlayActions;

public interface PlayListener {
    void playExecutionStarted();

    void playActionDone();

    void playExecutionAborted();
}
