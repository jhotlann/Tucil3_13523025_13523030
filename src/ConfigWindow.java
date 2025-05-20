package Tucil3_13523025_13523030.src;

import javax.swing.*;
import java.awt.*; 
import java.awt.event.*;

class ConfigWindow extends JFrame {
    private JTextField rowField = new JTextField(5);
    private JTextField colField = new JTextField(5);
    private JTextField exitRowField = new JTextField(5);
    private JTextField exitColField = new JTextField(5);
    private JTextField algo = new JTextField(5);
    private JButton nextButton = new JButton("Next");

    public ConfigWindow() {
        setTitle("Rush Hour Setup");
        setLayout(new GridLayout(6, 2, 5, 5));

        add(new JLabel("Number of Rows:"));
        add(rowField);

        add(new JLabel("Number of Columns:"));
        add(colField);

        add(new JLabel("Exit Row:"));
        add(exitRowField);

        add(new JLabel("Exit Column:"));
        add(exitColField);

        add(new JLabel("Algorithm (UCS / GBFS / AStar)"));
        add(algo);

        add(new JLabel());
        add(nextButton);
        

        nextButton.addActionListener(e -> {
            try {
                int rows = Integer.parseInt(rowField.getText());
                int cols = Integer.parseInt(colField.getText());
                int exitRow = Integer.parseInt(exitRowField.getText());
                int exitCol = Integer.parseInt(exitColField.getText());
                String algoString = algo.getText();

                dispose(); // Close this config window
                new MatrixInputGUI(rows, cols, exitRow, exitCol, algoString);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter valid integers.");
            }
        });

        setSize(600, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
}
