package Tucil3_13523025_13523030.src;
import java.util.PriorityQueue;


public class Greedy {
    public Greedy(Papan papan, Piece piece) {
        PriorityQueue<PieceHeuristic> pq = new PriorityQueue<>();
        boolean done = false;
        
        for (Piece p : papan.getPiece()) {
            PieceHeuristic a = new PieceHeuristic(p);
            pq.add(a);
        }
    }
}
