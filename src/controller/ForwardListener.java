package controller;

public interface ForwardListener {
    void executionStarted();

    void forwardActionDone(int rounds);

    void executionAborted();
}
