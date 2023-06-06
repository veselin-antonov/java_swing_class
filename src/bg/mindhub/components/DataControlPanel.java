package bg.mindhub.components;

import bg.mindhub.*;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class DataControlPanel extends JPanel {

    private List<JTextComponent> movieDataFields;

    public DataControlPanel(ActionListener buttonActionListener) {
        super(new GridBagLayout());

        this.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(20, 100, 20, 100),
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(SystemSettings.mainDarkColor),
                        "Edit Data",
                        TitledBorder.CENTER,
                        TitledBorder.TOP
                )
        ));
        this.setBackground(SystemSettings.mainBackgroundColor);

        GridBagConstraints constraints = new GridBagConstraints();

        //********** Data control labels **********
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.weightx = 0.1;
        constraints.insets.set(10, 30, 10, 0);
        constraints.fill = GridBagConstraints.NONE;
        constraints.anchor = GridBagConstraints.LINE_START;

        int tableColumnCount = Movie.TABLE_COLUMN_NAMES.length;
        String[] columnNames = Movie.TABLE_COLUMN_NAMES;

        for (int i = 0; i < tableColumnCount - 1; i++) {
            JLabel label = new JLabel(columnNames[i]);
            this.add(label, constraints);
            constraints.gridy++;
        }

        constraints.gridx = 3;
        constraints.gridy = 0;
        constraints.gridwidth = 3;
        constraints.weightx = 0.5;
        constraints.insets.right = 30;
        constraints.anchor = GridBagConstraints.CENTER;

        JLabel label = new JLabel(columnNames[tableColumnCount - 1]);
        this.add(label, constraints);

        //********** Data control text fields **********
        movieDataFields = new ArrayList<>();

        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.gridwidth = 2;
        constraints.weightx = 0.4;
        constraints.ipady = 5;
        constraints.insets.set(10, 0, 10, 0);
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.fill = GridBagConstraints.HORIZONTAL;

        for (int i = 0; i < Movie.TABLE_COLUMN_NAMES.length - 1; i++) {
            JTextField currentTextField = new MyTextField();
            movieDataFields.add(i, currentTextField);

            this.add(currentTextField, constraints);
            constraints.gridy++;
        }

        constraints.gridx = 3;
        constraints.gridy = 1;
        constraints.gridwidth = 3;
        constraints.gridheight = 3;
        constraints.weightx = 0.5;
        constraints.insets.set(10, 30, 10, 30);
        constraints.fill = GridBagConstraints.BOTH;

        JTextArea textArea = new MyTextArea();
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        movieDataFields.add(textArea);

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        this.add(scrollPane, constraints);

        movieDataFields.get(0).setEditable(false);

        //********** Data control buttons **********
        constraints.gridx = 0;
        constraints.gridy = 4;
        constraints.gridwidth = 6;
        constraints.weightx = 1;
        constraints.ipady = 0;
        constraints.insets.set(0, 0, 0, 0);
        constraints.fill = GridBagConstraints.HORIZONTAL;

        FlowLayout buttonPanelLayout = new FlowLayout();
        buttonPanelLayout.setHgap(30);

        JPanel buttonsPanel = new JPanel(buttonPanelLayout);
        buttonsPanel.setBackground(SystemSettings.mainBackgroundColor);
        this.add(buttonsPanel, constraints);

        MyButton clearButton = new MyButton("CLEAR", "clear", buttonActionListener, "icons/clearIcon.png");
        buttonsPanel.add(clearButton);

        MyButton updateButton = new MyButton("UPDATE", "update", buttonActionListener, "icons/updateIcon.png");
        buttonsPanel.add(updateButton);

        MyButton createButton = new MyButton("CREATE", "create", buttonActionListener, "icons/createIcon.png");
        buttonsPanel.add(createButton);

        MyButton deleteButton = new MyButton("DELETE", "delete", buttonActionListener, "icons/deleteIcon.png");
        buttonsPanel.add(deleteButton);
    }

    public void updateFields(Movie movie) {
        String[] data = movie.toTableData();
        for (int i = 0; i < movieDataFields.size(); i++) {
            movieDataFields.get(i).setText(data[i]);
        }
    }

    public void clearDataFields() {
        for (JTextComponent movieDataField : movieDataFields) {
            movieDataField.setText("");
        }
    }

    public long getMovieId() {
        String id = movieDataFields.get(0).getText().trim();
        if (id.isBlank()) {
            return -1L;
        }
        return Long.parseLong(id);
    }

    public List<String> getMovieData() {
        return movieDataFields.stream()
                .map(JTextComponent::getText)
                .map(String::trim)
                .collect(Collectors.toList());
    }

    public boolean checkDataValidity() {
        StringJoiner emptyFields = new StringJoiner(", ");
        short emptyFieldsCount = 0;

        for (int i = 1; i < movieDataFields.size(); i++) {
            if (movieDataFields.get(i).getText().isBlank()) {
                TextFieldHighlighter.markAsError(movieDataFields.get(i));
                emptyFields.add(Movie.TABLE_COLUMN_NAMES[i]);
                emptyFieldsCount++;
            }
        }

        if (emptyFieldsCount > 0) {
            boolean multipleEmptyFields = emptyFieldsCount > 1;
            String message = String.format(
                    "The following data field%s %s empty: %s",
                    multipleEmptyFields ? "s" : "",
                    multipleEmptyFields ? "are" : "is",
                    emptyFields
            );
            JOptionPane.showMessageDialog(this, message, "Invalid Input", JOptionPane.WARNING_MESSAGE);
            return false;
        }


        JTextComponent yearReleasedField = movieDataFields.get(2);
        try {
            Integer.parseInt(yearReleasedField.getText().trim());
        } catch (NumberFormatException e) {
            TextFieldHighlighter.markAsError(yearReleasedField);
            JOptionPane.showMessageDialog(this, "Invalid movie release year!", "Invalid Data", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }
}
