package lyingAgents.view;

import lyingAgents.model.Game;
import lyingAgents.model.GameListener;
import lyingAgents.utilities.Chips;
import lyingAgents.utilities.MiscFunc;
import lyingAgents.utilities.OfferOutcome;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CenterPanel extends JPanel implements GameListener {
    private final Game game;
    private final JScrollPane informationScroll;
    private final JTextPane informationPane;

    public CenterPanel(Game game) {
        this.game = game;
        this.game.addListener(this);
        this.informationPane = new JTextPane();
        this.informationScroll = new JScrollPane(this.informationPane);
        createInfoPanel();
        updateInfo();

        this.setPreferredSize(new Dimension(ViewSettings.BOARD_PANEL_SIZE,
                ViewSettings.AGENT_PANEL_HEIGHT + ViewSettings.LEGEND_PANEL_HEIGHT));
        this.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        this.setBackground(ViewSettings.getBackGroundColor());
        BoardPanel boardPanel = new BoardPanel(game);

        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        this.add(boardPanel, gbc);
        gbc.insets = new Insets(10, 0, 0, 0);
        gbc.gridy++;
        gbc.weighty = 1.0;
        gbc.gridheight = GridBagConstraints.REMAINDER;
        this.add(informationScroll, gbc);
    }

    private void createInfoPanel() {
        informationScroll.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(0, 0, 0, 0),
                BorderFactory.createCompoundBorder(
                        BorderFactory.createEtchedBorder(EtchedBorder.RAISED),
                        BorderFactory.createEmptyBorder(5, 5, 5, 5))));

        informationPane.setEditable(false);
        informationPane.setOpaque(false);

        MiscFunc.createsStyledDocument(informationPane.getStyledDocument());
    }

    private void updateInfo() {
        List<OfferOutcome> paretoOutcomes = game.getParetoOutcomes();
        Collections.sort(paretoOutcomes);
        Collections.reverse(paretoOutcomes);
        String[] content = new String[paretoOutcomes.size() + 2];
        String[] style = new String[paretoOutcomes.size() + 2];

        int idx = 0;
        style[idx] = "bold";
        content[idx++] = "Additional Information";
        style[idx] = "italic";

        if (paretoOutcomes.size() == 0) {
            content[idx] = "- No pareto improvements:";
        } else {
            content[idx++] = "- Pareto improvements:";
            for (OfferOutcome paretoOutcome : paretoOutcomes) {
                style[idx] = "regular";
                content[idx++] = "  * " +
                        Arrays.toString(Chips.getBins(paretoOutcome.getOfferForInit(), game.getBinMaxChips())) +
                        " - " + Arrays.toString(Chips.getBins(game.flipOffer(paretoOutcome.getOfferForInit()), game.getBinMaxChips())) +
                        "; " + paretoOutcome.getValueInit() + " - " + paretoOutcome.getValueResp() +
                        "; sw=" + paretoOutcome.getSocialWelfare();
            }
        }

        MiscFunc.addStylesToDocMulti(informationPane, content, style);
    }


    @Override
    public void gameChanged() {

    }

    @Override
    public void newGame() {
        updateInfo();
    }
}