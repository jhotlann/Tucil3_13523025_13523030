    package Tucil3_13523025_13523030.src;

    /**
     * Heuristic that calculates the number of blocking pieces and distance to the exit
     */
    public class BlockingDistanceHeuristic implements BoardHeuristic {
        
        @Override
        public int calculate(Papan state) {
            Piece primaryPiece = state.getPrimaryPiece();
            if (primaryPiece == null) {
                return Integer.MAX_VALUE; // No primary piece found
            }
            
            int exitRow = state.getBarisExit();
            int exitCol = state.getKolomExit();
            
            // Count blocking pieces and calculate distance to exit
            if (primaryPiece.isHorizontal()) {
                return calculateHorizontalPieceHeuristic(state, primaryPiece, exitRow, exitCol);
            } else {
                return calculateVerticalPieceHeuristic(state, primaryPiece, exitRow, exitCol);
            }
        }
        
        /**
         * Calculate heuristic for a horizontal primary piece
         */
        private int calculateHorizontalPieceHeuristic(Papan state, Piece primaryPiece, int exitRow, int exitCol) {
            int row = primaryPiece.getBaris();
            int col = primaryPiece.getKolom();
            int length = primaryPiece.getPanjang();
            int rightEnd = col + length - 1;
            
            // If not on the same row as exit, this is a bad state
            if (row != exitRow) {
                return 100; // Large penalty for being on the wrong row
            }
            
            if (rightEnd >= exitCol) {
                return 0; // Already at the exit
            }

            // Calculate the distance from the right end of the piece to the exit
            int distance = exitCol - rightEnd;
            
            // Count blocking pieces
            int blockingPieces = 0;
            char[][] board = state.getPapan();
            
            // Check if there are any blocking pieces between the primary piece and the exit
            for (int j = rightEnd + 1; j < exitCol; j++) {
                if (board[row][j] != '.' && board[row][j] != primaryPiece.getNama()) {
                    blockingPieces++;
                }
            }
            
            // The heuristic is the sum of the distance and the number of blocking pieces
            // We multiply blockingPieces by 2 to give it more weight
            return distance + (blockingPieces * 2);
        }
        
        /**
         * Calculate heuristic for a vertical primary piece
         */
        private int calculateVerticalPieceHeuristic(Papan state, Piece primaryPiece, int exitRow, int exitCol) {
            int row = primaryPiece.getBaris();
            int col = primaryPiece.getKolom();
            int length = primaryPiece.getPanjang();
            int bottomEnd = row + length - 1;
            
            // If not on the same column as exit, this is a bad state
            if (col != exitCol) {
                return 100; // Large penalty for being on the wrong column
            }

            if (bottomEnd >= exitRow) {
                return 0; // Already at the exit
            }
            
            // Calculate the distance from the bottom end of the piece to the exit
            int distance = exitRow - bottomEnd;
            
            // Count blocking pieces
            int blockingPieces = 0;
            char[][] board = state.getPapan();
            
            // Check if there are any blocking pieces between the primary piece and the exit
            for (int i = bottomEnd + 1; i < exitRow; i++) {
                if (board[i][col] != '.' && board[i][col] != primaryPiece.getNama()) {
                    blockingPieces++;
                }
            }
            
            // The heuristic is the sum of the distance and the number of blocking pieces
            // We multiply blockingPieces by 2 to give it more weight
            return distance + (blockingPieces * 2);
        }
    }