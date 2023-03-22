package view.settings;

import utilities.Settings;

import javax.swing.*;

public class Miscellaneous {
    public static int parseIntTextField(JTextField textField, boolean rand) {
        int textFieldValue = -1;
        int someValue = rand ? ((int) (Math.random() * Settings.CHIP_DIVERSITY)) : 0;
        try {
            textFieldValue = Integer.parseInt(textField.getText());
        } catch (NumberFormatException ignored) {
        } finally {
            textFieldValue = ((textFieldValue >= 0) && (textFieldValue < Settings.CHIP_DIVERSITY)) ? textFieldValue : someValue;
        }
        return textFieldValue;
    }
}
