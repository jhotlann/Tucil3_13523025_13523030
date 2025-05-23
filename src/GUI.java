package Tucil3_13523025_13523030.src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class GUI extends JFrame {
    private BoardPanel boardPanel;
    private List<Gerakan> solutionSteps;
    private Papan board;
    public GUI(Papan initialBoard, List<Gerakan> steps, String algo){
        this.board = new Papan(initialBoard);
        this.boardPanel = new BoardPanel(this.board);
        this.solutionSteps = steps;
        add(boardPanel);
        setSize(board.getKolom() * 100, board.getBaris()*100);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        setTitle("Rush Hour with " + algo);
        runSolution();
    }

    private void runSolution(){
        Timer timer = new Timer(500, new ActionListener() {
            private int index = 0;

            public void actionPerformed(ActionEvent e) {
                if (index < solutionSteps.size()) {
                    Gerakan move = solutionSteps.get(index);
                    board.movePiece(move);
                    boardPanel.repaint();
                    index++;
                } else {
                    ((Timer)e.getSource()).stop();
                }
            }
        });
        timer.start();
    }
}


