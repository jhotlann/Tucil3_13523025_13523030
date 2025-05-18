package Tucil3_13523025_13523030.src;

public class PieceHeuristic implements Comparable<PieceHeuristic> {
    private int heuristicScore;
    private char nama;

    public PieceHeuristic(Papan papan, Piece primary, int exitRow, int exitCol) {
        this.nama = primary.getNama();
        int dx = primary.getBaris() - exitRow;
        int dy = (primary.isHorizontal()) ? (primary.getKolom() + primary.getPanjang() - 1) - exitCol : primary.getKolom() - exitCol;
        int distance = Math.abs(dx) + Math.abs(dy);
        int blockingCount = countBlockingPieces(papan, primary, exitRow, exitCol);
        this.heuristicScore = distance + 2 * blockingCount; // blocking lebih berat
    }

    private int countBlockingPieces(Papan papan, Piece primary, int exitRow, int exitCol) {
        int count = 0;
        char[][] board = papan.getPapan();
        if (primary.isHorizontal() && primary.getBaris() == exitRow) {
            int r = primary.getBaris();
            for (int c = primary.getKolom() + primary.getPanjang(); c <= exitCol && c < board[0].length; c++) {
                if (board[r][c] != '.') count++;
            }
        }
        return count;
    }

    public int getHeuristicScore() {
        return heuristicScore;
    }

    public char getName() {
        return nama;
    }

    @Override
    public int compareTo(PieceHeuristic other) {
        return Integer.compare(this.heuristicScore, other.heuristicScore);
    }
}
