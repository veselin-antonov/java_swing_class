package bg.mindhub.components;

import bg.mindhub.SystemSettings;
import bg.mindhub.TextFieldHighlighter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class MyTextField extends JTextField implements FocusListener {
    public MyTextField() {
        setBorder(BorderFactory.createLineBorder(SystemSettings.mainDarkColor));
        setFont(SystemSettings.GLOBAL_FONT);

        this.addFocusListener(this);
    }

    @Override
    public void focusGained(FocusEvent e) {
        TextFieldHighlighter.reset(this);
    }

    @Override
    public void focusLost(FocusEvent e) {}
}
