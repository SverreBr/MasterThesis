package utilities;

// imports
import alternatingOffers.PlayerToM;
import utilities.Board;

import java.awt.*;

/**
 * utilities.Game class: the two players and the coloured trails board
 */
public class Game {

    /**
     * Fields
     */
    static final int SCORE_GOAL = 500;
    static final int SCORE_STEP = 100;
    static final int SCORE_SURPLUS = 50;
    static final int BOARD_WIDTH = 5;
    static final int BOARD_HEIGHT = 5;
    static final int TOKEN_DIVERSITY = 4;
    static final int TOKENS_PER_PLAYER = 4;



    public final PlayerToM initiator;
    public final PlayerToM responder;
    public final Board board;
    public final Settings settings;

    /**
     * Constructor
     */
    public Game() {
        this.settings = new Settings();
        this.board = new Board(BOARD_HEIGHT, BOARD_WIDTH, TOKEN_DIVERSITY, settings);
        this.initiator = new PlayerToM("Initiator", settings);
        this.responder = new PlayerToM("Responder", settings);

        initGame();
    }

    private void initGame() {
        // Distribute tokens to players
        generateAndDistributeTokens();
    }

    /**
     * Generate and distribute tokens for players
     */
    private void generateAndDistributeTokens() {
        int[] tokensInit = new int[TOKEN_DIVERSITY];
        int[] tokensResp = new int[TOKEN_DIVERSITY];

        for (int i = 0; i < TOKENS_PER_PLAYER; i++) {
            tokensInit[(int) (Math.random() * TOKEN_DIVERSITY)]++;
            tokensResp[(int) (Math.random() * TOKEN_DIVERSITY)]++;
        }
        initiator.obtainTokens(tokensInit);
        responder.obtainTokens(tokensResp);
    }

    public Dimension getBoardSize() {
        return new Dimension(board.getWidth(),board.getHeight());
    }
}