package lyingAgents.view;

import java.awt.*;

public class ViewSettings {
    /**
     * determines the width of the button panel
     */
    public static final int BUTTON_PANEL_WIDTH = 320;

    /**
     * determines the height of the agent panel
     */
    public static final int AGENT_PANEL_HEIGHT = 540;

    /**
     * determines the height of the text panel in the agent panel
     */
    public static final int AGENT_TEXT_HEIGHT = 290;

    /**
     * determines the height of the legend panel
     */
    public static final int LEGEND_PANEL_HEIGHT = 230;

    /**
     * Size of the board panel
     */
    public static final int BOARD_PANEL_SIZE = 601;

    /**
     * The symbol used to indicate the start
     */
    public static final String START_LOCATION_SYMBOL = "X";

    /**
     * The symbol used to indicate the goal position if for both agents equal
     */
    public static final String GOAL_LOCATION_SYMBOL = "G";

    /**
     * The symbol used to indicate the goal position of the initiator
     */
    public static final String GOAL_LOCATION_SYMBOL_INITIATOR = "Gi";

    /**
     * The symbol used to indicate the goal position of the responder
     */
    public static final String GOAL_LOCATION_SYMBOL_RESPONDER = "Gr";

    /**
     * Gets the background color of the simulation
     *
     * @return the color of the background for the simulation
     */
    public static Color getBackGroundColor() {
        return Color.decode("#DAEFF9");
    }
}
