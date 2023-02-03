package utilities;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Player: abstract class for players
 */
public abstract class Player {

    /**
     * list of integers that represent the colored chips, where each list index represents the color and the number in
     * the list is the amount of chips
     */
    private int[] chips;

    /**
     * Name of the agent
     */
    private final String name;

    /**
     * Model of the game
     */
    private final Game game;

    /**
     * Starting position of the agent
     */
    private Point startingPosition;

    /**
     * Goal position of the agent
     */
    private Point goalPosition;

    /**
     * List of messages send
     */
    private List<String> messages;

    /**
     * Constructor
     *
     * @param namePlayer name of the agent
     * @param game       model of the game
     */
    public Player(String namePlayer, Game game) {
        this.name = namePlayer;
        this.game = game;
        resetPlayer();
    }

    /**
     * Resets the agent. Removes all colored chips, and sets the starting and goal position to null
     */
    public void resetPlayer() {
        this.chips = this.game.makeNewChipBin();
        this.messages = new ArrayList<>();
        this.startingPosition = null;
        this.goalPosition = null;
    }

    /**
     * Getter for name
     *
     * @return the name of the agent
     */
    public String getName() {
        return this.name;
    }

    /**
     * Getter for game
     *
     * @return the game model
     */
    public Game getGame() {
        return this.game;
    }

    /**
     * Getter for messages
     *
     * @return list of messages
     */
    public List<String> getMessages() {
        return this.messages;
    }

    /**
     * Give the player colored chips
     */
    public void addChips(int[] newChips) {
        for (int i = 0; i < newChips.length; i++)
            this.chips[i] += newChips[i];
    }

    public void setNewChips(int[] newChips) {
        System.arraycopy(newChips, 0, this.chips, 0, newChips.length);
    }

    /**
     * Get the number of chips
     *
     * @return number of chips
     */
    public int getNumChips() {
        return this.game.calculateNumChips(this.chips);
    }

    /**
     * Get the chips of the agent
     *
     * @return an array of integers that represent the tokens
     */
    public int[] getChips() {
        return this.chips;
    }

    /**
     * Sets the starting position of the agent
     *
     * @param startingPosition the starting position of the agent
     */
    public void setStartingPosition(Point startingPosition) {
        this.startingPosition = startingPosition;
    }

    /**
     * Gets the starting position of the agent
     *
     * @return the starting position of the agent
     */
    public Point getStartingPosition() {
        return this.startingPosition;
    }

    /**
     * Sets the goal position of the agent
     *
     * @param goalPosition the goal position of the agent
     */
    public void setGoalPosition(Point goalPosition) {
        this.goalPosition = goalPosition;
    }

    /**
     * Gets the goal position of the agent
     *
     * @return the goal position of the agent
     */
    public Point getGoalPosition() {
        return this.goalPosition;
    }

    /**
     * Calculates the current score of the agent
     *
     * @return score of the agent
     */
    public int calculateCurrentScore() {
        return this.game.getBoard().calculateScoreAgent(this);
    }

    public void makeOffer(Player p, int[] offer) {
        int[] curOffer;
        boolean offerAccepted = false;

        if (offer == null) {
            // use own chips as offer received
            System.out.println("Select initial offer.");
            curOffer = selectInitialOffer();
        } else {
            // use chips as offer received
            offerAccepted = receiveOffer(p, offer);
            curOffer = selectOffer(p, offer); // TODO: check
        }

        if (offerAccepted) {
            System.out.println("Offer accepted");
            return;
        }

        if (curOffer == null)
            this.game.negotiationFailed();

        sendOffer(curOffer);
    }

    public int[] selectInitialOffer() {
        return makeRandomOffer();
    }

    public int[] selectOffer(Player p, int[] offer) {
        return makeRandomOffer();
    }

    private int[] makeRandomOffer() {
        int[] allChips = this.game.getAllChipsInGame().clone();
        System.out.println("all chips in the game: " + Arrays.toString(allChips));
        int[] newOffer = this.game.makeNewChipBin();
        int chipsToDraw = Settings.CHIPS_PER_PLAYER;
        int randNum;

        while (chipsToDraw > 0) {
            randNum = (int) (Math.random() * Settings.CHIP_DIVERSITY);
            if (allChips[randNum] > 0) {
                chipsToDraw -= 1;
                newOffer[randNum] += 1;
                allChips[randNum] -= 1;
            }
        }
        return newOffer;
    }

    public boolean receiveOffer(Player p, int[] offer) {
        int currPoints = game.getBoard().calculateScore(startingPosition, chips, startingPosition, goalPosition);
        int otherPoints = game.getBoard().calculateScore(startingPosition, offer, startingPosition, goalPosition);
        System.out.println("Current points = " + currPoints + ". New points = " + otherPoints);
        if (otherPoints > currPoints) {
            // accept offer?
            acceptOffer(offer);
            return true;
        }

        System.out.println("Offer not accepted.");
        return false;
    }

    public void acceptOffer(int[] offer) {
        this.game.offerAccepted(this, offer);
    }

    public void sendOffer(int[] offer) {
        this.game.makeOffer(this, offer);
        String offerMessage = "I offer: " + Arrays.toString(offer);

        this.messages.add(offerMessage);
    }
}
