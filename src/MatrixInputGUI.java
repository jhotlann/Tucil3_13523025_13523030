package Tucil3_13523025_13523030.src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class MatrixInputGUI extends JFrame {
    private JTextField[][] cells;
    private JButton solveButton = new JButton("Solve");

    public MatrixInputGUI(int rows, int cols, int rowExit, int colExit, String algo) {
        setTitle("Rush Hour Matrix Input");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel gridPanel = new JPanel(new GridLayout(rows, cols));
        cells = new JTextField[rows][cols];
        Font font = new Font("Monospaced", Font.BOLD, 20);

        // Create and add cells
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                JTextField tf = new JTextField(1);
                tf.setHorizontalAlignment(JTextField.CENTER);
                tf.setFont(font);
                cells[row][col] = tf;
                gridPanel.add(tf);
            }
        }

        // Button listener
        solveButton.addActionListener(e -> {
            char[][] matrix = new char[rows][cols];
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    String text = cells[i][j].getText();
                    matrix[i][j] = (text.length() > 0) ? text.charAt(0) : '.';
                }
            }

            Papan papan = new Papan(rows, cols, matrix, rowExit, colExit);
            Papan copy = new Papan(papan);

            if (algo.equals("UCS")){
                UCS ucs = new UCS(papan);
                List<Gerakan> steps = ucs.solve();
                if (steps == null || steps.isEmpty()) {
                    JOptionPane.showMessageDialog(
                        this,
                        "No solution found for the given input.",
                        "No Solution",
                        JOptionPane.WARNING_MESSAGE
                     );
                }
                else{
                    GUI gui = new GUI(copy, steps, algo);
                }
            }

            else if (algo.equals("GBFS")){
                GreedyBestFirstSearch greedy = new GreedyBestFirstSearch(papan);
                List<Gerakan> steps = greedy.solve();
                if (steps == null || steps.isEmpty()) {
                    JOptionPane.showMessageDialog(
                        this,
                        "No solution found for the given input.",
                        "No Solution",
                        JOptionPane.WARNING_MESSAGE
                     );
                }
                else{
                    GUI gui = new GUI(papan,steps, algo);
                }
            }else if (algo.equals("AStar")) {
                AStarSearch astar = new AStarSearch(papan);
                List<Gerakan> steps = astar.solve();
                if (steps == null || steps.isEmpty()) {
                    JOptionPane.showMessageDialog(
                        this,
                        "No solution found for the given input.",
                        "No Solution",
                        JOptionPane.WARNING_MESSAGE
                     );
                }
                else{
                    GUI gui = new GUI(papan,steps, algo);
                }
            }
            
            // new GUI(papan, steps);
            dispose();
        });

        add(gridPanel, BorderLayout.CENTER);
        add(solveButton, BorderLayout.SOUTH);

        setSize(500, 550);
        setVisible(true);
    }
}
