package gui;

import javax.swing.*;
import java.awt.*;

public class Popups extends JOptionPane {
    public Popups() {}

    public void showInvalidTextError() {
        showMessageDialog((Component)null, "The text you entered is probably not a positive integer.");
    }
}
