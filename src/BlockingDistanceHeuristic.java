    package Tucil3_13523025_13523030.src;

    
    public class BlockingDistanceHeuristic implements Heuristic {
        
        @Override
        public int calculate(Papan state) {
            Piece primaryPiece = state.getPrimaryPiece();
            if (primaryPiece == null) {
                return Integer.MAX_VALUE; 
            }
            
            int exitRow = state.getBarisExit();
            int exitCol = state.getKolomExit();
            
         
            if (primaryPiece.isHorizontal()) {
                return calculateHorizontalPieceHeuristic(state, primaryPiece, exitRow, exitCol);
            } else {
                return calculateVerticalPieceHeuristic(state, primaryPiece, exitRow, exitCol);
            }
        }
        
    
        private int calculateHorizontalPieceHeuristic(Papan state, Piece primaryPiece, int exitRow, int exitCol) {
            int row = primaryPiece.getBaris();
            int col = primaryPiece.getKolom();
            int length = primaryPiece.getPanjang();
            int rightEnd = col + length - 1;
            
           
            if (row != exitRow) {
                return 100; 
            }
            
            if (rightEnd >= exitCol) {
                return 0; 
            }

           
            int distance = exitCol - rightEnd;
            
            // Count blocking pieces
            int blockingPieces = 0;
            char[][] board = state.getPapan();
          
            for (int j = rightEnd + 1; j < exitCol; j++) {
                if (board[row][j] != '.' && board[row][j] != primaryPiece.getNama()) {
                    blockingPieces++;
                }
            }
            
            
            return distance + (blockingPieces * 2);
        }
        
       
        private int calculateVerticalPieceHeuristic(Papan state, Piece primaryPiece, int exitRow, int exitCol) {
            int row = primaryPiece.getBaris();
            int col = primaryPiece.getKolom();
            int length = primaryPiece.getPanjang();
            int bottomEnd = row + length - 1;
            
          
            if (col != exitCol) {
                return 100; 
            }

            if (bottomEnd >= exitRow) {
                return 0; 
            }
            
         
            int distance = exitRow - bottomEnd;
            
           
            int blockingPieces = 0;
            char[][] board = state.getPapan();
            
          
            for (int i = bottomEnd + 1; i < exitRow; i++) {
                if (board[i][col] != '.' && board[i][col] != primaryPiece.getNama()) {
                    blockingPieces++;
                }
            }
            
            
            return distance + (blockingPieces * 2);
        }
    }