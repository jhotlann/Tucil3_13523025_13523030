package Tucil3_13523025_13523030.src;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.*;
import java.awt.*;


import Tucil3_13523025_13523030.src.Papan;
import Tucil3_13523025_13523030.src.Piece;

public class BoardPanel extends JPanel{
    private Papan board;
    private HashMap<Character, Color> pieceColor = new HashMap<>();
    private final Color[] AVAILABLE_COLORS = {
    Color.BLUE,
        Color.GREEN,
        Color.MAGENTA,
        Color.ORANGE,
        Color.CYAN
    };

    public BoardPanel(Papan board) {
        this.board = board;
        int i = 0;
        for (Piece p : board.getPieces()){
            Character p_name = p.getNama();
            if (!pieceColor.containsKey(p_name)){
                pieceColor.put(p_name, AVAILABLE_COLORS[i % AVAILABLE_COLORS.length]);

                i++;
            }
        }
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Color color;
        List<Piece> pieces = board.getPieces();

        int cellsize = Math.min(getWidth() / board.getKolom(), getHeight() / board.getBaris());
        
        for (Piece p : pieces){
            if (p.isUtama()){
                color = Color.red;
            }

            else{
                color = pieceColor.get(p.getNama());
            }

            g.setColor(color);

            if (p.isHorizontal()){
                g.fillRect(p.getKolom() * cellsize, p.getBaris() * cellsize, p.getPanjang() * cellsize, cellsize);
            } 
            else{
                g.fillRect(p.getKolom() * cellsize, p.getBaris() * cellsize, cellsize, p.getPanjang() * cellsize);
            }

            // g.setColor(Color.BLACK);
            // for (int r = 0; r <= board.getBaris(); r++) {
            //     g.drawLine(0, r * cellsize, getWidth(), r * cellsize);
            //     g.drawLine(p.getKolom() * cellsize, p.getBaris() * cellsize, p.getKolom() * cellsize + cellsize,  p.getBaris() * cellsize + p.getPanjang() * cellsize);
            // }
            // for (int c = 0; c <= board.getKolom(); c++) {
            //     g.drawLine(c * cellsize, 0, c * cellsize, getHeight());
            // }
        }
    }
}