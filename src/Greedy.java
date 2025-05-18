package Tucil3_13523025_13523030.src;
import java.util.PriorityQueue;
import java.util.HashSet;
import java.util.ArrayList;

public class Greedy {
    public Greedy(Papan papanAwal, Piece primaryPiece, int exitRow, int exitCol) {
        PriorityQueue<State> pq = new PriorityQueue<>();
        HashSet<String> visited = new HashSet<>();

        State initialState = new State(papanAwal, new ArrayList<>(), 0);
        pq.add(initialState);

        while (!pq.isEmpty()) {
            State current = pq.poll();
            if (isAtExit(current.getPapan(), primaryPiece, exitRow, exitCol)) {
                System.out.println("Berhasil mencapai pintu keluar!");
                current.getPapan().displayBoard();
                return;
            }

            String hash = current.getPapan().hashString();
            if (visited.contains(hash)) continue;
            visited.add(hash);

            for (Piece piece : current.getPapan().getPieces()) {
                for (int delta = -1; delta <= 1; delta += 2) {
                    for (int step = 1; step < 6; step++) {
                        Gerakan g = generateGerakan(piece, delta * step);
                        if (g == null) continue;

                        Papan copy = new Papan(current.getPapan()); // pastikan deep copy
                        boolean moved = copy.movePiece(g);
                        if (moved) {
                            int score = new PieceHeuristic(copy, primaryPiece, exitRow, exitCol).getHeuristicScore();
                            ArrayList<Gerakan> newSteps = new ArrayList<>(current.getSteps());
                            newSteps.add(g);
                            pq.add(new State(copy, newSteps, score));
                        }
                    }
                }
            }
        }

        System.out.println("Tidak ada solusi ditemukan.");
    }

    private boolean isAtExit(Papan papan, Piece primary, int exitRow, int exitCol) {
        if (!primary.isHorizontal()) return false;
        return primary.getBaris() == exitRow && (primary.getKolom() + primary.getPanjang() - 1) == exitCol;
    }

    private Gerakan generateGerakan(Piece p, int langkah) {
        if (langkah == 0) return null;
        String arah = null;
        if (p.isHorizontal()) {
            arah = langkah > 0 ? "kanan" : "kiri";
        } else {
            arah = langkah > 0 ? "bawah" : "atas";
        }
        return new Gerakan(p, arah, Math.abs(langkah));
    }
}
