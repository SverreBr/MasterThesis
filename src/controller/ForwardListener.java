package controller;

/**
 * ForwardListener class: listens to the ForwardAction when rounds are forwarded
 */
public interface ForwardListener {

    /**
     * Notified that execution of forwarding rounds has begun
     */
    void executionStarted();

    /**
     * Notified that forward action is done
     *
     * @param rounds the number of rounds forwarded
     */
    void forwardActionDone(int rounds);

    /**
     * Notified that execution for forwarding rounds is aborted
     */
    void executionAborted();
}
